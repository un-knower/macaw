package cn.migu.macaw.schedule.api.model;

import javax.persistence.Column;
import javax.persistence.Table;

import cn.migu.macaw.common.entity.BaseEntity;

@Table(name = "JOB")
public class Job extends BaseEntity
{
    
    /**
     * 0:存储过程任务,非0:普通任务
     */
    @Column(name = "KIND")
    private Integer kind;
    
    /**
     * 任务编码
     */
    @Column(name = "CODE")
    private String code;
    
    /**
     * 任务名称
     */
    @Column(name = "NAME")
    private String name;
    
    /**
     * 任务类
     */
    @Column(name = "JOB_CLASS")
    private String jobClass;
    
    /**
     * 0：暂停，1：运行中，2：等待，4：异常
     */
    @Column(name = "STATE")
    private Integer state;
    
    /**
     * 运行表达式
     */
    @Column(name = "CRON_EXPRESSION")
    private String cronExpression;
    
    /**
     * 成功次数
     */
    @Column(name = "SUCCESS")
    private Integer success;
    
    /**
     * 失败次数
     */
    @Column(name = "FAIL")
    private Integer fail;
    
    /**
     * 批次号
     */
    @Column(name = "RUN_BATCHNO")
    private String runBatchno;
    
    /**
     * 0：等待，1：正常结束，2：运行中，3：异常结束
     */
    @Column(name = "REAL_STATE")
    private Integer realState;
    
    /**
     * 图形XML
     */
    @Column(name = "GRAPH_XML")
    private String graphXml;
    
    /**
     * 工程编码
     */
    @Column(name = "PROJECT_CODE")
    private String projectCode;
    
    /**
     * 任务描述
     */
    @Column(name = "NOTE")
    private String note;
    
    /**
     * 获取0:存储过程任务,非0:普通任务
     *
     * @return KIND - 0:存储过程任务,非0:普通任务
     */
    public Integer getKind()
    {
        return kind;
    }
    
    /**
     * 设置0:存储过程任务,非0:普通任务
     *
     * @param kind 0:存储过程任务,非0:普通任务
     */
    public void setKind(Integer kind)
    {
        this.kind = kind;
    }
    
    /**
     * 获取任务编码
     *
     * @return CODE - 任务编码
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * 设置任务编码
     *
     * @param code 任务编码
     */
    public void setCode(String code)
    {
        this.code = code == null ? null : code.trim();
    }
    
    /**
     * 获取任务名称
     *
     * @return NAME - 任务名称
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * 设置任务名称
     *
     * @param name 任务名称
     */
    public void setName(String name)
    {
        this.name = name == null ? null : name.trim();
    }
    
    /**
     * 获取任务类
     *
     * @return JOB_CLASS - 任务类
     */
    public String getJobClass()
    {
        return jobClass;
    }
    
    /**
     * 设置任务类
     *
     * @param jobClass 任务类
     */
    public void setJobClass(String jobClass)
    {
        this.jobClass = jobClass == null ? null : jobClass.trim();
    }
    
    /**
     * 获取0：暂停，1：运行中，2：等待，4：异常
     *
     * @return STATE - 0：暂停，1：运行中，2：等待，4：异常
     */
    public Integer getState()
    {
        return state;
    }
    
    /**
     * 设置0：暂停，1：运行中，2：等待，4：异常
     *
     * @param state 0：暂停，1：运行中，2：等待，4：异常
     */
    public void setState(Integer state)
    {
        this.state = state;
    }
    
    /**
     * 获取运行表达式
     *
     * @return CRON_EXPRESSION - 运行表达式
     */
    public String getCronExpression()
    {
        return cronExpression;
    }
    
    /**
     * 设置运行表达式
     *
     * @param cronExpression 运行表达式
     */
    public void setCronExpression(String cronExpression)
    {
        this.cronExpression = cronExpression == null ? null : cronExpression.trim();
    }
    
    /**
     * 获取成功次数
     *
     * @return SUCCESS - 成功次数
     */
    public Integer getSuccess()
    {
        return success;
    }
    
    /**
     * 设置成功次数
     *
     * @param success 成功次数
     */
    public void setSuccess(Integer success)
    {
        this.success = success;
    }
    
    /**
     * 获取失败次数
     *
     * @return FAIL - 失败次数
     */
    public Integer getFail()
    {
        return fail;
    }
    
    /**
     * 设置失败次数
     *
     * @param fail 失败次数
     */
    public void setFail(Integer fail)
    {
        this.fail = fail;
    }
    
    /**
     * 获取批次号
     *
     * @return RUN_BATCHNO - 批次号
     */
    public String getRunBatchno()
    {
        return runBatchno;
    }
    
    /**
     * 设置批次号
     *
     * @param runBatchno 批次号
     */
    public void setRunBatchno(String runBatchno)
    {
        this.runBatchno = runBatchno == null ? null : runBatchno.trim();
    }
    
    /**
     * 获取0：等待，1：正常结束，2：运行中，3：异常结束
     *
     * @return REAL_STATE - 0：等待，1：正常结束，2：运行中，3：异常结束
     */
    public Integer getRealState()
    {
        return realState;
    }
    
    /**
     * 设置0：等待，1：正常结束，2：运行中，3：异常结束
     *
     * @param realState 0：等待，1：正常结束，2：运行中，3：异常结束
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
     * 获取任务描述
     *
     * @return NOTE - 任务描述
     */
    public String getNote()
    {
        return note;
    }
    
    /**
     * 设置任务描述
     *
     * @param note 任务描述
     */
    public void setNote(String note)
    {
        this.note = note == null ? null : note.trim();
    }
    
    /**
     * 获取图形XML
     *
     * @return GRAPH_XML - 图形XML
     */
    public String getGraphXml()
    {
        return graphXml;
    }
    
    /**
     * 设置图形XML
     *
     * @param graphXml 图形XML
     */
    public void setGraphXml(String graphXml)
    {
        this.graphXml = graphXml == null ? null : graphXml.trim();
    }
}