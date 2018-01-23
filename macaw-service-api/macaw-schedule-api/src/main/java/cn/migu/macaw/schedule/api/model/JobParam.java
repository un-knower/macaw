package cn.migu.macaw.schedule.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "JOB_PARAM")
public class JobParam extends BaseEntity
{
    
    /**
     * job编码
     */
    @Column(name = "JOB_CODE")
    private String jobCode;
    
    /**
     * 参数分类,1：普通参数,2：变量参数,3.job循环变量
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
     * 获取参数分类,1：普通参数,2：变量参数
     *
     * @return KIND - 参数分类,1：普通参数,2：变量参数
     */
    public Integer getKind()
    {
        return kind;
    }
    
    /**
     * 设置参数分类,1：普通参数,2：变量参数
     *
     * @param kind 参数分类,1：普通参数,2：变量参数
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