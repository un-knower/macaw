package cn.migu.macaw.schedule.cache;

import java.util.HashSet;
import java.util.Set;

import cn.migu.common.redis.StringRedisService;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * job下task级缓存
 * 
 * @author soy
 */
@Component("jobTasksCache")
public class JobTasksCache
{
    /**
     * 缓存
     */
    @Autowired
    private StringRedisService redisService;

    private String genField(String nodeId, String key)
    {
        return StringUtils.join(nodeId, "_", key);
    }

    /**
     * 保存任务运行上下文信息
     * @param jobCode 任务编码
     * @param nodeId 节点编码
     * @param key 业务键
     * @param value 值
     */
    public void put(String jobCode, String nodeId, String key, String value)
    {
        if (null != redisService)
        {
            if (StringUtils.isNotEmpty(key))
            {
                if (StringUtils.isEmpty(value))
                {
                    redisService.put(jobCode, genField(nodeId, key), "");
                }
                else
                {
                    redisService.put(jobCode, genField(nodeId, key), value);
                }
            }
        }
    }
    
    /**
     * 追加信息
     * @param jobCode 任务编码
     * @param nodeId 节点编码
     * @param key 业务键
     * @param value 值
     */
    public void append(String jobCode, String nodeId, String key, String value)
    {
        if (null != redisService)
        {
            if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value))
            {
                
                String oldVal = this.get(jobCode, nodeId, key);
                
                oldVal = (StringUtils.isEmpty(oldVal)) ? "" : StringUtils.join(oldVal, ",");
                
                String newVal = StringUtils.join(oldVal, "[", value, "]");
                
                this.put(jobCode, nodeId, key, newVal);
                
            }
        }
    }
    
    /**
     * 组键
     * @param jobCode 任务编码
     * @param nodeId  节点编码
     * @param key     业务键值
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String combineKey(String jobCode, String nodeId, String key)
    {
        return StringUtils.join(jobCode, "_", nodeId, "_", key);
    }
    
    /**
     * 存储列表值
     * @param jobCode
     * @param nodeId
     * @param key
     * @param value
     * @throws CloneNotSupportedException
     * @see [类、类#方法、类#成员]
     */
    public void putList(String jobCode, String nodeId, String key, String value)
    {
        if (null != redisService)
        {
            if (!this.contains(jobCode, nodeId, key))
            {
                this.put(jobCode, nodeId, key, value);
            }
            else
            {
                String oldValue = this.get(jobCode, nodeId, key);
                
                String newValue = StringUtils.join(oldValue, ",", value);
                
                this.put(jobCode, nodeId, key, newValue);
            }
        }
    }
    
    /**
     * 获取值列表
     * @param jobCode
     * @param nodeId
     * @param key
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String[] getList(String jobCode, String nodeId, String key)
    {
        if (null != redisService)
        {
            if (this.contains(jobCode, nodeId, key))
            {
                String listValue = this.get(jobCode, nodeId, key);
                if (StringUtils.isNotEmpty(listValue))
                {
                    return StringUtils.split(listValue, ",");
                }
            }
            
        }
        
        return null;
    }
    
    /**
     * 删除list中的单个元素
     * @param jobCode
     * @param nodeId
     * @param key
     * @param value
     * @throws CloneNotSupportedException 
     * @see [类、类#方法、类#成员]
     */
    public void delElemList(String jobCode, String nodeId, String key, String value)
    {
        if (null != redisService)
        {
            if (this.contains(jobCode, nodeId, key))
            {
                String oldValue = this.get(jobCode, nodeId, key);
                
                String[] vs = StringUtils.split(oldValue, ",");
                
                String[] nvs = (String[])ArrayUtils.removeElement(vs, value);
                
                String newValue = "";
                
                if (null != nvs)
                {
                    newValue = StringUtils.join(nvs, ",");
                }
                
                this.put(jobCode, nodeId, key, newValue);
                
            }
        }
    }

    /**
     * 删除任务上下文
     * @param jobCode 任务编码
     */
    public void remove(String jobCode)
    {
        if (null != redisService)
        {
            redisService.remove(jobCode);
        }
    }

    /**
     * 删除field
     * @param jobCode
     * @param nodeId
     * @param key
     */
    public void remove(String jobCode, String nodeId, String key)
    {
        if (null != redisService)
        {
            redisService.deleteField(jobCode, genField(nodeId,key));
        }
    }

    /**
     * 是否包含业务键
     * @param jobCode
     * @param nodeId
     * @param key
     * @return
     */
    public boolean contains(String jobCode, String nodeId, String key)
    {
        if (null != redisService)
        {
            return redisService.exists(jobCode, genField(nodeId,key));
        }
        return false;
    }
    

    
    /**
     * 是否包含job
     * 可判断此job是否处在运行中
     * @param jobCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean contains(String jobCode)
    {
        if (null != redisService)
        {
            return redisService.exists(jobCode);
        }
        return false;
    }
    
    /**
     * 获取运行task信息
     * @param jobCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String get(String jobCode, String nodeId, String key)
    {
        if (null != redisService)
        {
            if (this.contains(jobCode, nodeId, key))
            {
                return (String)redisService.getValueByKeyANdField(jobCode, genField(nodeId,key));
            }
        }
        
        return null;
    }

    public boolean queryForHashKeyCounterExisted(String key, String field)
    {
        if (redisService.exists(key))
        {
            Object counter = redisService.getValueByKeyANdField(key, field);
            if (null != counter)
            {
                ScheduleLogTrace
                    .scheduleInfoLog(key, "*", "*", StringUtils.join(field, "'s counter value=", counter.toString()));

                int status = Integer.valueOf(counter.toString());

                if (status > 0)
                {
                    return true;
                }

                if (status < 0)
                {
                    LogUtils.runLogError(
                        StringUtils.join("key=", key, ",field=", field, "'s counter value less than zero."));
                }
            }
        }

        return false;
    }
    
    public static void main(String[] args)
    {
        Set<String> aa = new HashSet<>();
        aa.add("a");
        aa.add("b");
        aa.add("c");
        
        String[] ss = aa.toArray(new String[0]);
        for (String s : ss)
        {
            System.out.println(s);
        }
    }
}
