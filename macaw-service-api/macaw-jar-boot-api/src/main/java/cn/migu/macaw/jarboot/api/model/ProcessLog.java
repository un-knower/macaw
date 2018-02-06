package cn.migu.macaw.jarboot.api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import cn.migu.macaw.common.entity.BaseEntity;

@Table(name = "process_log")
public class ProcessLog extends BaseEntity
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
     * 端口
     */
    @Column(name = "PORT")
    private Integer port;
    
    /**
     * 1:运行,0:停止,2:等待,3:异常
     */
    @Column(name = "STATUS")
    private String status;
    
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
     * 占用内存,MB
     */
    @Column(name = "MEMORY")
    private String memory;
    
    /**
     * 占用CPU,核数
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
     * 获取端口
     *
     * @return PORT - 端口
     */
    public Integer getPort()
    {
        return port;
    }
    
    /**
     * 设置端口
     *
     * @param port 端口
     */
    public void setPort(Integer port)
    {
        this.port = port;
    }
    
    /**
     * 获取1:运行,0:停止,2:等待,3:异常
     *
     * @return STATUS - 1:运行,0:停止,2:等待,3:异常
     */
    public String getStatus()
    {
        return status;
    }
    
    /**
     * 设置1:运行,0:停止,2:等待,3:异常
     *
     * @param status 1:运行,0:停止,2:等待,3:异常
     */
    public void setStatus(String status)
    {
        this.status = status == null ? null : status.trim();
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
     * 获取占用内存,MB
     *
     * @return MEMORY - 占用内存,MB
     */
    public String getMemory()
    {
        return memory;
    }
    
    /**
     * 设置占用内存,MB
     *
     * @param memory 占用内存,MB
     */
    public void setMemory(String memory)
    {
        this.memory = memory == null ? null : memory.trim();
    }
    
    /**
     * 获取占用CPU,核数
     *
     * @return CPUS - 占用CPU,核数
     */
    public Integer getCpus()
    {
        return cpus;
    }
    
    /**
     * 设置占用CPU,核数
     *
     * @param cpus 占用CPU,核数
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