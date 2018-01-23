package cn.migu.macaw.schedule.dao;

import cn.migu.macaw.common.mapper.CommonMapper;
import cn.migu.macaw.schedule.api.model.JobLog;
import org.apache.ibatis.annotations.Param;

/**
 * Job Log Mapper
 * @author soy
 */
public interface JobLogMapper extends CommonMapper<JobLog>
{
    /**
     * 根据开始时间获取最新的记录
     * @param jobCode
     * @return
     */
    JobLog getRecentJobLog(@Param("jobCode") String jobCode);
}
