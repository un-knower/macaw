package cn.migu.macaw.jarboot.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import cn.migu.macaw.jarboot.dao.ProcessLogMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.migu.macaw.common.SSHManager;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.jarboot.api.model.Jar;
import cn.migu.macaw.jarboot.api.model.Process;
import cn.migu.macaw.jarboot.api.model.ProcessLog;
import cn.migu.macaw.jarboot.common.JarFuncType;
import cn.migu.macaw.jarboot.common.JarStatus;
import cn.migu.macaw.jarboot.dao.JarMapper;
import cn.migu.macaw.jarboot.dao.ProcessMapper;
import cn.migu.macaw.jarboot.model.HostPid;
import cn.migu.macaw.jarboot.model.JarConfParam;
import cn.migu.macaw.jarboot.service.IDeployJarBootService;

/**
 * 应用部署服务jar内部功能实现类
 *
 * @author soy
 */
@Service("deployJarBootService")
public class DeployJarBootServiceImpl implements IDeployJarBootService
{
    
    @Autowired
    private ProcessMapper processDao;
    
    @Autowired
    private JarMapper jarDao;

    @Autowired
    private ProcessLogMapper processLogDao;
    
    @Override
    public JarConfParam parseRequestParam(JarConfParam bean, HttpServletRequest request)
    {
        try
        {
            List<String> keys =
                BeanUtils.describe(bean).entrySet().stream().map(e -> e.getKey()).filter(k -> !StringUtils.equals("class",k)).collect(Collectors.toList());
            Map<String, String> paramMap = request.getParameterMap()
                .entrySet()
                .stream()
                .filter(e -> StringUtils.isNotBlank(e.getKey()) && keys.contains(e.getKey()))
                .filter(e -> null != e.getValue() && e.getValue().length > 0)
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()[0]));
            BeanUtils.populate(bean, paramMap);
            
            return bean;
        }
        catch (Exception e)
        {
            LogUtils.runLogError(e);
        }
        
        return null;
    }
    
    @Override
    public void killProcess(JarConfParam param)
    {
        List<HostPid> processHostList = getJarHostPidInfo(param.getAppId(), param.getServerId(), param.getObjId());
        if (null != processHostList)
        {
            if (StringUtils.isBlank(param.getKind()))
            {
                LogUtils.runLogError(
                    String.format("kill pid failed,result:jar type is null,app_id=%s,server_id=%s,jar_id=%s",
                        param.getAppId(),
                        param.getServerId(),
                        param.getObjId()));
            }
            else
            {
                JarFuncType jarType = JarFuncType.values()[Integer.valueOf(param.getKind())];
                for (HostPid hp : processHostList)
                {
                    
                    int signal = -9;
                    switch (jarType)
                    {
                        case CLEAN:
                            signal = -15;
                            break;
                        default:
                            break;
                    }
                    
                    String killShell = String.format("kill %d %d", signal, hp.getPid());
                    
                    SSHManager.execCommand(hp.getIp(), hp.getUsername(), hp.getPassword(), killShell);
                    
                    try
                    {
                        Thread.sleep(200);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    
                    String queryPidShell = String.format("ps -p %d | grep -v PID  | wc -l", hp.getPid());
                    String ret = SSHManager.execCommand(hp.getIp(), hp.getUsername(), hp.getPassword(), queryPidShell);
                    if (StringUtils.equals("0", ret))
                    {
                        //更改应用状态
                        updateJarStatus(param,JarStatus.STOP.ordinal());
                        
                        //删除进程记录
                        deleteProcess(param);
                        
                        //进程操作记录
                        addProcessLog(param, JarStatus.STOP.ordinal());
                    }
                    else
                    {
                        LogUtils.runLogError(String.format("don't finish removing process[%d] in host[%s]",hp.getPid(),hp.getIp()));
                    }
                }
            }
            
        }
    }

    @Override
    public String bootProcess(JarConfParam param)
    {
        JarFuncType jarType = JarFuncType.values()[Integer.valueOf(param.getKind())];

        switch(jarType)
        {
            case CUSTOM:
            case CUSTOM_CHECK_FILE:
                break;
            case FLUME:
                break;
            case NORMAL_STREAMING:
            case MERGE_STREAMING:
                break;
            default:
                break;
        }

        return null;
    }

    /**
     * 根据jar的配置信息查询部署的进程和主机信息
     * @param appId 配置服务id
     * @param serviceId 服务器id
     * @param jarId 配置jar id
     * @return  List<HostPid> - 配置部署jar所在的主机及启动进程号
     */
    private List<HostPid> getJarHostPidInfo(String appId, String serviceId, String jarId)
    {
        if (StringUtils.isNotBlank(appId) && StringUtils.isNotBlank(serviceId) && StringUtils.isNotBlank(jarId))
        {
            return processDao.getHostPidForJar(appId, serviceId, jarId);
        }
        
        return null;
    }

    /**
     * 更新部署应用状态
     * @param param 配置参数
     * @param status 状态
     */
    private void updateJarStatus(JarConfParam param,int status)
    {
        Jar jarEntity = new Jar();
        jarEntity.setStatus(status);
        jarEntity.setObjId(param.getObjId());
        jarEntity.setDealUser(param.getDealUser());
        jarDao.updateByPrimaryKey(jarEntity);

    }

    /**
     * 删除进程信息
     * @param param 配置参数
     */
    private void deleteProcess(JarConfParam param)
    {
        Process processEntity = new Process();
        processEntity.setAppId(param.getAppId());
        processEntity.setJarId(param.getObjId());
        processEntity.setServerId(param.getServerId());
        processDao.delete(processEntity);
    }

    /**
     * 新增进程操作日志
     * @param param 配置参数
     * @param status 状态
     */
    private void addProcessLog(JarConfParam param, int status)
    {
        Process condition = new Process();
        condition.setJarId(param.getObjId());
        condition.setServerId(param.getServerId());
        condition.setAppId(param.getAppId());
        
        List<Process> processList = processDao.select(condition);
        for (Process p : processList)
        {
            ProcessLog log = new ProcessLog();
            log.setAppId(param.getAppId());
            log.setServerId(param.getServerId());
            log.setJarId(param.getObjId());
            log.setPort(p.getPort());
            log.setStatus(String.valueOf(status));
            log.setKind(p.getKind());
            log.setProcessNo(p.getProcessNo());
            log.setMemory(p.getMemory());
            log.setCpus(p.getCpus());
            log.setNote(StringUtils.isBlank(param.getNote()) ? p.getNote() : param.getNote());
            processLogDao.insertSelective(log);
        }
    }
}