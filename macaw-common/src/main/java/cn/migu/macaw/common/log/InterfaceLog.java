package cn.migu.macaw.common.log;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;

/**
 * 接口日志
 * @author soy
 */
public class InterfaceLog
{
    private InterfaceLog()
    {
    }
    
    private static volatile InterfaceLog instance = null;
    
    public static InterfaceLog getInstance()
    {
        if (instance == null)
        {
            synchronized (InterfaceLog.class)
            {
                if (instance == null)
                {
                    instance = new InterfaceLog();
                }
            }
        }
        return instance;
    }

    public void info(Log log,InterfaceLogBean logBean)
    {
        String moduleName = logBean.getSystemModuleName();
        String reqResIdent = logBean.getReqResIdent();
        String uniqueId = logBean.getUniqueId();
        String intfSrc = logBean.getInterfaceSrc();
        String intfName = logBean.getInterfaceName();
        String userId = logBean.getUserId();
        String message = logBean.getMessage();
        String retInfo = logBean.getReturnInfo();

        StringBuffer sb = new StringBuffer();
        logAppend(sb,moduleName);
        logAppend(sb,reqResIdent);
        logAppend(sb,uniqueId);
        logAppend(sb,intfSrc);
        logAppend(sb,intfName);
        logAppend(sb,userId);
        logAppend(sb,message);
        logAppend(sb,retInfo);

        log.info(sb.toString());

    }

    private void logAppend(StringBuffer sb,String attribute)
    {
        if(StringUtils.isNotEmpty(attribute))
        {
            if(sb.length() > 0)
            {
                sb.append("|").append(attribute);
            }
            else
            {
                sb.append(attribute);
            }
        }
    }
}
