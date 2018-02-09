package cn.migu.macaw.jarboot.model;


/**
 * 部署服务jar配置参数
 *
 * @author soy
 */
public class JarConfParam
{
    /**
     * 配置部署application id
     */
    private String appId;

    /**
     * 服务部署主机id
     */
    private String serverId;

    /**
     * 配置jar id(主键)
     */
    private String objId;

    /**
     * 配置名称
     */
    private String name;

    /**
     * 启动端口
     */
    private String port;

    /**
     * 配置应用类型
     */
    private String kind;

    /**
     * spark executor内存大小
     */
    private String memory;

    /**
     * spark核数
     */
    private String cpus;

    /**
     * 操作用户
     */
    private String dealUser;

    /**
     * 部署jar路径
     */
    private String path;

    /**
     * 说明
     */
    private String note;

    /**
     * JMX端口
     */
    private String jmxPort;

    /**
     * streaming单个partition每秒可处理数量
     */
    private String dealNum;

    /**
     * streaming处理周期
     */
    private String refreshPeriod;

    /**
     * 离线数据源id
     */
    private String outSource;

    /**
     * 在线数据源id
     */
    private String inSource;

    /**
     * Xms
     */
    private String maxJvm;

    /**
     * Xmx
     */
    private String nowJvm;

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getServerId()
    {
        return serverId;
    }

    public void setServerId(String serverId)
    {
        this.serverId = serverId;
    }

    public String getObjId()
    {
        return objId;
    }

    public void setObjId(String objId)
    {
        this.objId = objId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String getKind()
    {
        return kind;
    }

    public void setKind(String kind)
    {
        this.kind = kind;
    }

    public String getMemory()
    {
        return memory;
    }

    public void setMemory(String memory)
    {
        this.memory = memory;
    }

    public String getCpus()
    {
        return cpus;
    }

    public void setCpus(String cpus)
    {
        this.cpus = cpus;
    }

    public String getDealUser()
    {
        return dealUser;
    }

    public void setDealUser(String dealUser)
    {
        this.dealUser = dealUser;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getJmxPort()
    {
        return jmxPort;
    }

    public void setJmxPort(String jmxPort)
    {
        this.jmxPort = jmxPort;
    }

    public String getDealNum()
    {
        return dealNum;
    }

    public void setDealNum(String dealNum)
    {
        this.dealNum = dealNum;
    }

    public String getRefreshPeriod()
    {
        return refreshPeriod;
    }

    public void setRefreshPeriod(String refreshPeriod)
    {
        this.refreshPeriod = refreshPeriod;
    }

    public String getOutSource()
    {
        return outSource;
    }

    public void setOutSource(String outSource)
    {
        this.outSource = outSource;
    }

    public String getInSource()
    {
        return inSource;
    }

    public void setInSource(String inSource)
    {
        this.inSource = inSource;
    }

    public String getMaxJvm()
    {
        return maxJvm;
    }

    public void setMaxJvm(String maxJvm)
    {
        this.maxJvm = maxJvm;
    }

    public String getNowJvm()
    {
        return nowJvm;
    }

    public void setNowJvm(String nowJvm)
    {
        this.nowJvm = nowJvm;
    }

    @Override public String toString()
    {
        return "JarConfParam{" + "appId='" + appId + '\'' + ", serverId='" + serverId + '\'' + ", objId='" + objId
            + '\'' + ", kind='" + kind + '\'' + ", memory='" + memory + '\'' + ", cpus='" + cpus + '\'' + ", dealUser='"
            + dealUser + '\'' + ", note='" + note + '\'' + '}';
    }
}
