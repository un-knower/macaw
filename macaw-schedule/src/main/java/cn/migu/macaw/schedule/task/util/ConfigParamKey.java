package cn.migu.macaw.schedule.task.util;

/**
 * 任务运行时配置参数键
 *
 * @author soy
 */
public interface ConfigParamKey
{

    /**
     * shell命令键
     */
    String OS_CMD = "os_cmd";

    /**
     * ssh远程连接主机用户名键
     */
    String SSH_USER = "ssh_user";

    /**
     * ssh远程连接主机密码键
     */
    String SSH_PASSWD = "ssh_passwd";

    /**
     * ssh远程连接主机地址键
     */
    String REMOTE_HOST = "remote_host";
}
