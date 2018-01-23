package cn.migu.macaw.schedule.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "PROC_LOG")
public class ProcLog extends BaseEntity
{
    
    /**
     * 0-存储过程，1-一级组合，2-二级组合
     */
    @Column(name = "TYPE")
    private Integer type;
    
    /**
     * job编码
     */
    @Column(name = "JOB_CODE")
    private String jobCode;
    
    /**
     * 运行批次号
     */
    @Column(name = "JOB_BATCHNO")
    private String jobBatchno;
    
    /**
     * 用户
     */
    @Column(name = "DEAL_USER")
    private String dealUser;
    
    /**
     * 存储过程编码
     */
    @Column(name = "PROC_CODE")
    private String procCode;
    
    /**
     * 批次号
     */
    @Column(name = "BATCHNO")
    private String batchno;
    
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
     * 1：正常结束，2：运行中，3：异常中止
     */
    @Column(name = "STATE")
    private Integer state;
    
    /**
     * 任务耗时
     */
    @Column(name = "ELAPSE")
    private Long elapse;
    
    /**
     * p_redo
     */
    @Column(name = "P_REDO")
    private String pRedo;
    
    /**
     * p_year
     */
    @Column(name = "OTHERS_VARS")
    private String othersVars;
    
    /**
     * p_month
     */
    @Column(name = "P_MONTH")
    private String pMonth;
    
    /**
     * p_day
     */
    @Column(name = "P_DAY")
    private String pDay;
    
    /**
     * 输入值
     */
    @Column(name = "INPUT")
    private String input;
    
    /**
     * 输出值
     */
    @Column(name = "OUTPUT")
    private String output;
    
    /**
     * 运行说明
     */
    @Column(name = "NOTE")
    private String note;
    
    /**
     * 获取0-存储过程，1-一级组合，2-二级组合
     *
     * @return TYPE - 0-存储过程，1-一级组合，2-二级组合
     */
    public Integer getType()
    {
        return type;
    }
    
    /**
     * 设置0-存储过程，1-一级组合，2-二级组合
     *
     * @param type 0-存储过程，1-一级组合，2-二级组合
     */
    public void setType(Integer type)
    {
        this.type = type;
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
     * 获取运行批次号
     *
     * @return JOB_BATCHNO - 运行批次号
     */
    public String getJobBatchno()
    {
        return jobBatchno;
    }
    
    /**
     * 设置运行批次号
     *
     * @param jobBatchno 运行批次号
     */
    public void setJobBatchno(String jobBatchno)
    {
        this.jobBatchno = jobBatchno == null ? null : jobBatchno.trim();
    }
    
    /**
     * 获取用户
     *
     * @return DEAL_USER - 用户
     */
    public String getDealUser()
    {
        return dealUser;
    }
    
    /**
     * 设置用户
     *
     * @param dealUser 用户
     */
    public void setDealUser(String dealUser)
    {
        this.dealUser = dealUser == null ? null : dealUser.trim();
    }
    
    /**
     * 获取存储过程编码
     *
     * @return PROC_CODE - 存储过程编码
     */
    public String getProcCode()
    {
        return procCode;
    }
    
    /**
     * 设置存储过程编码
     *
     * @param procCode 存储过程编码
     */
    public void setProcCode(String procCode)
    {
        this.procCode = procCode == null ? null : procCode.trim();
    }
    
    /**
     * 获取批次号
     *
     * @return BATCHNO - 批次号
     */
    public String getBatchno()
    {
        return batchno;
    }
    
    /**
     * 设置批次号
     *
     * @param batchno 批次号
     */
    public void setBatchno(String batchno)
    {
        this.batchno = batchno == null ? null : batchno.trim();
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
     * 获取1：正常结束，2：运行中，3：异常中止
     *
     * @return STATE - 1：正常结束，2：运行中，3：异常中止
     */
    public Integer getState()
    {
        return state;
    }
    
    /**
     * 设置1：正常结束，2：运行中，3：异常中止
     *
     * @param state 1：正常结束，2：运行中，3：异常中止
     */
    public void setState(Integer state)
    {
        this.state = state;
    }
    
    /**
     * 获取任务耗时
     *
     * @return ELAPSE - 任务耗时
     */
    public Long getElapse()
    {
        return elapse;
    }
    
    /**
     * 设置任务耗时
     *
     * @param elapse 任务耗时
     */
    public void setElapse(Long elapse)
    {
        this.elapse = elapse;
    }
    
    /**
     * 获取p_redo
     *
     * @return P_REDO - p_redo
     */
    public String getpRedo()
    {
        return pRedo;
    }
    
    /**
     * 设置p_redo
     *
     * @param pRedo p_redo
     */
    public void setpRedo(String pRedo)
    {
        this.pRedo = pRedo == null ? null : pRedo.trim();
    }
    
    /**
     * 获取p_year
     *
     * @return OTHERS_VARS - p_year
     */
    public String getOthersVars()
    {
        return othersVars;
    }
    
    /**
     * 设置p_year
     *
     * @param othersVars p_year
     */
    public void setOthersVars(String othersVars)
    {
        this.othersVars = othersVars == null ? null : othersVars.trim();
    }
    
    /**
     * 获取p_month
     *
     * @return P_MONTH - p_month
     */
    public String getpMonth()
    {
        return pMonth;
    }
    
    /**
     * 设置p_month
     *
     * @param pMonth p_month
     */
    public void setpMonth(String pMonth)
    {
        this.pMonth = pMonth == null ? null : pMonth.trim();
    }
    
    /**
     * 获取p_day
     *
     * @return P_DAY - p_day
     */
    public String getpDay()
    {
        return pDay;
    }
    
    /**
     * 设置p_day
     *
     * @param pDay p_day
     */
    public void setpDay(String pDay)
    {
        this.pDay = pDay == null ? null : pDay.trim();
    }
    
    /**
     * 获取输入值
     *
     * @return INPUT - 输入值
     */
    public String getInput()
    {
        return input;
    }
    
    /**
     * 设置输入值
     *
     * @param input 输入值
     */
    public void setInput(String input)
    {
        this.input = input == null ? null : input.trim();
    }
    
    /**
     * 获取输出值
     *
     * @return OUTPUT - 输出值
     */
    public String getOutput()
    {
        return output;
    }
    
    /**
     * 设置输出值
     *
     * @param output 输出值
     */
    public void setOutput(String output)
    {
        this.output = output == null ? null : output.trim();
    }
    
    /**
     * 获取运行说明
     *
     * @return NOTE - 运行说明
     */
    public String getNote()
    {
        return note;
    }
    
    /**
     * 设置运行说明
     *
     * @param note 运行说明
     */
    public void setNote(String note)
    {
        this.note = note == null ? null : note.trim();
    }
}