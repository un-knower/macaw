package cn.migu.macaw.schedule.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "NODE_PARAM")
public class NodeParam extends BaseEntity
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
     * 节点编码
     */
    @Column(name = "NODE_CODE")
    private String nodeCode;
    
    /**
     * 参数分类 1-系统类型,2-自定义替换标签,3-运行SQL,4-shell命令,5-存储过程变量,6-输出redis存储变量,7-输出源类型
     */
    @Column(name = "KIND")
    private Integer kind;
    
    /**
     * 参数键
     */
    @Column(name = "PKEY")
    private String pkey;
    
    /**
     * 参数值
     */
    @Column(name = "VALUE")
    private String value;
    
    /**
     * 顺序
     */
    @Column(name = "SEQ")
    private Integer seq;
    
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
     * 获取参数分类 1-系统类型,2-自定义替换标签,3-运行SQL,4-shell命令,5-存储过程变量,6-输出redis存储变量,7-输出源类型
     *
     * @return KIND - 参数分类 1-系统类型,2-自定义替换标签,3-运行SQL,4-shell命令,5-存储过程变量,6-输出redis存储变量,7-输出源类型
     */
    public Integer getKind()
    {
        return kind;
    }
    
    /**
     * 设置参数分类 1-系统类型,2-自定义替换标签,3-运行SQL,4-shell命令,5-存储过程变量,6-输出redis存储变量,7-输出源类型
     *
     * @param kind 参数分类 1-系统类型,2-自定义替换标签,3-运行SQL,4-shell命令,5-存储过程变量,6-输出redis存储变量,7-输出源类型
     */
    public void setKind(Integer kind)
    {
        this.kind = kind;
    }
    
    /**
     * 获取参数键
     *
     * @return KEY - 参数键
     */
    public String getPkey()
    {
        return pkey;
    }
    
    /**
     * 设置参数键
     *
     * @param pkey 参数键
     */
    public void setPkey(String pkey)
    {
        this.pkey = pkey == null ? null : pkey.trim();
    }
    
    /**
     * 获取参数值
     *
     * @return VALUE - 参数值
     */
    public String getValue()
    {
        return value;
    }
    
    /**
     * 设置参数值
     *
     * @param value 参数值
     */
    public void setValue(String value)
    {
        this.value = value == null ? null : value.trim();
    }
    
    /**
     * 获取顺序
     *
     * @return SEQ - 顺序
     */
    public Integer getSeq()
    {
        return seq;
    }
    
    /**
     * 设置顺序
     *
     * @param seq 顺序
     */
    public void setSeq(Integer seq)
    {
        this.seq = seq;
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