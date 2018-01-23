package cn.migu.macaw.schedule.service.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.migu.macaw.schedule.api.model.Job;
import cn.migu.macaw.schedule.api.model.ProcVariableMap;
import cn.migu.macaw.schedule.api.model.Procedure;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.migu.macaw.common.log.InterfaceLog;
import cn.migu.macaw.common.log.InterfaceLogBean;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.common.RemoteIpHelper;
import cn.migu.macaw.schedule.dao.JobMapper;
import cn.migu.macaw.schedule.dao.ProcVariableMapMapper;
import cn.migu.macaw.schedule.dao.ProcedureMapper;
import cn.migu.macaw.schedule.service.IJobCommService;

/**
 * 提供调度任务公共方法
 * 
 * @author soy
 */
@Service("jobCommService")
public class JobCommServiceImpl implements IJobCommService
{
    
    private static final Log jobLogger = LogFactory.getLog("job");
    
    @Resource
    private JdbcTemplate jdbcTemplate;
    
    @Resource
    private JobMapper jobDao;
    
    @Resource
    private ProcedureMapper procDao;
    
    @Resource
    private ProcVariableMapMapper tmpVarsDao;

    /**
     * 根据任务编码获取存储过程
     * @param jobCode
     * @return
     */
    @Override
    public Procedure getProcedure(String jobCode)
    {
        Procedure proc = new Procedure();
        proc.setJobCode(jobCode);
        return procDao.selectOne(proc);
    }
    
    /**
     * 存储过程临时变量-外部输入
     * @param request
     * @param job
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void addProcTmpVars(HttpServletRequest request, Job job)
    {
        if (null == job || 0 != job.getKind())
        {
            return;
        }
        Map<String, String[]> params = request.getParameterMap();
        
        Map<String, String> extraParams = params.entrySet()
            .stream()
            .filter(x -> StringUtils.isNotEmpty(x.getKey()) && !StringUtils.equals(x.getKey(), "name"))
            .collect(Collectors.toMap(Entry::getKey, e -> e.getValue()[0]));
        
        ProcVariableMap procTmpVars = new ProcVariableMap();
        procTmpVars.setJobCode(job.getCode());
        
        Procedure proc = this.getProcedure(job.getCode());
        procTmpVars.setProcCode(proc.getCode());
        
        extraParams.entrySet().forEach(set -> {
            procTmpVars.setVariCode(set.getKey());
            procTmpVars.setDefaultValue(set.getValue());
            tmpVarsDao.insertSelective(procTmpVars);
        });
    }
    
    /**
     * 任务已经被立即执行
     * @param jobName 任务编码
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public boolean isAlreadyTriggerRun(String jobName)
    {
        String querySql = StringUtils
            .join("select count(1) from qrtz_triggers where job_name='", jobName, "' and trigger_type='SIMPLE'");
        
        try
        {
            int count = jdbcTemplate.queryForObject(querySql, Integer.class);
            
            if (count >= 1)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
        }
        
        return false;
    }
    
    /**
     * 查找定时任务
     * @param name
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Job getJob(String name)
    {
        if (StringUtils.isEmpty(name))
        {
            return null;
        }
        
        Job condition = new Job();
        condition.setCode(name);
        
        return jobDao.selectOne(condition);
    }
    
    /**
     * job实时状态
     * @param jobCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public int getJobRealState(String jobCode)
    {
        Job job = this.getJob(jobCode);
        
        if (null != job)
        {
            return Integer.valueOf(job.getRealState());
        }
        
        return 0;
    }

    /**
     * 调度接口日志
     * @param request
     * @param key
     * @param content
     */
    @Override
    public void jobLog(HttpServletRequest request, String key, String content)
    {
        String ipAddress = RemoteIpHelper.getRemoteIpFrom(request);
        String hostName = request.getRemoteHost();
        
        InterfaceLogBean logBean = new InterfaceLogBean();
        
        logBean.setReqResIdent(key);
        logBean.setInterfaceName(request.getRequestURI());
        logBean.setSystemModuleName("schedule-interface");
        logBean.setMessage(this.getReqParams(request));
        logBean.setUserId(StringUtils.join(ipAddress, "@", hostName));
        
        if (null != content)
        {
            logBean.setReturnInfo(content);
        }
        
        InterfaceLog.getInstance().info(jobLogger, logBean);
    }
    
    /**
     * 获取请求参数
     * @param request
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getReqParams(HttpServletRequest request)
    {
        StringBuffer reqParams = new StringBuffer();
        
        Map<String, String[]> params = request.getParameterMap();
        Iterator<Entry<String, String[]>> it = params.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<String, String[]> entry = it.next();
            if (null != entry.getValue() && entry.getValue().length > 0)
            {
                reqParams.append(entry.getKey()).append("=").append(entry.getValue()[0]).append(",");
            }
        }
        
        return reqParams.toString();
    }
}
