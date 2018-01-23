package cn.migu.macaw.sparkdrivermgr.dao;

import cn.migu.macaw.sparkdrivermgr.api.model.SparkApplicationLog;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author soy
 */
public interface SparkApplicationLogMapper extends Mapper<SparkApplicationLog>
{
    /**
     * 根据spark appid获取最新的一条日志记录
     * @param appid spark appid
     * @return SparkApplicationLog - 日志记录
     */
    SparkApplicationLog getLatestAppLogByAppId(@Param("appid") String appid);

}