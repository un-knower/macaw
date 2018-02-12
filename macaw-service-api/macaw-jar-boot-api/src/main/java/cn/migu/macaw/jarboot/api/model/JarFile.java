package cn.migu.macaw.jarboot.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "jar_file")
public class JarFile extends BaseEntity
{

    /**
     * 所属系统
     */
    @Column(name = "APP_ID")
    private String appId;

    /**
     * 所属jar
     */
    @Column(name = "JAR_ID")
    private String jarId;

    /**
     * 所属文件
     */
    @Column(name = "FILE_ID")
    private String fileId;

    /**
     * jar类型
     */
    @Column(name = "JAR_KIND")
    private Integer jarKind;

    /**
     * 所属服务器
     */
    @Column(name = "SERVER_ID")
    private String serverId;

    /**
     * 2（停用）、1（启用）
     */
    @Column(name = "STATUS")
    private Integer status;

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

    /**
     * 获取jar类型
     *
     * @return JAR_KIND - jar类型
     */
    public Integer getJarKind() {
        return jarKind;
    }

    /**
     * 设置jar类型
     *
     * @param jarKind jar类型
     */
    public void setJarKind(Integer jarKind) {
        this.jarKind = jarKind;
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
     * 获取2（停用）、1（启用）
     *
     * @return STATUS - 2（停用）、1（启用）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置2（停用）、1（启用）
     *
     * @param status 2（停用）、1（启用）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}