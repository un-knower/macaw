package cn.migu.macaw.jarboot.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "server")
public class Server extends BaseEntity
{
    /**
     * 应用系统id
     */
    @Column(name = "APP_ID")
    private String appId;

    /**
     * IP地址
     */
    @Column(name = "IP")
    private String ip;

    /**
     * 端口号
     */
    @Column(name = "PORT")
    private Integer port;

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
     * 工程编码
     */
    @Column(name = "PROJECT_CODE")
    private String projectCode;

    /**
     * 说明
     */
    @Column(name = "NOTE")
    private String note;


    /**
     * 获取应用系统id
     *
     * @return APP_ID - 应用系统id
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置应用系统id
     *
     * @param appId 应用系统id
     */
    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    /**
     * 获取IP地址
     *
     * @return IP - IP地址
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置IP地址
     *
     * @param ip IP地址
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    /**
     * 获取端口号
     *
     * @return PORT - 端口号
     */
    public Integer getPort() {
        return port;
    }

    /**
     * 设置端口号
     *
     * @param port 端口号
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * 获取用户名
     *
     * @return USERNAME - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 获取密码
     *
     * @return PASSWORD - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 获取工程编码
     *
     * @return PROJECT_CODE - 工程编码
     */
    public String getProjectCode() {
        return projectCode;
    }

    /**
     * 设置工程编码
     *
     * @param projectCode 工程编码
     */
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode == null ? null : projectCode.trim();
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