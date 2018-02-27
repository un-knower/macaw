package cn.migu.macaw.schedule.task.bean.crossdata;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.migu.macaw.common.ServiceUrlProvider;
import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.schedule.dao.CrossdataBatchLogMapper;
import cn.migu.macaw.schedule.task.util.RequestServiceUri;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.util.ConfigParamUtil;
import cn.migu.macaw.schedule.task.util.ServiceReqClient;

/**
 * 数据同步task
 * 
 * @author soy
 */
@Component("dataSyncTask")
public class DataSyncTask extends CrossDataLogger implements ITask
{
    @Resource
    private ServiceReqClient client;
    
    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private CrossdataBatchLogMapper crossDataBatchLogDao;

    /**
     * 有效配置参数列表
     */
    Set<String> confParams = Sets.newHashSet("crossDataIp",
        "crossDataPort",
        "whereSql",
        "columnNum",
        "writeType",
        "lastSql",
        "fieldDelimiter",
        "mapNum",
        "dataSource",
        "partition",
        "connectUrl",
        "tableName",
        "databaseName",
        "auth",
        "t_dataSource",
        "t_partition",
        "t_connectUrl",
        "t_tableName",
        "t_databaseName",
        "t_auth",
        "hdfsIp",
        "hdfsPort",
        "hugeTableHA",
        "locationHdfs",
        "locationFtp",
        "fileName",
        "DelimiterFtp",
        "DelimiterHdfs");
        
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        Map<String, String> sysParams = configParamUtil.getJobNodeSysParams(brief);
        
        Map<String, String> runParams = sysParams.entrySet()
            .stream()
            .filter(e -> (confParams.contains(e.getKey()) && StringUtils.isNotEmpty(e.getValue())))
            .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

        Entity entity =
            client.postCrossDataTaskRet(ServiceUrlProvider.crossDataService(RequestServiceUri.COMMON_DB_CROSSDATA),
                runParams,
                brief);

        dataSyncLog(crossDataBatchLogDao,brief,entity);
        
    }
    
}
