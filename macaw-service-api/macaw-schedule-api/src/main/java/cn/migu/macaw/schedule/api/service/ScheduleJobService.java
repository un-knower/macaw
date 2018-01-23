package cn.migu.macaw.schedule.api.service;

import cn.migu.macaw.common.message.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 调度任务restful接口
 * @author soy
 */
@RequestMapping("/")
public interface ScheduleJobService
{
    /**
     * 创建任务
     * @param request
     * @return
     */
    @PostMapping("/schedule/startJob")
    Response createJob(HttpServletRequest request);

    /**
     * 更新任务cron表达式
     * @param request
     * @return
     */
    @PostMapping("/schedule/updateJob")
    Response updateJobCron(HttpServletRequest request);

    /**
     * 暂停任务
     * @param request
     * @return
     */
    @PostMapping("/schedule/pauseJob")
    Response pauseJob(HttpServletRequest request);

    /**
     * 恢复任务
     * @param request
     * @return
     */
    @PostMapping("/schedule/resumeJob")
    Response resumeJob(HttpServletRequest request);

    /**
     * 删除任务
     * @param request
     * @return
     */
    @PostMapping("/schedule/deleteJob")
    Response deleteJob(HttpServletRequest request);

    /**
     * 触发执行一次
     * @param request
     * @return
     */
    @PostMapping("/schedule/triggerJob")
    Response triggerJob(HttpServletRequest request);

    /**
     * 中断任务执行
     * @param request
     * @return
     */
    @PostMapping("/schedule/interruptJob")
    Response interruptJob(HttpServletRequest request);

    /**
     * 从指定node开始执行余下任务节点
     * @param request
     * @return
     */
    @PostMapping("/schedule/regionRun")
    Response runJobSpecNodes(HttpServletRequest request);

    /**
     * 任务单节点运行一次
     * @param request
     * @return
     */
    @PostMapping("/schedule/singleRun")
    Response runSingleJobNode(HttpServletRequest request);

}
