package cn.migu.macaw.jarboot.dao;

import cn.migu.macaw.jarboot.api.model.JarFile;
import cn.migu.macaw.jarboot.model.CollectHandleFile;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * jar-采集文件映射关系dao
 *
 * @author soy
 */
public interface JarFileMapper extends Mapper<JarFile>
{
    /**
     * 获取配置服务处理文件信息
     * @param appId 应用id
     * @param serverId 服务器id
     * @param jarId 部署jar id
     * @return List<CollectHandleFile> - 文件配置列表
     */
    List<CollectHandleFile> getCollectHandleFiles(@Param("appId") String appId,@Param("serverId") String serverId,@Param("jarId") String jarId);
}