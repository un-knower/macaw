package cn.migu.macaw.schedule.feign;

import cn.migu.macaw.common.ServiceName;
import cn.migu.macaw.hdfs.api.service.HdfsService;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * hdfs服务
 *
 * @author soy
 */
@FeignClient(name = ServiceName.HDFS)
public interface FeignHdfsService extends HdfsService
{
}
