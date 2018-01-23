package cn.migu.macaw.schedule.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "TASK_PARAM")
public class TaskParam extends BaseEntity
{
    
    /**
     * task编码
     */
    @Column(name = "TASK_CODE")
    private String taskCode;
    
    /**
     * task参数类型:1-系统类型,2-自定义替换标签,3-运行SQL,4-shell命令,5-存储过程变量,6-输出redis存储变量,7-输出源类型
     */
    @Column(name = "KIND")
    private Integer kind;
    
    @Column(name = "PKEY")
    private String pkey;
    
    @Column(name = "TEMPLATE_CODE")
    private String templateCode;
    
    @Column(name = "NOTE")
    private String note;
    
    @Column(name = "DEFALUT_VALUE")
    private String defalutValue;
    
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
     * 获取task参数类型:1-系统类型,2-自定义替换标签,3-运行SQL,4-shell命令,5-存储过程变量,6-输出redis存储变量,7-输出源类型
     *
     * @return KIND - task参数类型:1-系统类型,2-自定义替换标签,3-运行SQL,4-shell命令,5-存储过程变量,6-输出redis存储变量,7-输出源类型
     */
    public Integer getKind()
    {
        return kind;
    }
    
    /**
     * 设置task参数类型:1-系统类型,2-自定义替换标签,3-运行SQL,4-shell命令,5-存储过程变量,6-输出redis存储变量,7-输出源类型
     *
     * @param kind task参数类型:1-系统类型,2-自定义替换标签,3-运行SQL,4-shell命令,5-存储过程变量,6-输出redis存储变量,7-输出源类型
     */
    public void setKind(Integer kind)
    {
        this.kind = kind;
    }
    
    /**
     * @return KEY
     */
    public String getPkey()
    {
        return pkey;
    }
    
    /**
     * @param pkey
     */
    public void setPkey(String pkey)
    {
        this.pkey = pkey == null ? null : pkey.trim();
    }
    
    /**
     * @return TEMPLATE_CODE
     */
    public String getTemplateCode()
    {
        return templateCode;
    }
    
    /**
     * @param templateCode
     */
    public void setTemplateCode(String templateCode)
    {
        this.templateCode = templateCode == null ? null : templateCode.trim();
    }
    
    /**
     * @return NOTE
     */
    public String getNote()
    {
        return note;
    }
    
    /**
     * @param note
     */
    public void setNote(String note)
    {
        this.note = note == null ? null : note.trim();
    }
    
    /**
     * @return DEFALUT_VALUE
     */
    public String getDefalutValue()
    {
        return defalutValue;
    }
    
    /**
     * @param defalutValue
     */
    public void setDefalutValue(String defalutValue)
    {
        this.defalutValue = defalutValue == null ? null : defalutValue.trim();
    }
}