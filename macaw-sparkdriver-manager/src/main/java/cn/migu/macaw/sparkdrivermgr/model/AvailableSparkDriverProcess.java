package cn.migu.macaw.sparkdrivermgr.model;

/**
 * 可运行的spark driver进程
 * @author soy
 */
public class AvailableSparkDriverProcess
{
    private String objId;

    private String appId;

    private String jarId;

    private String serverId;

    private String ip;

    private Integer port;

    public String getObjId()
    {
        return objId;
    }

    public void setObjId(String objId)
    {
        this.objId = objId;
    }

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

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

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public Integer getPort()
    {
        return port;
    }

    public void setPort(Integer port)
    {
        this.port = port;
    }
}
