package cn.migu.macaw.schedule.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "NODE_LOG")
public class NodeLog extends BaseEntity
{
    
    /**
     * 任务编码
     */
    @Column(name = "JOB_CODE")
    private String jobCode;
    
    /**
     * task编码
     */
    @Column(name = "TASK_CODE")
    private String taskCode;
    
    /**
     * 节点编码
     */
    @Column(name = "NODE_CODE")
    private String nodeCode;
    
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
    
    /**
     * 输入参数
     */
    @Column(name = "INPUT")
    private String input;
    
    /**
     * 输出参数
     */
    @Column(name = "OUTPUT")
    private String output;
    
    @Column(name = "PROJECT_CODE")
    private String projectCode;
    
    /**
     * 运行说明
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
     * 获取task编码
     *
     * @return TASK_CODE - task编码
     */
    public String getTaskCode()
    {
        return taskCode;
    }
    
    /**
     * 设置task编码
     *
     * @param taskCode task编码
     */
    public void setTaskCode(String taskCode)
    {
        this.taskCode = taskCode == null ? null : taskCode.trim();
    }
    
    /**
     * 获取节点编码
     *
     * @return NODE_CODE - 节点编码
     */
    public String getNodeCode()
    {
        return nodeCode;
    }
    
    /**
     * 设置节点编码
     *
     * @param nodeCode 节点编码
     */
    public void setNodeCode(String nodeCode)
    {
        this.nodeCode = nodeCode == null ? null : nodeCode.trim();
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
     * 获取输入参数
     *
     * @return INPUT - 输入参数
     */
    public String getInput()
    {
        return input;
    }
    
    /**
     * 设置输入参数
     *
     * @param input 输入参数
     */
    public void setInput(String input)
    {
        this.input = input == null ? null : input.trim();
    }
    
    /**
     * 获取输出参数
     *
     * @return OUTPUT - 输出参数
     */
    public String getOutput()
    {
        return output;
    }
    
    /**
     * 设置输出参数
     *
     * @param output 输出参数
     */
    public void setOutput(String output)
    {
        this.output = output == null ? null : output.trim();
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
}