package cn.migu.macaw.jarboot.api.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 应用服务启动
 *
 * @author soy
 */
@RequestMapping("/jar-boot")
public interface JarBootService
{
    /**
     * 应用服务jar启动
     * @param request http请求
     * @return String - 返回信息
     */
    @PostMapping("/run-jar")
    String boot(HttpServletRequest request);

    /**
     * 应用服务jar启动重启
     * @param request http请求
     * @return String - 返回信息
     */
    @PostMapping("/restart-jar")
    String reboot(HttpServletRequest request);

    /**
     * 应用服务jar停止
     * @param request http请求
     * @return String - 返回信息
     */
    @PostMapping("/stop-jar")
    String stop(HttpServletRequest request);
}
