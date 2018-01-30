package cn.migu.macaw.schedule.task.bean;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.migu.macaw.schedule.service.IJobLogService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.migu.macaw.common.DateUtil;
import cn.migu.macaw.schedule.dao.JobLogMapper;
import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.api.model.Job;
import cn.migu.macaw.schedule.api.model.JobLog;
import cn.migu.macaw.schedule.service.IJobCommService;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.util.ConfigParamUtil;
import cn.migu.macaw.schedule.task.util.QuartzCronUtil;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;

import com.google.common.collect.Maps;

/**
 * 等待多job任务完成task
 * 
 * @author soy
 */
@Component("multiJobJoinTask")
public class MultiJobJoinTask implements ITask
{
    
    @Resource
    private IJobCommService jobCommService;

    @Autowired
    private IJobLogService jobLogService;
    
    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private JobLogMapper jobLogDao;
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        Map<String, String> sysParams = configParamUtil.getJobNodeSysParams(brief);
        
        if (MapUtils.isEmpty(sysParams))
        {
            throw new IllegalArgumentException("请填写需要等待的job编码");
        }
        
        Set<String> jobCodes =
            sysParams.entrySet()
                .stream()
                .map(e -> e.getValue())
                .filter(v -> StringUtils.isNotEmpty(v))
                .collect(Collectors.toSet());
        
        if (CollectionUtils.isEmpty(jobCodes))
        {
            throw new IllegalArgumentException("请填写需要等待的job编码");
        }
        
        Map<String, Date[]> jobCronStr = Maps.newHashMap();
        
        for (String jobCode : jobCodes)
        {
            Job job = jobCommService.getJob(jobCode);
            
            if (null == job)
            {
                throw new IllegalStateException("任务不存在:" + jobCode);
            }
            
            if (job.getState() != 1)
            {
                throw new IllegalStateException("在任务列表中有任务的cron表达没有被触发运行:" + jobCode);
            }
            
            if (StringUtils.isEmpty(job.getCronExpression()))
            {
                throw new IllegalStateException("任务的cron表达式为空:" + jobCode);
            }
            
            Date[] clntm = QuartzCronUtil.parseTriggerRangeToDate(job.getCronExpression().trim());
            jobCronStr.put(jobCode, clntm);
        }
        
        String jobCronLogStr =
            jobCronStr.entrySet()
                .stream()
                .map(e -> StringUtils.join(e.getKey(), ":", e.getValue()[0], "-", e.getValue()[1]))
                .collect(Collectors.joining(","));
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("等待任务执行时间估计->", jobCronLogStr));
        
        while (jobCronStr.size() > 0)
        {
            Iterator<String> codes = jobCodes.iterator();
            while (codes.hasNext())
            {
                String code = codes.next();
                
                Date[] lntm = jobCronStr.get(code);
                
                if (null != lntm)
                {
                    
                    boolean isFinshedJob = true;
                    
                    JobLog log = jobLogService.getEarliestJobLogByStartTimeRange(code, lntm[0], lntm[1]);
                    if (null != log)
                    {
                        if (1 == log.getState().intValue())
                        {
                            jobCronStr.remove(code);
                        }
                        else if (3 == log.getState().intValue())
                        {
                            throw new IllegalStateException("任务列表中有任务运行已经异常:" + code);
                        }
                        else
                        {
                            isFinshedJob = false;
                        }
                    }
                    else
                    {
                        isFinshedJob = false;
                    }
                    
                    if (!isFinshedJob)
                    {
                        int isOverLastTime = DateUtil.compareCurrentDate(lntm[1]);
                        if (isOverLastTime <= 0)
                        {
                            String errMsg = StringUtils.join("等待任务列表中有任务[", code, "]当前时间超过下次执行时间", lntm[1]);
                            throw new IllegalStateException(errMsg);
                        }
                    }
                    
                }
            }
            
            if (jobCronStr.size() > 0)
            {
                Thread.sleep(10000);
            }
            
        }
    }
}
