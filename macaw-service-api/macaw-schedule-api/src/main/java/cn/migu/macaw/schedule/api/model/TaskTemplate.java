package cn.migu.macaw.schedule.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "TASK_TEMPLATE")
public class TaskTemplate extends BaseEntity
{
    
    /**
     * task编码
     */
    @Column(name = "TASK_CODE")
    private String taskCode;
    
    /**
     * 模板编码
     */
    @Column(name = "CODE")
    private String code;
    
    /**
     * 模板名称
     */
    @Column(name = "NAME")
    private String name;
    
    /**
     * task bean名称
     */
    @Column(name = "TASK_CLASS")
    private String taskClass;
    
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
     * 获取模板编码
     *
     * @return CODE - 模板编码
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * 设置模板编码
     *
     * @param code 模板编码
     */
    public void setCode(String code)
    {
        this.code = code == null ? null : code.trim();
    }
    
    /**
     * 获取模板名称
     *
     * @return NAME - 模板名称
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * 设置模板名称
     *
     * @param name 模板名称
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
}