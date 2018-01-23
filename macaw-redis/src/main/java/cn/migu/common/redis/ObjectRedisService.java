package cn.migu.common.redis;

import java.io.Serializable;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.stereotype.Service;

/**
 * java对象序列化后存redis
 * @author soy
 */
@Service
public class ObjectRedisService
{
    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;
    
    /**
     * 批量删除key
     * @param pattern
     */
    public void removePattern(final String pattern)
    {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
        {
            redisTemplate.delete(keys);
        }
    }
    
    /**
     * 删除对应的value
     * @param key
     */
    public void remove(final String key)
    {
        if (exists(key))
        {
            redisTemplate.delete(key);
        }
    }
    
    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    public boolean exists(final String key)
    {
        return redisTemplate.hasKey(key);
    }
    
    /**
     * 读取缓存
     * @param key
     * @return
     */
    public Object get(final String key)
    {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }
    
    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value)
    {
        boolean result = false;
        try
        {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * hash键是否存在
     * @param key
     * @param field
     * @return
     */
    public boolean exists(String key, String field)
    {
        BoundHashOperations hashOps = redisTemplate.boundHashOps(key);
        return hashOps.hasKey(field);
    }
    
    /**
     * 获取hash值
     * @param key
     * @param field
     * @return
     */
    public <T> T getValueByKeyANdField(String key, String field, Class<T> clazz)
    {
        BoundHashOperations hashOps = redisTemplate.boundHashOps(key);
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer(clazz));
        return (T)hashOps.get(field);
    }
    
    /**
     * 存入hash值
     * @param key
     * @param field
     * @param value
     * @param <T>
     */
    public <T> void put(String key, String field, T value,Class<T> clazz)
    {
        BoundHashOperations hashOps = redisTemplate.boundHashOps(key);
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer(clazz));
        hashOps.put(field, value);
    }
    
}
