package cn.migu.macaw.jarboot.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "hdfs_log_tmp")
public class HdfsLogTmp extends BaseEntity
{
    /**
     * 所属系统
     */
    @Column(name = "APP_ID")
    private String appId;

    /**
     * 所属服务器
     */
    @Column(name = "SERVER_ID")
    private String serverId;

    /**
     * 所属jar
     */
    @Column(name = "JAR_ID")
    private String jarId;

    /**
     * 更新时间
     */
    @Column(name = "UP_TIME")
    private Date upTime;

    /**
     * hdfs路径
     */
    @Column(name = "HDFS_PATH")
    private String hdfsPath;

    /**
     * 获取所属系统
     *
     * @return APP_ID - 所属系统
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置所属系统
     *
     * @param appId 所属系统
     */
    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    /**
     * 获取所属服务器
     *
     * @return SERVER_ID - 所属服务器
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * 设置所属服务器
     *
     * @param serverId 所属服务器
     */
    public void setServerId(String serverId) {
        this.serverId = serverId == null ? null : serverId.trim();
    }

    /**
     * 获取所属jar
     *
     * @return JAR_ID - 所属jar
     */
    public String getJarId() {
        return jarId;
    }

    /**
     * 设置所属jar
     *
     * @param jarId 所属jar
     */
    public void setJarId(String jarId) {
        this.jarId = jarId == null ? null : jarId.trim();
    }

    /**
     * 获取更新时间
     *
     * @return UP_TIME - 更新时间
     */
    public Date getUpTime() {
        return upTime;
    }

    /**
     * 设置更新时间
     *
     * @param upTime 更新时间
     */
    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }

    /**
     * 获取hdfs路径
     *
     * @return HDFS_PATH - hdfs路径
     */
    public String getHdfsPath() {
        return hdfsPath;
    }

    /**
     * 设置hdfs路径
     *
     * @param hdfsPath hdfs路径
     */
    public void setHdfsPath(String hdfsPath) {
        this.hdfsPath = hdfsPath == null ? null : hdfsPath.trim();
    }
}