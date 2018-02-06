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
     * 说明
     */
    private String note;

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

    @Override public String toString()
    {
        return "JarConfParam{" + "appId='" + appId + '\'' + ", serverId='" + serverId + '\'' + ", objId='" + objId
            + '\'' + ", kind='" + kind + '\'' + ", memory='" + memory + '\'' + ", cpus='" + cpus + '\'' + ", dealUser='"
            + dealUser + '\'' + ", note='" + note + '\'' + '}';
    }
}
