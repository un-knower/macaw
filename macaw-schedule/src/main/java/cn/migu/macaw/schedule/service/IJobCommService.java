package cn.migu.macaw.schedule.service;

import cn.migu.macaw.schedule.api.model.Job;
import cn.migu.macaw.schedule.api.model.Procedure;

import javax.servlet.http.HttpServletRequest;

/**
 * 调度任务公共方法接口
 * (内部使用)
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年9月14日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IJobCommService
{
    Procedure getProcedure(String jobCode);
    
    boolean isAlreadyTriggerRun(String jobName);
    
    void jobLog(HttpServletRequest request, String key, String content);
    
    Job getJob(String name);
    
    void addProcTmpVars(HttpServletRequest request, Job job);
    
    int getJobRealState(String jobCode);
}
