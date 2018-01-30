package cn.migu.macaw.schedule.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import cn.migu.macaw.schedule.api.model.NodeLog;
import cn.migu.macaw.schedule.dao.*;
import cn.migu.macaw.schedule.log.State;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.migu.macaw.common.NetUtils;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.schedule.api.model.Job;
import cn.migu.macaw.schedule.api.model.JobLog;
import cn.migu.macaw.schedule.api.model.ProcLog;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.service.IJobCommService;
import cn.migu.macaw.schedule.service.IJobLogService;
import cn.migu.macaw.schedule.service.IJobTasksService;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import cn.migu.macaw.schedule.workflow.DataConstants;
import tk.mybatis.mapper.entity.Example;

/**
 * job日志实现
 * 
 * @author soy
 */
@Service("jobLogService")
public class JobLogServiceImpl implements IJobLogService
{
    @Resource
    private JobLogMapper jobLogDao;
    
    @Resource
    private JobMapper jobDao;

    @Autowired
    private NodeLogMapper nodeLogDao;
    
    @Resource
    private ProcVariableMapper procVarsDao;
    
    @Resource
    private ProcLogMapper procLogDao;
    
    @Resource
    private JobTasksCache jobTasksCache;
    
    @Resource
    private IJobTasksService jobTasksService;
    
    @Resource
    private IJobCommService jobCommService;

    
    /**
     * job日志初始化
     */
    @Override
    public JobLog initJobLog(Job job, String batchNo)
    {
        if (null == job)
        {
            return null;
        }
        
        JobLog jobLog = new JobLog();
        
        String localIp = this.getLocalIp();
        
        jobLog.setJobCode(job.getCode());
        jobLog.setBatchno(batchNo);
        jobLog.setStartTime(new Date());
        jobLog.setState(State.RUNNING.ordinal());
        jobLog.setProjectCode(job.getProjectCode());
        
        jobLog.setObjId(UUID.randomUUID().toString().replace("-", ""));
        
        jobLog.setGraphXml(job.getGraphXml());
        
        //jobLog.setSumDate(this.queryProcSumDate(job.getCode()));
        
        jobLog.setNote(this.getJobRunInfo(localIp, null));
        
        this.updateRealState(job, State.RUNNING.ordinal());
        
        jobLogDao.insertSelective(jobLog);
        
        ScheduleLogTrace.scheduleInfoLog(StringUtils.join("[", job.getCode(), "]-[", batchNo, "] ", "开始执行..."));
        
        this.updateJobCurrentBatchNo(job, batchNo);
        
        jobLog.setBeginDate(new Date());
        
        return jobLog;
    }
    
    /**
     * job成功运行后日志
     */
    @Override
    public void successJobLog(JobLog jobLog, Job job)
    {
        if (null == jobLog)
        {
            return;
        }
        
        //设置job状态为TERMINATION
        jobLog.setState(State.TERMINATION.ordinal());
        jobLog.setEndTime(new Date());
        Date sysDate = new Date();
        
        long consumptionTime = sysDate.getTime() - jobLog.getBeginDate().getTime();
        jobLog.setElapse(consumptionTime);
        
        String excepMsg = null;
        String excepFlag = jobTasksCache.get(jobLog.getJobCode(), "", DataConstants.JOB_EXCEP_FLAG);
        
        if (StringUtils.isNotEmpty(excepFlag))
        {
            excepMsg = jobTasksCache.get(jobLog.getJobCode(), "", DataConstants.JOB_EXCEP_MSG);
            jobLog.setState(State.EXCEPTION.ordinal());
            
            this.updateJobCount(job, false);
            
            this.updateRealState(job, State.EXCEPTION.ordinal());
            
        }
        else
        {
            String interruptFlag = jobTasksCache.get(jobLog.getJobCode(), "", DataConstants.JOB_INTERRUPT_FLAG);
            if (StringUtils.isNotEmpty(interruptFlag))
            {
                jobLog.setState(State.EXCEPTION.ordinal());
                
                this.updateJobCount(job, false);
                
                this.updateRealState(job, State.EXCEPTION.ordinal());
                
                excepMsg = "中断执行";
            }
            else
            {
                this.updateJobCount(job, true);
                
                this.updateRealState(job, State.TERMINATION.ordinal());
                
                jobTasksService.jobCallbackIntf(job.getCode(), "success", false);
            }
            
        }
        
        String localIp = this.getLocalIp();
        
        jobLog.setNote(this.getJobRunInfo(localIp, excepMsg));
        
        /*jobLog.setSumDate(this.queryProcSumDate(job.getCode()));*/
        
        if (StringUtils.isNotEmpty(jobLog.getObjId()))
        {
            jobLogDao.updateByPrimaryKeySelective(jobLog);
        }
        else
        {
            LogUtils.runLogError("[正常结束]更新job_log时主键为空");
        }
        
        ScheduleLogTrace.scheduleInfoLog(StringUtils.join("[", job.getCode(), "]-[", jobLog.getBatchno(), "] 结束执行..."));
    }
    
    /**
     * job异常运行后日志
     */
    @Override
    public void excepJobLog(JobLog jobLog, Job job, String jobCode, String errMsg)
    {
        if (null == jobLog && StringUtils.isNotEmpty(jobCode))
        {
            //查找时间最新的并且状态为运行中的日志,然后设置异常状态
            JobLog newJobLog = getRecentJobLog(jobCode);
            //jobLogDao.selectByRowBounds()
            if (null != newJobLog)
            {
                if (newJobLog.getState() != State.TERMINATION.ordinal()
                    && newJobLog.getState() != State.EXCEPTION.ordinal())
                {
                    jobLog = newJobLog;
                }
            }
        }
        
        if (null != jobLog)
        {
            String localIp = this.getLocalIp();
            
            String note = this.getJobRunInfo(localIp, errMsg);
            jobLog.setNote(note);
            
            /*String jobCode = jobLog.getJobCode();*/
            String batchCode = jobLog.getBatchno();
            
            jobLog.setBatchno(batchCode);
            jobLog.setState(State.EXCEPTION.ordinal());
            jobLog.setEndTime(new Date());
            /*Date sysDate = simpleDate.parse(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));*/
            long consumptionTime = System.currentTimeMillis() - jobLog.getBeginDate().getTime();
            jobLog.setElapse(consumptionTime);
            
            /*jobLog.setSumDate(this.queryProcSumDate(job.getCode()));*/
            
            if (StringUtils.isNotEmpty(jobLog.getObjId()))
            {
                jobLogDao.updateByPrimaryKeySelective(jobLog);
            }
            else
            {
                LogUtils.runLogError("[异常结束]更新job_log时主键为空");
            }
            
            ScheduleLogTrace.scheduleWarnLog(StringUtils.join("[", jobCode, "]-[", batchCode, "] 异常结束！！！"));
        }
        
        if (null == job && StringUtils.isNotEmpty(jobCode))
        {
            job = jobCommService.getJob(jobCode);
        }
        
        if (null != job)
        {
            this.updateJobCount(job, false);
            
            this.updateRealState(job, State.EXCEPTION.ordinal());
            
            this.updateProcRealState(job, String.valueOf(State.EXCEPTION.ordinal()));
        }
        
    }

    @Override
    public JobLog getRecentJobLog(String jobCode)
    {
        PageHelper.startPage(1,1);
        Example example = new Example(JobLog.class);
        example.createCriteria().andEqualTo("jobCode",jobCode);
        example.setOrderByClause("start_time desc");

        return jobLogDao.selectOneByExample(example);
    }

    @Override
    public NodeLog getRecentNodeLog(String jobCode, String nodeCode)
    {
        PageHelper.startPage(1,1);
        Example example = new Example(NodeLog.class);
        example.createCriteria().andEqualTo("nodeCode",nodeCode).andEqualTo("jobCode",jobCode);
        example.setOrderByClause("start_time desc");

        return nodeLogDao.selectOneByExample(example);
    }

    @Override
    public JobLog getEarliestJobLogByStartTimeRange(String jobCode, Date begin, Date end)
    {
        PageHelper.startPage(1,1);
        Example example = new Example(JobLog.class);
        example.createCriteria().andBetween("startTime",begin,end).andEqualTo("jobCode",jobCode);
        example.setOrderByClause("start_time asc");

        return jobLogDao.selectOneByExample(example);
    }

    /**
     * 更新成功失败计数
     * @param pJob 任务对象
     * @param flag 成功失败标识
     * @see [类、类#方法、类#成员]
     */
    private void updateJobCount(Job pJob, boolean flag)
    {
        
        if (null == pJob)
        {
            return;
        }
        
        Job job = new Job();
        job.setObjId(pJob.getObjId());
        
        int succ = (null == pJob.getSuccess()) ? 0 : pJob.getSuccess();
        
        int fail = (null == pJob.getFail()) ? 0 : pJob.getFail();
        
        if (flag)
        {
            succ++;
            job.setSuccess(succ);
        }
        else
        {
            fail++;
            job.setFail(fail);
        }
        
        jobDao.updateByPrimaryKeySelective(job);
        
    }
    
    /**
     * 更新当前job的运行批次号
     * 如果任务运行完成,这个字段保存的是最近一次运行的批次号
     * @param pJob
     * @param batchNo
     * @see [类、类#方法、类#成员]
     */
    private void updateJobCurrentBatchNo(Job pJob, String batchNo)
    {
        if (null == pJob)
        {
            return;
        }
        
        Job job = new Job();
        job.setObjId(pJob.getObjId());
        
        job.setRunBatchno(batchNo);
        
        jobDao.updateByPrimaryKeySelective(job);
    }
    
    /**
     * 更新job实时状态
     * @param job
     * @param state
     * @see [类、类#方法、类#成员]
     */
    private void updateRealState(Job job, int state)
    {
        Job tJob = new Job();
        tJob.setObjId(job.getObjId());
        tJob.setRealState(state);
        
        jobDao.updateByPrimaryKeySelective(tJob);
    }
    
    /**
     * 当存储过程初始化任务没有运行时出现异常时,更新job日志实时状态
     * @param job
     * @param state
     * @see [类、类#方法、类#成员]
     */
    private void updateProcRealState(Job job, String state)
    {
        if (!StringUtils.equals(state, String.valueOf(State.EXCEPTION.ordinal())) || 0 != job.getKind())
        {
            return;
        }
        
        Example example = new Example(ProcLog.class);
        example.createCriteria().andEqualTo("jobCode", job.getCode()).andIsNull("jobBatchno");
        
        List<ProcLog> pls = procLogDao.selectByExample(example);
        
        if (CollectionUtils.isNotEmpty(pls))
        {
            for (ProcLog pl : pls)
            {
                pl.setJobBatchno(job.getRunBatchno());
                
                procLogDao.updateByPrimaryKeySelective(pl);
            }
        }
    }
    
    /**
     * 任务运行信息
     * @param ip
     * @param excep
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getJobRunInfo(String ip, String excep)
    {
        StringBuffer runInfo = new StringBuffer();
        runInfo.append("运行node:").append(ip);
        if (null != excep)
        {
            runInfo.append(" 异常信息:").append(excep);
        }
        
        return runInfo.toString();
    }
    
    /**
     * 获取job执行主机地址
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getLocalIp()
    {
        String localIp = "unknow host";
        try
        {
            localIp = NetUtils.ipAddressToUrlString(InetAddress.getLocalHost());
        }
        catch (UnknownHostException e)
        {
            LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
        }
        
        return localIp;
    }
    
}
