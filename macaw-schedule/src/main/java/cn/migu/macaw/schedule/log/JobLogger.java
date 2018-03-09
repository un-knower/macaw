package cn.migu.macaw.schedule.log;

import java.util.Arrays;

import javax.annotation.Resource;

import cn.migu.macaw.schedule.api.model.Job;
import cn.migu.macaw.schedule.api.model.JobLog;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.schedule.service.IJobCommService;
import cn.migu.macaw.schedule.service.IJobLogService;
import cn.migu.macaw.schedule.service.IJobTasksService;

/**
 * 
 * 定时任务日志
 *
 * @author soy
 */
@Aspect
@Component
public class JobLogger
{
    @Resource
    private IJobLogService jobLogService;
    
    @Resource
    private IJobCommService jobCommService;
    
    @Resource
    private IJobTasksService jobTasksService;
    
    /**
     * 
     * 定义job日志入库任务
     * @see [类、类#方法、类#成员]
     */
    @Pointcut("execution(public void cn.migu.macaw.schedule.service.impl.JobTasksServiceImpl.jobRun(String,String))")
    public void jobRun()
    {
    }
    
    /**
     * 
     * 对每一个定时任务记录日志
     * <功能详细描述>
     * @return
     * @throws Exception 
     * @see [类、类#方法、类#成员]
     */
    @Around("jobRun()")
    public Object aroundJobLogger(ProceedingJoinPoint pjp)
        throws Exception
    {
        Object obj = null;
        String jobCode = null;
        String batchCode = null;
        
        Job job = null;
        
        JobLog jobLog = null;
        
        try
        {
            jobCode = (String)Arrays.asList(pjp.getArgs()).get(0);
            batchCode = (String)Arrays.asList(pjp.getArgs()).get(1);
            
            job = jobCommService.getJob(jobCode);
            
            jobLog = jobLogService.initJobLog(job, batchCode);
            
            //运行job任务
            obj = pjp.proceed();
            
            jobLogService.successJobLog(jobLog, job,batchCode);
            
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            String errMsg = ExceptionUtils.getStackTrace(e);
            LogUtils.runLogError(errMsg);
            
            jobLogService.excepJobLog(jobLog, job,batchCode, jobCode, errMsg);
            
        }
        finally
        {
            jobTasksService.freeResource(jobCode, batchCode);
        }
        
        return obj;
    }
    
}
