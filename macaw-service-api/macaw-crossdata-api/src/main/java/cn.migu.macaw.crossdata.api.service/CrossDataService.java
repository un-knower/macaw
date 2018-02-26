package cn.migu.macaw.crossdata.api.service;

import cn.migu.macaw.common.message.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 数据同步服务
 *
 * @author soy
 */
@RequestMapping("/")
public interface CrossDataService
{
    /**
     * 数据同步服务
     * @param request http request
     * @return  Response - 返回消息
     */
    @PostMapping("crossdata-start")
    Response dataBaseSyncStart(HttpServletRequest request);

    /**
     * 停止数据同步服务
     * @param request http request
     * @return  Response - 返回消息
     */
    @PostMapping("crossdata-stop")
    Response dataBaseSyncStop(HttpServletRequest request);
}
