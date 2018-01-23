package cn.migu.macaw.schedule.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "SPROC")
public class Procedure extends BaseEntity
{
    
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
     * job编码
     */
    @Column(name = "JOB_CODE")
    private String jobCode;
    
    /**
     * 操作人
     */
    @Column(name = "DEAL_USER")
    private String dealUser;
    
    /**
     * 操作时间
     */
    @Column(name = "DEAL_TIME")
    private Date dealTime;
    
    /**
     * CPU核数
     */
    @Column(name = "CPUS")
    private String cpus;
    
    /**
     * 内存
     */
    @Column(name = "MEMORY")
    private String memory;
    
    /**
     * 0：停止，1：运行，2：等待
     */
    @Column(name = "REAL_STATE")
    private Integer realState;
    
    /**
     * 工程编码
     */
    @Column(name = "PROJECT_CODE")
    private String projectCode;
    
    /**
     * Spark数据源
     */
    @Column(name = "DATABASE_SPARK")
    private String databaseSpark;
    
    /**
     * ht数据源
     */
    @Column(name = "DATABASE_HT")
    private String databaseHt;
    
    /**
     * 内容文本
     */
    @Column(name = "CONTENT")
    private String content;
    
    /**
     * 说明
     */
    @Column(name = "NOTE")
    private String note;
    
    /**
     * 获取名称
     *
     * @return NAME - 名称
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name)
    {
        this.name = name == null ? null : name.trim();
    }
    
    /**
     * 获取编码
     *
     * @return CODE - 编码
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * 设置编码
     *
     * @param code 编码
     */
    public void setCode(String code)
    {
        this.code = code == null ? null : code.trim();
    }
    
    /**
     * 获取job编码
     *
     * @return JOB_CODE - job编码
     */
    public String getJobCode()
    {
        return jobCode;
    }
    
    /**
     * 设置job编码
     *
     * @param jobCode job编码
     */
    public void setJobCode(String jobCode)
    {
        this.jobCode = jobCode == null ? null : jobCode.trim();
    }
    
    /**
     * 获取操作人
     *
     * @return DEAL_USER - 操作人
     */
    public String getDealUser()
    {
        return dealUser;
    }
    
    /**
     * 设置操作人
     *
     * @param dealUser 操作人
     */
    public void setDealUser(String dealUser)
    {
        this.dealUser = dealUser == null ? null : dealUser.trim();
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
     * 获取CPU核数
     *
     * @return CPUS - CPU核数
     */
    public String getCpus()
    {
        return cpus;
    }
    
    /**
     * 设置CPU核数
     *
     * @param cpus CPU核数
     */
    public void setCpus(String cpus)
    {
        this.cpus = cpus;
    }
    
    /**
     * 获取内存
     *
     * @return MEMORY - 内存
     */
    public String getMemory()
    {
        return memory;
    }
    
    /**
     * 设置内存
     *
     * @param memory 内存
     */
    public void setMemory(String memory)
    {
        this.memory = memory;
    }
    
    /**
     * 获取0：停止，1：运行，2：等待
     *
     * @return REAL_STATE - 0：停止，1：运行，2：等待
     */
    public Integer getRealState()
    {
        return realState;
    }
    
    /**
     * 设置0：停止，1：运行，2：等待
     *
     * @param realState 0：停止，1：运行，2：等待
     */
    public void setRealState(Integer realState)
    {
        this.realState = realState;
    }
    
    /**
     * 获取工程编码
     *
     * @return PROJECT_CODE - 工程编码
     */
    public String getProjectCode()
    {
        return projectCode;
    }
    
    /**
     * 设置工程编码
     *
     * @param projectCode 工程编码
     */
    public void setProjectCode(String projectCode)
    {
        this.projectCode = projectCode == null ? null : projectCode.trim();
    }
    
    /**
     * 获取Spark数据源
     *
     * @return DATABASE_SPARK - Spark数据源
     */
    public String getDatabaseSpark()
    {
        return databaseSpark;
    }
    
    /**
     * 设置Spark数据源
     *
     * @param databaseSpark Spark数据源
     */
    public void setDatabaseSpark(String databaseSpark)
    {
        this.databaseSpark = databaseSpark == null ? null : databaseSpark.trim();
    }
    
    /**
     * 获取ht数据源
     *
     * @return DATABASE_HT - ht数据源
     */
    public String getDatabaseHt()
    {
        return databaseHt;
    }
    
    /**
     * 设置ht数据源
     *
     * @param databaseHt ht数据源
     */
    public void setDatabaseHt(String databaseHt)
    {
        this.databaseHt = databaseHt == null ? null : databaseHt.trim();
    }
    
    /**
     * 获取内容文本
     *
     * @return CONTENT - 内容文本
     */
    public String getContent()
    {
        return content;
    }
    
    /**
     * 设置内容文本
     *
     * @param content 内容文本
     */
    public void setContent(String content)
    {
        this.content = content == null ? null : content.trim();
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