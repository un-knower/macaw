package cn.migu.macaw.schedule.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "JOB_NODE")
public class JobNode extends BaseEntity
{
    
    /**
     * job编码
     */
    @Column(name = "JOB_CODE")
    private String jobCode;
    
    /**
     * task编码
     */
    @Column(name = "TASK_CODE")
    private String taskCode;
    
    /**
     * 节点名称
     */
    @Column(name = "NAME")
    private String name;
    
    /**
     * 节点编码
     */
    @Column(name = "CODE")
    private String code;
    
    /**
     * 父节点编码
     */
    @Column(name = "PARENT_CODE")
    private String parentCode;
    
    /**
     * 1：生效，0：无效
     */
    @Column(name = "STATE")
    private Integer state;
    
    /**
     * 操作时间
     */
    @Column(name = "DEAL_TIME")
    private Date dealTime;
    
    /**
     * 操作人
     */
    @Column(name = "DEAL_USER")
    private String dealUser;
    
    /**
     * 模板编码
     */
    @Column(name = "TEMPLATE_CODE")
    private String templateCode;
    
    /**
     * 说明
     */
    @Column(name = "NOTE")
    private String note;
    
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
     * 获取节点名称
     *
     * @return NAME - 节点名称
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * 设置节点名称
     *
     * @param name 节点名称
     */
    public void setName(String name)
    {
        this.name = name == null ? null : name.trim();
    }
    
    /**
     * 获取节点编码
     *
     * @return CODE - 节点编码
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * 设置节点编码
     *
     * @param code 节点编码
     */
    public void setCode(String code)
    {
        this.code = code == null ? null : code.trim();
    }
    
    /**
     * 获取父节点编码
     *
     * @return PARENT_CODE - 父节点编码
     */
    public String getParentCode()
    {
        return parentCode;
    }
    
    /**
     * 设置父节点编码
     *
     * @param parentCode 父节点编码
     */
    public void setParentCode(String parentCode)
    {
        this.parentCode = parentCode == null ? null : parentCode.trim();
    }
    
    /**
     * 获取1：生效，0：无效
     *
     * @return STATE - 1：生效，0：无效
     */
    public Integer getState()
    {
        return state;
    }
    
    /**
     * 设置1：生效，0：无效
     *
     * @param state 1：生效，0：无效
     */
    public void setState(Integer state)
    {
        this.state = state;
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
     * 获取模板编码
     *
     * @return TEMPLATE_CODE - 模板编码
     */
    public String getTemplateCode()
    {
        return templateCode;
    }
    
    /**
     * 设置模板编码
     *
     * @param templateCode 模板编码
     */
    public void setTemplateCode(String templateCode)
    {
        this.templateCode = templateCode == null ? null : templateCode.trim();
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