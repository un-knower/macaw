package cn.migu.macaw.jarboot.service;

import cn.migu.macaw.jarboot.model.HostPid;
import cn.migu.macaw.jarboot.model.JarConfParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 服务部署的应用jar功能
 *
 * @author soy
 */
public interface IDeployJarBootService
{

    /**
     * 根据http request解析请求参数
     * @param bean 空的参数对象
     * @param request http 请求
     * @return JarConfParam - 解析后的部署应用配置参数实例
     */
    JarConfParam parseRequestParam(JarConfParam bean,HttpServletRequest request);


    /**
     * 停止部署服务进程
     *
     * @param param 配置部署服务参数
     */
    void killProcess(JarConfParam param);

    /**
     * 部署服务进程启动
     * @param param 配置部署服务参数
     * @return String - 返回码
     */
    String bootProcess(JarConfParam param);
}
