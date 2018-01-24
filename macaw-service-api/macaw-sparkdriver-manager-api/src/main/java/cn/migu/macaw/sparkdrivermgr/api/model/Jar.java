package cn.migu.macaw.sparkdrivermgr.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "jar")
public class Jar extends BaseEntity
{
    /**
     * 所属系统
     */
    @Column(name = "APP_ID")
    private String appId;
    
    /**
     * 部署服务器
     */
    @Column(name = "SERVER_ID")
    private String serverId;
    
    /**
     * 1：flume，2：spring，3：自定义，4：普通spark，5：合并spark，6：clean，7：校验文件clean
     */
    @Column(name = "KIND")
    private Integer kind;
    
    /**
     * jar名称
     */
    @Column(name = "NAME")
    private String name;
    
    /**
     * 1：启动，0：停止
     */
    @Column(name = "STATUS")
    private Integer status;

    /**
     * 单节点spark driver进程最大数
     */
    @Column(name = "MAX_JVM")
    private Integer maxJvm;
    
    /**
     * 部署路径
     */
    @Column(name = "PATH")
    private String path;

    /**
     * 用户名
     */
    @Column(name = "USERNAME")
    private String username;

    /**
     * 密码
     */
    @Column(name = "PASSWORD")
    private String password;

    /**
     * 服务器ip
     */
    @Transient
    private String ip;
    
    /**
     * 获取所属系统
     *
     * @return APP_ID - 所属系统
     */
    public String getAppId()
    {
        return appId;
    }
    
    /**
     * 设置所属系统
     *
     * @param appId 所属系统
     */
    public void setAppId(String appId)
    {
        this.appId = appId == null ? null : appId.trim();
    }
    
    /**
     * 获取部署服务器
     *
     * @return SERVER_ID - 部署服务器
     */
    public String getServerId()
    {
        return serverId;
    }
    
    /**
     * 设置部署服务器
     *
     * @param serverId 部署服务器
     */
    public void setServerId(String serverId)
    {
        this.serverId = serverId == null ? null : serverId.trim();
    }
    
    /**
     * 获取1：flume，2：spring，3：自定义，4：普通spark，5：合并spark，6：clean，7：校验文件clean
     *
     * @return KIND - 1：flume，2：spring，3：自定义，4：普通spark，5：合并spark，6：clean，7：校验文件clean
     */
    public Integer getKind()
    {
        return kind;
    }
    
    /**
     * 设置1：flume，2：spring，3：自定义，4：普通spark，5：合并spark，6：clean，7：校验文件clean
     *
     * @param kind 1：flume，2：spring，3：自定义，4：普通spark，5：合并spark，6：clean，7：校验文件clean
     */
    public void setKind(Integer kind)
    {
        this.kind = kind;
    }
    
    /**
     * 获取jar名称
     *
     * @return NAME - jar名称
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * 设置jar名称
     *
     * @param name jar名称
     */
    public void setName(String name)
    {
        this.name = name == null ? null : name.trim();
    }
    
    /**
     * 获取1：启动，0：停止
     *
     * @return STATUS - 1：启动，0：停止
     */
    public Integer getStatus()
    {
        return status;
    }
    
    /**
     * 设置1：启动，0：停止
     *
     * @param status 1：启动，0：停止
     */
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
    /**
     * 获取部署路径
     *
     * @return PATH - 部署路径
     */
    public String getPath()
    {
        return path;
    }
    
    /**
     * 设置部署路径
     *
     * @param path 部署路径
     */
    public void setPath(String path)
    {
        this.path = path == null ? null : path.trim();
    }

    /**
     * 获取用户名
     *
     * @return USERNAME - 用户名
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * 获取密码
     *
     * @return PASSWORD - 密码
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * 获取服务器ip地址
     *
     * @return IP - 服务器ip地址
     */
    public String getIp()
    {
        return ip;
    }

    /**
     * 设置服务器ip地址
     *
     * @param ip 服务器ip地址
     */
    public void setIp(String ip)
    {
        this.ip = ip;
    }

    /**
     * 设置spark driver进程最大数
     * @param maxJvm spark driver进程最大数
     */
    public void setMaxJvm(Integer maxJvm)
    {
        this.maxJvm = maxJvm;
    }

    /**
     * 获取spark driver进程最大数
     *
     * @return Integer - spark driver进程最大数
     */
    public Integer getMaxJvm()
    {
        return maxJvm;
    }

}