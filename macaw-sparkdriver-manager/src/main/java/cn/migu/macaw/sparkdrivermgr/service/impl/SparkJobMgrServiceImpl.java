package cn.migu.macaw.sparkdrivermgr.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.collect.Maps;

import cn.migu.macaw.common.DateUtil;
import cn.migu.macaw.common.RestTemplateProvider;
import cn.migu.macaw.common.SysRetCode;
import cn.migu.macaw.common.log.InterfaceLogBean;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.common.message.Response;
import cn.migu.macaw.sparkdrivermgr.GlobalParam;
import cn.migu.macaw.sparkdrivermgr.api.model.SparkApplicationLog;
import cn.migu.macaw.sparkdrivermgr.cache.SparkJobContext;
import cn.migu.macaw.sparkdrivermgr.common.DriverType;
import cn.migu.macaw.sparkdrivermgr.common.ProcessStatus;
import cn.migu.macaw.sparkdrivermgr.common.ReqRespLog;
import cn.migu.macaw.sparkdrivermgr.common.RetCodeDesc;
import cn.migu.macaw.sparkdrivermgr.manager.DataSheetHandler;
import cn.migu.macaw.sparkdrivermgr.manager.RemoteLaunchJar;
import cn.migu.macaw.sparkdrivermgr.model.AvailableSparkDriverProcess;
import cn.migu.macaw.sparkdrivermgr.model.SparkJobMetaData;
import cn.migu.macaw.sparkdrivermgr.service.ISparkJobMgrService;

/**
 * spark job调度业务层
 * @author soy
 */
@Component("sparkJobMgrService")
public class SparkJobMgrServiceImpl implements ISparkJobMgrService
{
    
    @Resource
    private DataSheetHandler dataSheetHandler;
    
    @Resource
    private RemoteLaunchJar remoteJar;
    
    @Resource
    private ReqRespLog reqRespLog;
    
    @Resource
    private RetCodeDesc retCodeDesc;
    
    @Resource
    private SparkJobContext sparkJobContext;
    
    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;
    
    @Autowired
    private GlobalParam globalParam;
    
    private static final Log sparkDriverInterlog = LogFactory.getLog("spark-driver-interactive");
    
    /**
     * 
     * 获取空闲的服务器
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getValidResource(SparkJobMetaData resource)
    {
        AvailableSparkDriverProcess driverProcess = dataSheetHandler.getIdleDriver(resource.getAppId());
        if (null == driverProcess)
        {
            LogUtils.runLogError(StringUtils.join(resource.getAppId(), ",driver进程资源不足!"));
            return SysRetCode.SPARK_DRIVER_INSUFFICIENT;
        }
        
        resource.setDriverIp(driverProcess.getIp());
        resource.setPort(driverProcess.getPort());
        
        return SysRetCode.SUCCESS;
    }
    
    /**
     * 提交任务到spark driver服务器
     * @param request
     * @param resCtx
     * @see [类、类#方法、类#成员]
     */
    @Override
    public String submit(HttpServletRequest request, SparkJobMetaData resCtx, InterfaceLogBean logBean)
    {
        String retCode = SysRetCode.SUCCESS;
        
        Map<String, String> form =  reqParamToMap(request);
        
        String targetUrl = request.getRequestURL().toString();
        
        targetUrl = StringUtils.substringAfter(targetUrl, "/rs");
        
        targetUrl = StringUtils.join("http://", resCtx.getDriverIp(), ":", resCtx.getPort(), targetUrl);
        
        targetUrl = StringUtils.replace(targetUrl, ".do", "");
        
        try
        {
            
            reqRespLog.requestLog(request, sparkDriverInterlog, logBean);
            String result = RestTemplateProvider.postFormForEntity(restTemplate, targetUrl, String.class, form);
            reqRespLog
                .responseLog(sparkDriverInterlog, logBean, StringUtils.join("[计算中心请求地址:", targetUrl, ",返回结果:", result, "]"));
            
            Response response = JSON.parseObject(result, Response.class, Feature.InitStringFieldAsEmpty);
            
            String code = response.getResponse().getCode();
            
            if (!StringUtils.equals(code, SysRetCode.SUCCESS))
            {
                if (resCtx.isResAlloc())
                {
                    this.freeResource(resCtx);
                }
                return SysRetCode.SPARK_APP_CREATED_FAILED;
            }
            
            if (StringUtils.isNotEmpty(response.getResponse().getAppname()))
            {
                resCtx.setAppName(response.getResponse().getAppname());
                resCtx.setAppId(response.getResponse().getAppid());
                //记录app log
                dataSheetHandler.updateAppLog(resCtx);
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogUtils.runLogError(e);
            retCode = SysRetCode.SPARK_DRIVER_REQUEST_ERROR;
            
            if (resCtx.isResAlloc())
            {
                this.freeResource(resCtx);
            }
        }
        
        return retCode;
    }
    
    @Override
    public synchronized String allocResource(HttpServletRequest request, SparkJobMetaData resCtx)
    
    {
        
        //请求类型
        this.setContextAppType(request, resCtx);
        
        String ret = this.getValidResource(resCtx);
        if (!StringUtils.equals(ret, SysRetCode.SUCCESS))
        {
            return ret;
        }
        
        //1-更新进程状态
        dataSheetHandler.updateProcessStatus(resCtx.getProcessId(), ProcessStatus.PROCESS_READY);
        
        resCtx.setResAlloc(true);
        
        return SysRetCode.SUCCESS;
    }
    
    /**
     * 释放资源
     */
    @Override
    public void freeResource(SparkJobMetaData resCtx)
    {
        
        //恢复进程状态
        dataSheetHandler.updateProcessStatus(resCtx.getProcessId(), ProcessStatus.PROCESS_WAITING);
        
        resCtx.setResAlloc(false);
    }
    
    /**
     * 停止spark context请求
     * @param request
     * @param resCtx
     * @param logBean
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void stopSparkCtx(HttpServletRequest request, SparkJobMetaData resCtx, InterfaceLogBean logBean)
    {
        String stopScUrl =
            StringUtils.join("http://", resCtx.getDriverIp(), ":", resCtx.getPort(), "/SparkSQL/stopSparkSession.do");
        
        try
        {
            Map<String, String> params = Maps.newHashMap();
            params.put("dataSource", "hive");
            reqRespLog.requestLog(request, sparkDriverInterlog, logBean);
            String result = RestTemplateProvider.postFormForEntity(restTemplate, stopScUrl, String.class, params);
            reqRespLog
                .responseLog(sparkDriverInterlog, logBean, StringUtils.join("[计算中心请求地址:", stopScUrl, ",返回结果:", result, "]"));
        }
        catch (Exception e)
        {
            LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
        }
    }
    
    /**
     * 停止spark app
     * @param appName
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public String killSparkApp(String appName, String appId)
    {
        SparkApplicationLog appLog = null;
        
        if (StringUtils.isNotEmpty(appName))
        {
            appLog = dataSheetHandler.getAppByName(appName);
        }
        else if (StringUtils.isNotEmpty(appId))
        {
            appLog = dataSheetHandler.getLatestAppById(appId);
        }
        
        if (null != appLog)
        {
            Map<String, String> param = Maps.newHashMap();
            param.put("appName", appName);
            
            String url = StringUtils.join("http://",
                appLog.getDriverIp(),
                ":",
                String.valueOf(appLog.getDriverPort()),
                "/stopSparkAppOnSub.do");
            
            String result;
            try
            {
                
                Response response = RestTemplateProvider.postFormForEntity(restTemplate, url, Response.class, param);
                
                if (!StringUtils.equals(response.getResponse().getCode(), SysRetCode.SUCCESS))
                {
                    LogUtils.runLogError(
                        StringUtils.join(response.getResponse().getCode(), "--", response.getResponse().getDesc()));
                    return SysRetCode.SPARK_APP_STOP_FAIL;
                }
                
                remoteJar.callback(appName, "KILLED", appLog.getDriverPort().intValue(), null);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                LogUtils.runLogError(e);
                return SysRetCode.SPARK_APP_STOP_FAIL;
            }
            
        }
        
        return SysRetCode.SUCCESS;
    }
    
    /**
     * 校验资源参数(资源核数和内存数)
     * @param coreNum 资源核数
     * @param memSize 内存大小
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public String checkResParam(String coreNum, String memSize)
    {
        if (StringUtils.isEmpty(coreNum))
        {
            return SysRetCode.SPARK_CORENUM_EMPTY;
        }
        
        if (StringUtils.isEmpty(memSize))
        {
            return SysRetCode.SPARK_MEMSIZE_EMPTY;
        }
        
        if (!NumberUtils.isNumber(coreNum))
        {
            return SysRetCode.SPARK_CORENUM_NONNUMERIC;
        }
        
        if (!NumberUtils.isNumber(memSize))
        {
            return SysRetCode.SPARK_MEMSIZE_NONNUMERIC;
        }
        
        int iCoreNum = Integer.valueOf(coreNum);
        
        if (iCoreNum <= 0 || iCoreNum > globalParam.getSparkMaxCores())
        {
            return SysRetCode.SPARK_CORENUM_OVERFLOW;
        }
        
        int iMemSize = Integer.valueOf(memSize);
        
        if (iMemSize <= 0 || iMemSize > globalParam.getSparkMaxMem())
        {
            return SysRetCode.SPARK_CORENUM_OVERFLOW;
        }
        
        return SysRetCode.SUCCESS;
    }
    
    /**
     * 初始化spark context
     * @param request
     * @param resCtx
     * @param entity
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void initSparkCtx(HttpServletRequest request, SparkJobMetaData resCtx, Entity entity,
        InterfaceLogBean logBean)
    {

        Map<String,String> form = reqParamToMap(request);

        String targetUrl =
            StringUtils.join("http://", resCtx.getDriverIp(), ":", resCtx.getPort(), "/SparkSQL/startSparkSession.do");
        try
        {
            reqRespLog.requestLog(request, sparkDriverInterlog, logBean);
            String result = RestTemplateProvider.postFormForEntity(restTemplate, targetUrl, String.class, form);
            reqRespLog
                .responseLog(sparkDriverInterlog, logBean, StringUtils.join("[计算中心请求地址:", targetUrl, ",返回结果:", result, "]"));
            
            Response response = JSON.parseObject(result, Response.class, Feature.InitStringFieldAsEmpty);
            
            if (null != response && null != response.getResponse())
            {
                Entity retEntry = response.getResponse();
                if (StringUtils.equals(retEntry.getCode(), SysRetCode.SUCCESS))
                {
                    BeanUtils.copyProperties(retEntry, entity);
                    
                    if (StringUtils.isNotEmpty(retEntry.getAppid()))
                    {
                        resCtx.setAppId(retEntry.getAppid());
                        resCtx.setAppName(retEntry.getAppname());
                        
                        //缓存appid-ResourceContext关系
                        sparkJobContext.put(retEntry.getAppid(), resCtx);
                        
                    }
                    
                }
                else
                {
                    BeanUtils.copyProperties(retEntry, entity);
                }
            }
            else
            {
                entity.setCode(SysRetCode.SPARK_APP_CREATED_FAILED);
                entity.setDesc(retCodeDesc.getDesc(SysRetCode.SPARK_APP_CREATED_FAILED));
                
                //释放资源
                if (resCtx.isResAlloc())
                {
                    this.freeResource(resCtx);
                }
            }
            
        }
        catch (Exception e)
        {
            LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
            entity.setCode(SysRetCode.SPARK_APP_CREATED_FAILED);
            entity.setDesc(retCodeDesc.getDesc(SysRetCode.SPARK_APP_CREATED_FAILED));
            
            //释放资源
            if (resCtx.isResAlloc())
            {
                this.freeResource(resCtx);
            }
        }
        
    }
    
    /**
     * 从redis缓存中获取资源操作上下文
     * @param resCtx
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public String getResourceCtxCache(SparkJobMetaData resCtx)
    {
        if (StringUtils.isNotEmpty(resCtx.getAppId()))
        {
            SparkJobMetaData allocCtx = sparkJobContext.get(resCtx.getAppId());
            if (null != allocCtx)
            {
                BeanUtils.copyProperties(allocCtx, resCtx);
                resCtx.setResAlloc(false);
                
                return SysRetCode.SUCCESS;
            }
        }
        
        return SysRetCode.ERROR;
    }
    
    @Override
    public Response querySparkApp(String ip, String port, String appId)
    {
        Response resp = new Response();
        Entity entity = new Entity();
        
        if (StringUtils.isEmpty(appId))
        {
            entity.setCode(SysRetCode.SPARK_APP_NOT_FOUND);
            entity.setDesc(retCodeDesc.getDesc(SysRetCode.SPARK_APP_NOT_FOUND));
            
            resp.setResponse(entity);
            return resp;
        }
        
        this.querySparkAppFromDb(appId, entity);
        
        resp.setResponse(entity);
        
        return resp;
    }
    
    /**
     * 从数据库查询app状态
     * @param appId
     * @param entity
     * @see [类、类#方法、类#成员]
     */
    private void querySparkAppFromDb(String appId, Entity entity)
    {
        SparkApplicationLog app = dataSheetHandler.getLatestAppById(appId);
        
        if (null == app)
        {
            entity.setCode(SysRetCode.SPARK_APP_NOT_FOUND);
            entity.setDesc(retCodeDesc.getDesc(SysRetCode.SPARK_APP_NOT_FOUND));
            
            return;
        }
        
        entity.setAppid(appId);
        entity.setAppname(app.getAppName());
        entity.setCode(app.getStatus());
        entity.setDesc(retCodeDesc.getDesc(app.getStatus()));
        
        if (!StringUtils.equals(app.getStatus(), SysRetCode.SPARK_APP_RUNNING))
        {
            entity.setStarttime(DateUtil.getDateString(app.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
            entity.setEndtime(DateUtil.getDateString(app.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
            
            entity.setTimecost(StringUtils.join(app.getDuration().toString(), "s"));
            
            String stackError = StringUtils.substringAfter(app.getNote(), "errorStack=>");
            if (StringUtils.isNotEmpty(stackError))
            {
                entity.setErrorStack(stackError);
            }
        }
        
    }
    
    /**
     * 是否为算法中心请求
     */
    private boolean isAlgoReq(HttpServletRequest request)
    {
        String type = request.getParameter("targetType");
        
        if (StringUtils.isEmpty(type) || !StringUtils.equals(type, DriverType.ALGO))
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * 设置APP类型
     * @param request
     * @param ctx
     * @see [类、类#方法、类#成员]
     */
    private void setContextAppType(HttpServletRequest request, SparkJobMetaData ctx)
    {
        String sparkPath = request.getParameter("sparkPath");
        String sparkMasterIp = StringUtils.split(StringUtils.substringAfter(sparkPath, "spark://"), ":")[0];
        
        if (this.isAlgoReq(request))
        {
            ctx.setAppId(StringUtils.join(sparkMasterIp, "-", DriverType.ALGO));
        }
        else
        {
            ctx.setAppId(StringUtils.join(sparkMasterIp, "-", DriverType.CALC));
        }
    }

    /**
     * http请求参数转换为map
     * @param request http请求
     * @return map结构
     */
    private Map<String,String> reqParamToMap(HttpServletRequest request)
    {
        return request.getParameterMap()
            .entrySet()
            .stream()
            .filter(e -> null != e.getValue() && e.getValue().length > 0)
            .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()[0]));
    }
}
