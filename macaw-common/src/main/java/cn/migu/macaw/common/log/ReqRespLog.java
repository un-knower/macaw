package cn.migu.macaw.common.log;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import cn.migu.macaw.common.RemoteIpHelper;

/**
 * 日志类
 * @author soy
 */
@Component("reqRespLog")
public class ReqRespLog
{
    /**
     * log bean初始化
     * @param request
     * @return
     * @see [类、类#方法、类#成员]
     */
    public InterfaceLogBean initLogBean(HttpServletRequest request,String moduleName)
    {
        InterfaceLogBean interfaceLogBean = new InterfaceLogBean();
        
        interfaceLogBean.setUniqueId(UUID.randomUUID().toString().replace("-",""));
        interfaceLogBean.setInterfaceName(request.getRequestURI());
        interfaceLogBean.setSystemModuleName(moduleName);
        
        String ip = RemoteIpHelper.getRemoteIpFrom(request);
        String port = String.valueOf(request.getRemotePort());
        
        interfaceLogBean.setInterfaceSrc(StringUtils.join(ip, ":", port));
        
        return interfaceLogBean;
        
    }
    
    /**
     * 请求日志
     * @param request
     * @param paramLog
     * @param interfaceLogBean
     * @see [类、类#方法、类#成员]
     */
    public void requestLog(HttpServletRequest request, Log paramLog, InterfaceLogBean interfaceLogBean)
    {
        interfaceLogBean.setReqResIdent("request");
        
        StringBuffer reqParams = new StringBuffer();
        
        Map<String, String[]> params = request.getParameterMap();
        Iterator<Entry<String, String[]>> it = params.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<String, String[]> entry = it.next();
            if (null != entry.getValue() && entry.getValue().length > 0)
            {
                reqParams.append(entry.getKey()).append("=").append(entry.getValue()[0]).append(",");
            }
        }
        
        interfaceLogBean.setMessage(StringUtils.join("请求参数:", reqParams.toString()));
        
        InterfaceLog.getInstance().info(paramLog, interfaceLogBean);
        
    }
    
    /**
     * 响应日志
     * @param paramLog
     * @param interfaceLogBean
     * @param respStr
     * @see [类、类#方法、类#成员]
     */
    public void responseLog(Log paramLog, InterfaceLogBean interfaceLogBean, String respStr)
    {
        interfaceLogBean.setReqResIdent("response");
        
        interfaceLogBean.setMessage(StringUtils.join("响应信息->", respStr));
        
        InterfaceLog.getInstance().info(paramLog, interfaceLogBean);
    }

    /**
     * 过程信息日志
     * @param paramLog 日志对象
     * @param interfaceLogBean 日志bean
     * @param message 记录信息
     */
    public void otherLog(Log paramLog, InterfaceLogBean interfaceLogBean, String message)
    {
        interfaceLogBean.setReqResIdent("process");

        interfaceLogBean.setMessage(message);

        InterfaceLog.getInstance().info(paramLog, interfaceLogBean);
    }
}
