package cn.migu.macaw.schedule.cache;

import java.util.Map;
import java.util.stream.Collectors;

import cn.migu.common.redis.StringRedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 存储过程上下文
 * 
 * @author soy
 */
@Component("procContextCache")
public class ProcContextCache
{
    public static String SYMBOL = "proc_";
    
    @Autowired
    private StringRedisService redisService;

    /**
     * 存储过程键前缀
     * @param key
     * @return
     */
    private String procKeyPrefix(String key)
    {
        return StringUtils.join(SYMBOL, key);
    }
    /**
     * 存储job-key
     * @param jobCode
     * @param key
     * @param value
     * @throws CloneNotSupportedException
     * @see [类、类#方法、类#成员]
     */
    public void put(String jobCode, String key, String value)
    {
        if (null != redisService)
        {
            if (StringUtils.isNotEmpty(key))
            {
                if (StringUtils.isEmpty(value))
                {
                    redisService.put(jobCode, procKeyPrefix(key), "");
                }
                else
                {
                    redisService.put(jobCode, procKeyPrefix(key), value);
                }
                
            }
        }
    }
    
    /**
     * 删除job
     * @param jobCode 键
     * @see [类、类#方法、类#成员]
     */
    public void remove(String jobCode)
    {
        if (null != redisService)
        {
            redisService.remove(jobCode);
        }
    }
    
    /**
     * 删除job-key
     * @param jobCode
     * @param key
     * @see [类、类#方法、类#成员]
     */
    public void remove(String jobCode, String key)
    {
        if (null != redisService)
        {
            redisService.deleteField(jobCode, procKeyPrefix(key));
        }
    }
    
    /**
     * 是否包含job-key
     * @param jobCode
     * @param key
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean contains(String jobCode, String key)
    {
        if (null != redisService)
        {
            return redisService.exists(jobCode, procKeyPrefix(key));
        }
        return false;
    }
    
    /**
     * 获取运行时job-key信息
     * @param jobCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String get(String jobCode, String key)
    {
        if (null != redisService)
        {
            if (this.contains(jobCode, key))
            {
                
                return (String)redisService.getValueByKeyANdField(jobCode, procKeyPrefix(key));
            }
        }
        
        return null;
    }
    
    /**
     * 获取该运行job下的运行上下文
     * @param jobCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> get(String jobCode)
    {
        if (null != redisService)
        {
            if (redisService.exists(jobCode))
            {
                return redisService.getEntries(jobCode);
            }

        }
        
        return null;
    }
    
    /**
     * 获取上下文所有变量
     * @param jobCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getProcVars(String jobCode)
    {
        Map<String, String> procVars = this.get(jobCode);
        
        return procVars.entrySet().stream().filter(m -> StringUtils.startsWith(m.getKey(), SYMBOL)).collect(
            Collectors.toMap(p -> (String)StringUtils.substringAfter(p.getKey(), SYMBOL),
                p -> (String)p.getValue()));
        
    }
}
