package cn.migu.macaw.schedule.api.service;

import cn.migu.macaw.common.message.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * spark集群/job回调事件
 *
 * @author  zhaocan
 */
@RequestMapping("/schedule")
public interface SparkClusterJobEvent
{
    /**
     * spark集群重启事件
     * 此事件在spark官方脚本中添加
     * @param request - http request
     */
    @PostMapping("/sparkClusterRestartEvent")
    void sparkClusterRestartEvent(HttpServletRequest request);

    /**
     * 可能的spark job残余数据处理
     * @param request - http request
     * @return Response - 返回消息
     */
    @PostMapping("/residualDriverHandle")
    Response residualAppHandle(HttpServletRequest request);
}
