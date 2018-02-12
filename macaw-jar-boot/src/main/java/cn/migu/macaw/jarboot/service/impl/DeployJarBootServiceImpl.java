package cn.migu.macaw.jarboot.service.impl;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.migu.macaw.common.DateUtil;
import cn.migu.macaw.common.OnlineDataSourceType;
import cn.migu.macaw.common.ReturnCode;
import cn.migu.macaw.common.SSHManager;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.jarboot.JarBootConfigAttribute;
import cn.migu.macaw.jarboot.api.model.*;
import cn.migu.macaw.jarboot.api.model.Process;
import cn.migu.macaw.jarboot.common.*;
import cn.migu.macaw.jarboot.dao.*;
import cn.migu.macaw.jarboot.model.CollectHandleFile;
import cn.migu.macaw.jarboot.model.DataFileCollectConf;
import cn.migu.macaw.jarboot.model.HostPid;
import cn.migu.macaw.jarboot.model.JarConfParam;
import cn.migu.macaw.jarboot.service.IDeployJarBootService;
import tk.mybatis.mapper.entity.Example;

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
    
    @Autowired
    private DataFileMapper dataFileDao;
    
    @Autowired
    private ServerMapper serverDao;
    
    @Autowired
    private JarMonitorMapper jarMonitorDao;
    
    @Autowired
    private JarSourceMapper jarSourceDao;
    
    @Autowired
    private JarFileMapper jarFileDao;

    @Autowired
    private JarMonitorValueMapper jarMonitorValueDao;
    
    @Autowired
    private JarBootConfigAttribute jarBootConfigAttribute;
    
    @Autowired
    private BootCommandGenerator bootCommandGenerator;

    @Autowired
    private ZkUtils zkUtils;
    
    @Override
    public JarConfParam parseRequestParam(JarConfParam bean, HttpServletRequest request)
    {
        try
        {
            List<String> keys = BeanUtils.describe(bean)
                .entrySet()
                .stream()
                .map(e -> e.getKey())
                .filter(k -> !StringUtils.equals("class", k))
                .collect(Collectors.toList());
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
                        updateJarStatus(param, JarStatus.STOP.ordinal());
                        
                        //删除进程记录
                        deleteProcess(param);
                        
                        //进程操作记录
                        addProcessLog(param, JarStatus.STOP.ordinal());
                    }
                    else
                    {
                        LogUtils.runLogError(
                            String.format("don't finish removing process[%d] in host[%s]", hp.getPid(), hp.getIp()));
                    }
                }
            }
            
        }
    }
    
    @Override
    public ReturnCode bootProcess(JarConfParam param)
    {
        if (null != param)
        {
            return ReturnCode.BOOT_PARAM_PARSE_ERROR;
        }
        
        ReturnCode retCode = bootConfigParamCheck(param);
        if (retCode == ReturnCode.SUCCESS)
        {
            //设置监控属性信息
            monitorConf(param);

            Server s = getHostInfo(param.getServerId());
            if (null == s)
            {
                return ReturnCode.DEPLOY_HOST_NOT_EXISTED;
            }

            retCode = bootJar(param, s);
            if(ReturnCode.SUCCESS == retCode || ReturnCode.NOT_ANY_RESOURCES == retCode)
            {
                Jar jar = new Jar();
                jar.setObjId(param.getObjId());
                jar.setStatus(JarStatus.RUNNING.ordinal());
                jarDao.updateByPrimaryKeySelective(jar);
            }
            else
            {
                killProcess(param);
            }
            
            return retCode;
            
        }
        else
        {
            return retCode;
        }
    }
    
    /**
     * 启动参数基本检测
     * @param param
     * @return
     */
    private ReturnCode bootConfigParamCheck(JarConfParam param)
    {
        if (StringUtils.isBlank(param.getObjId()))
        {
            return ReturnCode.JAR_ID_EMPTY;
        }
        
        if (StringUtils.isBlank(param.getAppId()))
        {
            return ReturnCode.DEPLOY_APP_ID_EMPTY;
        }
        
        if (StringUtils.isBlank(param.getServerId()))
        {
            return ReturnCode.DEPLOY_SERVER_ID_EMPTY;
        }
        
        if (StringUtils.isBlank(param.getPath()))
        {
            return ReturnCode.DEPLOY_PATH_EMPTY;
        }
        
        if (!NumberUtils.isNumber(param.getKind()))
        {
            return ReturnCode.DEPLOY_JAR_TYPE_ERROR;
        }
        
        //不同功能jar指定参数检测
        JarFuncType type = JarFuncType.values()[Integer.valueOf(param.getKind())];
        
        switch (type)
        {
            case CUSTOM:
            case CUSTOM_CHECK_FILE:
                if (StringUtils.isBlank(param.getName()))
                {
                    return ReturnCode.DEPLOY_JAR_NAME_EMPTY;
                }
                if (StringUtils.isBlank(param.getDealUser()))
                {
                    return ReturnCode.DEPLOY_USER_EMPTY;
                }
                if (StringUtils.isBlank(param.getNote()))
                {
                    return ReturnCode.DATA_COLLECT_TIME_EMPTY;
                }
                if (StringUtils.isBlank(param.getPort()))
                {
                    return ReturnCode.DEPLOY_JAR_PORT_EMPTY;
                }
                break;
            case FLUME:
                if (StringUtils.isBlank(param.getDealUser()))
                {
                    return ReturnCode.DEPLOY_USER_EMPTY;
                }
                if (StringUtils.isBlank(param.getPort()))
                {
                    return ReturnCode.DEPLOY_JAR_PORT_EMPTY;
                }
                break;
            case NORMAL_STREAMING:
            case MERGE_STREAMING:
                if (StringUtils.isBlank(param.getName()))
                {
                    return ReturnCode.DEPLOY_JAR_NAME_EMPTY;
                }
                if (StringUtils.isBlank(param.getDealUser()))
                {
                    return ReturnCode.DEPLOY_USER_EMPTY;
                }
                if (StringUtils.isBlank(param.getPort()))
                {
                    return ReturnCode.DEPLOY_JAR_PORT_EMPTY;
                }
                break;
            case SPRINGBOOT:
                return ReturnCode.NOT_SUPPORT_BOOT;
            case CLEAN:
                break;
            default:
                return ReturnCode.DEPLOY_JAR_TYPE_ERROR;
        }
        
        return ReturnCode.SUCCESS;
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
     * 根据主键获取主机信息
     * @param id 主键
     * @return Server - 主机实例
     */
    private Server getHostInfo(String id)
    {
        Server s = new Server();
        s.setObjId(id);
        return serverDao.selectOne(s);
    }
    
    /**
     * 更新部署应用状态
     * @param param 配置参数
     * @param status 状态
     */
    private void updateJarStatus(JarConfParam param, int status)
    {
        Jar jarEntity = new Jar();
        jarEntity.setStatus(status);
        jarEntity.setObjId(param.getObjId());
        jarEntity.setDealUser(param.getDealUser());
        jarDao.updateByPrimaryKey(jarEntity);
        
    }
    
    /**
     * 增加进程
     * @param param 部署服务配置参数
     * @param pid 进程号
     */
    private void addProcess(JarConfParam param, String pid)
    {
        Process tProcess = new Process();
        tProcess.setAppId(param.getAppId());
        tProcess.setServerId(param.getServerId());
        tProcess.setJarId(param.getObjId());
        tProcess.setPort(Integer.valueOf(param.getPort()));
        tProcess.setKind(param.getKind());
        tProcess.setProcessNo(Integer.valueOf(pid));
        tProcess.setStatus(JarStatus.RUNNING.ordinal());
        tProcess.setDealUser(param.getDealUser());
        processDao.insertSelective(tProcess);
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
    
    /**
     * 自定义jar信息外部方法验证
     * @param param 部署服务配置参数
     * @param hostInfo 部署主机信息
     * @return boolean - 检测结果
     */
    private ReturnCode checkCustomExtFuncMethod(JarConfParam param, Server hostInfo)
    {
        SftpManager sftpManager = new SftpManager();
        try
        {
            String localJarPath = StringUtils
                .join(jarBootConfigAttribute.getCustomJarLocalPath(), "/", jarBootConfigAttribute.getCustomJarName());
            String remoteJarFile = StringUtils.join(param.getPath(), "/", jarBootConfigAttribute.getCustomJarName());
            File localFile = new File(localJarPath);
            if (!localFile.exists())
            {
                //如果自定义文件采集jar本地不存在则下载
                sftpManager.createChannel(hostInfo.getUsername(),
                    hostInfo.getPassword(),
                    hostInfo.getIp(),
                    hostInfo.getPort());
                sftpManager.download(remoteJarFile, localFile);
            }
            
            //采集文件配置信息
            DataFile collectFileConf =
                dataFileDao.getExtFuncDataCollect(param.getAppId(), param.getServerId(), param.getObjId());
            
            if (null == collectFileConf)
            {
                return ReturnCode.CUSTOM_ETL_JAR_EXT_FUNC_NOT_EXISTED;
            }
            //检测自定义外部方法是否存在
            URL url = localFile.toURI().toURL();
            ClassLoader loader = new URLClassLoader(new URL[] {url});
            Class<?> cls = loader.loadClass(jarBootConfigAttribute.getCustomJarHandleClassName());
            if (null == collectFileConf.getMergeNum() || null == collectFileConf.getMergeTime())
            {
                cls.getMethod(collectFileConf.getExtFunction(), String.class, String.class);
            }
            else
            {
                cls.getMethod(collectFileConf.getExtFunction(), List.class, String.class);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogUtils.runLogError(e);
            
            return ReturnCode.FAILED;
        }
        finally
        {
            sftpManager.closeChannel();
        }
        
        return ReturnCode.SUCCESS;
        
    }
    
    /**
     * 启动功能jar
     * @param param 部署服务配置参数
     * @param hostInfo 部署主机信息
     * @return ReturnCode - 返回信息码
     */
    private ReturnCode bootJar(JarConfParam param, Server hostInfo)
    {
        String shell = "";
        JarFuncType type = JarFuncType.values()[Integer.valueOf(param.getKind())];
        switch (type)
        {
            case CUSTOM:
                ReturnCode retCode = checkCustomExtFuncMethod(param, hostInfo);
                if (ReturnCode.SUCCESS == retCode)
                {
                    shell = bootCommandGenerator.customEtlJarWithExtFuncBoot(param, hostInfo);
                }
                else if (ReturnCode.CUSTOM_ETL_JAR_EXT_FUNC_NOT_EXISTED == retCode)
                {
                    shell = bootCommandGenerator.customEtlJarBoot(param, hostInfo);
                }
                else
                {
                    return retCode;
                }
                break;
            case CUSTOM_CHECK_FILE:
                shell = bootCommandGenerator.customEtlJarBoot(param, hostInfo);
                break;
            case FLUME:
                shell = bootCommandGenerator.flumeJarBoot(param);
                break;
            case NORMAL_STREAMING:
            case MERGE_STREAMING:
                shell = bootCommandGenerator.streamingBoot(param);
                break;
            case CLEAN:
                shell = bootCommandGenerator.cleanJarBoot(param, hostInfo);
                break;
            default:
                break;
        }
        
        Pair<ReturnCode, String> pairRet = JarBootShell
            .execCommandForParseRetLine(hostInfo.getIp(), hostInfo.getUsername(), hostInfo.getPassword(), shell);
        ReturnCode retCode = pairRet.getLeft();
        if (JarFuncType.CLEAN == type && ReturnCode.SUCCESS == retCode)
        {
            //进程信息
            addProcess(param, pairRet.getRight());
            addProcessLog(param, JarStatus.RUNNING.ordinal());
        }
        
        return retCode;
    }
    
    /*****************监控*************************/
    
    /**
     * 增加监控配置
     * @param param 部署服务配置
     * @param dfc 数据采集配置
     * @param name 监控名
     * @param code 监控编码
     */
    private void addMonitorConf(JarConfParam param, DataFileCollectConf dfc, String name, String code)
    {
        JarMonitor jarMonitor = new JarMonitor();
        jarMonitor.setAppId(param.getAppId());
        jarMonitor.setServerId(param.getServerId());
        jarMonitor.setJarId(param.getObjId());
        jarMonitor.setName(name);
        jarMonitor.setCode(code);
        jarMonitor.setFormat(JarMonitorFormat.STRING.ordinal());
        jarMonitor.setJarKind(Integer.valueOf(param.getKind()));
        jarMonitor.setNote(StringUtils.join(dfc.getFileCode(), "&", dfc.getFileName()));
        jarMonitor.setScope(-1);
        jarMonitor.setPeriod(-1);
        jarMonitorDao.insertSelective(jarMonitor);
    }
    
    /**
     * 根据部署信息删除服务监控配置
     * @param appId 服务id
     * @param serverId 部署主机id
     * @param jarId 部署jar id
     */
    private void deleteMonitorConf(String appId, String serverId, String jarId)
    {
        JarMonitor jarMonitor = new JarMonitor();
        jarMonitor.setJarId(jarId);
        jarMonitor.setAppId(appId);
        jarMonitor.setServerId(serverId);
        jarMonitorDao.delete(jarMonitor);
    }
    
    /**
     * 更新监控信息
     * @param param 部署服务配置参数
     * @param fileCode 文件编码
     * @param fileName 文件名称
     * @param name 监控名
     * @param code 监控编码
     */
    private void updateOrAddMonitor(JarConfParam param, String fileCode, String fileName, String name, String code)
    {
        JarMonitor cJarMonitor = new JarMonitor();
        cJarMonitor.setServerId(param.getServerId());
        cJarMonitor.setAppId(param.getAppId());
        cJarMonitor.setName(name);
        cJarMonitor.setCode(code);
        JarMonitor tJarMonitor = jarMonitorDao.selectOne(cJarMonitor);
        if (null == tJarMonitor)
        {
            cJarMonitor.setJarId(param.getObjId());
            cJarMonitor.setFormat(JarMonitorFormat.STRING.ordinal());
            cJarMonitor.setJarKind(Integer.valueOf(param.getKind()));
            if(!StringUtils.isBlank(fileCode) && !StringUtils.isBlank(fileName))
            {
                cJarMonitor.setNote(StringUtils.join(fileCode, "&", fileName));
            }
            cJarMonitor.setScope(-1);
            cJarMonitor.setPeriod(-1);
            jarMonitorDao.insertSelective(cJarMonitor);
        }
        else
        {
            tJarMonitor.setJarId(param.getObjId());
            tJarMonitor.setFormat(JarMonitorFormat.STRING.ordinal());
            tJarMonitor.setJarKind(Integer.valueOf(param.getKind()));
            if(!StringUtils.isBlank(fileCode) && !StringUtils.isBlank(fileName))
            {
                tJarMonitor.setNote(StringUtils.join(fileCode, "&", fileName));
            }
            jarMonitorDao.updateByPrimaryKey(tJarMonitor);
        }
    }

    /**
     * 更新或新增监控值
     * @param valType 值类型
     * @param fileId 文件id
     * @param code 监控项键
     * @param desc 监控项描述
     * @param keyValue 监控项关联值
     * @param period  周期
     * @param scope 振幅
     */
    private void updateOrAddMonitorValue(JarMonitorValueType valType,String fileId, String code,String desc, String keyValue,Integer period,Integer scope)
    {
        JarMonitorValue jarMonitorValue = new JarMonitorValue();
        jarMonitorValue.setKeyValue(keyValue);
        jarMonitorValue.setName(desc);
        JarMonitorValue tJarMonitorValue = jarMonitorValueDao.selectOne(jarMonitorValue);

        if(null != tJarMonitorValue)
        {
            Example example = new Example(JarMonitorValue.class);
            List<String> filterCodes = Arrays.asList(new String[]{"lastUpdateTime","lastFileTime","allFileNum"});
            example.createCriteria().andNotIn("code",filterCodes);
            tJarMonitorValue.setMonitorValue("0");
            tJarMonitorValue.setStatus(StatusValue.VALID.ordinal());
            jarMonitorValueDao.updateByExampleSelective(tJarMonitorValue,example);
        }
        else
        {
            jarMonitorValue.setFileId(fileId);
            jarMonitorValue.setKind(valType.ordinal());
            jarMonitorValue.setCode(code);
            jarMonitorValue.setMonitorValue(("lastFileTime".equals(code) || "lastUpdateTime".equals(code))
                ? DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss") : "0");
            jarMonitorValue.setPeriod(
                (null != period && "lastFileTime".equals(code)) ? period : -1);
            jarMonitorValue
                .setScope((null != scope && "lastFileTime".equals(code)) ? scope : -1);
            jarMonitorValue.setStatus(StatusValue.VALID.ordinal());
            jarMonitorValueDao.insertSelective(jarMonitorValue);
        }


    }
    
    /**
     * 监控信息设置
     * @param param 部署服务配置参数
     */
    private void monitorConf(JarConfParam param)
    {
        JarFuncType type = JarFuncType.values()[Integer.valueOf(param.getKind())];
        switch (type)
        {
            case FLUME:
                flumeMonitor(param);
                break;
            case CUSTOM:
                dataCollectMonitor(param);
                break;
            case NORMAL_STREAMING:
            case MERGE_STREAMING:
            case CLEAN:
                dataHandleMonitor(param,type);
                break;
            default:
        }
    }
    
    /**
     * flume监控项设置
     * @param param 部署服务配置参数
     */
    private void flumeMonitor(JarConfParam param)
    {
        List<DataFileCollectConf> fileColConf =
            jarSourceDao.getDataFileCollectConf(param.getAppId(), param.getServerId(), param.getObjId());
        zkUtils.saveZk(param,fileColConf,jarBootConfigAttribute.getZkUrl(),jarBootConfigAttribute.getRollInterval());
        for (DataFileCollectConf fcc : fileColConf)
        {
            String kv = "";
            if (OnlineDataSourceType.HDFS.ordinal() == fcc.getKind())
            {
                kv = StringUtils.join(param.getObjId() , "_" , fcc.getObjId() , "c1");
            }
            else if(OnlineDataSourceType.IDOT_KAFKA.ordinal() == fcc.getKind())
            {
                kv = StringUtils.join(param.getObjId() , "_" , fcc.getObjId() , "k1");
            }

            final String tkv = kv;

            List<Triple<String,String,String>> items = flumeMonitorItem(fcc.getKind(), fcc.getObjId());
            items.stream().forEach(e -> {
                updateOrAddMonitor(param, fcc.getFileCode(), fcc.getFileName(), e.getLeft(), e.getMiddle());
                updateOrAddMonitorValue(JarMonitorValueType.FLUME,"",e.getLeft(),e.getRight(),tkv,null,null);
            });
        }
    }

    /**
     * 数据流处理监控(spark streaming,clean jar)
     *
     * @param param 部署服务配置参数
     */
    private void dataHandleMonitor(JarConfParam param,final JarFuncType type)
    {
        List<Triple<String,String,String>> items = dataHandleMonitorItem(param.getObjId());
        items.stream().forEach(e -> {
            updateOrAddMonitor(param, "", "", e.getLeft(), e.getMiddle());

            switch (type)
            {
                case CLEAN:
                    updateOrAddMonitorValue(JarMonitorValueType.CLEAN,"",e.getLeft(),e.getRight(),e.getMiddle(),null,null);
                    break;
                case NORMAL_STREAMING:
                case MERGE_STREAMING:
                    updateOrAddMonitorValue(JarMonitorValueType.STREAMING,"",e.getLeft(),e.getRight(),e.getMiddle(),null,null);
                    break;
                default:
                    break;
            }
        });
    }
    
    /**
     * 自定义jar监控项设置
     * @param param 部署服务配置参数
     */
    private void dataCollectMonitor(JarConfParam param)
    {
        List<CollectHandleFile> files =
            jarFileDao.getCollectHandleFiles(param.getAppId(), param.getServerId(), param.getObjId());
        if (CollectionUtils.isNotEmpty(files))
        {
            for (CollectHandleFile cf : files)
            {
                List<Triple<String,String,String>> items = dataCollectMonitorItem(param.getObjId(), cf.getFileId());
                items.stream().forEach(e -> {
                    updateOrAddMonitor(param, cf.getFileCode(), cf.getFileName(), e.getLeft(), e.getMiddle());
                    updateOrAddMonitorValue(JarMonitorValueType.CUSTOM,cf.getFileId(),e.getLeft(),e.getRight(),e.getMiddle(),null,null);
                });

                List<Triple<String,String,String>> originalItems = originalMonitorItem(cf.getFileId());
                items.stream().forEach(e -> {
                    updateOrAddMonitorValue(JarMonitorValueType.ORIGIANL,cf.getFileId(),e.getLeft(),e.getRight(),e.getMiddle(),cf.getPeriod(),cf.getScope());
                });
            }
        }
    }
    
    /**
     * 数据文件采集监控项
     * @param kind 在线数据源类型
     * @param id 数据采集配置id
     * @return Map<String, String> - 监控项配置
     */
    private List<Triple<String,String,String>> flumeMonitorItem(int kind, String id)
    {
        List<Triple<String,String,String>> listItem = Lists.newArrayList();
        
        if (OnlineDataSourceType.HDFS.ordinal() == kind)
        {
            String val = StringUtils.join("org.apache.flume.sink:type=", id, "k1");
            listItem.add(Triple.of("EventPutSuccessCount",val,"Channel event数"));
            listItem.add(Triple.of("EventDrainSuccessCount",val,"Sink event数"));
            listItem.add(Triple.of("lastUpdateTime",val,"更新时间"));
        }
        else if (OnlineDataSourceType.IDOT_KAFKA.ordinal() == kind)
        {
            String val = StringUtils.join("org.apache.flume.channel:type=", id, "c1");
            listItem.add(Triple.of("EventPutSuccessCount",val,"Channel event数"));
            listItem.add(Triple.of("EventDrainSuccessCount",val,"Sink event数"));
            listItem.add(Triple.of("lastUpdateTime",val,"更新时间"));
        }
        
        return listItem;
    }

    /**
     * 原始文件监控项
     * @param fileId 文件id
     * @return
     */
    private List<Triple<String,String,String>> originalMonitorItem(String fileId)
    {
        List<Triple<String,String,String>> listItem = Lists.newArrayList();
        listItem.add(Triple.of("lastFileTime",fileId,"最新文件时间"));
        listItem.add(Triple.of("allFileNum",fileId,"文件数"));

        return listItem;
    }
    
    /**
     * 自定义采集jar监控项
     * @param jarId 部署jar id
     * @param fileId 采集文件配置id
     * @return Map<String, String> - 监控项配置
     */
    private List<Triple<String,String,String>> dataCollectMonitorItem(String jarId, String fileId)
    {
        List<Triple<String,String,String>> listItem = Lists.newArrayList();
        String val = StringUtils.join(jarId, "_", fileId);
        listItem.add(Triple.of("lastUpdateTime",val,"更新时间"));
        listItem.add(Triple.of("fileNum",val,"处理文件数"));
        listItem.add(Triple.of("recordNum",val,"处理文件数"));
        
        return listItem;
    }
    
    /**
     * 数据处理监控
     * @param jarId 部署jar id
     * @return Map<String, String> - 监控项配置
     */
    private List<Triple<String,String,String>> dataHandleMonitorItem(String jarId)
    {
        List<Triple<String,String,String>> listItem = Lists.newArrayList();
        listItem.add(Triple.of("lastUpdateTime", jarId,"更新时间"));
        listItem.add(Triple.of("originalNum", jarId,"处理原始记录数"));
        listItem.add(Triple.of("validNum", jarId,"处理有效记录数"));
        
        return listItem;
    }
    
}