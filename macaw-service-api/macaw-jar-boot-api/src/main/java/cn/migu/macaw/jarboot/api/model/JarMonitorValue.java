package cn.migu.macaw.jarboot.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "jar_monitor_value")
public class JarMonitorValue extends BaseEntity
{

    /**
     * 所属系统
     */
    @Column(name = "FILE_ID")
    private String fileId;

    /**
     * 1：原始目录，2：自定义，3：Flume，4：Clean，5：Streaming
     */
    @Column(name = "KIND")
    private Integer kind;

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
     * 键值
     */
    @Column(name = "KEY_VALUE")
    private String keyValue;

    /**
     * 参数值
     */
    @Column(name = "MONITOR_VALUE")
    private String monitorValue;

    /**
     * 更新时间
     */
    @Column(name = "REFRESH_TIME")
    private Date refreshTime;

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
     * 状态，1：正常，2：异常
     */
    @Column(name = "STATUS")
    private Integer status;

    /**
     * 说明
     */
    @Column(name = "NOTE")
    private String note;

    /**
     * 获取所属系统
     *
     * @return FILE_ID - 所属系统
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * 设置所属系统
     *
     * @param fileId 所属系统
     */
    public void setFileId(String fileId) {
        this.fileId = fileId == null ? null : fileId.trim();
    }

    /**
     * 获取1：原始目录，2：自定义，3：Flume，4：Clean，5：Streaming
     *
     * @return KIND - 1：原始目录，2：自定义，3：Flume，4：Clean，5：Streaming
     */
    public Integer getKind() {
        return kind;
    }

    /**
     * 设置1：原始目录，2：自定义，3：Flume，4：Clean，5：Streaming
     *
     * @param kind 1：原始目录，2：自定义，3：Flume，4：Clean，5：Streaming
     */
    public void setKind(Integer kind) {
        this.kind = kind;
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
     * 获取键值
     *
     * @return KEY_VALUE - 键值
     */
    public String getKeyValue() {
        return keyValue;
    }

    /**
     * 设置键值
     *
     * @param keyValue 键值
     */
    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue == null ? null : keyValue.trim();
    }

    /**
     * 获取参数值
     *
     * @return MONITOR_VALUE - 参数值
     */
    public String getMonitorValue() {
        return monitorValue;
    }

    /**
     * 设置参数值
     *
     * @param monitorValue 参数值
     */
    public void setMonitorValue(String monitorValue) {
        this.monitorValue = monitorValue == null ? null : monitorValue.trim();
    }

    /**
     * 获取更新时间
     *
     * @return REFRESH_TIME - 更新时间
     */
    public Date getRefreshTime() {
        return refreshTime;
    }

    /**
     * 设置更新时间
     *
     * @param refreshTime 更新时间
     */
    public void setRefreshTime(Date refreshTime) {
        this.refreshTime = refreshTime;
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
     * 获取状态，1：正常，2：异常
     *
     * @return STATUS - 状态，1：正常，2：异常
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态，1：正常，2：异常
     *
     * @param status 状态，1：正常，2：异常
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