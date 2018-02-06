package cn.migu.macaw.jarboot.api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import cn.migu.macaw.common.entity.BaseEntity;

@Table(name = "process")
public class Process extends BaseEntity
{
    /**
     * 系统id
     */
    @Column(name = "APP_ID")
    private String appId;
    
    /**
     * 服务器id
     */
    @Column(name = "SERVER_ID")
    private String serverId;
    
    /**
     * 应用jar id
     */
    @Column(name = "JAR_ID")
    private String jarId;
    
    /**
     * 进程端口
     */
    @Column(name = "PORT")
    private Integer port;
    
    /**
     * 1:flume,2:spark,3:自定义,4:kafka
     */
    @Column(name = "KIND")
    private String kind;
    
    /**
     * 进程id
     */
    @Column(name = "PROCESS_NO")
    private Integer processNo;
    
    /**
     * 1:使用,0:未使用
     */
    @Column(name = "STATUS")
    private Integer status;
    
    /**
     * 内存大小
     */
    @Column(name = "MEMORY")
    private String memory;
    
    /**
     * 核数
     */
    @Column(name = "CPUS")
    private Integer cpus;
    
    /**
     * 操作时间
     */
    @Column(name = "DEAL_TIME")
    private Date dealTime;
    
    /**
     * 操作用户
     */
    @Column(name = "DEAL_USER")
    private String dealUser;
    
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
    
    /**
     * 说明
     */
    @Column(name = "NOTE")
    private String note;
    
    /**
     * 获取系统id
     *
     * @return APP_ID - 系统id
     */
    public String getAppId()
    {
        return appId;
    }
    
    /**
     * 设置系统id
     *
     * @param appId 系统id
     */
    public void setAppId(String appId)
    {
        this.appId = appId == null ? null : appId.trim();
    }
    
    /**
     * 获取服务器id
     *
     * @return SERVER_ID - 服务器id
     */
    public String getServerId()
    {
        return serverId;
    }
    
    /**
     * 设置服务器id
     *
     * @param serverId 服务器id
     */
    public void setServerId(String serverId)
    {
        this.serverId = serverId == null ? null : serverId.trim();
    }
    
    /**
     * 获取应用jar id
     *
     * @return JAR_ID - 应用jar id
     */
    public String getJarId()
    {
        return jarId;
    }
    
    /**
     * 设置应用jar id
     *
     * @param jarId 应用jar id
     */
    public void setJarId(String jarId)
    {
        this.jarId = jarId == null ? null : jarId.trim();
    }
    
    /**
     * 获取进程端口
     *
     * @return PORT - 进程端口
     */
    public Integer getPort()
    {
        return port;
    }
    
    /**
     * 设置进程端口
     *
     * @param port 进程端口
     */
    public void setPort(Integer port)
    {
        this.port = port;
    }
    
    /**
     * 获取1:flume,2:spark,3:自定义,4:kafka
     *
     * @return KIND - 1:flume,2:spark,3:自定义,4:kafka
     */
    public String getKind()
    {
        return kind;
    }
    
    /**
     * 设置1:flume,2:spark,3:自定义,4:kafka
     *
     * @param kind 1:flume,2:spark,3:自定义,4:kafka
     */
    public void setKind(String kind)
    {
        this.kind = kind == null ? null : kind.trim();
    }
    
    /**
     * 获取进程id
     *
     * @return PROCESS_NO - 进程id
     */
    public Integer getProcessNo()
    {
        return processNo;
    }
    
    /**
     * 设置进程id
     *
     * @param processNo 进程id
     */
    public void setProcessNo(Integer processNo)
    {
        this.processNo = processNo;
    }
    
    /**
     * 获取1:使用,0:未使用
     *
     * @return STATUS - 1:使用,0:未使用
     */
    public Integer getStatus()
    {
        return status;
    }
    
    /**
     * 设置1:使用,0:未使用
     *
     * @param status 1:使用,0:未使用
     */
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
    /**
     * 获取内存大小
     *
     * @return MEMORY - 内存大小
     */
    public String getMemory()
    {
        return memory;
    }
    
    /**
     * 设置内存大小
     *
     * @param memory 内存大小
     */
    public void setMemory(String memory)
    {
        this.memory = memory == null ? null : memory.trim();
    }
    
    /**
     * 获取核数
     *
     * @return CPUS - 核数
     */
    public Integer getCpus()
    {
        return cpus;
    }
    
    /**
     * 设置核数
     *
     * @param cpus 核数
     */
    public void setCpus(Integer cpus)
    {
        this.cpus = cpus;
    }
    
    /**
     * 获取操作时间
     *
     * @return DEAL_TIME - 操作时间
     */
    public Date getDealTime()
    {
        return dealTime;
    }
    
    /**
     * 设置操作时间
     *
     * @param dealTime 操作时间
     */
    public void setDealTime(Date dealTime)
    {
        this.dealTime = dealTime;
    }
    
    /**
     * 获取操作用户
     *
     * @return DEAL_USER - 操作用户
     */
    public String getDealUser()
    {
        return dealUser;
    }
    
    /**
     * 设置操作用户
     *
     * @param dealUser 操作用户
     */
    public void setDealUser(String dealUser)
    {
        this.dealUser = dealUser == null ? null : dealUser.trim();
    }
    
    /**
     * @return UPDATE_TIME
     */
    public Date getUpdateTime()
    {
        return updateTime;
    }
    
    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
    
    /**
     * 获取说明
     *
     * @return NOTE - 说明
     */
    public String getNote()
    {
        return note;
    }
    
    /**
     * 设置说明
     *
     * @param note 说明
     */
    public void setNote(String note)
    {
        this.note = note == null ? null : note.trim();
    }
}