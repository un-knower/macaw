package cn.migu.macaw.schedule.task.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;

import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.schedule.task.TaskNodeBrief;

/**
 * Task跟踪日志类
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年3月2日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Component("taskTraceLogUtil")
public class TaskTraceLogUtil
{
    @Resource
    private ServiceReqClient httpClientUtil;

    /**
     * 发出请求描述,对以下请求记录日志
     */
    private String[][] reqDesc =
        {{ServiceReqClient.SPARK_SQL_EXECUTE, "单SQL执行"}, {ServiceReqClient.SPARK_SQL_EXECUTE_CTX, "单SQL执行_IN-THE-CONTEXT"},
            {ServiceReqClient.JDBC_EXECUTE_QUERY, "JDBC查询HT"}, {ServiceReqClient.LOADL_FILE_TO_DATABASE, "加载文件至数据库"},
            {ServiceReqClient.EXECUTE_SQL_DATAFRAME, "SQL/DataFrame操作"}, {ServiceReqClient.SPARK_SELECT_TO_FILE, "SQL生成文件"},
            {ServiceReqClient.SPARK_SQLLIST_PATH, "多SQL执行"}, {ServiceReqClient.SPARK_JSON_TO_DB, "json数据->Data WareHouse"},
            {ServiceReqClient.SINGLE_DATASOURCE_TABLE, "crossdata DB->HUGETABLE"}, {ServiceReqClient.ALGO_TRAIN, "算法中心数据训练"},
            {ServiceReqClient.ALGO_USE, "算法中心数据使用"}, {ServiceReqClient.SPARK_SELECT_TO_TEXT, "SQL生成单个文本文件"},
            {ServiceReqClient.COMMON_DB_CROSSDATA, "数据同步"}, {ServiceReqClient.SPARK_DRIVER_INIT, "Spark Driver申请"},
            {ServiceReqClient.SPARK_DRIVER_FREE, "Spark Driver释放"}, {ServiceReqClient.SPARK_STOP_APP, "停止Spark Application"},
            {ServiceReqClient.SPARK_SELECT_QUERY, "Spark SQL查询返回"},
            {ServiceReqClient.SAVE_TO_REDIS_BYSQL, "根据spark sql保存数据到redis"},
            {ServiceReqClient.DB_CROSSDATA_KILL, "Kill CrossData Job(By JobCode)"}};
    
    /**
     * 请求日志
     * @param url
     * @param params
     * @param jobCode
     * @param batchNo
     * @see [类、类#方法、类#成员]
     */
    public void reqPostLog(String url, Map<String, String> params, String jobCode, String batchNo)
    {
        //获取url描述
        String lastName = FilenameUtils.getName(url);
        String desc = this.getReqDesc(lastName);
        
        if (null == desc)
        {
            return;
        }
        
        String postFormParam = null;
        if (MapUtils.isNotEmpty(params))
        {
            Map<String, String> nMapParams =
                params.entrySet().stream().filter(e -> StringUtils.isNotEmpty(e.getValue())).collect(
                    Collectors.toMap(p -> (String)(String)p.getKey(), p -> (String)p.getValue()));
            if (MapUtils.isNotEmpty(nMapParams))
            {
                postFormParam = Joiner.on(",").withKeyValueSeparator("=").join(nMapParams);
            }
        }
        
        StringBuffer reqPathStr = new StringBuffer("服务请求");
        reqPathStr.append("[").append(desc).append("]");
        reqPathStr.append("[").append(batchNo).append("]");
        reqPathStr.append(" ").append("请求路径:");
        reqPathStr.append(url);
        
        StringBuffer reqParamStr = new StringBuffer(desc);
        reqParamStr.append(" 请求参数:");
        if (null != postFormParam)
        {
            reqParamStr.append(postFormParam);
        }

        ScheduleLogTrace.scheduleInfoLog(StringUtils.join(reqPathStr.toString(), reqParamStr.toString()));
        
    }
    
    /**
     * post请求 task trace
     * @param url 请求地址
     * @param entity 请求参数
     * @param brief task执行上下文
     * @see [类、类#方法、类#成员]
     */
    public void reqPostLog(String url, Map<String,String> entity, TaskNodeBrief brief)
    {
        //获取url描述
        String lastName = FilenameUtils.getName(url);
        String desc = this.getReqDesc(lastName);
        
        if (null == desc)
        {
            return;
        }
        
        //获取参数信息
        String postFormParam = StringEscapeUtils.unescapeJava(this.getPostForm(entity));
        if (null == postFormParam)
        {
            return;
        }
        
        StringBuffer reqPathStr = new StringBuffer("服务请求");
        reqPathStr.append("[").append(desc).append("]");
        reqPathStr.append("[").append(brief.getBatchCode()).append("]");
        reqPathStr.append(" ").append("请求路径:");
        reqPathStr.append(url);
        
        StringBuffer reqParamStr = new StringBuffer(desc);
        reqParamStr.append(" 请求参数:");
        reqParamStr.append(postFormParam);

        ScheduleLogTrace.scheduleInfoLog(StringUtils.join(reqPathStr.toString(), reqParamStr.toString()));
        
    }
    
    /**
     * post响应 task trace
     * @param url 请求地址
     * @param brief task执行上下文
     * @param resp 响应字符串
     * @see [类、类#方法、类#成员]
     */
    public void resqPostLog(String url, TaskNodeBrief brief, String resp)
    {
        String lastName = FilenameUtils.getName(url);
        
        //获取url描述
        String desc = this.getReqDesc(lastName);
        
        if (null == desc)
        {
            return;
        }
        
        StringBuffer respPathStr = new StringBuffer("服务响应");
        respPathStr.append("[").append(desc).append("]");
        respPathStr.append("[").append(brief.getBatchCode()).append("]");
        respPathStr.append(" ").append("请求路径:");
        respPathStr.append(url);
        
        StringBuffer respStr = new StringBuffer("内容:");
        if (StringUtils.isNotEmpty(resp))
        {
            respStr.append("[").append(resp).append("]");
        }

        ScheduleLogTrace.scheduleInfoLog(StringUtils.join(respPathStr.toString(), respStr.toString()));

    }
    
    /**
     * 
     * 响应
     * @param url
     * @param resp
     * @param batchNo
     * @see [类、类#方法、类#成员]
     */
    public void resqPostLog(String url, String resp, String batchNo)
    {
        String lastName = FilenameUtils.getName(url);
        
        //获取url描述
        String desc = this.getReqDesc(lastName);
        
        if (null == desc)
        {
            return;
        }
        
        StringBuffer respPathStr = new StringBuffer("服务响应");
        respPathStr.append("[").append(desc).append("]");
        respPathStr.append("[").append(batchNo).append("]");
        respPathStr.append(" ").append("请求路径:");
        respPathStr.append(url);
        
        StringBuffer respStr = new StringBuffer("内容:");
        if (StringUtils.isNotEmpty(resp))
        {
            respStr.append("[").append(resp).append("]");
        }

        ScheduleLogTrace.scheduleInfoLog(StringUtils.join(respPathStr.toString(), respStr.toString()));
        
    }

    
    /**
     * 获取post参数列表
     * @param entity
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getPostForm(Map<String,String> entity)
    {
        return JSON.toJSONString(entity);
    }
    
    /**
     * 获取请求描述信息
     * @param url
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getReqDesc(String url)
    {
        for (String[] req : this.reqDesc)
        {
            if (StringUtils.contains(req[0], url))
            {
                return req[1];
            }
        }
        return null;
    }
}
