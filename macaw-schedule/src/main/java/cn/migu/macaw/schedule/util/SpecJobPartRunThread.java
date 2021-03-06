package cn.migu.macaw.schedule.util;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;

import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.schedule.api.model.Job;
import cn.migu.macaw.schedule.api.model.JobLog;
import cn.migu.macaw.schedule.service.IJobLogService;
import cn.migu.macaw.schedule.service.IJobTasksService;
import cn.migu.macaw.schedule.workflow.DataConstants;

/**
 * 局部job执行线程
 * 
 * @author soy
 */
public class SpecJobPartRunThread implements Runnable
{
    /**
     * 单节点或指定节点执行
     * 线程计数器
     */
    static final AtomicInteger NODE_EXECUTOR_THREAD_LIMIT = new AtomicInteger(0);
    
    private Job job;
    
    private String nodeCode;
    
    private String type;
    
    private IJobLogService jobLogService;
    
    private IJobTasksService jobTasksService;
    
    public SpecJobPartRunThread(Job job, String nodeCode, String type, IJobLogService jobLogService,
        IJobTasksService jobTasksService)
    {
        this.job = job;
        this.nodeCode = nodeCode;
        this.type = type;
        this.jobLogService = jobLogService;
        this.jobTasksService = jobTasksService;
    }
    
    @Override
    public void run()
    {
        AtomicInteger counter = NODE_EXECUTOR_THREAD_LIMIT;
        if (counter.get() >= DataConstants.MAX_NODE_THREAD)
        {
            throw new IllegalStateException("threads num exceeds the maximum limit");
        }
        else
        {
            NODE_EXECUTOR_THREAD_LIMIT.getAndIncrement();
        }
        
        switch (type)
        {
            case "region":
                this.runRegion();
                break;
            case "single":
                this.runSingle();
                break;
            default:
                break;
        }
    }
    
    /**
     * 从指定节点执行job
     * @see [类、类#方法、类#成员]
     */
    private void runRegion()
    {
        String batchNo = StringUtils.join("region_", UUID.randomUUID().toString().replace("-", ""));
        
        JobLog jobLog = jobLogService.initJobLog(job, batchNo);
        
        try
        {
            jobTasksService.runSpecRegion(this.job.getCode(), nodeCode, batchNo);
            
            jobLogService.successJobLog(jobLog, job, batchNo);
        }
        catch (Exception e)
        {
            String errMsg = ExceptionUtils.getStackTrace(e);
            LogUtils.runLogError(errMsg);
            
            jobLogService.excepJobLog(jobLog, job, batchNo, job.getCode(), errMsg);
        }
        finally
        {
            NODE_EXECUTOR_THREAD_LIMIT.getAndDecrement();
            jobTasksService.freeResource(job.getCode(), batchNo);
        }
    }
    
    /**
     * 单节点执行
     * @see [类、类#方法、类#成员]
     */
    private void runSingle()
    {
        String batchNo = StringUtils.join("single_", UUID.randomUUID().toString().replace("-", ""));
        
        JobLog jobLog = jobLogService.initJobLog(job, batchNo);
        
        try
        {
            jobTasksService.runSingleNode(this.job.getCode(), nodeCode, batchNo);
            
            jobLogService.successJobLog(jobLog, job, batchNo);
        }
        catch (Exception e)
        {
            String errMsg = ExceptionUtils.getStackTrace(e);
            LogUtils.runLogError(errMsg);
            
            jobLogService.excepJobLog(jobLog, job, batchNo, job.getCode(), errMsg);
        }
        finally
        {
            NODE_EXECUTOR_THREAD_LIMIT.getAndDecrement();
            jobTasksService.freeResource(job.getCode(), batchNo);
        }
    }
    
}
