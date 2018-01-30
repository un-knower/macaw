package cn.migu.macaw.schedule.api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.migu.macaw.common.entity.BaseEntity;

@Table(name = "JOB_LOG")
public class JobLog extends BaseEntity
{
    
    /**
     * 任务编码
     */
    @Column(name = "JOB_CODE")
    private String jobCode;
    
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
     * 任务耗时ms
     */
    @Column(name = "ELAPSE")
    private Long elapse;
    
    @Column(name = "PROJECT_CODE")
    private String projectCode;
    
    /**
     * 运行说明
     */
    @Column(name = "NOTE")
    private String note;
    
    /**
     * 图形xml
     */
    @Column(name = "GRAPH_XML")
    private String graphXml;

    /**
     * 统计日期,存储过程关联job使用
     */
    @Column(name = "SUM_DATE")
    private String sumDate;

    @Transient
    private Date beginDate;
    
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
     * 获取任务耗时ms
     *
     * @return ELAPSE - 任务耗时ms
     */
    public Long getElapse()
    {
        return elapse;
    }
    
    /**
     * 设置任务耗时ms
     *
     * @param elapse 任务耗时ms
     */
    public void setElapse(Long elapse)
    {
        this.elapse = elapse;
    }
    
    /**
     * @return PROJECT_CODE
     */
    public String getProjectCode()
    {
        return projectCode;
    }
    
    /**
     * @param projectCode
     */
    public void setProjectCode(String projectCode)
    {
        this.projectCode = projectCode == null ? null : projectCode.trim();
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
    
    /**
     * 获取图形xml
     *
     * @return GRAPH_XML - 图形xml
     */
    public String getGraphXml()
    {
        return graphXml;
    }
    
    /**
     * 设置图形xml
     *
     * @param graphXml 图形xml
     */
    public void setGraphXml(String graphXml)
    {
        this.graphXml = graphXml == null ? null : graphXml.trim();
    }

    /**
     * 获取统计日期
     * @return String - 日期字符串
     */
    public String getSumDate()
    {
        return sumDate;
    }

    /**
     * 设置统计日期
     * @param sumDate 统计日期字符串
     */
    public void setSumDate(String sumDate)
    {
        this.sumDate = sumDate;
    }

    public Date getBeginDate()
    {
        return beginDate;
    }

    public void setBeginDate(Date beginDate)
    {
        this.beginDate = beginDate;
    }
}