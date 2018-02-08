package cn.migu.macaw.jarboot.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import cn.migu.macaw.common.log.LogUtils;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * sftp操作
 *
 * @author soy
 */
public class SftpManager
{
    
    private Session session = null;
    
    private ChannelSftp channel = null;
    
    private int TIMEOUT = 60000;
    
    /**
     * 创建sftp通道连接
     * @param username 用户名
     * @param password 密码
     * @param ip 主机地址
     * @param port 端口号
     * @return ChannelSftp - sftp通道对象
     * @throws JSchException
     */
    public ChannelSftp createChannel(String username, String password, String ip, int port)
        throws JSchException
    {
        JSch jsch = new JSch();
        // 根据用户名，主机ip，端口获取一个Session对象
        session = jsch.getSession(username, ip, port);
        if (password != null)
        {
            session.setPassword(password);
        }
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setTimeout(TIMEOUT);
        session.connect(); // 通过Session建立链接
        
        channel = (ChannelSftp)session.openChannel("sftp");
        channel.connect();
        
        return channel;
    }
    
    /**
     * 关闭sftp通道连接
     *
     * @throws Exception
     */
    public void closeChannel()
    {
        if (channel != null)
        {
            channel.disconnect();
        }
        if (session != null)
        {
            session.disconnect();
        }
    }

    /**
     * 下载文件
     * @param remoteFile 远程主机文件名
     * @param localFile 本地文件名
     */
    public void download(String remoteFile,File localFile)
    {
        OutputStream output = null;
        try
        {
            if(!localFile.exists())
            {
                output = new FileOutputStream(localFile);

                channel.get(remoteFile, output);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogUtils.runLogError(e);
        }
        finally
        {
            if (output != null)
            {
                try
                {
                    output.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    LogUtils.runLogError(e);
                }
            }
            
        }
        
    }

    public static void main(String[] args)
    {
        SftpManager sftpChannel = new SftpManager();
        try
        {
            sftpChannel.createChannel("service","Emc20090","192.168.129.186",22);
            sftpChannel.download("/apps/service/unify-streaming.jar",new File("D://unify-streaming.jar"));
        }
        catch (JSchException e)
        {
            e.printStackTrace();
        }
        finally
        {
            sftpChannel.closeChannel();
        }
    }
}
