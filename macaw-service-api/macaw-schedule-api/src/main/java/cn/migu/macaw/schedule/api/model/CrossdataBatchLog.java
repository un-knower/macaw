package cn.migu.macaw.schedule.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "crossdata_batch_log")
public class CrossdataBatchLog extends BaseEntity
{
    /**
     * crossdata job id
     */
    @Column(name = "JOB_ID")
    private String jobId;

    /**
     * 运行批次号
     */
    @Column(name = "BATCH_NO")
    private String batchNo;

    /**
     * 输入源
     */
    @Column(name = "IN_SOURCE")
    private String inSource;

    /**
     * 输出源
     */
    @Column(name = "OUT_SOURCE")
    private String outSource;

    /**
     * 类型
     */
    @Column(name = "DATA_KIND")
    private Integer dataKind;

    /**
     * 出库数量
     */
    @Column(name = "OUTPUT_NUM")
    private Long outputNum;

    /**
     * 入库数量
     */
    @Column(name = "INPUT_NUM")
    private Long inputNum;

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
     * 耗时
     */
    @Column(name = "ELAPSE")
    private Long elapse;

    /**
     * 任务编号
     */
    @Column(name = "JOB_CODE")
    private String jobCode;

    /**
     * 任务状态
     */
    @Column(name = "JOB_STATE")
    private String jobState;


    /**
     * 获取crossdata job id
     *
     * @return JOB_ID - crossdata job id
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * 设置crossdata job id
     *
     * @param jobId crossdata job id
     */
    public void setJobId(String jobId) {
        this.jobId = jobId == null ? null : jobId.trim();
    }

    /**
     * 获取运行批次号
     *
     * @return BATCH_NO - 运行批次号
     */
    public String getBatchNo() {
        return batchNo;
    }

    /**
     * 设置运行批次号
     *
     * @param batchNo 运行批次号
     */
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

    /**
     * 获取输入源
     *
     * @return IN_SOURCE - 输入源
     */
    public String getInSource() {
        return inSource;
    }

    /**
     * 设置输入源
     *
     * @param inSource 输入源
     */
    public void setInSource(String inSource) {
        this.inSource = inSource == null ? null : inSource.trim();
    }

    /**
     * 获取输出源
     *
     * @return OUT_SOURCE - 输出源
     */
    public String getOutSource() {
        return outSource;
    }

    /**
     * 设置输出源
     *
     * @param outSource 输出源
     */
    public void setOutSource(String outSource) {
        this.outSource = outSource == null ? null : outSource.trim();
    }

    /**
     * 获取类型
     *
     * @return DATA_KIND - 类型
     */
    public Integer getDataKind() {
        return dataKind;
    }

    /**
     * 设置类型
     *
     * @param dataKind 类型
     */
    public void setDataKind(Integer dataKind) {
        this.dataKind = dataKind;
    }

    /**
     * 获取出库数量
     *
     * @return OUTPUT_NUM - 出库数量
     */
    public Long getOutputNum() {
        return outputNum;
    }

    /**
     * 设置出库数量
     *
     * @param outputNum 出库数量
     */
    public void setOutputNum(Long outputNum) {
        this.outputNum = outputNum;
    }

    /**
     * 获取入库数量
     *
     * @return INPUT_NUM - 入库数量
     */
    public Long getInputNum() {
        return inputNum;
    }

    /**
     * 设置入库数量
     *
     * @param inputNum 入库数量
     */
    public void setInputNum(Long inputNum) {
        this.inputNum = inputNum;
    }

    /**
     * 获取开始时间
     *
     * @return START_TIME - 开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取结束时间
     *
     * @return END_TIME - 结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取耗时
     *
     * @return ELAPSE - 耗时
     */
    public Long getElapse() {
        return elapse;
    }

    /**
     * 设置耗时
     *
     * @param elapse 耗时
     */
    public void setElapse(Long elapse) {
        this.elapse = elapse;
    }

    /**
     * 获取任务编号
     *
     * @return JOB_CODE - 任务编号
     */
    public String getJobCode() {
        return jobCode;
    }

    /**
     * 设置任务编号
     *
     * @param jobCode 任务编号
     */
    public void setJobCode(String jobCode) {
        this.jobCode = jobCode == null ? null : jobCode.trim();
    }

    /**
     * 获取任务状态
     *
     * @return JOB_STATE - 任务状态
     */
    public String getJobState() {
        return jobState;
    }

    /**
     * 设置任务状态
     *
     * @param jobState 任务状态
     */
    public void setJobState(String jobState) {
        this.jobState = jobState == null ? null : jobState.trim();
    }
}