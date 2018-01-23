package cn.migu.macaw.common.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * log工具类
 * @author soy
 */
public class LogUtils
{
    public static void runLogError(Throwable e)
    {
        Log log = LogFactory.getLog("error");
        log.error("",e);
    }

    public static void runLogError(String error)
    {
        Log log = LogFactory.getLog("error");
        log.error(error);
    }


    public static void info(Log log,String info)
    {
        log.info(info);
    }

    public static void warn(Log log,String warn)
    {
        log.warn(warn);
    }

    public static void error(Log log,String error)
    {
        log.error(error);
    }

    public static void exception(Log log,Throwable e)
    {
        log.error("",e);
    }
}
