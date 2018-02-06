package cn.migu.macaw.jarboot.dao;

import cn.migu.macaw.jarboot.api.model.Process;
import cn.migu.macaw.jarboot.model.HostPid;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 进程DAO
 *
 * @author soy
 */
public interface ProcessMapper extends Mapper<Process>
{
    /**
     * 获取应用的主机地址和进程id
     * @param appId
     * @param serverId
     * @param jarId
     * @return
     */
    List<HostPid> getHostPidForJar(@Param("appId") String appId,@Param("serverId") String serverId,@Param("jarId") String jarId);
}