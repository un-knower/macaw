package cn.migu.macaw.schedule.quartz;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.spi.MutableTrigger;
import org.springframework.stereotype.Component;

/**
 * quartz job调度模板类
 * 提供job运行、暂停等功能
 * 
 * @author  soy
 */
@Component("schedulerTemplate")
public class SchedulerTemplate
{
    /**
     * quartz调度实例
     */
    @Resource
    private Scheduler scheduler;

    /**
     *  job默认组
     */
    public static final String JOB_DEFAULT_GROUP = "macaw_group";
    
    /**
     * 创建一个调度任务
     * @param jobDetail
     * @param trigger
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Date scheduleJob(JobDetail jobDetail, Trigger trigger)
    {
        try
        {
            return scheduler.scheduleJob(jobDetail, trigger);
        }
        catch (SchedulerException e)
        {
            throw new QuartzRuntimeException(e);
        }
    }
    
    /**
     * 删除定时任务
     * @param name
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean deleteJob(String name)
    {
        try
        {
            return scheduler.deleteJob(JobKey.jobKey(name, JOB_DEFAULT_GROUP));
        }
        catch (SchedulerException e)
        {
            throw new QuartzRuntimeException(e);
        }
    }
    
    /**
     * 修改已存在的jobdetail
     * @param newJobDetail
     * @return 原来的jobdetail
     * @see [类、类#方法、类#成员]
     */
    public JobDetail updateJobDetail(JobDetail newJobDetail)
    {
        try
        {
            JobDetail oldJob = scheduler.getJobDetail(newJobDetail.getKey());
            scheduler.addJob(newJobDetail, true);
            return oldJob;
        }
        catch (SchedulerException e)
        {
            throw new QuartzRuntimeException(e);
        }
    }
    
    /**
     * 修改job触发器
     * @param newTrigger
     * @return 原来的job触发器
     * @see [类、类#方法、类#成员]
     */
    public Trigger updateTrigger(Trigger newTrigger)
    {
        try
        {
            Trigger oldTrigger = scheduler.getTrigger(newTrigger.getKey());
            scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);
            return oldTrigger;
        }
        catch (SchedulerException e)
        {
            throw new QuartzRuntimeException(e);
        }
    }
    
    /**
     * 暂停任务
     * @param jobKey
     * @see [类、类#方法、类#成员]
     */
    public void pauseJob(String jobKey)
    {
        try
        {
            scheduler.pauseJob(JobKey.jobKey(jobKey, JOB_DEFAULT_GROUP));
        }
        catch (SchedulerException e)
        {
            throw new QuartzRuntimeException(e);
        }
    }
    
    /**
     * 恢复任务
     * @param jobKey
     * @see [类、类#方法、类#成员]
     */
    public void resumeJob(String jobKey)
    {
        try
        {
            scheduler.resumeJob(JobKey.jobKey(jobKey, JOB_DEFAULT_GROUP));
        }
        catch (SchedulerException e)
        {
            throw new QuartzRuntimeException(e);
        }
    }
    
    /**
     * 立即触发任务执行
     * @param jobKey
     * @see [类、类#方法、类#成员]
     */
    public void triggerJob(String jobKey)
    {
        try
        {
            scheduler.triggerJob(JobKey.jobKey(jobKey, JOB_DEFAULT_GROUP));
        }
        catch (SchedulerException e)
        {
            throw new QuartzRuntimeException(e);
        }
    }
    
    /**
     * 中断执行任务
     * @param jobKey
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean interrupt(String jobKey)
    {
        try
        {
            return scheduler.interrupt(JobKey.jobKey(jobKey, JOB_DEFAULT_GROUP));
        }
        catch (SchedulerException e)
        {
            throw new QuartzRuntimeException(e);
        }
    }
    
    /**
     * 开启cron表达式触发的任务
     * @param jobKey
     * @param cron
     * @param jobClass
     * @param dataMap
     * @param startTime
     * @param endTime
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Date scheduleCronJob(JobKey jobKey, String cron, Class<? extends Job> jobClass, Map<String, Object> dataMap,
        Date startTime, Date endTime)
    {
        JobDetail job = createJobDetail(jobKey, jobClass, true, dataMap);
        TriggerKey triggerKey = TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup());
        Trigger trigger = createCronTrigger(triggerKey, cron, startTime, endTime);
        return scheduleJob(job, trigger);
    }
    
    /**
     * 创建job detail
     * @param jobKey
     * @param jobClass
     * @param durable
     * @param dataMap
     * @return
     * @see [类、类#方法、类#成员]
     */
    public JobDetail createJobDetail(JobKey jobKey, Class<? extends Job> jobClass, boolean durable,
        Map<String, Object> dataMap)
    {
        JobDetail jobDetail = newJob(jobClass).withIdentity(jobKey).storeDurably(durable).requestRecovery().build();
        if (dataMap != null)
        {
            jobDetail.getJobDataMap().putAll(dataMap);
        }

        return jobDetail;
    }
    
    /**
     * 创建定时器
     * @param triggerKey
     * @param cron
     * @param startTime
     * @param endTime
     * @return
     * @see [类、类#方法、类#成员]
     */
    public MutableTrigger createCronTrigger(TriggerKey triggerKey, String cron, Date startTime, Date endTime)
    {
        if (startTime == null)
        {
            startTime = new Date();
        }

        try
        {
            CronTrigger trigger =
                newTrigger().withIdentity(triggerKey)
                    .startAt(startTime)
                    .endAt(endTime)
                    .withSchedule(cronSchedule(cron))
                    .build();
            return (MutableTrigger)trigger;
        }
        catch (RuntimeException e)
        {
            throw new QuartzRuntimeException(e);
        }
    }
    
    /**
     * 创建任务
     * @param name 任务名称
     * @param cron 触发表达式
     * @param jobClass
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Date scheduleCronJob(String name, String cron, Class<? extends Job> jobClass)
    {
        return scheduleCronJob(JobKey.jobKey(name, JOB_DEFAULT_GROUP), cron, jobClass, null, new Date(), null);
    }
    
    /**
     * 创建任务
     * @param name 任务名称
     * @param cron 触发表达式
     * @param jobClass 任务类
     * @param dataMap 任务参数
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Date scheduleCronJob(String name, String cron, Class<? extends Job> jobClass, Map<String, Object> dataMap)
    {
        return scheduleCronJob(JobKey.jobKey(name, JOB_DEFAULT_GROUP), cron, jobClass, dataMap, new Date(), null);
    }
    
    /**
     * 创建任务
     * @param name 任务名称
     * @param cron 触发表达式
     * @param jobClass 任务类
     * @param dataMap 任务参数
     * @param startTime 开始时间
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Date scheduleCronJob(String name, String cron, Class<? extends Job> jobClass, Map<String, Object> dataMap,
        Date startTime)
    {
        return scheduleCronJob(JobKey.jobKey(name, JOB_DEFAULT_GROUP), cron, jobClass, dataMap, startTime, null);
    }
    
}
