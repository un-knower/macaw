package cn.migu.macaw.sparkdrivermgr.dao;

import cn.migu.macaw.sparkdrivermgr.api.model.Jar;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author soy
 */
public interface JarMapper extends Mapper<Jar>
{
    /**
     * 根据spark driver类型获取jar的部署信息
     * @param driverType
     * @return List<Jar> - 启动jar部署信息列表
     */
    List<Jar> getDriverDeploymentByType(@Param("driverType") String driverType);

    /**
     * 获取所有spark driver的部署信息
     *
     * @return List<Jar> - 启动jar部署信息列表
     */
    List<Jar> getAllDriverDeployment();
}