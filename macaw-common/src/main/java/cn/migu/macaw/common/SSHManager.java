package cn.migu.macaw.common;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import cn.migu.macaw.common.log.LogUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * ssh manager
 *
 * @author soy
 */
public class SSHManager
{

    /**
     * 执行远程主机linux命令
     * @param host 主机地址
     * @param username 用户名
     * @param password 密码
     * @param command 命令
     */
    public static String execCommand(String host, String username, String password, String command)
    {
        Connection conn = new Connection(host);
        Session session = null;
        try
        {
            conn.connect();
            boolean isConn = conn.authenticateWithPassword(username, password);
            if (!isConn)
            {
                LogUtils.runLogError(String.format("login on %s username[%s] or password[%s] error",host,username,password));
            }
            else
            {
                session = conn.openSession();
                if (session != null)
                {
                    session.execCommand(command);

                    InputStream outIs = new StreamGobbler(session.getStdout());
                    String out = IOUtils.toString(outIs, StandardCharsets.UTF_8);
                    outIs.close();

                    InputStream errIs = new StreamGobbler(session.getStderr());
                    String err = IOUtils.toString(errIs,StandardCharsets.UTF_8);
                    errIs.close();

                    return StringUtils.join(out,err);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogUtils.runLogError(e);
        }
        finally
        {
            if(null != session)
            {
                session.close();
            }
            conn.close();

        }

        return "";
    }



    public static void main(String[] args)
    {
        String ret = SSHManager.execCommand("192.168.129.186","service","Emc20090","ps -p 4184 | grep -v PID  | wc -l");

        if (StringUtils.equals("0", ret.trim()))
        {
            System.out.println(ret);
        }
    }
}
