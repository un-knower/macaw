package cn.migu.macaw.jarboot.rest;

import javax.servlet.http.HttpServletRequest;

import cn.migu.macaw.common.ReturnCode;
import cn.migu.macaw.common.ServiceName;
import cn.migu.macaw.common.log.InterfaceLogBean;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.common.log.ReqRespLog;
import cn.migu.macaw.jarboot.model.JarConfParam;
import cn.migu.macaw.jarboot.service.IDeployJarBootService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import cn.migu.macaw.jarboot.api.service.JarBootService;

/**
 * 服务启动restful接口
 *
 * @author soy
 */
@RestController
public class JarBootRestImpl extends PreLogger implements JarBootService
{
    private static final Log jarBootLog = LogFactory.getLog("jar-boot-interface");

    @Autowired
    private IDeployJarBootService deployJarBootService;

    @Autowired
    private ReqRespLog reqRespLog;

    @Override
    public String boot(HttpServletRequest request)
    {
        InterfaceLogBean logBean = createLogBean(reqRespLog,request);

        reqRespLog.requestLog(request,jarBootLog,logBean);

        JarConfParam param = deployJarBootService.parseRequestParam(new JarConfParam(),request);

        ReturnCode retCode = deployJarBootService.bootProcess(param);

        reqRespLog.responseLog(jarBootLog,logBean,retCode.getName());

        return retCode.getName();
    }
    
    @Override
    public String reboot(HttpServletRequest request)
    {
        InterfaceLogBean logBean = createLogBean(reqRespLog,request);

        reqRespLog.requestLog(request,jarBootLog,logBean);

        JarConfParam param = deployJarBootService.parseRequestParam(new JarConfParam(),request);

        deployJarBootService.killProcess(param);

        ReturnCode retCode = deployJarBootService.bootProcess(param);

        reqRespLog.responseLog(jarBootLog,logBean,retCode.getName());

        return retCode.getName();
    }
    
    @Override
    public String stop(HttpServletRequest request)
    {
        InterfaceLogBean logBean = createLogBean(reqRespLog,request);

        reqRespLog.requestLog(request,jarBootLog,logBean);

        JarConfParam param = deployJarBootService.parseRequestParam(new JarConfParam(),request);

        String ret = ReturnCode.SUCCESS.getName();

        if(null == param)
        {
            ret = ReturnCode.BOOT_PARAM_PARSE_ERROR.getName();
        }
        else
        {
            try
            {
                deployJarBootService.killProcess(param);
            }
            catch(Exception e)
            {
                LogUtils.runLogError(e);
                ret = ReturnCode.STOP_FAILED.getName();
            }
        }

        reqRespLog.responseLog(jarBootLog,logBean,ret);

        return ret;
    }


}
