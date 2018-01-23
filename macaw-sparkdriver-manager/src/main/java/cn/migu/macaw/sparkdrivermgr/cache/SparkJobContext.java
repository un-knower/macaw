package cn.migu.macaw.sparkdrivermgr.cache;

import cn.migu.common.redis.StringRedisService;
import cn.migu.macaw.sparkdrivermgr.model.SparkJobMetaData;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * spark job所需上下文缓存
 * @author soy
 */
@Component("sparkJobContext")
public class SparkJobContext implements ContextKeys
{
    @Autowired
    private StringRedisService redisService;

    /**
     * 缓存appid-resctx
     * @param appId spark job id
     * @param resCtx spark job信息
     * @see [类、类#方法、类#成员]
     */
    public void put(String appId, SparkJobMetaData resCtx)
    {
        if (null != redisService)
        {
            redisService.put(appId, SPARK_JOB_CTX, JSON.toJSONString(resCtx));
        }
    }

    /**
     * 删除appid-resctx
     * @param appId spark job id
     */
    public void remove(String appId)
    {
        if (null != redisService)
        {
            redisService.remove(appId);
        }
    }

    /**
     * 获取appid对应的资源上下文
     * @param appId spark job id
     * @return ResourceContext
     * @see [类、类#方法、类#成员]
     */
    public SparkJobMetaData get(String appId)
    {
        if (null != redisService)
        {
            if (redisService.exists(appId))
            {

                String ctxJsonStr = (String)redisService.getValueByKeyANdField(appId, SPARK_JOB_CTX);

                return  JSON.parseObject(ctxJsonStr, SparkJobMetaData.class, Feature.InitStringFieldAsEmpty);
            }
        }

        return null;
    }

    /**
     * 是否包含key
     * @param key
     * @return
     */
    public boolean contains(String key)
    {
        return redisService.exists(key);
    }



    /**
     * 清空数据库
     * @see [类、类#方法、类#成员]
     */
    public void clear()
    {
        if (null != redisService)
        {
            redisService.flushDb();
        }
    }
}
