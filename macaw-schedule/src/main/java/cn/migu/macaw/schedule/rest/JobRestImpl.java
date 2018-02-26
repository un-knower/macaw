package cn.migu.macaw.schedule.rest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.migu.macaw.common.NetUtils;
import cn.migu.macaw.common.RestTemplateProvider;
import cn.migu.macaw.schedule.api.service.ScheduleJobService;
import com.google.common.collect.Maps;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import com.alibaba.fastjson.JSON;

import cn.migu.macaw.common.SysRetCode;
import cn.migu.macaw.common.ThreadUtilities;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.common.message.Response;
import cn.migu.macaw.schedule.api.model.Job;
import cn.migu.macaw.schedule.dao.JobMapper;
import cn.migu.macaw.schedule.quartz.SchedulerTemplate;
import cn.migu.macaw.schedule.service.IJobCommService;
import cn.migu.macaw.schedule.service.IJobLogService;
import cn.migu.macaw.schedule.service.IJobTasksService;
import cn.migu.macaw.schedule.util.SpecJobPartRunThread;
import cn.migu.macaw.schedule.workflow.DataConstants;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * quartz job触发执行controller
 * 
 * @author soy
 */
@RestController
public class JobRestImpl implements ScheduleJobService
{

    @Resource
    private SchedulerTemplate schedulerTemplate;
    
    @Resource
    private IJobCommService jobCommService;
    
    @Resource
    private IJobTasksService jobTasksService;
    
    @Resource
    private JobMapper jobDao;
    
    @Resource
    private IJobLogService jobLogService;

    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;
    
    /**
     * 初始化创建定时任务
     * @param request
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Response createJob(HttpServletRequest request)
    {
        
        jobCommService.jobLog(request, "request", null);
        
        Entity content = new Entity();
        
        String name = request.getParameter("name");
        
        Job job = lookupJob(name, content);
        
        if (null != job)
        {
            if (StringUtils.isNotEmpty(job.getCronExpression()) && StringUtils.isNotEmpty(job.getJobClass()))
            {
                Class<? extends org.quartz.Job> jobClass;
                try
                {
                    jobClass = (Class<? extends org.quartz.Job>)Class.forName(job.getJobClass());
                    
                    schedulerTemplate.scheduleCronJob(name, job.getCronExpression(), jobClass);
                    
                    schedulerTemplate.pauseJob(name);
                }
                catch (Exception e)
                {
                    LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
                    
                    content.setDesc(ExceptionUtils.getStackTrace(e));
                    content.setCode(SysRetCode.ERROR);
                    
                }
                
            }
            else
            {
                content.setCode(SysRetCode.JOB_TRIGGER_OR_CLASS);
                content.setDesc("触发器或任务执行类为空");
            }
        }
        else
        {
            content.setCode(SysRetCode.JOB_NAME_ABSENT);
            content.setDesc("任务名称不存在:" + name);
        }
        
        Response resp = new Response(content);
        
        jobCommService.jobLog(request, "response", JSON.toJSONString(resp));
        
        return resp;
    }
    
    /**
     * 更新job
     * @param request
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Response updateJobCron(HttpServletRequest request)
    {
        jobCommService.jobLog(request, "request", null);
        
        Entity content = new Entity();
        
        String name = request.getParameter("name");
        
        Job job = lookupJob(name, content);
        
        if (null != job)
        {
            if (StringUtils.isNotEmpty(job.getCronExpression()) && StringUtils.isNotEmpty(job.getJobClass()))
            {
                try
                {
                    Trigger newTrigger = schedulerTemplate.createCronTrigger(TriggerKey.triggerKey(name,
                        SchedulerTemplate.JOB_DEFAULT_GROUP), job.getCronExpression(), null, null);
                    schedulerTemplate.updateTrigger(newTrigger);
                    
                    //查询job状态
                    int state = job.getState();
                    if (state == 0)
                    {
                        schedulerTemplate.pauseJob(name);
                    }
                }
                catch (Exception e)
                {
                    LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
                    content.setCode(SysRetCode.ERROR);
                    content.setDesc(ExceptionUtils.getStackTrace(e));
                }
                
            }
            else
            {
                content.setCode(SysRetCode.JOB_TRIGGER_OR_CLASS);
                content.setDesc("触发器或任务执行类为空");
            }
        }
        else
        {
            content.setCode(SysRetCode.JOB_NAME_ABSENT);
            content.setDesc("任务名称不存在:" + name);
        }
        
        Response resp = new Response(content);
        
        jobCommService.jobLog(request, "response", JSON.toJSONString(resp));
        
        return resp;
    }
    
    /**
     * 停止定时任务
     * @param request
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Response pauseJob(HttpServletRequest request)
    {
        jobCommService.jobLog(request, "request", null);
        
        Entity content = new Entity();
        
        String name = request.getParameter("name");
        
        Job job = lookupJob(name, content);
        
        if (null != job)
        {
            try
            {
                schedulerTemplate.pauseJob(name);
            }
            catch (Exception e)
            {
                LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
                content.setCode(SysRetCode.ERROR);
                content.setDesc(ExceptionUtils.getStackTrace(e));
            }
        }
        else
        {
            content.setCode(SysRetCode.JOB_NAME_ABSENT);
            content.setDesc("任务名称不存在:" + name);
        }
        
        Response resp = new Response(content);
        
        jobCommService.jobLog(request, "response", JSON.toJSONString(resp));
        
        return resp;
    }
    
    /**
     * 恢复定时任务执行
     * @param request
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Response resumeJob(HttpServletRequest request)
    {
        jobCommService.jobLog(request, "request", null);
        
        Entity content = new Entity();
        
        String name = request.getParameter("name");
        
        Job job = lookupJob(name, content);
        
        if (null != job)
        {
            try
            {
                schedulerTemplate.resumeJob(name);
            }
            catch (Exception e)
            {
                LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
                content.setCode(SysRetCode.ERROR);
                content.setDesc(ExceptionUtils.getStackTrace(e));
            }
        }
        else
        {
            content.setCode(SysRetCode.JOB_NAME_ABSENT);
            content.setDesc("任务名称不存在:" + name);
        }
        
        Response resp = new Response(content);
        
        jobCommService.jobLog(request, "response", JSON.toJSONString(resp));
        
        return resp;
    }
    
    /**
     * 删除定时任务
     * @param request
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Response deleteJob(HttpServletRequest request)
    {
        jobCommService.jobLog(request, "request", null);
        
        Entity content = new Entity();
        
        String name = request.getParameter("name");
        
        Job job = lookupJob(name, content);
        
        if (null != job)
        {
            try
            {
                schedulerTemplate.deleteJob(name);
            }
            catch (Exception e)
            {
                LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
                content.setCode(SysRetCode.ERROR);
                content.setDesc(ExceptionUtils.getStackTrace(e));
            }
        }
        else
        {
            content.setCode(SysRetCode.JOB_NAME_ABSENT);
            content.setDesc("任务名称不存在:" + name);
        }
        
        Response resp = new Response(content);
        
        jobCommService.jobLog(request, "response", JSON.toJSONString(resp));
        
        return resp;
    }
    
    /**
     * 立即触发任务执行
     * @param request
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Response triggerJob(HttpServletRequest request)
    {
        
        jobCommService.jobLog(request, "request", null);
        
        Entity content = new Entity();
        
        String name = request.getParameter("name");
        
        Job job = lookupJob(name, content);
        
        if (null != job)
        {
            if (jobCommService.isAlreadyTriggerRun(name))
            {
                content.setCode(SysRetCode.JOB_ALREADY_TRIGGERED);
                content.setDesc("job已经被触发执行:" + name);
            }
            else
            {
                try
                {
                    job.setRealState(0);
                    jobDao.updateByPrimaryKeySelective(job);
                    
                    //外部传入存储过程变量
                    jobCommService.addProcTmpVars(request, job);
                    
                    schedulerTemplate.triggerJob(name);
                    
                }
                catch (Exception e)
                {
                    LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
                    job.setRealState(3);
                    jobDao.updateByPrimaryKeySelective(job);
                    
                    content.setCode(SysRetCode.ERROR);
                    content.setDesc(ExceptionUtils.getStackTrace(e));
                }
            }
        }
        else
        {
            content.setCode(SysRetCode.JOB_NAME_ABSENT);
            content.setDesc("任务名称不存在:" + name);
        }
        
        Response resp = new Response(content);
        
        jobCommService.jobLog(request, "response", JSON.toJSONString(resp));
        
        return resp;
    }
    
    /**
     * 终止任务执行
     * @param request
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Response interruptJob(HttpServletRequest request)
    {
        
        jobCommService.jobLog(request, "request", null);
        
        Entity content = new Entity();
        
        String name = request.getParameter("name");
        
        Job job = lookupJob(name, content);

        if (null != job)
        {
            String redirectAddress = jobTasksService.getJobInstanceAddress(name);

            if(null != redirectAddress)
            {
                if(StringUtils.equals(NetUtils.LOCAL_FLAG,redirectAddress))
                {
                    try
                    {
                        schedulerTemplate.interrupt(name);
                    }
                    catch (Exception e)
                    {
                        LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
                        content.setCode(SysRetCode.ERROR);
                        content.setDesc(ExceptionUtils.getStackTrace(e));
                    }

                    try
                    {
                        //region
                        Thread rt = ThreadUtilities.getThread(StringUtils.join("region_", name));
                        if (null != rt)
                        {
                            while(!rt.isInterrupted())
                            {
                                rt.interrupt();
                            }
                            jobTasksService.setJobTaskCtxFlag(name, "", DataConstants.JOB_INTERRUPT_FLAG, "1");
                            jobTasksService.closeHttpReq(name);
                        }

                        //single
                        Thread st = ThreadUtilities.getThread(StringUtils.join("single_", name));
                        if (null != st)
                        {
                            while(!st.isInterrupted())
                            {
                                st.interrupt();
                            }
                            jobTasksService.setJobTaskCtxFlag(name, "", DataConstants.JOB_INTERRUPT_FLAG, "1");
                            jobTasksService.closeHttpReq(name);
                        }
                    }
                    catch (Exception e)
                    {
                        LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
                        content.setCode(SysRetCode.ERROR);
                        content.setDesc(ExceptionUtils.getStackTrace(e));
                    }
                }
                else
                {
                    //重定向请求
                    Map<String,String> params = Maps.newHashMap();
                    params.put("name",name);
                    RestTemplateProvider.postFormForEntity(restTemplate,StringUtils.join(redirectAddress,"/schedule/interruptJob"),String.class,params);
                }

            }
        }
        else
        {
            content.setCode(SysRetCode.JOB_NAME_ABSENT);
            content.setDesc("任务名称不存在:" + name);
        }
        
        Response resp = new Response(content);
        
        jobCommService.jobLog(request, "response", JSON.toJSONString(resp));
        
        return resp;
    }
    
    /**
     * 运行指定节点集合
     * @param request
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Response runJobSpecNodes(HttpServletRequest request)
    {
        jobCommService.jobLog(request, "request", null);
        
        Entity content = new Entity();
        
        String jobCode = request.getParameter("name");
        
        String firstNode = request.getParameter("firstNode");
        
        Job job = lookupJob(jobCode, content);
        if (null != job)
        {
            try
            {
                /**
                 * 现在没有创建线程池来管理这些线程,此接口没有性能要求,
                 * 并且实现流程中使用计数器限制了创建线程数,如使用线程池方式，
                 * 可参考
                 * https://github.com/eclipse/jetty.project/issues/1788
                 */
                new Thread(new SpecJobPartRunThread(job, firstNode, "region", jobLogService, jobTasksService)).start();
            }
            catch (Exception e)
            {
                LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
                content.setCode(SysRetCode.ERROR);
                content.setDesc(ExceptionUtils.getStackTrace(e));
            }
        }
        else
        {
            content.setCode(SysRetCode.JOB_NAME_ABSENT);
            content.setDesc("任务名称不存在:" + jobCode);
        }
        
        Response resp = new Response(content);
        
        jobCommService.jobLog(request, "response", JSON.toJSONString(resp));
        
        return resp;
        
    }
    
    /**
     * 运行指定单节点
     * @param request
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Response runSingleJobNode(HttpServletRequest request)
    {
        jobCommService.jobLog(request, "request", null);
        
        Entity content = new Entity();
        
        String jobCode = request.getParameter("name");
        
        String nodeCode = request.getParameter("firstNode");
        
        Job job = lookupJob(jobCode, content);
        if (null != job)
        {
            try
            {
                /**
                 * 现在没有创建线程池来管理这些线程,此接口没有性能要求,
                 * 并且实现流程中使用计数器限制了创建线程数,如使用线程池
                 * 可参考方式
                 * https://github.com/eclipse/jetty.project/issues/1788
                 */
                new Thread(new SpecJobPartRunThread(job, nodeCode, "single", jobLogService, jobTasksService)).start();
            }
            catch (Exception e)
            {
                LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
                content.setCode(SysRetCode.ERROR);
                content.setDesc(ExceptionUtils.getStackTrace(e));
            }
        }
        else
        {
            content.setCode(SysRetCode.JOB_NAME_ABSENT);
            content.setDesc("任务名称不存在:" + jobCode);
        }
        
        Response resp = new Response(content);
        jobCommService.jobLog(request, "response", JSON.toJSONString(resp));
        
        return resp;
        
    }
    
    /**
     * 查询job信息
     * @param jobCode
     * @param content
     * @return
     * @see [类、类#方法、类#成员]
     */
    private Job lookupJob(String jobCode, Entity content)
    {
        Job job = null;
        try
        {
            job = jobCommService.getJob(jobCode);
        }
        catch (Exception e)
        {
            LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
        }
        
        return job;
    }
    
}
