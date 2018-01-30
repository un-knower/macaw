package cn.migu.macaw.schedule.service;

import cn.migu.macaw.schedule.api.model.Job;
import cn.migu.macaw.schedule.api.model.JobLog;
import cn.migu.macaw.schedule.api.model.NodeLog;

import java.util.Date;

/**
 * job日志,包括任务日志,运行节点日志
 * 
 * @author soy
 */
public interface IJobLogService
{
    /**
     * job日志初始化
     * @param job 任务对象
     * @param batchNo 批次号
     * @return JobLog - 任务日志对象
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

    /**
     * 根据开始时间查询最新的任务日志
     * @param jobCode 任务编码
     * @return JobLog - 任务日志
     */
    JobLog getRecentJobLog(String jobCode);

    /**
     * 查询获取开始时间最新的节点日志
     * @param jobCode 任务编码
     * @param nodeCode 节点编码
     * @return NodeLog - 节点日志对象
     */
    NodeLog getRecentNodeLog(String jobCode, String nodeCode);

    /**
     * 按开始时间获取指定时间区间内时间最早的一条日志记录
     * @param jobCode
     * @param begin
     * @param end
     * @return JobLog - 任务日志对象
     */
    JobLog getEarliestJobLogByStartTimeRange(String jobCode,Date begin,Date end);
}
