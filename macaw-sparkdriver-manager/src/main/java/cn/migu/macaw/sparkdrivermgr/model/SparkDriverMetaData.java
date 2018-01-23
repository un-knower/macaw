package cn.migu.macaw.sparkdrivermgr.model;

/**
 * spark driver元数据信息
 *
 * @author soy
 */
public class SparkDriverMetaData
{
    private String jarId;

    private String serverId;

    private String launchCmd;

    private String driverType;

    private String driverIp;

    private int driverPort;

    private String username;

    private String password;

    private String processId;

    private int pid;

    public String getJarId()
    {
        return jarId;
    }

    public void setJarId(String jarId)
    {
        this.jarId = jarId;
    }

    public String getServerId()
    {
        return serverId;
    }

    public void setServerId(String serverId)
    {
        this.serverId = serverId;
    }

    public String getLaunchCmd()
    {
        return launchCmd;
    }

    public void setLaunchCmd(String launchCmd)
    {
        this.launchCmd = launchCmd;
    }

    public String getDriverType()
    {
        return driverType;
    }

    public void setDriverType(String driverType)
    {
        this.driverType = driverType;
    }

    public String getDriverIp()
    {
        return driverIp;
    }

    public void setDriverIp(String driverIp)
    {
        this.driverIp = driverIp;
    }

    public int getDriverPort()
    {
        return driverPort;
    }

    public void setDriverPort(int driverPort)
    {
        this.driverPort = driverPort;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getProcessId()
    {
        return processId;
    }

    public void setProcessId(String processId)
    {
        this.processId = processId;
    }

    public int getPid()
    {
        return pid;
    }

    public void setPid(int pid)
    {
        this.pid = pid;
    }
}
