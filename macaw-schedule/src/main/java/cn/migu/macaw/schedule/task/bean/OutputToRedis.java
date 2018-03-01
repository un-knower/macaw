package cn.migu.macaw.schedule.task.bean;

import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.migu.macaw.common.RequestKey;
import cn.migu.macaw.common.ServiceUrlProvider;
import cn.migu.macaw.schedule.task.util.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.datasource.DataSourceAdapter;
import cn.migu.macaw.schedule.task.datasource.DataSourceFlatAttr;

import com.google.common.collect.Maps;

/**
 * redis component
 * 
 * @author soy
 */
@Component("outputToRedis")
public class OutputToRedis implements ITask
{
    private final String SQL = "sql";

    private final String REDIS_DATA_TYPE = "storgeType";
    
    @Resource
    private DataSourceAdapter dataSourceAdapter;
    
    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private StringTagReplaceUtil stringTagReplaceUtil;
    
    @Resource
    private SparkResourceMgr srm;
    
    @Resource
    private ServiceReqClient client;
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        
        Map<String, String> sysParams = configParamUtil.getJobNodeSysParams(brief);
        if (MapUtils.isEmpty(sysParams))
        {
            throw new IllegalArgumentException("无配置参数");
        }
        
        if (!sysParams.containsKey(SQL) || StringUtils.isEmpty(sysParams.get(SQL).trim()))
        {
            throw new IllegalArgumentException("没有配置数据提取sql");
        }
        
        String outputSql = sysParams.get(SQL).trim();
        
        String redisDataType = sysParams.get(REDIS_DATA_TYPE);
        
        DataSourceFlatAttr dsAttr = dataSourceAdapter.getNodeDataSourceConf(brief.getJobCode(), brief.getNodeId());
        if (null == dsAttr)
        {
            throw new IllegalArgumentException("没有配置redis数据源");
        }
        
        String ip = dsAttr.getRedisHost();
        if (StringUtils.isEmpty(ip))
        {
            throw new IllegalArgumentException("没有配置redis ip地址");
        }
        
        int port = (0 == dsAttr.getPort()) ? 6379 : dsAttr.getPort();
        
        String outputFormatSql = stringTagReplaceUtil.replaceLabelsInNode(outputSql, brief);
        
        srm.sparkResourceAlloc(brief, sysParams);
        
        Map<String, String> params = Maps.newHashMap();
        params.put(RequestKey.SQL, outputFormatSql);
        params.put(RequestKey.HOST, ip);
        params.put(RequestKey.PORT, String.valueOf(port));
        params.put(RequestKey.DB_INDEX, String.valueOf(dsAttr.getDbIndex()));
        if (StringUtils.isNotEmpty(dsAttr.getRedisPasswd()))
        {
            params.put(RequestKey.PASSWORD, dsAttr.getRedisPasswd());
        }
        
        if (StringUtils.isNotEmpty(redisDataType))
        {
            params.put(REDIS_DATA_TYPE, redisDataType);
        }
        
        //其他参数
        Map<String, String> otherParams =
            sysParams.entrySet()
                .stream()
                .filter(e -> !StringUtils.equals(e.getKey(), "core_num")
                    && !StringUtils.equals(e.getKey(), "memory_size") && !StringUtils.equals(e.getKey(), SQL)
                    && !StringUtils.equals(e.getKey(), REDIS_DATA_TYPE))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        if (MapUtils.isNotEmpty(otherParams))
        {
            otherParams.forEach((k, v) -> params.put(StringUtils.trim(k), StringUtils.trim(v)));
        }
        
        //非spark sql数据来源处理
        String dbUrl = dsAttr.getConnectAddr();
        if (StringUtils.isNotEmpty(dbUrl))
        {
            String driverName = dsAttr.getDriverClass();
            if (StringUtils.isEmpty(driverName))
            {
                throw new IllegalArgumentException("数据库driver class不能为空");
            }
            
            if (StringUtils.equals(driverName, "com.chinamobile.cmss.ht.Driver"))
            {
                throw new IllegalArgumentException("不支持 jdbc hugetable driver");
            }
            
            String userName = dsAttr.getUsername();
            String dbPasswd = dsAttr.getPassword();
            
            params.put(RequestKey.DB_URL, dbUrl);
            params.put(RequestKey.DB_DRIVER, driverName);
            params.put(RequestKey.DB_USER, userName);
            params.put(RequestKey.DB_PASSWORD, dbPasswd);
        }

        client.submitSparkTask(ServiceUrlProvider.sparkJobMgrService(RequestServiceUri.SAVE_TO_REDIS_BYSQL), params, brief);
    }
}
