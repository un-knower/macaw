package cn.migu.macaw.schedule.service;

import cn.migu.macaw.schedule.api.model.Job;
import cn.migu.macaw.schedule.api.model.JobLog;

/**
 * job日志
 * 
 * @author soy
 */
public interface IJobLogService
{
    /**
     * job日志初始化
     * @param job 任务对象
     * @param batchNo 批次号
     * @return
     */
    JobLog initJobLog(Job job, String batchNo);

    /**
     * 任务成功运行结束日志
     * @param jobLog 任务日志对象
     * @param job 任务对象
     */
    void successJobLog(JobLog jobLog, Job job);

    /**
     * 任务异常运行日志
     * @param jobLog 任务日志对象
     * @param job 任务对象
     * @param addJobCode 新增标识
     * @param errMsg 异常信息
     */
    void excepJobLog(JobLog jobLog, Job job, String addJobCode, String errMsg);
}
