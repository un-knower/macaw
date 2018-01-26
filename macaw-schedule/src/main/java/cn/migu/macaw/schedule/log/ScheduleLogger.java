package cn.migu.macaw.schedule.log;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;

import cn.migu.macaw.schedule.api.model.Job;
import cn.migu.macaw.schedule.api.model.NodeLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.schedule.dao.NodeLogMapper;
import cn.migu.macaw.schedule.service.IJobCommService;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.workflow.DataConstants;

/**
 * 
 * 节点调度日志
 *
 * @author soy
 */
@Aspect
@Component
public class ScheduleLogger
{
    
    @Resource
    private NodeLogMapper nodeLogDao;
    
    @Resource
    private JobTasksCache jobTasksCache;
    
    @Resource
    private IJobCommService jobCommService;

    
    /**
     * 
     * 定义生产消息日志入库任务
     * @see [类、类#方法、类#成员]
     */
    @Pointcut("execution(public void cn.migu.macaw.schedule.service.impl.JobTasksServiceImpl.runNode(cn.migu.macaw.schedule.task.TaskNodeBrief,cn.migu.macaw.dag.idgraph.IdDag,String))")
    public void scheduleAddLog()
    {
    }
    
    /**
     * 
     * 对每一个node记录日志
     * <功能详细描述>
     * @return
     * @throws Throwable 
     * @see [类、类#方法、类#成员]
     */
    @Around("scheduleAddLog()")
    public Object aroundSchdeduleLogger(ProceedingJoinPoint pjp)
        throws Throwable
    {
        Object obj = null;
        TaskNodeBrief brief = null;
        NodeLog nodeLog = new NodeLog();
        
        long start = System.currentTimeMillis();
        
        try
        {
            brief = (TaskNodeBrief)Arrays.asList(pjp.getArgs()).get(0);
            
            nodeLog.setTaskCode(brief.getTaskCode());
            nodeLog.setJobCode(brief.getJobCode());
            nodeLog.setNodeCode(brief.getNodeId());
            nodeLog.setStartTime(new Date());
            nodeLog.setBatchno(brief.getBatchCode());
            nodeLog.setState(State.RUNNING.ordinal());
            
            Job job = jobCommService.getJob(brief.getJobCode());
            nodeLog.setProjectCode(job.getProjectCode());
            
            //生成obj_id
            nodeLog.setObjId(UUID.randomUUID().toString().replace("-", ""));
            nodeLogDao.insertSelective(nodeLog);
            
            //重置节点运行时日志描述信息
            jobTasksCache.put(brief.getJobCode(), brief.getNodeId(), DataConstants.NODE_RUNNING_TRACE, "");
            
            //运行job任务
            obj = pjp.proceed();
            long end = System.currentTimeMillis();
            
            nodeLog.setState(State.TERMINATION.ordinal());
            nodeLog.setEndTime(new Date());
            nodeLog.setElapse(end - start);
            
            nodeLog.setNote(jobTasksCache.get(brief.getJobCode(), brief.getNodeId(), DataConstants.NODE_RUNNING_TRACE));
            
            if (StringUtils.isNotEmpty(nodeLog.getObjId()))
            {
                nodeLogDao.updateByPrimaryKeySelective(nodeLog);
            }
            else
            {
                LogUtils.runLogError("[正常结束]更新job_node_log时主键为空");
            }
            
        }
        catch (Throwable e)
        {
            String errMsg = ExceptionUtils.getStackTrace(e);

            brief = (TaskNodeBrief)Arrays.asList(pjp.getArgs()).get(0);
            
            String nodeRunningMsg =
                jobTasksCache.get(brief.getJobCode(), brief.getNodeId(), DataConstants.NODE_RUNNING_TRACE);
            
            nodeLog.setNote(StringUtils.join(nodeRunningMsg, "==>异常信息:", errMsg));
            
            nodeLog.setState(State.EXCEPTION.ordinal());
            nodeLog.setEndTime(new Date());
            long end = System.currentTimeMillis();
            nodeLog.setElapse(end - start);
            nodeLog.setBatchno(brief.getBatchCode());
            if (StringUtils.isNotEmpty(nodeLog.getObjId()))
            {
                nodeLogDao.updateByPrimaryKeySelective(nodeLog);
            }
            else
            {
                LogUtils.runLogError("[异常结束]更新job_node_log时主键为空");
            }

            
            throw e;
        }
        
        return obj;
    }
    
}
