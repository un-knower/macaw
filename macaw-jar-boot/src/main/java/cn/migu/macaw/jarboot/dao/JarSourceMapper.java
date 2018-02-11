package cn.migu.macaw.jarboot.dao;

import cn.migu.macaw.jarboot.api.model.JarSource;
import cn.migu.macaw.jarboot.model.DataFileCollectConf;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * jar source dao
 *
 * @author soy
 */
public interface JarSourceMapper extends Mapper<JarSource>
{
    /**
     * 查询数据文件采集应用系统配置信息
     * @param appId 应用配置id
     * @param serverId 部署服务器id
     * @param jarId 服务部署id
     * @return List<DataFileCollectConf> - 数据文件采集应用系统配置列表
     */
    List<DataFileCollectConf> getDataFileCollectConf(@Param("appId") String appId,@Param("serverId") String serverId,@Param("jarId") String jarId);
}