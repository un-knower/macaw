package cn.migu.common.redis;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * redis服务
 * @author soy
 */
@Service
public class StringRedisService
{
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    /**
     * 批量删除对应的value
     * @param keys
     */
    public void remove(final String... keys)
    {
        for (String key : keys)
        {
            remove(key);
        }
    }
    
    /**
     * 批量删除key
     * @param pattern
     */
    public void removePattern(final String pattern)
    {
        Set<String> keys = redisTemplate.keys(pattern);
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
    public String get(final String key)
    {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }
    
    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, String value)
    {
        boolean result = false;
        try
        {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
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
     * 写入缓存
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public boolean set(final String key, String value, Long expireTime)
    {
        boolean result = false;
        try
        {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 根据Key刷新超时时间
     * @param key
     * @param expireTime
     * @return
     */
    public boolean flushExpireTime(final String key, Long expireTime)
    {
        boolean result = false;
        try
        {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
    
    public Long increase(String key, String field)
    {
        BoundHashOperations hashOps = redisTemplate.boundHashOps(key);
        if (exists(key, field))
        {
            return hashOps.increment(field, 1L);
        }
        else
        {
            hashOps.putIfAbsent(field, 1);
            return 1L;
        }
    }
    
    public Long decrease(String key, String field)
    {
        BoundHashOperations hashOps = redisTemplate.boundHashOps(key);
        if (exists(key, field))
        {
            long count = hashOps.increment(field, -1L);
            if (count == 0)
            {
                deleteField(key, field);
                return 0L;
            }
            else
            {
                return count;
            }
        }
        return 0L;
    }
    
    public void deleteField(String key, String field)
    {
        BoundHashOperations hashOps = redisTemplate.boundHashOps(key);
        hashOps.delete(field);
    }
    
    public void batchDeleteField(String key, String... field)
    {
        BoundHashOperations hashOps = redisTemplate.boundHashOps(key);
        hashOps.delete(field);
    }
    
    public Set<String> getFields(String key)
    {
        BoundHashOperations hashOps = redisTemplate.boundHashOps(key);
        return hashOps.keys();
    }
    
    public boolean exists(String key, String field)
    {
        BoundHashOperations hashOps = redisTemplate.boundHashOps(key);
        return hashOps.hasKey(field);
    }
    
    public Object getValueByKeyANdField(String key, String field)
    {
        BoundHashOperations hashOps = redisTemplate.boundHashOps(key);
        return hashOps.get(field);
    }
    
    public Map<String, String> getEntries(String key)
    {
        BoundHashOperations hashOps = redisTemplate.boundHashOps(key);
        return hashOps.entries();
    }
    
    public void put(String key, String field, String value)
    {
        BoundHashOperations hashOps = redisTemplate.boundHashOps(key);
        hashOps.put(field, value);
    }

    public void addForSet(String key, String value)
    {
        redisTemplate.opsForSet().add(key, value);
    }

    public void delForSet(String key, String value)
    {
        redisTemplate.opsForSet().remove(key, value);
    }

    public Set<String> queryForSet(String key)
    {
        return redisTemplate.opsForSet().members(key);
    }

    public void setForHashKeyCounter(String key, String field)
    {
        redisTemplate.opsForHash().increment(key, field, 1);
    }

    public void delForHashKeyCounter(String key, String field)
    {
        if (redisTemplate.hasKey(key))
        {
            redisTemplate.opsForHash().increment(key, field, -1);
        }
    }

    public void flushDb()
    {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
    
}
