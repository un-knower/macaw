package cn.migu.macaw.schedule.rest;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.migu.macaw.common.RequestKey;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.schedule.api.model.Procedure;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.util.RequestServiceUri;
import cn.migu.macaw.schedule.workflow.DataConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Maps;

import cn.migu.macaw.common.RestTemplateProvider;
import cn.migu.macaw.common.ServiceUrlProvider;
import cn.migu.macaw.common.message.Response;
import cn.migu.macaw.schedule.api.model.Job;
import cn.migu.macaw.schedule.api.service.SparkClusterJobEvent;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.dao.JobMapper;
import cn.migu.macaw.schedule.dao.ProcedureMapper;
import cn.migu.macaw.schedule.service.IJobTasksService;
import cn.migu.macaw.schedule.task.util.ServiceReqClient;
import tk.mybatis.mapper.entity.Example;

/**
 * spark集群/job事件处理接口
 *
 * @author soy
 */
@RestController
public class SparkClusterJobRestImpl implements SparkClusterJobEvent
{
    
    @Autowired
    private ServiceReqClient client;
    
    @Autowired
    private ProcedureMapper procDao;
    
    @Autowired
    private JobMapper jobDao;
    
    @Autowired
    private IJobTasksService jobTasksService;
    
    @Autowired
    private JobTasksCache jobTasksCache;
    
    @Resource(name = "restTemplateForLoadBalance")
    private RestTemplate restTemplateForLoadBalance;
    
    private static final Log logger = LogFactory.getLog(SparkClusterJobRestImpl.class);
    
    @Override
    public void sparkClusterRestartEvent(HttpServletRequest request)
    {
        logger.info("=>接收到spark集群重启事件");
        
        //查询所有运行中的存储过程对应的job
        Example example = new Example(Job.class);
        example.createCriteria().andEqualTo("kind", 0).andEqualTo("realState", 2);
        
        List<Job> runningJobs = jobDao.selectByExample(example);
        if (CollectionUtils.isNotEmpty(runningJobs))
        {
            
            Map<String, String> formParam = Maps.newHashMap();
            
            for (Job job : runningJobs)
            {
                String jobCode = job.getCode();
                
                formParam.put("name", jobCode);
                
                try
                {
                    RestTemplateProvider.postFormForEntity(restTemplateForLoadBalance,
                        ServiceUrlProvider.jobScheduleService("interruptJob.do"),
                        String.class,
                        formParam);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public Response residualAppHandle(HttpServletRequest request)
    {
        String jobCode = request.getParameter(RequestKey.JOB_CODE);

        String batchCode = request.getParameter(RequestKey.BATCH_NO);

        String appId = request.getParameter(RequestKey.APP_ID);

        String appName = request.getParameter(RequestKey.APP_NAME);

        String isProcedureStr = request.getParameter(RequestKey.IS_PROCEDURE);

        boolean isProcedure = StringUtils.isNotEmpty(isProcedureStr) ? true : false;

        if (StringUtils.isEmpty(jobCode) || StringUtils.isEmpty(appId))
        {
            logger.info("spark context fallback:=>job_code为空或appId为空");
            return null;
        }

        String realJobCode = jobCode;

        if (isProcedure)
        {
            Procedure proc = new Procedure();
            proc.setCode(jobCode);
            Procedure tProc = procDao.selectOne(proc);
            if (null == tProc)
            {
                logger.info( "spark context fallback:procedure is not found" + realJobCode);
                return null;
            }

            realJobCode = tProc.getJobCode();
        }
        else
        {
            if (StringUtils.isEmpty(appName))
            {
                logger.info(StringUtils.join("fallback appname is empty, jobCode=", realJobCode));

                return null;
            }
        }

        logger.info(StringUtils.join("spark context fallback:=>job_code=",
            realJobCode,
            ",batchCode=",
            batchCode,
            ",appId=",
            appId));

        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }

        if (!jobTasksService.isResidualCtx(realJobCode))
        {

            if (isProcedure)
            {
                String cAppid = jobTasksCache.get(realJobCode, "-", DataConstants.SPARK_CONTEXT_APPID);

                if (StringUtils.equals(cAppid, appId))
                {
                    return null;
                }
                else
                {
                    logger.info(StringUtils.join("fallback appid is not same as current appid,",
                        appId,
                        "<=>",
                        cAppid,
                        "jobCode=",
                        realJobCode));
                }
            }
            else
            {
                //缓存中查找到,说明任务在运行中
                String[] appNames = jobTasksCache.getList(jobCode, "-", DataConstants.RUNNING_SPARK_APPNAMES);
                if (ArrayUtils.contains(appNames, appName))
                {
                    return null;
                }
                else
                {
                    logger.info(StringUtils.join("fallback appid is not in running app list,", appId, ",jobCode=", realJobCode));
                }

            }

        }

        try
        {
            Thread.sleep(500);
            TaskNodeBrief brief = new TaskNodeBrief(realJobCode, batchCode);

            String resUrl = ServiceUrlProvider.sparkJobMgrService(RequestServiceUri.SPARK_DRIVER_FREE);

            if (isProcedure)
            {
                //释放请求
                client.postCommonTask(resUrl, client.sparkCtxFreeEntity(appId), brief);
            }
            else
            {
                client.stopSparkTask(appName, appId, brief);
            }

        }
        catch (Exception e)
        {
            LogUtils.runLogError(e);
        }

        return null;
    }
}
