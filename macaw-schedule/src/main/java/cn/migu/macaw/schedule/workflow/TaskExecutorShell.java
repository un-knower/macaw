package cn.migu.macaw.schedule.workflow;

import cn.migu.macaw.common.ApplicationContextProvider;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;

import com.nirmata.workflow.WorkflowManager;
import com.nirmata.workflow.executor.TaskExecution;
import com.nirmata.workflow.executor.TaskExecutionStatus;
import com.nirmata.workflow.executor.TaskExecutor;
import com.nirmata.workflow.models.ExecutableTask;
import com.nirmata.workflow.models.TaskExecutionResult;

import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.service.IJobTasksService;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;

/**
 * task执行模板
 * @author soy
 */
public class TaskExecutorShell implements TaskExecutor
{
    private final IdDag<String> dag;
    
    public TaskExecutorShell(IdDag<String> dag)
    {
        this.dag = dag;
    }
    
    @Override
    public TaskExecution newTaskExecution(WorkflowManager wfManager, ExecutableTask execTask)
    {
        
        return new TaskExecution()
        {
            @Override
            public TaskExecutionResult execute()
            {
                String nodeId = execTask.getTaskId().getId();
                
                String type = execTask.getMetaData().get(DataConstants.TASK_NODE_TYPE);
                
                String jobCode = execTask.getMetaData().get(DataConstants.JOB_CODE);

                String batchNo = execTask.getMetaData().get(DataConstants.BATCH_NO);
                
                if (StringUtils.isEmpty(jobCode))
                {
                    return new TaskExecutionResult(TaskExecutionStatus.FAILED_STOP, "NEXT");
                }
                
                ScheduleLogTrace.scheduleInfoLog(jobCode, batchNo, StringUtils.join(nodeId,"开始被调度"));
                
                //判断是否取消执行
                JobTasksCache jobTasksCache = (JobTasksCache)ApplicationContextProvider.getBean("jobTasksCache");
                
                String isCustomCancel = jobTasksCache.get(jobCode, nodeId, DataConstants.CUSTOM_CANCEL_RUN);
                if (StringUtils.equals(isCustomCancel, DataConstants.VALID))
                {
                    ScheduleLogTrace.scheduleInfoLog(jobCode, batchNo, StringUtils.join(nodeId,"被取消执行[配置]"));
                    
                    return new TaskExecutionResult(TaskExecutionStatus.FAILED_CONTINUE, "NEXT");
                }
                
                String isCancel = jobTasksCache.get(jobCode, nodeId, DataConstants.CANCEL_RUN);
                if (StringUtils.equals(isCancel, DataConstants.VALID))
                {
                    ScheduleLogTrace.scheduleInfoLog(jobCode, batchNo, StringUtils.join(nodeId,"被取消执行[决策]"));
                    
                    return new TaskExecutionResult(TaskExecutionStatus.FAILED_CONTINUE, "NEXT");
                }
                
                //terminate task:job流程直接终止
                if (StringUtils.equals(type, DataConstants.TASK_TYPE_DIRECT_TERMINATE))
                {
                    ScheduleLogTrace.scheduleInfoLog(jobCode, batchNo, StringUtils.join(nodeId,"直接终止"));
                    return new TaskExecutionResult(TaskExecutionStatus.FAILED_STOP, "NEXT");
                }
                
                IJobTasksService jobTasksService = (IJobTasksService)ApplicationContextProvider.getBean("jobTasksService");
                
                if (null != jobTasksService)
                {
                    try
                    {
                        String taskCode = execTask.getMetaData().get(DataConstants.TASK_CODE);
                        String taskClass = execTask.getMetaData().get(DataConstants.TASK_NODE_TYPE);
                        TaskNodeBrief brief = new TaskNodeBrief(jobCode, nodeId, taskCode, batchNo);
                        brief.setTaskClassType(taskClass);
                        
                        jobTasksService.runNode(brief, dag, type);
                        
                        ScheduleLogTrace.scheduleInfoLog(jobCode, batchNo, StringUtils.join(nodeId,"执行结束"));
                    }
                    catch (Throwable e)
                    {
                        LogUtils.runLogError(e);
                        String err = ExceptionUtils.getStackTrace(e);
                        ScheduleLogTrace.scheduleWarnLog(jobCode, batchNo,StringUtils.join(nodeId,"执行异常:", err));
                        
                        if (!jobTasksService.isResidualCtx(jobCode))
                        {
                            jobTasksService.setJobTaskCtxFlag(jobCode, "", DataConstants.JOB_EXCEP_FLAG, "1");
                            jobTasksService.setJobTaskCtxFlag(jobCode, "", DataConstants.JOB_EXCEP_MSG, err);
                        }
                        
                        return new TaskExecutionResult(TaskExecutionStatus.FAILED_STOP, "NEXT");
                    }
                    
                }
                else
                {
                    LogUtils.runLogError("jobTasksService不能初始化");
                    return new TaskExecutionResult(TaskExecutionStatus.FAILED_STOP, "NEXT");
                }
                
                return new TaskExecutionResult(TaskExecutionStatus.SUCCESS, "NEXT");
            }
        };
    }
    
}
