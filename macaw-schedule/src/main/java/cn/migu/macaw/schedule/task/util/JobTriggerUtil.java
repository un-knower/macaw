package cn.migu.macaw.schedule.task.util;

import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import cn.migu.macaw.schedule.service.IJobLogService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Maps;

import cn.migu.macaw.common.RestTemplateProvider;
import cn.migu.macaw.common.ServiceUrlProvider;
import cn.migu.macaw.common.SysRetCode;
import cn.migu.macaw.common.message.Response;
import cn.migu.macaw.schedule.api.model.JobLog;
import cn.migu.macaw.schedule.api.model.NodeLog;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.dao.JobLogMapper;
import cn.migu.macaw.schedule.dao.NodeLogMapper;
import cn.migu.macaw.schedule.service.IJobCommService;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import cn.migu.macaw.schedule.workflow.DataConstants;

/**
 * 任务启动触发工具类
 * 
 * @author soy
 */
@Component("jobTriggerUtil")
public class JobTriggerUtil
{
    /**
     * 等待次数
     */
    private final String WAIT_TIME = "wait_time";
    
    /**
     * 等待时间间隔(单位:秒)
     */
    private final String WAIT_INTERVAL = "wait_interval";

    /**
     * 触发调用job重试数
     */
    private final int JOB_TRIGGER_RETRY_TIME = 5;
    
    @Resource
    private RunningResMgr runningResMgr;
    
    @Resource
    private IJobCommService jobCommService;
    
    @Resource
    private JobTasksCache jobCtxCache;
    
    @Resource
    private JobLogMapper jobLogDao;

    @Autowired
    private NodeLogMapper nodeLogDao;
    
    @Autowired
    private IJobLogService jobLogService;
    
    @Resource(name = "restTemplateForLoadBalance")
    private RestTemplate restTemplateForLoadBalance;
    
    public void triggerAndWait(TaskNodeBrief brief, String jobCode, Map<String, String> sysParams, boolean isProcedure,
        String relateProName, String relateParentProName)
        throws Exception
    {
        String parentJobName = brief.getJobCode();
        String subJobName = jobCode;
        String logLabel = "普通任务";
        
        if (isProcedure)
        {
            parentJobName = relateParentProName;
            subJobName = relateProName;
            logLabel = "存储过程";
        }
        
        Map<String, String> formParam = Maps.newHashMap();
        formParam.put("name", jobCode);
        
        Response resp = RestTemplateProvider.postFormForEntity(restTemplateForLoadBalance,
            ServiceUrlProvider.jobScheduleService("triggerJob.do"),
            Response.class,
            formParam);
        
        String code = resp.getResponse().getCode();
        
        int waitTimes = 0;
        while (StringUtils.equals(code, SysRetCode.JOB_ALREADY_TRIGGERED))
        {
            if (waitTimes >= 240)
            {
                throw new TimeoutException("调用" + logLabel + subJobName + "超时");
            }
            Thread.sleep(30000);
            resp = RestTemplateProvider.postFormForEntity(restTemplateForLoadBalance,
                ServiceUrlProvider.jobScheduleService("triggerJob.do"),
                Response.class,
                formParam);
            code = resp.getResponse().getCode();
            waitTimes++;
        }
        
        if (StringUtils.equals(code, SysRetCode.SUCCESS))
        {
            //保证job已被调用
            Thread.sleep(3000);
            
            int realState = jobCommService.getJobRealState(jobCode);
            for (int i = 0; i < JOB_TRIGGER_RETRY_TIME; i++)
            {
                if (0 != realState)
                {
                    break;
                }
                
                Thread.sleep(3000);
                
                realState = jobCommService.getJobRealState(jobCode);
            }
            
            if (0 == realState)
            {
                throw new RuntimeException(StringUtils.join(logLabel, "[", subJobName, "]被阻塞执行"));
            }
            
            if (isProcedure)
            {
                this.updateProcNodeRelateBatchNo(brief.getJobCode(), jobCode, brief.getNodeId());
            }
            
            runningResMgr.addSubJobRecord(brief.getJobCode(), jobCode);
            
            String waitTimeStr = sysParams.get(WAIT_TIME);
            int waitTime = NumberUtils.isNumber(waitTimeStr) ? Integer.valueOf(waitTimeStr) : 720;
            
            String waitIntervalStr = sysParams.get(WAIT_INTERVAL);
            long waitInterval = NumberUtils.isNumber(waitIntervalStr) ? Integer.valueOf(waitIntervalStr) : 10;
            
            ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("等待", logLabel, "[", subJobName, "]执行中......"));
            
            for (int i = 0; i < waitTime; i++)
            {
                if (jobCtxCache.contains(jobCode))
                {
                    Thread.sleep(waitInterval * 1000);
                }
                else
                {
                    JobLog tJobLog = jobLogService.getRecentJobLog(jobCode);
                    int state = tJobLog.getState();
                    
                    runningResMgr.delSubJobRecord(brief.getJobCode(), jobCode);
                    
                    switch (state)
                    {
                        //正常结束
                        case 1:
                            String succMsg =
                                StringUtils.join(logLabel, parentJobName, "调用", logLabel, subJobName, "结束");
                            jobCtxCache.append(brief.getJobCode(),
                                brief.getNodeId(),
                                DataConstants.NODE_RUNNING_TRACE,
                                succMsg);
                            
                            ScheduleLogTrace.scheduleInfoLog(brief, succMsg);
                            return;
                        //异常结束
                        case 3:
                            throw new RuntimeException(StringUtils
                                .join(logLabel, "[", parentJobName, "]", "调用", logLabel, "[", subJobName, "]运行失败"));
                        default:
                            ScheduleLogTrace.scheduleWarnLog(brief,
                                StringUtils.join(logLabel,
                                    "[",
                                    parentJobName,
                                    "]",
                                    "调用",
                                    logLabel,
                                    "[",
                                    subJobName,
                                    "]结束,但返回状态(",
                                    String.valueOf(state),
                                    ")不正确!!"));
                            return;
                    }
                }
                
            }
            
            throw new RuntimeException(StringUtils.join(logLabel,
                "调用[",
                parentJobName,
                "->",
                subJobName,
                "]超时,等待时间为",
                String.valueOf(waitTime * waitInterval),
                "s"));
        }
        else
        {
            throw new RuntimeException(StringUtils
                .join(logLabel, "调用[", parentJobName, "->", subJobName, "]异常:", resp.getResponse().getDesc()));
        }
    }
    
    /**
     * 更新存储过程关联job运行批次号
     * @param parentJobCode 父节点任务编码
     * @param subJobCode 子节点任务编码
     * @param nodeCode 节点编码
     */
    private void updateProcNodeRelateBatchNo(String parentJobCode, String subJobCode, String nodeCode)
    {
        JobLog tJobLog = jobLogService.getRecentJobLog(subJobCode);
        
        if (null != tJobLog)
        {
            NodeLog nl = jobLogService.getRecentNodeLog(parentJobCode, nodeCode);
            
            if (null != nl)
            {
                nl.setInput(tJobLog.getObjId());
                nodeLogDao.updateByPrimaryKeySelective(nl);
            }
            
        }
    }
}
