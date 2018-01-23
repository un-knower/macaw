package cn.migu.macaw.schedule.util;

import java.util.UUID;

import cn.migu.macaw.schedule.api.model.Job;
import cn.migu.macaw.schedule.api.model.JobLog;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;

import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.schedule.service.IJobLogService;
import cn.migu.macaw.schedule.service.IJobTasksService;

/**
 * 局部job执行线程
 * 
 * @author soy
 */
public class SpecJobPartRunThread implements Runnable
{
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
            
            jobLogService.successJobLog(jobLog, job);
        }
        catch (Exception e)
        {
            String errMsg = ExceptionUtils.getStackTrace(e);
            LogUtils.runLogError(errMsg);
            
            jobLogService.excepJobLog(jobLog, job, job.getCode(), errMsg);
        }
        finally
        {
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
            
            jobLogService.successJobLog(jobLog, job);
        }
        catch (Exception e)
        {
            String errMsg = ExceptionUtils.getStackTrace(e);
            LogUtils.runLogError(errMsg);
            
            jobLogService.excepJobLog(jobLog, job, job.getCode(), errMsg);
        }
        finally
        {
            jobTasksService.freeResource(job.getCode(), batchNo);
        }
    }
    
}
