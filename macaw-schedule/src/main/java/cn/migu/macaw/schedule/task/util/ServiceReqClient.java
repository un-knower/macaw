package cn.migu.macaw.schedule.task.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import cn.migu.macaw.schedule.PlatformAttr;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import cn.migu.common.redis.StringRedisService;
import cn.migu.macaw.common.RestTemplateProvider;
import cn.migu.macaw.common.ServiceName;
import cn.migu.macaw.common.ServiceUrlProvider;
import cn.migu.macaw.common.SysRetCode;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.common.message.Response;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.datasource.DataSourceAdapter;
import cn.migu.macaw.schedule.task.datasource.DataSourceFlatAttr;
import cn.migu.macaw.schedule.workflow.DataConstants;

/**
 * http client工具类
 *
 * @author soy
 */
@Component("serviceReqClient")
public class ServiceReqClient implements RequestKey,RequestServiceUri
{

    /**
     * 请求重试次数
     */
    private final int REQ_RETRY = 3;

    /**
     * hugetable类型
     */
    public static String DATA_SOURCE_HUGETABLE = "hugetable";

    /**
     * hive类型
     */
    public static String DATA_SOURCE_HIVE = "hive";

    
    /**
     * task trace tool
     */
    @Resource
    private TaskTraceLogUtil taskTraceLogUtil;
    
    /**
     * job[node|task]级缓存
     */
    @Resource
    private JobTasksCache jobTasksCache;
    
    @Resource
    private StringRedisService redisService;
    
    /**
     * 运行时app记录
     */
    @Resource
    private RunningResMgr runningResMgr;
    
    @Resource
    private SparkEventHandler sparkEventHandler;
    
    /**
     * 数据源配置适配器
     */
    @Resource
    private DataSourceAdapter dataSourceAdapter;
    
    @Resource
    private SqlServiceUtil sqlServiceUtil;
    
    @Resource(name = "restTemplateForLoadBalance")
    private RestTemplate restTemplateForLoadBalance;

    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private PlatformAttr platformAttr;
    
    /**
     *
     * @param url
     * @param entity
     * @param brief
     * @throws Exception
     */
    public void postCommonTask(String url, Map<String, String> entity, TaskNodeBrief brief)
        throws Exception
    {
        String response = this.post(url, entity, brief);
        
        Response sqlrObj = JSON.parseObject(response, Response.class, Feature.InitStringFieldAsEmpty);
        
        Entity sr = sqlrObj.getResponse();
        if (!StringUtils.equals(sr.getCode(), SysRetCode.SUCCESS))
        {
            LogUtils.runLogError(response);
            String errMsg = StringUtils.isEmpty(sr.getErrorStack()) ? "调用服务中心组件失败" : sr.getErrorStack();
            throw new RuntimeException(errMsg);
        }
    }
    
    /**
     *
     * @param url
     * @param entity
     * @param brief
     * @return
     * @throws Exception
     */
    public Entity postCommonTaskForEntity(String url, Map<String, String> entity, TaskNodeBrief brief)
        throws Exception
    {
        String response = this.post(url, entity, brief);
        
        Response sqlrObj = JSON.parseObject(response, Response.class, Feature.InitStringFieldAsEmpty);
        
        Entity sr = sqlrObj.getResponse();
        if (!StringUtils.equals(sr.getCode(), SysRetCode.SUCCESS))
        {
            LogUtils.runLogError(response);
            String errMsg = StringUtils.isEmpty(sr.getErrorStack()) ? "调用服务中心组件失败" : sr.getErrorStack();
            throw new RuntimeException(errMsg);
        }
        
        return sr;
        
    }
    
    /**
     *
     * @param url
     * @param entity
     * @param brief
     * @return
     * @throws Exception
     */
    public String postCommonTaskForString(String url, Map<String, String> entity, TaskNodeBrief brief)
        throws Exception
    {
        String response = this.post(url, entity, brief);
        
        Response sqlrObj = JSON.parseObject(response, Response.class, Feature.InitStringFieldAsEmpty);
        
        Entity sr = sqlrObj.getResponse();
        if (!StringUtils.equals(sr.getCode(), SysRetCode.SUCCESS))
        {
            LogUtils.runLogError(response);
            String errMsg = StringUtils.isEmpty(sr.getErrorStack()) ? "调用服务中心组件失败" : sr.getErrorStack();
            throw new RuntimeException(errMsg);
        }
        
        String content = sr.getContent();
        
        return content;
        
    }
    
    /**
     * 数据同步任务
     * @param url
     * @param postParams
     * @param brief
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public Entity postCrossDataTaskRet(String url, Map<String, String> postParams, TaskNodeBrief brief)
        throws Exception
    {
        String crossDataIp = dataSourceAdapter.getCrossdataIp(brief);
        String crossDataPort = dataSourceAdapter.getCrossdataPort(brief);
        
        postParams.put(CROSS_DATA_IP, crossDataIp);
        
        postParams.put(CROSS_DATA_PORT, crossDataPort);
        
        String response = null;
        
        Response sqlrObj = null;
        
        Entity sr = null;
        
        redisService.setForHashKeyCounter(brief.getJobCode(), FieldKey.CROSSDATA_JOB_EXISTED);
        
        for (int i = 0; i < REQ_RETRY; i++)
        {
            response = this.post(url, postParams, brief);
            
            sqlrObj = JSON.parseObject(response, Response.class, Feature.InitStringFieldAsEmpty);
            
            sr = sqlrObj.getResponse();
            if (StringUtils.contains(sr.getErrorStack(), "org.apache.thrift.transport.TTransportException"))
            {
                if (2 == i)
                {
                    LogUtils.runLogError(response);
                }
                else
                {
                    Thread.sleep(30000);
                }
                
                continue;
            }
            else
            {
                if (!StringUtils.equals(sr.getCode(), SysRetCode.SUCCESS))
                {
                    LogUtils.runLogError(response);
                }
                break;
            }
            
        }
        
        redisService.delForHashKeyCounter(brief.getJobCode(), FieldKey.CROSSDATA_JOB_EXISTED);
        
        return sr;
        
    }
    
    /**
     * soa请求组件返回内容转换为指定对象
     *
     * @param content
     * @param clazz
     * @return
     * @see [类、类#方法、类#成员]
     */
    public <T> T toJavaObject(String content, Class<T> clazz)
    {
        Preconditions.checkNotNull(content, "json字符串为空");
        
        Preconditions.checkNotNull(clazz, "转换类型为空");
        
        return JSON.parseObject(content, clazz);
        
        //return JSONObject.toJavaObject(JSONObject.parseObject(content), clazz);
    }
    
    /**
     * soa请求组件返回内容转换为指定对象
     *
     * @param content
     * @param clazz
     * @return
     * @see [类、类#方法、类#成员]
     */
    public <T> List<T> toJavaList(String content, Class<T> clazz)
    {
        Preconditions.checkNotNull(content, "json字符串为空");
        
        Preconditions.checkNotNull(clazz, "转换类型为空");
        
        return JSON.parseArray(content, clazz);
    }
    
    /**
     * spark任务提交接口
     * @param url
     * @param entity
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String submitSparkTask(String url, Map<String, String> entity, TaskNodeBrief brief)
        throws Exception
    {
        
        //目前支持重新尝试3次提交
        for (int i = 0; i < REQ_RETRY; i++)
        {
            try
            {
                return postSparkTask(url, entity, brief);
            }
            catch (Exception e)
            {
                String errStack = ExceptionUtils.getStackTrace(e);
                String appId = "";
                try
                {
                    if (e instanceof RuntimeException && sparkEventHandler.isHandlerEvent(errStack))
                    {
                        
                        Optional<Map.Entry<String, String>> result = entity.entrySet()
                            .stream()
                            .filter(nvp -> StringUtils.equals(nvp.getKey(), APP_ID)
                                && StringUtils.isNotEmpty(nvp.getValue()))
                            .findFirst();
                        if (result.isPresent())
                        {
                            entity.remove(result.get());
                            
                            appId = result.get().getValue();
                            sparkEventHandler.reAllocDriver(errStack, brief, appId);
                            String newAppId =
                                jobTasksCache.get(brief.getJobCode(), "-", DataConstants.SPARK_CONTEXT_APPID);
                            
                            entity.put(APP_ID, newAppId);
                            
                        }
                        
                        //postSparkTask(url, newEntity, brief);
                        
                    }
                    else
                    {
                        throw e;
                    }
                    
                }
                catch (Exception e1)
                {
                    throw e1;
                }
                
            }
        }
        
        throw new RuntimeException("[内部]spark任务重提交次数超限,最大3次");
        
    }
    
    /**
     * spark任务提交请求
     *
     * @param url    请求url地址
     * @param entity http报文中的自定义data信息
     * @author zhaocan
     * @see [类、类#方法、类#成员]
     */
    public String postSparkTask(String url, Map<String, String> entity, TaskNodeBrief brief)
        throws Exception
    {
        //1.提交spark任务执行请求
        String respPhase1 = "";
        String appName = "";
        Entity context = null;
        
        //扩展请求参数
        this.postFormExtend(entity, brief);
        
        for (int i = 0; i < REQ_RETRY; i++)
        {
            respPhase1 = this.post(url, entity, brief);
            /*respPhase1 = StringUtil.getResp(respPhase1);*/
            
            Response response = JSON.parseObject(respPhase1, Response.class, Feature.InitStringFieldAsEmpty);
            
            context = response.getResponse();
            if (StringUtils.isNotEmpty(context.getCode()) && StringUtils.equals(context.getCode(), SysRetCode.SUCCESS))
            {
                
                //记录appid
                runningResMgr.addSparkAppRecord(brief.getJobCode(), context.getAppname());
                
                break;
            }
            
            Thread.sleep(2000);
        }
        
        if (StringUtils.equals(context.getCode(), SysRetCode.SUCCESS))
        {
            //2.等待任务执行结果
            appName = context.getAppname();
            String appId = context.getAppid();
            
            String queryUrl = ServiceUrlProvider.sparkJobMgrService(SPARK_JOB_STATUS_QUERY);
            //sysParamCache.get(brief.getJobCode(), ModuleUrlKey.RESOURCE_URL_KEY) + sparkTaskQuery;
            String respPhase2 = this.post(queryUrl, this.sparkAppEntity(entity, appName, appId, brief, true), brief);
            
            /*respPhase2 = StringUtil.getResp(respPhase2);*/
            
            Response qobj = JSON.parseObject(respPhase2, Response.class, Feature.InitStringFieldAsEmpty);
            Entity qr = qobj.getResponse();
            
            if (StringUtils.equals(qr.getCode(), SysRetCode.SPARK_APP_FINISHED))
            {
                runningResMgr.delSparkAppRecord(brief.getJobCode(), appName);
                return appName;
            }
            
            if (StringUtils.equals(qr.getCode(), SysRetCode.SPARK_APP_KILLED)
                || StringUtils.equals(qr.getCode(), SysRetCode.SPARK_APP_FAILED))
            {
                runningResMgr.delSparkAppRecord(brief.getJobCode(), appName);
                
                String errMsg =
                    StringUtils.isEmpty(qr.getErrorStack()) ? "spark任务执行失败,具体信息请查询后台日志" : qr.getErrorStack();
                throw new RuntimeException(errMsg);
            }
            if (StringUtils.isEmpty(qr.getCode()) || StringUtils.equals(qr.getCode(), SysRetCode.SPARK_APP_RUNNING))
            {
                String retryNumStr =
                    jobTasksCache.get(brief.getJobCode(), brief.getNodeId(), DataConstants.SPARK_QUERY_RETRY_TIME);
                //默认查询时长3小时
                int retryNum = StringUtils.isEmpty(retryNumStr) ? 1080 : Integer.parseInt(retryNumStr);
                int waitTime = 10000;
                
                for (int i = 0; i < retryNum; i++)
                {
                    String respPhaseLoop =
                        this.post(queryUrl, this.sparkAppEntity(entity, appName, appId, brief, true), brief);
                    
                    /*respPhaseLoop = StringUtil.getResp(respPhaseLoop);*/
                    LogUtils.runLogError(respPhaseLoop);
                    Response qlobj = JSON.parseObject(respPhaseLoop, Response.class, Feature.InitStringFieldAsEmpty);
                    Entity qrl = qlobj.getResponse();
                    
                    if (StringUtils.equals(qrl.getCode(), SysRetCode.SPARK_APP_FINISHED))
                    {
                        runningResMgr.delSparkAppRecord(brief.getJobCode(), appName);
                        return appName;
                    }
                    if (StringUtils.isNotEmpty(qrl.getCode()) && !StringUtils.equals(qrl.getCode(), SysRetCode.SPARK_APP_RUNNING))
                    {
                        runningResMgr.delSparkAppRecord(brief.getJobCode(), appName);
                        
                        String errMsg =
                            StringUtils.isEmpty(qrl.getErrorStack()) ? "spark任务执行失败,具体信息请查询后台日志" : qrl.getErrorStack();
                        
                        throw new RuntimeException(errMsg);
                    }
                    
                    Thread.sleep(waitTime);
                }
                
                /*this.stopSparkTask(appName, appId, brief);*/
                
                throw new RuntimeException("请求执行spark任务超时,目前最长查询时长为:" + (retryNum * waitTime) / 1000 + "s");
                
            }
            
        }
        else
        {
            throw new RuntimeException(respPhase1);
        }
        return appName;
        
    }
    
    /**
     * 客户端发送请求
     *
     * @param url 发送地址
     * @return String json字符串
     * @author zhaocan
     * @see [类、类#方法、类#成员]
     */
    public String post(String url, Map<String, String> entity, TaskNodeBrief brief)
        throws Exception
    {
        //记录post log信息
        taskTraceLogUtil.reqPostLog(url, entity, brief);

        RestTemplate tRestTemplate = restTemplateForLoadBalance;
        if(StringUtils.contains(StringUtils.substringAfter(url,"http://"),":"))
        {
            tRestTemplate = restTemplate;
        }

        String result = RestTemplateProvider.postFormForEntity(tRestTemplate, url, String.class, entity);
        
        taskTraceLogUtil.resqPostLog(url, brief, result);
        
        return result;
        
    }
    
    /**
     * 创建sql http发送entity
     *
     * @param sql sql语句
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> sqlHiveEntity(String sql)
    {
        Map<String, String> entity = Maps.newHashMap();

        entity.put(DATA_SOURCE, DATA_SOURCE_HIVE);
        entity.put(SQL, sql);
        
        return entity;
    }
    
    /**
     * spark sql请求参数
     * @param sql sql语句
     * @param appId
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> sqlHiveEntity(String sql, String appId)
    {
        Map<String, String> entity = Maps.newHashMap();

        entity.put(DATA_SOURCE, DATA_SOURCE_HIVE);
        entity.put(SQL, sql);
        entity.put(APP_ID, appId);
        
        return entity;
    }
    
    /**
     * spark driver初始化参数
     * @param brief 元数据信息
     * @param cores
     * @param memory
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> sparkCtxInitEntity(TaskNodeBrief brief, String procCode, String cores, String memory)
    {
        Map<String, String> entity = Maps.newHashMap();
        
        entity.put(CORES_NUM, cores);
        
        entity.put(EXECUTOR_MEMORY, memory);
        
        entity.put(JOB_CODE, procCode);
        
        entity.put(DATA_SOURCE, DATA_SOURCE_HIVE);
        
        this.getClusterMaster(entity, brief);
        
        return entity;
    }
    
    /**
     * spark driver释放请求参数
     * @param appId
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> sparkCtxFreeEntity(String appId)
    {
        Map<String, String> entity = Maps.newHashMap();
        entity.put(APP_ID, appId);
        
        return entity;
    }
    
    /**
     * sql list请求 entity
     *
     * @param sqls
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> sqlListEntity(List<String> sqls)
    {
        
        Map<String, String> entity = Maps.newHashMap();
        
        String jsonString = JSONArray.toJSONString(sqls).replaceAll("\n", " ").replace("\\n", " ");

        entity.put(DATA_SOURCE, DATA_SOURCE_HIVE);
        entity.put(SQL_LIST, jsonString);
        
        return entity;
    }
    
    /**
     * 创建spark任务查询请求数据
     *
     * @param name application名字
     * @param id   application id
     * @param brief  task执行上下文
     * @param flag true-带spark_sub_soa检测参数
     * @return
     * @author zhaocan
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> sparkAppEntity(Map<String, String> entity, String name, String id, TaskNodeBrief brief,
        boolean flag)
    {
        entity.put(APP_NAME, name);
        entity.put(APP_ID, id);
        
        return entity;
    }
    
    /**
     * 查询方法-带返回值
     * <功能详细描述>
     *
     * @param params
     * @param brief
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public String excuteQuery(Map<String, String> params, TaskNodeBrief brief)
        throws Exception
    {
        String realUrl = StringUtils.join(platformAttr.getBasePlatformUrl(), JDBC_EXECUTE_QUERY);
            //StringUtils.join("http://", ServiceName.DATA_SYN_AND_HT, "/", JDBC_EXECUTE_QUERY);
        //sysParamCache.get(brief.getJobCode(), ModuleUrlKey.DATA_EXTRAC_URL_KEY) + JDBC_EXECUTE_QUERY;
        String response = post(realUrl, params, brief);
        
        Response sqlrObj = JSON.parseObject(response, Response.class, Feature.InitStringFieldAsEmpty);
        
        Entity sr = sqlrObj.getResponse();
        if (!StringUtils.equals(sr.getCode(), SysRetCode.SUCCESS))
        {
            LogUtils.runLogError(response);
            throw new RuntimeException("查询hugetable错误");
        }
        return response;
    }
    
    /**
     * 1.当查询数量大于20时，应使用分页的方式调用
     * 2.colNames要和sql语句中的列名一一对应
     *
     * @param sql 查询sql
     * @param brief tasks执行上下文
     * @param dsAttr 数据源属性
     * @return
     * @throws Exception
     * @author zhaocan
     * @see [类、类#方法、类#成员]
     */
    public List<Object[]> executeJdbcQuery(String sql, TaskNodeBrief brief, DataSourceFlatAttr dsAttr,
        String... colNames)
        throws Exception
    {
        List<Object[]> rst = new ArrayList<Object[]>();
        Map<String, String> entity = Maps.newHashMap();
        entity.put(DATA_SOURCE, DATA_SOURCE_HUGETABLE);
        entity.put(SQL, sql);
        
        if (sqlServiceUtil.isHtConnection(dsAttr))
        {
            entity.put(DATA_BASE_NAME, dsAttr.getConnectAddr());
            entity.put(USER_NAME, dsAttr.getUsername());
            entity.put(PASSWORD, dsAttr.getPassword());
        }
        
        JSONArray jsonArray = null;
        
        for (int i = 0; i < REQ_RETRY; i++)
        {
            String qryUrl = StringUtils.join(platformAttr.getBasePlatformUrl(), JDBC_EXECUTE_QUERY);
                //StringUtils.join("http://", ServiceName.DATA_SYN_AND_HT, "/", JDBC_EXECUTE_QUERY);
            String result = this.post(qryUrl, entity, brief);
            
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject res = (JSONObject)jsonObject.get("response");
            jsonArray = (JSONArray)JSONArray.parse((String)res.get("content"));
            //
            
            String errorStack = null;
            if (null == jsonArray)
            {
                errorStack = (String)res.get("errorStack");
                if (StringUtils.isNotEmpty(errorStack))
                {
                    if (StringUtils.contains(errorStack,
                        "com.chinamobile.cmss.ht.jdbc.JdbcSQLException: Connection is broken"))
                    {
                        if (i == 2)
                        {
                            throw new SQLException(errorStack);
                        }
                        Thread.sleep(30000);
                        continue;
                    }
                    else
                    {
                        throw new SQLException(errorStack);
                    }
                    
                }
            }
            
            break;
        }
        
        int jsonLength = (null == jsonArray) ? 0 : jsonArray.size();
        if (jsonLength != 0)
        {
            for (int i = 0; i < jsonLength; i++)
            {
                JSONObject jo = (JSONObject)jsonArray.get(i);
                Object[] objs = new Object[colNames.length];
                for (int j = 0; j < colNames.length; j++)
                {
                    Object value = jo.get(colNames[j]);
                    if (null == value)
                    {
                        value = jo.get(StringUtils.lowerCase(colNames[j]));
                    }
                    objs[j] = value;
                }
                rst.add(objs);
            }
        }
        
        return rst;
    }
    
    /**
     * 提交spark任务请求时附加参数
     *
     * @param entity
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    private void postFormExtend(Map<String, String> entity, TaskNodeBrief brief)
    {
        
        entity.put(BATCH_NO, brief.getBatchCode());
        entity.put(JOB_CODE, brief.getJobCode());
        entity.put(TASK_CODE, brief.getNodeId());
        
        String coreNumStr = jobTasksCache.get(brief.getJobCode(), brief.getNodeId(), DataConstants.CORE_NUM);
        int coreNum = StringUtils.isEmpty(coreNumStr) ? 0 : Integer.valueOf(coreNumStr);
        if (0 != coreNum)
        {
            entity.put(CORES_NUM, String.valueOf(coreNum));
        }
        else
        {
            entity.put(CORES_NUM, String.valueOf(SparkResourceMgr.DEFAULT_CORE_NUM));
        }
        
        String memSizeStr = jobTasksCache.get(brief.getJobCode(), brief.getNodeId(), DataConstants.MEM_SIZE);
        int memSize = StringUtils.isEmpty(memSizeStr) ? 0 : Integer.valueOf(memSizeStr);
        if (0 != memSize)
        {
            entity.put(EXECUTOR_MEMORY, String.valueOf(memSize));
        }
        else
        {
            entity.put(EXECUTOR_MEMORY, String.valueOf(SparkResourceMgr.DEFAULT_MEM_SIZE));
        }
        
        this.getClusterMaster(entity, brief);
    }
    
    /**
     * hdfs/spark集群master参数
     *
     * @param entity 参数列表
     * @param brief  执行上下文
     * @return
     * @see [类、类#方法、类#成员]
     */
    private void getClusterMaster(Map<String, String> entity, TaskNodeBrief brief)
    {
        
        DataSourceFlatAttr dsAttr = dataSourceAdapter.getNodeDataSourceConf(brief.getJobCode(), brief.getNodeId());
        
        entity.put(SPARK_PATH, dataSourceAdapter.getSparkMasterUrl(brief, dsAttr));
        entity.put(HDFS_PATH, dataSourceAdapter.getHdfsUrl(brief, dsAttr));
    }
    
    /**
     * 停止提交的spark任务
     *
     * @param name
     * @param id
     * @param brief
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public void stopSparkTask(String name, String id, TaskNodeBrief brief)
        throws Exception
    {
        String stopUrl = ServiceUrlProvider.sparkJobMgrService(SPARK_STOP_APP);
        String stopRet = this.post(stopUrl, this.sparkAppEntity(Maps.newHashMap(), name, id, brief, false), brief);
        
        Response resp = JSON.parseObject(stopRet, Response.class, Feature.InitStringFieldAsEmpty);
        
        if (!StringUtils.equals(resp.getResponse().getCode(), SysRetCode.SUCCESS))
        {
            String msg = StringUtils.join("停止spark任务:", name, "失败[", brief.getBatchCode(), "]");
            LogUtils.runLogError(msg);
        }
    }
    

    
}
