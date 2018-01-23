package cn.migu.macaw.sparkdrivermgr.rest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.migu.macaw.common.RestTemplateProvider;
import cn.migu.macaw.common.SysRetCode;
import cn.migu.macaw.common.log.InterfaceLogBean;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.common.message.Response;
import cn.migu.macaw.sparkdrivermgr.GlobalParam;
import cn.migu.macaw.sparkdrivermgr.api.model.Jar;
import cn.migu.macaw.sparkdrivermgr.api.model.SparkApplicationLog;
import cn.migu.macaw.sparkdrivermgr.api.service.SparkDriverManagerService;
import cn.migu.macaw.sparkdrivermgr.cache.SparkJobContext;
import cn.migu.macaw.sparkdrivermgr.common.DriverType;
import cn.migu.macaw.sparkdrivermgr.common.ProcessStatus;
import cn.migu.macaw.sparkdrivermgr.common.ReqRespLog;
import cn.migu.macaw.sparkdrivermgr.common.RetCodeDesc;
import cn.migu.macaw.sparkdrivermgr.hook.SparkSubmitHook;
import cn.migu.macaw.sparkdrivermgr.manager.DataSheetHandler;
import cn.migu.macaw.sparkdrivermgr.manager.RemoteLaunchJar;
import cn.migu.macaw.sparkdrivermgr.model.SparkDriverMetaData;
import cn.migu.macaw.sparkdrivermgr.model.SparkJobMetaData;
import cn.migu.macaw.sparkdrivermgr.api.model.Process;
import cn.migu.macaw.sparkdrivermgr.service.ISparkJobMgrService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * spark driver管理restful接口实现
 * @author soy
 */
public class SparkDriverMgrRestImpl implements SparkDriverManagerService
{
    private static final Log sparkJobForwardLog = LogFactory.getLog("spark-job-forword");

    private static final Log querySparkLog = LogFactory.getLog("spark-job-query");

    private static final Log driverInteractivelog = LogFactory.getLog("spark-driver-interactive");

    @Autowired
    private ReqRespLog reqRespLog;

    @Autowired
    private RetCodeDesc retCodeDesc;

    @Autowired
    private ISparkJobMgrService sparkJobMgrService;

    @Autowired
    private RemoteLaunchJar remoteJar;

    @Autowired
    private DataSheetHandler dataSheetHandler;

    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private SparkJobContext sparkJobContext;

    @Autowired
    private GlobalParam globalParam;

    @Autowired
    private SparkSubmitHook sparkSubmitHook;

    @Override
    public Response sparkJobForward(HttpServletRequest request)
    {
        Response resp = new Response();
        Entity entity = new Entity();

        InterfaceLogBean logBean = reqRespLog.initLogBean(request);

        reqRespLog.requestLog(request, sparkJobForwardLog, logBean);

        //获取CPU核数
        String coresNum = request.getParameter("coresNum");
        //获取内存参数
        String memSize = request.getParameter("executorMemory");

        String ret = sparkJobMgrService.checkResParam(coresNum, memSize);
        if (!StringUtils.equals(ret, SysRetCode.SUCCESS))
        {
            entity.setCode(ret);
            entity.setDesc(retCodeDesc.getDesc(ret));
            resp.setResponse(entity);

            reqRespLog.responseLog(sparkJobForwardLog, logBean, StringUtils.join("资源参数不合法:核数-", coresNum, ",内存-", memSize));

            return resp;
        }
        else
        {
            String batchNo = request.getParameter("batchNo");
            String jobCode = request.getParameter("jobCode");
            String taskCode = request.getParameter("taskCode");

            String appId = request.getParameter("appId");

            //获取可用资源
            SparkJobMetaData resCtx = new SparkJobMetaData();
            resCtx.setBatchNo(batchNo);
            resCtx.setJobCode(jobCode);
            resCtx.setTaskCode(taskCode);
            resCtx.setCoreNum(Integer.valueOf(coresNum));
            resCtx.setMemSize(Integer.valueOf(memSize));
            resCtx.setAppId(appId);

            String retCode = sparkJobMgrService.getResourceCtxCache(resCtx);
            if (!StringUtils.equals(retCode, SysRetCode.SUCCESS))
            {
                retCode = sparkJobMgrService.allocResource(request, resCtx);
            }

            if (!StringUtils.equals(retCode, SysRetCode.SUCCESS))
            {
                entity.setCode(retCode);
                entity.setDesc(retCodeDesc.getDesc(retCode));
                resp.setResponse(entity);

                reqRespLog.responseLog(sparkJobForwardLog, logBean, "申请driver资源失败");

                return resp;
            }

            //发送请求到远端
            retCode = sparkJobMgrService.submit(request, resCtx, logBean);
            if (!StringUtils.equals(retCode, SysRetCode.SUCCESS))
            {
                entity.setCode(retCode);
                entity.setDesc(retCodeDesc.getDesc(retCode));
                resp.setResponse(entity);

                reqRespLog.responseLog(sparkJobForwardLog, logBean, "任务提交driver失败");

                return resp;
            }

            entity.setAppid(resCtx.getAppId());
            entity.setAppname(resCtx.getAppName());

            sparkSubmitHook.sendAliveMsg(resCtx, false);

        }

        reqRespLog.responseLog(sparkJobForwardLog, logBean, "提交任务成功");

        resp.setResponse(entity);

        return resp;
    }
    
    @Override
    public Response stopSparkJob(HttpServletRequest request)
    {
        Response resp = new Response();
        Entity entity = new Entity();

        InterfaceLogBean logBean = reqRespLog.initLogBean(request);

        reqRespLog.requestLog(request, sparkJobForwardLog, logBean);

        //spark服务地地址
        String appName = request.getParameter("appName");
        String appId = request.getParameter("appId");
        if (StringUtils.isEmpty((appName)) && StringUtils.isEmpty((appId)))
        {
            entity.setCode(SysRetCode.ERROR);
            entity.setDesc("spark任务名和Id为空");
            resp.setResponse(entity);

            reqRespLog.responseLog(sparkJobForwardLog, logBean, "spark进程名或ID不能为空");

            return resp;
        }

        String retCode = sparkJobMgrService.killSparkApp(appName, appId);
        if (!StringUtils.equals(retCode, SysRetCode.SUCCESS))
        {
            entity.setCode(SysRetCode.ERROR);
            entity.setDesc("停止spark app失败");
            resp.setResponse(entity);

            reqRespLog.responseLog(sparkJobForwardLog, logBean, "停止spark app失败");

            return resp;
        }

        reqRespLog.responseLog(sparkJobForwardLog, logBean, "停止spark app成功");

        resp.setResponse(entity);

        return resp;
    }
    
    @Override
    public Response querySparkJobStatus(HttpServletRequest request)
    {
        InterfaceLogBean logBean = reqRespLog.initLogBean(request);

        reqRespLog.requestLog(request, querySparkLog, logBean);

        String appId = request.getParameter("appId");

        String sparkIp = request.getParameter("sparkIp");
        String sparkMgrPort = request.getParameter("sparkMgrPort");

        Response resp = sparkJobMgrService.querySparkApp(sparkIp, sparkMgrPort, appId);

        reqRespLog.responseLog(querySparkLog, logBean, JSONObject.toJSONString(resp));

        return resp;
    }
    
    @Override
    public Response processAttachedStartCompleted(HttpServletRequest request)
    {
        InterfaceLogBean logBean = reqRespLog.initLogBean(request);

        reqRespLog.requestLog(request, sparkJobForwardLog, logBean);

        String driverPort = request.getParameter("driverPort");
        String pid = request.getParameter("pid");
        String appId = request.getParameter("appId");
        String serverId = request.getParameter("serverId");
        String jarId = request.getParameter("jarId");
        Process unifyP = new Process();

        unifyP.setAppId(appId);
        unifyP.setServerId(serverId);
        unifyP.setJarId(jarId);
        unifyP.setPort(Integer.valueOf(driverPort));
        unifyP.setProcessNo(Integer.valueOf(pid));
        unifyP.setStatus(ProcessStatus.PROCESS_WAITING);
        unifyP.setKind("2");

        dataSheetHandler.addProcess(unifyP);

        reqRespLog.responseLog(sparkJobForwardLog, logBean, "增加进程记录成功");

        return null;
    }
    
    @Override
    public Response sparkJobSubmitCompleted(HttpServletRequest request)
    {
        Response resp = new Response();
        Entity entity = new Entity();

        InterfaceLogBean logBean = reqRespLog.initLogBean(request);

        reqRespLog.requestLog(request, sparkJobForwardLog, logBean);

        String appId = request.getParameter("appId");
        String appName = request.getParameter("appName");
        String clientIp = request.getParameter("clientIp");
        String driverIp = request.getParameter("driverIp");
        String driverPort = request.getParameter("driverPort");
        String note = request.getParameter("note");

        //在spark进程任务表中插入一条记录
        SparkApplicationLog sparkapp = new SparkApplicationLog();
        sparkapp.setAppid(appId);
        sparkapp.setAppName(appName);
        sparkapp.setSubmitIp(clientIp);
        sparkapp.setStartTime(new Date());
        sparkapp.setSparkMode("FAIR");
        sparkapp.setDriverIp(driverIp);
        sparkapp.setDriverPort(Integer.valueOf(driverPort));
        sparkapp.setStatus(SysRetCode.SPARK_APP_RUNNING);
        sparkapp.setNote(note);

        try
        {
            dataSheetHandler.addAppLog(sparkapp);
            reqRespLog.responseLog(sparkJobForwardLog, logBean, "新增APP记录成功");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            String errMsg = ExceptionUtils.getStackTrace(e);
            LogUtils.runLogError(errMsg);
            reqRespLog.responseLog(sparkJobForwardLog, logBean, errMsg);
            entity.setCode(SysRetCode.ERROR);
            entity.setDesc("新增APP记录失败");
            resp.setResponse(entity);
        }

        return resp;
    }
    
    @Override
    public Response sparkJobCompleted(HttpServletRequest request)
    {
        InterfaceLogBean logBean = reqRespLog.initLogBean(request);

        reqRespLog.requestLog(request, sparkJobForwardLog, logBean);

        String appName = request.getParameter("appName");
        String status = request.getParameter("status");

        String port = request.getParameter("port");

        try
        {
            remoteJar.callback(appName, status, Integer.valueOf(port), request);
            reqRespLog.responseLog(sparkJobForwardLog, logBean, StringUtils.join("回调成功:", appName, ",状态为", status));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            String errMsg = ExceptionUtils.getStackTrace(e);
            LogUtils.runLogError(errMsg);
            reqRespLog.responseLog(sparkJobForwardLog, logBean, errMsg);
        }

        return null;
    }
    
    @Override
    public Response initSparkSession(HttpServletRequest request)
    {
        Response resp = new Response();
        Entity entity = new Entity();

        InterfaceLogBean logBean = reqRespLog.initLogBean(request);

        reqRespLog.requestLog(request, sparkJobForwardLog, logBean);

        SparkJobMetaData resCtx = new SparkJobMetaData();

        String cRet = this.inputParams(request, resCtx);
        if (!StringUtils.equals(cRet, SysRetCode.SUCCESS))
        {
            entity.setCode(cRet);
            entity.setDesc(retCodeDesc.getDesc(cRet));
            resp.setResponse(entity);

            reqRespLog.responseLog(sparkJobForwardLog, logBean, retCodeDesc.getDesc(cRet));

            return resp;
        }

        //申请driver(计算中心)
        String alloRet = sparkJobMgrService.allocResource(request, resCtx);
        if (!StringUtils.equals(alloRet, SysRetCode.SUCCESS))
        {
            entity.setCode(alloRet);
            entity.setDesc(retCodeDesc.getDesc(alloRet));
            resp.setResponse(entity);

            reqRespLog.responseLog(sparkJobForwardLog, logBean, retCodeDesc.getDesc(alloRet));

            return resp;
        }

        //调用计算中心接口初始化sparkcontext
        sparkJobMgrService.initSparkCtx(request, resCtx, entity, logBean);

        resp.setResponse(entity);

        if (StringUtils.equals(entity.getCode(), SysRetCode.SUCCESS))
        {
            sparkSubmitHook.sendAliveMsg(resCtx, true);
            reqRespLog.responseLog(sparkJobForwardLog, logBean, "初始化spark driver成功");
        }
        else
        {
            reqRespLog.responseLog(sparkJobForwardLog, logBean, retCodeDesc.getDesc(entity.getCode()));
        }

        return resp;
    }
    
    @Override
    public Response freeSparkSession(HttpServletRequest request)
    {
        Response resp = new Response();
        Entity entity = new Entity();

        InterfaceLogBean logBean = reqRespLog.initLogBean(request);

        reqRespLog.requestLog(request, sparkJobForwardLog, logBean);

        String appId = request.getParameter("appId");

        if (StringUtils.isEmpty(appId))
        {
            entity.setCode(SysRetCode.ERROR);
            entity.setDesc("spark application id为空");
        }
        else
        {
            try
            {
                SparkJobMetaData resCtx = sparkJobContext.get(appId);
                if (null == resCtx)
                {
                    entity.setCode(SysRetCode.ERROR);
                    entity.setDesc("spark application id为空");
                }
                else
                {

                    SparkDriverMetaData jbp = remoteJar.resCtxToJarBootParam(resCtx);
                    if (null != jbp)
                    {
                        sparkJobMgrService.stopSparkCtx(request, resCtx, logBean);
                        //删除进程
                        dataSheetHandler.deleteProcessById(jbp.getProcessId());

                        remoteJar.restart(jbp);
                    }

                    sparkJobContext.remove(appId);

                }
            }
            catch (Exception e)
            {
                sparkJobContext.remove(appId);

                entity.setCode(SysRetCode.ERROR);
                entity.setDesc("释放spark context资源异常");

                LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
            }
        }

        resp.setResponse(entity);

        reqRespLog.responseLog(sparkJobForwardLog, logBean, JSON.toJSONString(entity));

        return resp;
    }
    
    @Override
    public Response execSelectSqlInSession(HttpServletRequest request)
    {
        Response resp = new Response();
        Entity entity = new Entity();

        InterfaceLogBean logBean = reqRespLog.initLogBean(request);

        reqRespLog.requestLog(request, sparkJobForwardLog, logBean);

        String appId = request.getParameter("appId");

        if (StringUtils.isEmpty(appId))
        {
            entity.setCode(SysRetCode.ERROR);
            entity.setDesc("spark application id为空");
        }
        else
        {
            try
            {
                SparkJobMetaData resCtx = sparkJobContext.get(appId);
                if (null == resCtx)
                {
                    entity.setCode(SysRetCode.ERROR);
                    entity.setDesc("spark application id查询不到资源映射上下文");
                }
                else
                {
                    String url =
                        StringUtils.join("http://",
                            resCtx.getDriverIp(),
                            ":",
                            resCtx.getPort(),
                            "/SparkSQL/doSessionSelectSql.do");

                    Map<String,String> params = request.getParameterMap().entrySet().stream().filter(e -> null != e.getValue() && e.getValue().length > 0).collect(
                        Collectors.toMap(e -> e.getKey(),e->e.getValue()[0]));


                    reqRespLog.requestLog(request, driverInteractivelog, logBean);
                    String result = RestTemplateProvider.postFormForEntity(restTemplate,url,String.class,params);
                    reqRespLog.responseLog(driverInteractivelog,
                        logBean,
                        StringUtils.join("[计算中心请求地址:", url, ",返回结果:", result, "]"));

                    Response response = JSON.parseObject(result, Response.class, Feature.InitStringFieldAsEmpty);
                    Entity entityRst = response.getResponse();

                    if (StringUtils.equals(entityRst.getCode(), SysRetCode.SUCCESS))
                    {
                        entity.setContent(entityRst.getContent());
                    }
                    else
                    {
                        entity.setCode(entityRst.getCode());
                        entity.setDesc("查询异常");
                        entity.setErrorStack(entityRst.getErrorStack());
                    }

                    entity.setAppid(entityRst.getAppid());
                }
            }
            catch (Exception e)
            {
                entity.setCode(SysRetCode.ERROR);
                entity.setDesc("查询sql异常");

                LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
            }
        }

        resp.setResponse(entity);

        reqRespLog.responseLog(sparkJobForwardLog, logBean, JSON.toJSONString(entity));

        return resp;
    }
    
    @Override
    public Response restartProcessAttached(HttpServletRequest request)
    {
        Response response = new Response();

        Entity entity = new Entity();

        String masterIp = request.getParameter("sparkMasterIp");
        if (StringUtils.isEmpty(masterIp))
        {
            entity.setCode(SysRetCode.PARAM_INCOMPLETE);
            entity.setDesc("需要指定重启的driver访问的spark master地址");
            response.setResponse(entity);

            return response;
        }

        String driverType = request.getParameter("driverType");
        if (StringUtils.isNotEmpty(driverType) && !StringUtils.equals(driverType, DriverType.CALC)
            && !StringUtils.equals(driverType, DriverType.ALGO))
        {
            entity.setCode(SysRetCode.PARAM_INCOMPLETE);
            entity.setDesc(StringUtils.join("driver type必须为", DriverType.CALC, "或", DriverType.ALGO));
            response.setResponse(entity);

            return response;
        }

        //获取jar信息
        List<Jar> jars = dataSheetHandler.queryDriverDeployment(masterIp, driverType);
        if (CollectionUtils.isNotEmpty(jars))
        {
            for (Jar driver : jars)
            {
                remoteJar.stopAll(driver);
                dataSheetHandler.deleteProcess(driver);
            }

            for (Jar driver : jars)
            {
                int max = driver.getMaxJvm().intValue();
                for (int i = 0; i < max; i++)
                {

                    String port = globalParam.getDriverPortPool()[i];

                    remoteJar.start(driver, port);

                }
            }
        }

        response.setResponse(entity);
        return response;
    }

    @Override
    public void sparkDriverShutDown(HttpServletRequest request)
    {
        InterfaceLogBean logBean = reqRespLog.initLogBean(request);

        reqRespLog.requestLog(request,driverInteractivelog , logBean);

        String appId = request.getParameter("appId");

        if (StringUtils.isNotEmpty(appId))
        {
            SparkApplicationLog app = dataSheetHandler.getLatestAppById(appId);

            if (null != app && null == app.getEndTime()
                && StringUtils.equals(app.getStatus(), SysRetCode.SPARK_APP_RUNNING))
            {
                Date nowTime = new Date();
                app.setEndTime(new Date());

                long dur = nowTime.getTime() / 1000 - app.getStartTime().getTime() / 1000;

                app.setDuration(dur);
                app.setStatus(SysRetCode.SPARK_APP_FAILED);

                app.setNote(StringUtils.join(app.getNote(), "errorStack=>driver异常停止"));

                dataSheetHandler.updateAppLog(app);

            }

            sparkJobContext.remove(appId);
        }

        reqRespLog.responseLog(driverInteractivelog, logBean, "处理结束");

    }

    /**
     * 申请时spark context时输入参数
     * @param request
     * @param resCtx
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String inputParams(HttpServletRequest request, SparkJobMetaData resCtx)
    {
        //获取CPU核数
        String coresNum = request.getParameter("coresNum");
        //获取内存参数
        String memSize = request.getParameter("executorMemory");

        String ret = sparkJobMgrService.checkResParam(coresNum, memSize);
        if (!StringUtils.equals(ret, SysRetCode.SUCCESS))
        {
            return ret;
        }

        //业务参数,选填
        String batchNo = request.getParameter("batchNo");
        String jobCode = request.getParameter("jobCode");
        String taskCode = request.getParameter("taskCode");

        resCtx.setBatchNo(batchNo);
        resCtx.setJobCode(jobCode);
        resCtx.setTaskCode(taskCode);
        resCtx.setCoreNum(Integer.valueOf(coresNum));
        resCtx.setMemSize(Integer.valueOf(memSize));

        return SysRetCode.SUCCESS;
    }
}
