package cn.migu.macaw.jarboot.rest;

import cn.migu.macaw.common.ServiceName;
import cn.migu.macaw.common.log.InterfaceLogBean;
import cn.migu.macaw.common.log.ReqRespLog;

import javax.servlet.http.HttpServletRequest;

/**
 *
 *
 * @author soy
 */
public class PreLogger
{
    /**
     * 初始化log bean
     * @param request http请求对象
     * @return InterfaceLogBean - log对象
     */
     InterfaceLogBean createLogBean(ReqRespLog reqRespLog,HttpServletRequest request)
    {
        return reqRespLog.initLogBean(request, ServiceName.JAR_BOOT);
    }
}
