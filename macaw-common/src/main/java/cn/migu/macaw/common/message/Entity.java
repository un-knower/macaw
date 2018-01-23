package cn.migu.macaw.common.message;

import cn.migu.macaw.common.SysRetCode;

/**
 * 消息交互响应体
 * @author  soy
 */
public class Entity
{
    /**
     * 成功为0000,错误为ffff
     */
    private String code;

    /**
     * 具体描述信息
     */
    private String desc;

    /**
     * 调用app名称
     */
    private String appname;

    /**
     * 调用app ID
     */
    private String appid;

    /**
     * 任务开始时间
     */
    private String starttime;

    /**
     * 任务结束时间
     */
    private String endtime;

    /**
     * 耗时
     */
    private String timecost;

    /**
     * 返回内容,例如查询sql返回结果,文件操作结果等
     */
    private String content;

    /**
     * 错误异常堆栈
     */
    private String errorStack;
    
    public Entity()
    {
        this.code = SysRetCode.SUCCESS;
        this.desc = "success";
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
    
    @Override
    public String toString()
    {
        return "Entity [code=" + code + ", desc=" + desc + "]";
    }

    /**
     * @return 返回 appname
     */
    public String getAppname()
    {
        return appname;
    }

    /**
     * @param appname 对appname进行赋值
     */
    public void setAppname(String appname)
    {
        this.appname = appname;
    }

    /**
     * @return 返回 appid
     */
    public String getAppid()
    {
        return appid;
    }

    /**
     * @param appid 对appid进行赋值
     */
    public void setAppid(String appid)
    {
        this.appid = appid;
    }

    /**
     * @return 返回 starttime
     */
    public String getStarttime()
    {
        return starttime;
    }

    /**
     * @param starttime 对starttime进行赋值
     */
    public void setStarttime(String starttime)
    {
        this.starttime = starttime;
    }

    /**
     * @return 返回 endtime
     */
    public String getEndtime()
    {
        return endtime;
    }

    /**
     * @param endtime 对endtime进行赋值
     */
    public void setEndtime(String endtime)
    {
        this.endtime = endtime;
    }

    /**
     * @return 返回 timecost
     */
    public String getTimecost()
    {
        return timecost;
    }

    /**
     * @param timecost 对timecost进行赋值
     */
    public void setTimecost(String timecost)
    {
        this.timecost = timecost;
    }

    /**
     * @return 返回 content
     */
    public String getContent()
    {
        return content;
    }

    /**
     * @param content 对content进行赋值
     */
    public void setContent(String content)
    {
        this.content = content;
    }

    /**
     * @return 返回 errorStack
     */
    public String getErrorStack()
    {
        return errorStack;
    }

    /**
     * @param errorStack 对errorStack进行赋值
     */
    public void setErrorStack(String errorStack)
    {
        this.errorStack = errorStack;
    }
    
}
