package cn.migu.macaw.sparkdrivermgr.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import cn.migu.macaw.common.SysRetCode;

/**
 * 返回码描述
 * @author soy
 */
@Component("retCodeDesc")
public class RetCodeDesc
{
    private Map<String, String> desc;
    
    public RetCodeDesc()
    {
        desc = new HashMap<String, String>();
        desc.put(SysRetCode.SUCCESS, "操作成功");
        desc.put(SysRetCode.SPARK_CORENUM_EMPTY, "资源核数为空");
        desc.put(SysRetCode.SPARK_MEMSIZE_EMPTY, "资源内存数为空");
        desc.put(SysRetCode.SPARK_CORENUM_NONNUMERIC, "资源核数不是数字");
        desc.put(SysRetCode.SPARK_MEMSIZE_NONNUMERIC, "资源内存大小不是数字");
        desc.put(SysRetCode.SPARK_CORENUM_OVERFLOW, "资源核数超过限制");
        desc.put(SysRetCode.SPARK_MEMSIZE_OVERFLOW, "资源内存大小超过限制");
        desc.put(SysRetCode.SPARK_DRIVER_INSUFFICIENT, "driver资源不足");
        desc.put(SysRetCode.SPARK_DRIVER_REQUEST_ERROR, "driver资源请求异常");
        desc.put(SysRetCode.SPARK_APP_STOP_FAIL, "停止spark app失败");
        desc.put(SysRetCode.SPARK_RES_PARAM_ERROR, "spark资源参数错误");
        desc.put(SysRetCode.SPARK_APP_NOT_FOUND, "spark app无查询结果");
        desc.put(SysRetCode.SPARK_APP_FINISHED, "spark app执行已完成");
        desc.put(SysRetCode.SPARK_APP_RUNNING, "spark app正在运行中");
        desc.put(SysRetCode.SPARK_APP_KILLED, "spark app已被停止");
        desc.put(SysRetCode.SPARK_APP_FAILED, "spark app执行失败");
        desc.put(SysRetCode.SPARK_APP_CREATED_FAILED, "spark创建任务失败");
        desc.put(SysRetCode.SPARK_JAR_CONFIG_ERROR, "对应jar配置错误");
        desc.put(SysRetCode.ERROR, "操作失败");
    }
    
    /**
     * 返回码描述
     * @param key
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String getDesc(String key)
    {
        if (null != desc)
        {
            if (desc.containsKey(key))
            {
                return desc.get(key);
            }
        }
        
        return "未知错误";
    }
}
