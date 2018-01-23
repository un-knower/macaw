package cn.migu.macaw.schedule.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "TASK")
public class Task extends BaseEntity
{
    
    /**
     * 1：系统流程2：数据操作3：外部交互4：自然语言
        5：分类模型6：推荐模型7：爬虫
     */
    @Column(name = "TYPE")
    private Integer type;
    
    /**
     * 1：老task，2：新task
     */
    @Column(name = "KIND")
    private Integer kind;
    
    /**
     * task编码
     */
    @Column(name = "CODE")
    private String code;
    
    /**
     * task名称
     */
    @Column(name = "NAME")
    private String name;
    
    /**
     * task bean名称
     */
    @Column(name = "TASK_CLASS")
    private String taskClass;
    
    /**
     * 循环次数
     */
    @Column(name = "LOOP_NUM")
    private Integer loopNum;
    
    /**
     * 失败重试数
     */
    @Column(name = "RETRY_NUM")
    private Integer retryNum;
    
    /**
     * 等待时间，毫秒
     */
    @Column(name = "WAIT_TIME")
    private Integer waitTime;
    
    /**
     * task描述
     */
    @Column(name = "NOTE")
    private String note;
    
    /**
     * 获取1：系统流程2：数据操作3：外部交互4：自然语言
        5：分类模型6：推荐模型7：爬虫
     *
     * @return TYPE - 1：系统流程2：数据操作3：外部交互4：自然语言
                5：分类模型6：推荐模型7：爬虫
     */
    public Integer getType()
    {
        return type;
    }
    
    /**
     * 设置1：系统流程2：数据操作3：外部交互4：自然语言
    5：分类模型6：推荐模型7：爬虫
    
     *
     * @param type 1：系统流程2：数据操作3：外部交互4：自然语言
    5：分类模型6：推荐模型7：爬虫
    
     */
    public void setType(Integer type)
    {
        this.type = type;
    }
    
    /**
     * 获取1：老task，2：新task
     *
     * @return KIND - 1：老task，2：新task
     */
    public Integer getKind()
    {
        return kind;
    }
    
    /**
     * 设置1：老task，2：新task
     *
     * @param kind 1：老task，2：新task
     */
    public void setKind(Integer kind)
    {
        this.kind = kind;
    }
    
    /**
     * 获取task编码
     *
     * @return CODE - task编码
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * 设置task编码
     *
     * @param code task编码
     */
    public void setCode(String code)
    {
        this.code = code == null ? null : code.trim();
    }
    
    /**
     * 获取task名称
     *
     * @return NAME - task名称
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * 设置task名称
     *
     * @param name task名称
     */
    public void setName(String name)
    {
        this.name = name == null ? null : name.trim();
    }
    
    /**
     * 获取task bean名称
     *
     * @return TASK_CLASS - task bean名称
     */
    public String getTaskClass()
    {
        return taskClass;
    }
    
    /**
     * 设置task bean名称
     *
     * @param taskClass task bean名称
     */
    public void setTaskClass(String taskClass)
    {
        this.taskClass = taskClass == null ? null : taskClass.trim();
    }
    
    /**
     * 获取循环次数
     *
     * @return LOOP_NUM - 循环次数
     */
    public Integer getLoopNum()
    {
        return loopNum;
    }
    
    /**
     * 设置循环次数
     *
     * @param loopNum 循环次数
     */
    public void setLoopNum(Integer loopNum)
    {
        this.loopNum = loopNum;
    }
    
    /**
     * 获取失败重试数
     *
     * @return RETRY_NUM - 失败重试数
     */
    public Integer getRetryNum()
    {
        return retryNum;
    }
    
    /**
     * 设置失败重试数
     *
     * @param retryNum 失败重试数
     */
    public void setRetryNum(Integer retryNum)
    {
        this.retryNum = retryNum;
    }
    
    /**
     * 获取等待时间，毫秒
     *
     * @return WAIT_TIME - 等待时间，毫秒
     */
    public Integer getWaitTime()
    {
        return waitTime;
    }
    
    /**
     * 设置等待时间，毫秒
     *
     * @param waitTime 等待时间，毫秒
     */
    public void setWaitTime(Integer waitTime)
    {
        this.waitTime = waitTime;
    }
    
    /**
     * 获取task描述
     *
     * @return NOTE - task描述
     */
    public String getNote()
    {
        return note;
    }
    
    /**
     * 设置task描述
     *
     * @param note task描述
     */
    public void setNote(String note)
    {
        this.note = note == null ? null : note.trim();
    }
}