package cn.migu.macaw.sparkdrivermgr.dao;

import cn.migu.macaw.sparkdrivermgr.api.model.Process;
import cn.migu.macaw.sparkdrivermgr.model.AvailableSparkDriverProcess;
import cn.migu.macaw.sparkdrivermgr.model.SparkDriverMetaData;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author soy
 */
public interface ProcessMapper extends Mapper<Process>
{
    /**
     * 查询可用的spark driver
     * 规则(数量最多时间最长):
     * 1.某主机上driver资源剩余最多
     * 2.启动时间最长的driver
     * @param driverType driver类型
     * @return
     */
    AvailableSparkDriverProcess queryAvailableDriver(@Param("driverType") String driverType);

    /**
     * 根据进程表主键查询spark driver元数据信息
     * @param processId 进程主键
     * @return SparkDriverMetaData spark driver元数据
     */
    SparkDriverMetaData queryDriverMeta(@Param("processId") String processId);

    /**
     * 根据部署jar id删除相关连的进程
     *
     * @param jarId 部署jar id
     */
    void deleteProcessByJarId(@Param("jarId") String jarId);

}