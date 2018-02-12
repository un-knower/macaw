package cn.migu.macaw.jarboot.model;

import cn.migu.macaw.jarboot.common.RemainCode;

/**
 *
 * @author xxx
 */
public class BaseResponseEntity implements RemainCode
{
    /**
     * 成功为00,其他为01
     */
    private String code;

    /**
     * 具体描述信息
     */
    private String desc;

    /**
     * 具体内容信息
     */
    private String content;
    
    public BaseResponseEntity()
    {
        this.code = SUCCESS;
        this.desc = "OK";
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
}