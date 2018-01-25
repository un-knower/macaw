package cn.migu.macaw.schedule.service;

import cn.migu.macaw.schedule.api.model.Job;
import cn.migu.macaw.schedule.api.model.Procedure;

import javax.servlet.http.HttpServletRequest;

/**
 * 调度任务公共方法接口
 * (内部使用)
 * 
 * @author soy
 */
public interface IJobCommService
{
    /**
     * 根据任务编码获取存储过程信息
     * @param jobCode 任务编码
     * @return Procedure - 存储过程实体类
     */
    Procedure getProcedure(String jobCode);

    /**
     * 判断任务是否正在运行
     * @param jobName 任务编码
     * @return true-在运行,false-无任务运行
     */
    boolean isAlreadyTriggerRun(String jobName);

    /**
     * 记录任务日志
     * @param request http请求
     * @param key
     * @param content
     */
    void jobLog(HttpServletRequest request, String key, String content);

    /**
     * 根据任务编码获取定时任务信息
     * @param name 任务编码
     * @return Job - 定时任务实例
     */
    Job getJob(String name);

    /**
     * 新增存储过程临时变量
     * @param request http请求
     * @param job 任务实例
     */
    void addProcTmpVars(HttpServletRequest request, Job job);

    /**
     * 获取任务实时状态
     * @param jobCode 任务编码
     * @return 状态值
     */
    int getJobRealState(String jobCode);
}
