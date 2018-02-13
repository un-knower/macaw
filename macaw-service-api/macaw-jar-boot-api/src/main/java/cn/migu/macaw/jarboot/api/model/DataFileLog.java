package cn.migu.macaw.jarboot.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "data_file_log")
public class DataFileLog extends BaseEntity
{
    /**
     * 应用系统
     */
    @Column(name = "APP_ID")
    private String appId;

    /**
     * 服务器id
     */
    @Column(name = "SERVER_ID")
    private String serverId;

    /**
     * 部署jar id
     */
    @Column(name = "JAR_ID")
    private String jarId;

    /**
     * 文件id
     */
    @Column(name = "FILE_ID")
    private String fileId;

    /**
     * 前名称
     */
    @Column(name = "FNAME")
    private String fname;

    /**
     * 后名称
     */
    @Column(name = "BNAME")
    private String bname;

    /**
     * 文件大小,字节
     */
    @Column(name = "FILE_SIZE")
    private Long fileSize;

    /**
     * 记录数
     */
    @Column(name = "RECORDS")
    private Long records;

    /**
     * 拷贝时间
     */
    @Column(name = "COPY_TIME")
    private Date copyTime;

    /**
     * 采集路径
     */
    @Column(name = "COLLECT_PATH")
    private String collectPath;

    /**
     * 采集时间
     */
    @Column(name = "COLLECT_TIME")
    private Date collectTime;

    /**
     * 采集人
     */
    @Column(name = "COLLECT_USER")
    private String collectUser;

    @Column(name = "ENCODE")
    private String encode;

    /**
     * -1：不校验，0：校验不通过，1：校验通过
     */
    @Column(name = "STATUS")
    private Integer status;

    /**
     * 说明
     */
    @Column(name = "NOTE")
    private String note;

    /**
     * 获取应用系统
     *
     * @return APP_ID - 应用系统
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置应用系统
     *
     * @param appId 应用系统
     */
    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    /**
     * 获取服务器id
     *
     * @return SERVER_ID - 服务器id
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * 设置服务器id
     *
     * @param serverId 服务器id
     */
    public void setServerId(String serverId) {
        this.serverId = serverId == null ? null : serverId.trim();
    }

    /**
     * 获取部署jar id
     *
     * @return JAR_ID - 部署jar id
     */
    public String getJarId() {
        return jarId;
    }

    /**
     * 设置部署jar id
     *
     * @param jarId 部署jar id
     */
    public void setJarId(String jarId) {
        this.jarId = jarId == null ? null : jarId.trim();
    }

    /**
     * 获取文件id
     *
     * @return FILE_ID - 文件id
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * 设置文件id
     *
     * @param fileId 文件id
     */
    public void setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
    }

    /**
     * 获取前名称
     *
     * @return FNAME - 前名称
     */
    public String getFname() {
        return fname;
    }

    /**
     * 设置前名称
     *
     * @param fname 前名称
     */
    public void setFname(String fname) {
        this.fname = fname == null ? null : fname.trim();
    }

    /**
     * 获取后名称
     *
     * @return BNAME - 后名称
     */
    public String getBname() {
        return bname;
    }

    /**
     * 设置后名称
     *
     * @param bname 后名称
     */
    public void setBname(String bname) {
        this.bname = bname == null ? null : bname.trim();
    }

    /**
     * 获取文件大小,字节
     *
     * @return FILE_SIZE - 文件大小,字节
     */
    public Long getFileSize() {
        return fileSize;
    }

    /**
     * 设置文件大小,字节
     *
     * @param fileSize 文件大小,字节
     */
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * 获取记录数
     *
     * @return RECORDS - 记录数
     */
    public Long getRecords() {
        return records;
    }

    /**
     * 设置记录数
     *
     * @param records 记录数
     */
    public void setRecords(Long records) {
        this.records = records;
    }

    /**
     * 获取拷贝时间
     *
     * @return COPY_TIME - 拷贝时间
     */
    public Date getCopyTime() {
        return copyTime;
    }

    /**
     * 设置拷贝时间
     *
     * @param copyTime 拷贝时间
     */
    public void setCopyTime(Date copyTime) {
        this.copyTime = copyTime;
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
     * 获取采集时间
     *
     * @return COLLECT_TIME - 采集时间
     */
    public Date getCollectTime() {
        return collectTime;
    }

    /**
     * 设置采集时间
     *
     * @param collectTime 采集时间
     */
    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    /**
     * 获取采集人
     *
     * @return COLLECT_USER - 采集人
     */
    public String getCollectUser() {
        return collectUser;
    }

    /**
     * 设置采集人
     *
     * @param collectUser 采集人
     */
    public void setCollectUser(String collectUser) {
        this.collectUser = collectUser == null ? null : collectUser.trim();
    }

    /**
     * @return ENCODE
     */
    public String getEncode() {
        return encode;
    }

    /**
     * @param encode
     */
    public void setEncode(String encode) {
        this.encode = encode == null ? null : encode.trim();
    }

    /**
     * 获取-1：不校验，0：校验不通过，1：校验通过
     *
     * @return STATUS - -1：不校验，0：校验不通过，1：校验通过
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置-1：不校验，0：校验不通过，1：校验通过
     *
     * @param status -1：不校验，0：校验不通过，1：校验通过
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取说明
     *
     * @return NOTE - 说明
     */
    public String getNote() {
        return note;
    }

    /**
     * 设置说明
     *
     * @param note 说明
     */
    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }
}