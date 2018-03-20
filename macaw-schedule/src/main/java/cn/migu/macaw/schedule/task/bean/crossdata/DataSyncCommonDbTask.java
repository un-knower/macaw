package cn.migu.macaw.schedule.task.bean.crossdata;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.migu.macaw.common.ServiceUrlProvider;
import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.schedule.task.util.RequestServiceUri;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import cn.migu.macaw.schedule.dao.CrossdataBatchLogMapper;
import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.datasource.DataSourceAdapter;
import cn.migu.macaw.schedule.task.datasource.DataSourceFlatAttr;
import cn.migu.macaw.schedule.task.util.ConfigParamUtil;
import cn.migu.macaw.schedule.task.util.ServiceReqClient;

/**
 * 数据库表数据同步Task
 * 
 * @author soy
 */
@Component("dataSyncCommonDbTask")
public class DataSyncCommonDbTask extends CrossDataLogger implements ITask
{
    @Resource
    private ServiceReqClient client;
    
    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private CrossdataBatchLogMapper crossDataBatchLogDao;
    
    @Resource
    private DataSourceAdapter dataSourceMgr;

    private Set<String> confParams = Sets.newHashSet("whereSql",
        "columnNum",
        "writeType",
        "lastSql",
        "mapNum",
        "partition",
        "tableName",
        "t_tableName",
        "t_partition",
        "recordPerStatement",
        "databaseName",
        "t_databaseName");
        
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        Map<String, String> sysParams = configParamUtil.getJobNodeSysParams(brief);
        
        Map<String, String> runParams = sysParams.entrySet()
            .stream()
            .filter(e -> (confParams.contains(e.getKey()) && StringUtils.isNotEmpty(e.getValue())))
            .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
            
        this.dataSourceTranslate(runParams, brief);

        Entity entity =
            client.postCrossDataTaskRet(ServiceUrlProvider.crossDataService(RequestServiceUri.COMMON_DB_CROSSDATA),
                runParams,
                brief);

        dataSyncLog(crossDataBatchLogDao,brief,entity);
        
    }
    
    /**
     * 数据源信息
     * @param runParams 运行参数
     * @param brief 任务简明信息
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    private void dataSourceTranslate(Map<String, String> runParams, TaskNodeBrief brief)
        throws Exception
    {
        DataSourceFlatAttr dsAttr = dataSourceMgr.getNodeDataSourceConf(brief.getJobCode(), brief.getNodeId());
        
        //源表信息
        String sDsType = dsAttr.getDsType();
        String sConnUrl = dsAttr.getConnectAddr();
        String sUser = dsAttr.getUsername();
        String sPwd = dsAttr.getPassword();
        
        String tDsType = dsAttr.getTargetDsType();
        String tConnUrl = dsAttr.getTargetConnectAddr();
        String tUser = dsAttr.getTargetUsername();
        String tPwd = dsAttr.getTargetPassword();
        
        Preconditions.checkArgument(StringUtils.isNotEmpty(sDsType), "源数据源类型为空");
        
        Preconditions.checkArgument(StringUtils.isNotEmpty(sConnUrl), "源数据连接地址为空");
        
        Preconditions.checkArgument(StringUtils.isNotEmpty(tDsType), "目标数据源类型为空");
        
        Preconditions.checkArgument(StringUtils.isNotEmpty(tConnUrl), "目标数据连接地址为空");
        
        runParams.put("dataSource", sDsType);
        runParams.put("t_dataSource", tDsType);
        
        runParams.put("connectUrl", sConnUrl);
        runParams.put("t_connectUrl", tConnUrl);
        
        runParams.put("auth", StringUtils.join(sUser, "/", sPwd));
        runParams.put("t_auth", StringUtils.join(tUser, "/", tPwd));
        
    }
    
}
