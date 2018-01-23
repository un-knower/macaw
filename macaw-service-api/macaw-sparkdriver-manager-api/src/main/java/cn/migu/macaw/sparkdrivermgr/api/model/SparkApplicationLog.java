package cn.migu.macaw.sparkdrivermgr.api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

import cn.migu.macaw.common.entity.BaseEntity;

@Table(name = "spark_application_log")
public class SparkApplicationLog extends BaseEntity
{
    
    /**
     * 任务编码
     */
    @Column(name = "JOB_CODE")
    private String jobCode;
    
    /**
     * 运行批次号
     */
    @Column(name = "BATCHNO")
    private String batchno;
    
    /**
     * spark appid
     */
    @Column(name = "APPID")
    private String appid;
    
    /**
     * spark appname
     */
    @Column(name = "APP_NAME")
    private String appName;
    
    /**
     * 状态，running、finished、failed、killed
     */
    @Column(name = "STATUS")
    private String status;
    
    /**
     * 资源核数
     */
    @Column(name = "CORES")
    private Integer cores;
    
    /**
     * executor内存大小
     */
    @Column(name = "EXECUTOR_MEMORY")
    private String executorMemory;
    
    /**
     * 用户
     */
    @Column(name = "USER_NAME")
    private String userName;
    
    /**
     * 提交ip
     */
    @Column(name = "SUBMIT_IP")
    private String submitIp;
    
    /**
     * 开始时间
     */
    @Column(name = "START_TIME")
    private Date startTime;
    
    /**
     * 结束时间
     */
    @Column(name = "END_TIME")
    private Date endTime;
    
    /**
     * 持续时长,秒
     */
    @Column(name = "DURATION")
    private Long duration;
    
    /**
     * 模式,FIFO|FAIR
     */
    @Column(name = "SPARK_MODE")
    private String sparkMode;
    
    /**
     * driver地址
     */
    @Column(name = "DRIVER_IP")
    private String driverIp;
    
    /**
     * driver端口号
     */
    @Column(name = "DRIVER_PORT")
    private Integer driverPort;
    
    /**
     * 说明
     */
    @Column(name = "NOTE")
    private String note;
    
    /**
     * 获取任务编码
     *
     * @return JOB_CODE - 任务编码
     */
    public String getJobCode()
    {
        return jobCode;
    }
    
    /**
     * 设置任务编码
     *
     * @param jobCode 任务编码
     */
    public void setJobCode(String jobCode)
    {
        this.jobCode = jobCode == null ? null : jobCode.trim();
    }
    
    /**
     * 获取运行批次号
     *
     * @return BATCHNO - 运行批次号
     */
    public String getBatchno()
    {
        return batchno;
    }
    
    /**
     * 设置运行批次号
     *
     * @param batchno 运行批次号
     */
    public void setBatchno(String batchno)
    {
        this.batchno = batchno == null ? null : batchno.trim();
    }
    
    /**
     * 获取spark appid
     *
     * @return APPID - spark appid
     */
    public String getAppid()
    {
        return appid;
    }
    
    /**
     * 设置spark appid
     *
     * @param appid spark appid
     */
    public void setAppid(String appid)
    {
        this.appid = appid == null ? null : appid.trim();
    }
    
    /**
     * 获取spark appname
     *
     * @return APP_NAME - spark appname
     */
    public String getAppName()
    {
        return appName;
    }
    
    /**
     * 设置spark appname
     *
     * @param appName spark appname
     */
    public void setAppName(String appName)
    {
        this.appName = appName == null ? null : appName.trim();
    }
    
    /**
     * 获取状态，running、finished、failed、killed
     *
     * @return STATUS - 状态，running、finished、failed、killed
     */
    public String getStatus()
    {
        return status;
    }
    
    /**
     * 设置状态，running、finished、failed、killed
     *
     * @param status 状态，running、finished、failed、killed
     */
    public void setStatus(String status)
    {
        this.status = status == null ? null : status.trim();
    }
    
    /**
     * 获取资源核数
     *
     * @return CORES - 资源核数
     */
    public Integer getCores()
    {
        return cores;
    }
    
    /**
     * 设置资源核数
     *
     * @param cores 资源核数
     */
    public void setCores(Integer cores)
    {
        this.cores = cores;
    }
    
    /**
     * 获取executor内存大小
     *
     * @return EXECUTOR_MEMORY - executor内存大小
     */
    public String getExecutorMemory()
    {
        return executorMemory;
    }
    
    /**
     * 设置executor内存大小
     *
     * @param executorMemory executor内存大小
     */
    public void setExecutorMemory(String executorMemory)
    {
        this.executorMemory = executorMemory == null ? null : executorMemory.trim();
    }
    
    /**
     * 获取用户
     *
     * @return USER_NAME - 用户
     */
    public String getUserName()
    {
        return userName;
    }
    
    /**
     * 设置用户
     *
     * @param userName 用户
     */
    public void setUserName(String userName)
    {
        this.userName = userName == null ? null : userName.trim();
    }
    
    /**
     * 获取提交ip
     *
     * @return SUBMIT_IP - 提交ip
     */
    public String getSubmitIp()
    {
        return submitIp;
    }
    
    /**
     * 设置提交ip
     *
     * @param submitIp 提交ip
     */
    public void setSubmitIp(String submitIp)
    {
        this.submitIp = submitIp == null ? null : submitIp.trim();
    }
    
    /**
     * 获取开始时间
     *
     * @return START_TIME - 开始时间
     */
    public Date getStartTime()
    {
        return startTime;
    }
    
    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }
    
    /**
     * 获取结束时间
     *
     * @return END_TIME - 结束时间
     */
    public Date getEndTime()
    {
        return endTime;
    }
    
    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }
    
    /**
     * 获取持续时长,秒
     *
     * @return DURATION - 持续时长,秒
     */
    public Long getDuration()
    {
        return duration;
    }
    
    /**
     * 设置持续时长,秒
     *
     * @param duration 持续时长,秒
     */
    public void setDuration(Long duration)
    {
        this.duration = duration;
    }
    
    /**
     * 获取模式,FIFO|FAIR
     *
     * @return SPARK_MODE - 模式,FIFO|FAIR
     */
    public String getSparkMode()
    {
        return sparkMode;
    }
    
    /**
     * 设置模式,FIFO|FAIR
     *
     * @param sparkMode 模式,FIFO|FAIR
     */
    public void setSparkMode(String sparkMode)
    {
        this.sparkMode = sparkMode == null ? null : sparkMode.trim();
    }
    
    /**
     * 获取driver地址
     *
     * @return DRIVER_IP - driver地址
     */
    public String getDriverIp()
    {
        return driverIp;
    }
    
    /**
     * 设置driver地址
     *
     * @param driverIp driver地址
     */
    public void setDriverIp(String driverIp)
    {
        this.driverIp = driverIp == null ? null : driverIp.trim();
    }
    
    /**
     * 获取driver端口号
     *
     * @return DRIVER_PORT - driver端口号
     */
    public Integer getDriverPort()
    {
        return driverPort;
    }
    
    /**
     * 设置driver端口号
     *
     * @param driverPort driver端口号
     */
    public void setDriverPort(Integer driverPort)
    {
        this.driverPort = driverPort;
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