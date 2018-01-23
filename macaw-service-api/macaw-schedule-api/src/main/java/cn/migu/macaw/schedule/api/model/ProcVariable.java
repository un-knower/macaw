package cn.migu.macaw.schedule.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "PROC_VARIABLE")
public class ProcVariable extends BaseEntity
{
    
    /**
     * job编码
     */
    @Column(name = "JOB_CODE")
    private String jobCode;
    
    /**
     * 过程编码
     */
    @Column(name = "PROC_CODE")
    private String procCode;
    
    /**
     * 1-输入，2-输出，3-临时
     */
    @Column(name = "KIND")
    private Integer kind;
    
    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;
    
    /**
     * 编码
     */
    @Column(name = "CODE")
    private String code;
    
    /**
     * string，number，date
     */
    @Column(name = "TYPE")
    private String type;
    
    /**
     * 默认值
     */
    @Column(name = "DEFAULT_VALUE")
    private String defaultValue;
    
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
     * 获取过程编码
     *
     * @return PROC_CODE - 过程编码
     */
    public String getProcCode()
    {
        return procCode;
    }
    
    /**
     * 设置过程编码
     *
     * @param procCode 过程编码
     */
    public void setProcCode(String procCode)
    {
        this.procCode = procCode == null ? null : procCode.trim();
    }
    
    /**
     * 获取1-输入，2-输出，3-临时
     *
     * @return KIND - 1-输入，2-输出，3-临时
     */
    public Integer getKind()
    {
        return kind;
    }
    
    /**
     * 设置1-输入，2-输出，3-临时
     *
     * @param kind 1-输入，2-输出，3-临时
     */
    public void setKind(Integer kind)
    {
        this.kind = kind;
    }
    
    /**
     * 获取名称
     *
     * @return NAME - 名称
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name)
    {
        this.name = name == null ? null : name.trim();
    }
    
    /**
     * 获取编码
     *
     * @return CODE - 编码
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * 设置编码
     *
     * @param code 编码
     */
    public void setCode(String code)
    {
        this.code = code == null ? null : code.trim();
    }
    
    /**
     * 获取string，number，date
     *
     * @return TYPE - string，number，date
     */
    public String getType()
    {
        return type;
    }
    
    /**
     * 设置string，number，date
     *
     * @param type string，number，date
     */
    public void setType(String type)
    {
        this.type = type == null ? null : type.trim();
    }
    
    /**
     * 获取默认值
     *
     * @return DEFAULT_VALUE - 默认值
     */
    public String getDefaultValue()
    {
        return defaultValue;
    }
    
    /**
     * 设置默认值
     *
     * @param defaultValue 默认值
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue == null ? null : defaultValue.trim();
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