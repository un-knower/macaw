package cn.migu.macaw.sparkdrivermgr.hook;

import java.util.Map;

import cn.migu.macaw.common.RequestKey;
import cn.migu.macaw.common.RestTemplateProvider;
import cn.migu.macaw.common.ServiceName;
import cn.migu.macaw.common.ServiceUrlProvider;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;

import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.sparkdrivermgr.model.SparkJobMetaData;

import com.google.common.collect.Maps;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * spark job提交问题处理
 * @author soy
 */
@Service
public class SparkSubmitHook
{
    @Resource(name = "restTemplateForLoadBalance")
    private RestTemplate restTemplateForLoadBalance;

    /**
     * 发送线程
     * @param resCtx
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Async
    public void sendAliveMsg(final SparkJobMetaData resCtx, final boolean isProcedure)
    {
        postAliveMsg(resCtx, isProcedure);
    }
    
    /**
     * 发送附加消息
     * @param resCtx
     * @see [类、类#方法、类#成员]
     */
    public void postAliveMsg(final SparkJobMetaData resCtx, boolean isProcedure)
    {
        if (StringUtils.isNotEmpty(resCtx.getJobCode()) && StringUtils.isNotEmpty(resCtx.getAppId()))
        {
            Map<String, String> params = Maps.newHashMap();
            params.put(RequestKey.JOB_CODE, resCtx.getJobCode());
            params.put(RequestKey.APP_ID, resCtx.getAppId());
            params.put(RequestKey.BATCH_NO, resCtx.getBatchNo());
            if (isProcedure)
            {
                params.put(RequestKey.IS_PROCEDURE, "1");
            }
            else
            {
                if (StringUtils.isNotEmpty(resCtx.getAppName()))
                {
                    params.put(RequestKey.APP_NAME, resCtx.getAppName());
                }
            }
            
            try
            {
                RestTemplateProvider.postFormForEntity(restTemplateForLoadBalance,
                    ServiceUrlProvider.jobScheduleService("residualDriverHandle.do"),String.class,params);
            }
            catch (Exception e)
            {
                LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
            }
            
        }
    }
}
