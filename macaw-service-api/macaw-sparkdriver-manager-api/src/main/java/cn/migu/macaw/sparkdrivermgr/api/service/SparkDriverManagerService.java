package cn.migu.macaw.sparkdrivermgr.api.service;

import cn.migu.macaw.common.message.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * spark driver资源管理
 * @author soy
 */
@RequestMapping("/resource")
public interface SparkDriverManagerService
{
    /**
     * spark任务请求转发
     * 把spark job的具体业务请求转发至相应的driver进程
     * 1.负载均衡
     * 2.等待spark driver上的spark session创建成功返回
     * @param request
     * @return
     */
    @PostMapping("/rs/**")
    Response sparkJobForward(HttpServletRequest request);

    /**
     * 删除spark任务
     * @param request
     * @return
     */
    @PostMapping("/stopSparkApp")
    Response stopSparkJob(HttpServletRequest request);

    /**
     * 查询spark job状态
     * @param request
     * @return
     */
    @PostMapping("/querySparkApp")
    Response querySparkJobStatus(HttpServletRequest request);

    /**
     * spark job所依赖的进程启动完成
     * @param request
     * @return
     */
    @PostMapping("/insertJob")
    Response processAttachedStartCompleted(HttpServletRequest request);

    /**
     * spark job提交完成通知
     * @param request
     * @return
     */
    @PostMapping("/sparkAppSubmit")
    Response sparkJobSubmitCompleted(HttpServletRequest request);

    /**
     * spark任务完成后通知
     * 当spark driver端运行完成时调用该接口
     * @param request
     * @return
     */
    @PostMapping("/backBySparkAppEnd")
    Response sparkJobCompleted(HttpServletRequest request);

    /**
     * 初始化创建spark session
     * @param request
     * @return
     */
    @PostMapping("/initSparkDriver")
    Response initSparkSession(HttpServletRequest request);

    /**
     * 释放spark session
     * @param request
     * @return
     */
    @PostMapping("/freeSparkDriver")
    Response freeSparkSession(HttpServletRequest request);

    /**
     * 在已创建完成的spark session中执行
     * select sql并返回查询内容
     * @param request
     * @return
     */
    @PostMapping("/doSessionSelectSql")
    Response execSelectSqlInSession(HttpServletRequest request);

    /**
     * 重启spark driver所在的进程
     * @param request
     * @return
     */
    @PostMapping("/restartDriver")
    Response restartProcessAttached(HttpServletRequest request);

    /**
     * 处理driver端异常关闭请求
     * @param request
     * @return
     */
    @PostMapping("/driverShutdownAction")
    void sparkDriverShutDown(HttpServletRequest request);

}
