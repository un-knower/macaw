package cn.migu.macaw.jarboot.dao;

import cn.migu.macaw.jarboot.api.model.DataFile;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * 采集数据文件Dao
 *
 * @author soy
 */
public interface DataFileMapper extends Mapper<DataFile>
{
    /**
     * 获取数据文件采集信息
     * @param appId 部署服务id
     * @param serverId 部署服务器id
     * @param jarId 部署服务jar id
     * @return DataFile - 配置采集文件实例
     */
    DataFile getExtFuncDataCollect(@Param("appId") String appId,@Param("serverId") String serverId,@Param("jarId") String jarId);

}