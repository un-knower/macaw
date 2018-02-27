package cn.migu.macaw.schedule.task.bean.crossdata;

import java.util.Date;

import com.alibaba.fastjson.JSON;

import cn.migu.macaw.common.DateUtil;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.common.message.CrossDataResult;
import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.schedule.api.model.CrossdataBatchLog;
import cn.migu.macaw.schedule.dao.CrossdataBatchLogMapper;
import cn.migu.macaw.schedule.task.TaskNodeBrief;

/**
 * 数据同步日志
 *
 * @author soy
 */
public class CrossDataLogger
{
    void dataSyncLog(CrossdataBatchLogMapper dataBatchDao, TaskNodeBrief brief, Entity entity)
    {
        try
        {
            String content = entity.getContent();
            CrossDataResult result = JSON.parseObject(content, CrossDataResult.class);
            
            CrossdataBatchLog log = new CrossdataBatchLog();
            log.setBatchNo(brief.getBatchCode());
            log.setJobId(result.getJobId());
            log.setDataKind(3);
            log.setInputNum(result.getInputRecordNum());
            log.setOutputNum(result.getOutputRecordNum());
            String dateFormat = "yyyy-MM-dd HH:mm:ss";
            Date startDate = DateUtil.getDateFormat(result.getStarttime(), dateFormat);
            Date endDate = DateUtil.getDateFormat(result.getEndtime(), dateFormat);
            log.setStartTime(startDate);
            log.setEndTime(endDate);
            log.setJobCode(brief.getJobCode());
            log.setJobState(result.getJobState());
            long elapseTime = endDate.getTime() - startDate.getTime();
            log.setElapse(elapseTime);
            dataBatchDao.insertSelective(log);
        }
        catch (Exception e)
        {
            LogUtils.runLogError(e);
        }
    }
    
}
