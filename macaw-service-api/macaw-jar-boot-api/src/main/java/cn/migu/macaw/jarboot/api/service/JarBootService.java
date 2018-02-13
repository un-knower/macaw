package cn.migu.macaw.jarboot.api.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 应用服务启动
 *
 * @author soy
 */
@RequestMapping("/")
public interface JarBootService
{
    /**
     * 应用服务jar启动
     * @param request http请求
     * @return String - 返回信息
     */
    @PostMapping("/runJar")
    String boot(HttpServletRequest request);

    /**
     * 应用服务jar启动重启
     * @param request http请求
     * @return String - 返回信息
     */
    @PostMapping("/stopAndRunJar")
    String reboot(HttpServletRequest request);

    /**
     * 应用服务jar停止
     * @param request http请求
     * @return String - 返回信息
     */
    @PostMapping("/stopJar")
    String stop(HttpServletRequest request);
}
