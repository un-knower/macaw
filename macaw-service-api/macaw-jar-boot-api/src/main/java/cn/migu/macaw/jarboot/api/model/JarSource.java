package cn.migu.macaw.jarboot.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "jar_source")
public class JarSource extends BaseEntity
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
     * 数据源
     */
    @Column(name = "SOURCE_ID")
    private String sourceId;

    /**
     * 采集路径
     */
    @Column(name = "COLLECT_PATH")
    private String collectPath;

    /**
     * 所属文件
     */
    @Column(name = "FILE_ID")
    private String fileId;

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
     * 获取数据源
     *
     * @return SOURCE_ID - 数据源
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * 设置数据源
     *
     * @param sourceId 数据源
     */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId == null ? null : sourceId.trim();
    }

    /**
     * 获取采集路径
     *
     * @return COLLECT_PATH - 采集路径
     */
    public String getCollectPath() {
        return collectPath;
    }

    /**
     * 设置采集路径
     *
     * @param collectPath 采集路径
     */
    public void setCollectPath(String collectPath) {
        this.collectPath = collectPath == null ? null : collectPath.trim();
    }

    /**
     * 获取所属文件
     *
     * @return FILE_ID - 所属文件
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * 设置所属文件
     *
     * @param fileId 所属文件
     */
    public void setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
    }
}