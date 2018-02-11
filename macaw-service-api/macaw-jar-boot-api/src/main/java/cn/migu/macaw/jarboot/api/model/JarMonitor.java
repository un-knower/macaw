package cn.migu.macaw.jarboot.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "jar_monitor")
public class JarMonitor extends BaseEntity
{
    /**
     * 所属系统
     */
    @Column(name = "APP_ID")
    private String appId;

    /**
     * 部署服务器id
     */
    @Column(name = "SERVER_ID")
    private String serverId;

    /**
     * 所属jar id
     */
    @Column(name = "JAR_ID")
    private String jarId;

    /**
     * 1：flume，2：spring，3：自定义，4：普通spark，5：合并spark
     */
    @Column(name = "JAR_KIND")
    private Integer jarKind;

    /**
     * 1：map，2：string
     */
    @Column(name = "FORMAT")
    private Integer format;

    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 编码
     */
    @Column(name = "CODE")
    private String code;

    /**
     * 更新周期，毫秒
     */
    @Column(name = "PERIOD")
    private Integer period;

    /**
     * 更新幅度
     */
    @Column(name = "SCOPE")
    private Integer scope;

    /**
     * 说明
     */
    @Column(name = "NOTE")
    private String note;

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
     * 获取部署服务器id
     *
     * @return SERVER_ID - 部署服务器id
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * 设置部署服务器id
     *
     * @param serverId 部署服务器id
     */
    public void setServerId(String serverId) {
        this.serverId = serverId == null ? null : serverId.trim();
    }

    /**
     * 获取所属jar id
     *
     * @return JAR_ID - 所属jar id
     */
    public String getJarId() {
        return jarId;
    }

    /**
     * 设置所属jar id
     *
     * @param jarId 所属jar id
     */
    public void setJarId(String jarId) {
        this.jarId = jarId == null ? null : jarId.trim();
    }

    /**
     * 获取1：flume，2：spring，3：自定义，4：普通spark，5：合并spark
     *
     * @return JAR_KIND - 1：flume，2：spring，3：自定义，4：普通spark，5：合并spark
     */
    public Integer getJarKind() {
        return jarKind;
    }

    /**
     * 设置1：flume，2：spring，3：自定义，4：普通spark，5：合并spark
     *
     * @param jarKind 1：flume，2：spring，3：自定义，4：普通spark，5：合并spark
     */
    public void setJarKind(Integer jarKind) {
        this.jarKind = jarKind;
    }

    /**
     * 获取1：map，2：string
     *
     * @return FORMAT - 1：map，2：string
     */
    public Integer getFormat() {
        return format;
    }

    /**
     * 设置1：map，2：string
     *
     * @param format 1：map，2：string
     */
    public void setFormat(Integer format) {
        this.format = format;
    }

    /**
     * 获取名称
     *
     * @return NAME - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取编码
     *
     * @return CODE - 编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置编码
     *
     * @param code 编码
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 获取更新周期，毫秒
     *
     * @return PERIOD - 更新周期，毫秒
     */
    public Integer getPeriod() {
        return period;
    }

    /**
     * 设置更新周期，毫秒
     *
     * @param period 更新周期，毫秒
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }

    /**
     * 获取更新幅度
     *
     * @return SCOPE - 更新幅度
     */
    public Integer getScope() {
        return scope;
    }

    /**
     * 设置更新幅度
     *
     * @param scope 更新幅度
     */
    public void setScope(Integer scope) {
        this.scope = scope;
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