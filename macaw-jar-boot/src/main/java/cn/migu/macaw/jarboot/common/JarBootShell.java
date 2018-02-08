package cn.migu.macaw.jarboot.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import cn.migu.macaw.common.log.LogUtils;

/**
 * 部署服务jar启动shell封装
 *
 * @author soy
 */
public class JarBootShell
{
    private final static String CUSTOM_JAR_BOOT_SUCCESS = "ZDY-Start-Successfully";

    private final static String SQL_MONITOR_BOOT_SUCCESS = "SQJ-Monitored-Successfully";

    private final static String STREAMING_BOOT_SUCCESS = "STREAING STARTED";

    private final static String SPRINGBOOT_SUCCESS = "SPRINGBOOT-SUCCESS";

    private final static String CLEAN_JAR_BOOT_SUCESS = "cleanjar Start Successfully";

    private final static String PORT_ALREADY_USED = "Address already in use";

    private final static String SOURCE_PATH_ERROR = "SQJ-Source-Path-Not-Exists";

    private final static String COLLECT_PATH_ERROR = "SQJ-Collect-Path-Not-Exists";

    private final static String PARAMS_LESS_EIGHT_ERROR = "args length less than 8";

    private final static String PARAMS_LESS_FOUR_ERROR = "cleanjar args length less than 4";

    private final static String CLEAN_JAR_BOOT_FAILED = "cleanjar Start failed";

    private final static String NO_FILE_DIR = "No such file or directory";

    private final static String DIR_NOT_EXISTED = "Directory does not exist";

    private final static String PERMISSION_DENY = "Permission denied";

    private final static String JAR_FILE_NOT_EXISTED = "Unable to access jarfile";

    private final static String NOT_ANY_RESOURCES = "Initial job has not accepted any resources";

    private final static String SPRINGBOOT_ERROR = "SPRINGBOOT-ERROR";

    /**
     * 服务启动命令解析
     * 对于各个部署服务启动应在返回值中添加必要的返回成功或异常说明
     * @param host 主机地址
     * @param username 用户名
     * @param password 密码
     * @param command shell命令
     * @return String - 返回信息
     */
    public static String execCommandForParseRetLine(String host, String username, String password, String command)
    {
        Connection conn = new Connection(host);
        Session session = null;
        InputStream errIs = null;
        try
        {
            conn.connect();
            boolean isConn = conn.authenticateWithPassword(username, password);
            if (!isConn)
            {
                LogUtils.runLogError(String.format("login on %s username[%s] or password[%s] error",host,username,password));
                return "启动服务使用的用户名或密码错误";
            }
            else
            {
                session = conn.openSession();
                if (session != null)
                {
                    session.execCommand(command);

                    errIs = new StreamGobbler(session.getStderr());
                    LineIterator lineIterator = IOUtils.lineIterator(errIs, StandardCharsets.UTF_8);
                    while(lineIterator.hasNext())
                    {
                        String line = lineIterator.nextLine();
                        System.out.println(line);

                        String parseInfo = parseRet(line);
                        if(StringUtils.isNotEmpty(parseInfo))
                        {
                            return parseInfo;
                        }

                        if(null == line)
                        {
                            return "启动异常";
                        }
                    }

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogUtils.runLogError(e);
            return "启动异常:内部错误";
        }
        finally
        {
            if(null != errIs)
            {
                try
                {
                    errIs.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if(null != session)
            {
                session.close();
            }
            conn.close();
        }

        return "启动异常";
    }

    /**
     * 应用启动
     * @param shell 启动命令
     * @param host 主机
     * @param username 用户名
     * @param password 密码
     * @return String - 返回值
     */
    public final static String startShell(String shell,String host,String username,String password)
    {
        String response = execCommandForParseRetLine(host,username,password,shell);

        return parseRet(response);

    }

    /**
     * 解析返回消息
     * @param ret 返回字符串
     * @return String - 返回信息
     */
    public final static String parseRet(String ret)
    {
        if(StringUtils.contains(ret,CUSTOM_JAR_BOOT_SUCCESS) || StringUtils.contains(ret,SQL_MONITOR_BOOT_SUCCESS)
            || StringUtils.contains(ret,STREAMING_BOOT_SUCCESS) || StringUtils.contains(ret,SPRINGBOOT_SUCCESS)
            || StringUtils.contains(ret,CLEAN_JAR_BOOT_SUCESS))
        {
            return "启动成功";
        }

        if(StringUtils.contains(ret,PORT_ALREADY_USED))
        {
            return "端口占用";
        }

        if(StringUtils.contains(ret,SOURCE_PATH_ERROR))
        {
            return "原始路径有误";
        }

        if(StringUtils.contains(ret,COLLECT_PATH_ERROR))
        {
            return "采集路径有误";
        }

        if(StringUtils.contains(ret,PARAMS_LESS_EIGHT_ERROR))
        {
            return "输入参数少于8个";
        }

        if(StringUtils.contains(ret,CLEAN_JAR_BOOT_FAILED))
        {
            return "clean jar启动失败";
        }

        if(StringUtils.contains(ret,PARAMS_LESS_FOUR_ERROR))
        {
            return "参数长度不能小于4个,顺序为appID,serviceID,port,jarID";
        }

        if(StringUtils.contains(ret,NO_FILE_DIR))
        {
            return "找不到路径，请检查部署路径或采集路径是否正确";
        }

        if(StringUtils.contains(ret,DIR_NOT_EXISTED))
        {
            return "采集目录不存在";
        }

        if(StringUtils.contains(ret,PERMISSION_DENY))
        {
            return "权限不足";
        }

        if(StringUtils.contains(ret,JAR_FILE_NOT_EXISTED))
        {
            return "部署路径不存在";
        }

        if(StringUtils.contains(ret,NOT_ANY_RESOURCES))
        {
            return "启动成功,但当前集群无可用资源";
        }

        if(StringUtils.contains(ret,SPRINGBOOT_ERROR))
        {
            return "该JAR已启动或启动有误";
        }

        return "";
    }

    public static void main(String[] args)
    {
        String shell  = "/apps/service/t1.sh shanpao ff14af92-6b26-4e9d-afa1-56e557c91a1e 22ebbf65-f8ea-4126-b13e-bc3b4c27fb9c spark://192.168.129.186:7077 hdfs://hadoop 10004 sp_ugc_new.jar wy 1 1024 10 null";
        System.out.println(JarBootShell.startShell(shell,"192.168.129.186","root","Emc20090"));
    }
}
