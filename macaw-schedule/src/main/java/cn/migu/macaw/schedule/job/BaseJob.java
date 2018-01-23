package cn.migu.macaw.schedule.job;

import org.quartz.JobExecutionContext;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Quartz任务的基类
 * 
 * @author soy
 */
public class BaseJob
{

    /**
     * 获取Spring注入的Bean，需要修改Spring的配置文件
     *
     */
    public Object getBean(JobExecutionContext context, String bean)
    {
        Object obj = null;
        try
        {
            //获取JobExecutionContext中的service对象
            SchedulerContext skedCtx = context.getScheduler().getContext();
            obj = skedCtx.get(bean);
        }
        catch (SchedulerException e)
        {
            e.printStackTrace();
        }
        return obj;
    }


    /**
     *
     * 获取运行时job名
     * @param context
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String getJobNameMsg(JobExecutionContext context)
    {
        JobDetailImpl job = (JobDetailImpl)context.getJobDetail();
        return job.getName();
    }
    
}
