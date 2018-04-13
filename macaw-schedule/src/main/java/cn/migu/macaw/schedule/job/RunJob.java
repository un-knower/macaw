package cn.migu.macaw.schedule.job;

import java.util.UUID;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import cn.migu.macaw.common.ThreadUtilities;
import cn.migu.macaw.schedule.service.IJobTasksService;
import cn.migu.macaw.schedule.workflow.DataConstants;

/**
 * 
 * 系统定时任务入口
 * 
 * @author soy
 */
@DisallowConcurrentExecution
public class RunJob extends BaseJob implements InterruptableJob
{
    
    /**
     * 与job运行绑定的线程
     */
    private volatile Thread thread;
    
    private IJobTasksService jobTasksService;
    
    private String jobCode;
    
    @Override
    public void execute(JobExecutionContext context)
        throws JobExecutionException
    {
        //1.线程对象初始化
        thread = Thread.currentThread();
        
        //2.获取定时任务id
        String jobCode = this.getJobNameMsg(context);
        
        //3.执行job
        String batchNo = UUID.randomUUID().toString().replace("-", "");
        
        this.jobCode = jobCode;
        
        //4.从上下文中得到jobService实例
        IJobTasksService jobTasksService = (IJobTasksService)getBean(context, "jobTasksService");
        
        this.jobTasksService = jobTasksService;
        
        try
        {
            jobTasksService.jobRun(jobCode, batchNo);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            thread = null;
        }
        
    }
    
    @Override
    public void interrupt()
        throws UnableToInterruptJobException
    {
        if (thread != null)
        {
            //判断线程是否存在
            Thread t = ThreadUtilities.getThread(thread.getId());
            if (null != t)
            {
                this.jobTasksService.setJobTaskCtxFlag(this.jobCode, "", DataConstants.JOB_INTERRUPT_FLAG, "1");
                
                thread.interrupt();
                
                //this.jobTasksService.closeHttpReq(this.jobCode);
            }
            
        }
        
    }
    
}
