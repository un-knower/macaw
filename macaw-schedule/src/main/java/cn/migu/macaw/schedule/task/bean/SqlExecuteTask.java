package cn.migu.macaw.schedule.task.bean;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import cn.migu.macaw.common.ServiceUrlProvider;
import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.datasource.DataSourceAdapter;
import cn.migu.macaw.schedule.task.datasource.DataSourceFlatAttr;
import cn.migu.macaw.schedule.task.util.*;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import cn.migu.macaw.schedule.workflow.DataConstants;

/**
 * sql执行任务--无返回值
 * 执行sql，除非异常跳出，否则无论执行是否失败都会执行下个节点
 * 
 * @author
 */
@Component("sqlExecuteTask")
public class SqlExecuteTask implements ITask
{
    /**
     * 服务请求对象
     */
    @Resource
    private ServiceReqClient serviceClient;
    
    @Resource
    private StringTagReplaceUtil convertSqlUtil;
    
    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private SparkResourceMgr srm;
    
    @Resource
    private SqlServiceUtil sqlService;
    
    @Resource
    private JobTasksCache jobTasksCache;
    
    @Resource
    private DataSourceAdapter dataSourceAdapter;
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        //获取sql 1 从节点中获取
        List<String> nodeRunSqls = configParamUtil.getJobNodeRunSqlParams(brief);
        
        if (CollectionUtils.isEmpty(nodeRunSqls))
        {
            ScheduleLogTrace.scheduleWarnLog(brief, "没有配置运行sql");
            
            return;
        }
        
        if (nodeRunSqls.size() > 1)
        {
            ScheduleLogTrace.scheduleWarnLog(brief, "运行sql数量大于1");
            
            throw new IllegalArgumentException("运行sql数量大于1");
        }
        
        String nodeRunSql = nodeRunSqls.get(0);
        
        nodeRunSql = convertSqlUtil.replaceLabelsInNode(nodeRunSql.replace("\n", " "), brief);
        
        //替换环境上下文中的sql标签
        nodeRunSql = convertSqlUtil.replaceLabelsInCtx(brief.getJobCode(), nodeRunSql);
        
        jobTasksCache.append(brief.getJobCode(),
            brief.getNodeId(),
            DataConstants.NODE_RUNNING_TRACE,
            StringUtils.join("运行sql:", nodeRunSql));
        
        //node运行时系统配置参数
        Map<String, String> nodeSysConf = configParamUtil.getJobNodeSysParams(brief);
        
        DataSourceFlatAttr dsAttr = dataSourceAdapter.getNodeDataSourceConf(brief.getJobCode(), brief.getNodeId());
        
        if (!sqlService.isSparkSql(dsAttr))
        {
            
            if (sqlService.isHtConnection(dsAttr))
            {
                sqlService.executeForHt(brief, dsAttr, nodeRunSql);
            }
            else
            {
                sqlService.executeRemote(dsAttr, nodeRunSql, brief);
            }
        }
        else
        {
            srm.sparkResourceAlloc(brief, nodeSysConf);
            
            String coreNum = jobTasksCache.get(brief.getJobCode(), brief.getNodeId(), DataConstants.CORE_NUM);
            
            String memSize = jobTasksCache.get(brief.getJobCode(), brief.getNodeId(), DataConstants.MEM_SIZE);
            
            jobTasksCache.append(brief.getJobCode(),
                brief.getNodeId(),
                DataConstants.NODE_RUNNING_TRACE,
                StringUtils.join("核数=", coreNum, ",单节点内存=", memSize, "M"));
            
            String appId = srm.getSparkContextAppIdJobScope(brief.getJobCode());
            if (StringUtils.isNotEmpty(appId))
            {
                boolean isUnUsed = srm.getSparkCtxUsedJobScope(brief.getJobCode());
                if (isUnUsed)
                {
                    serviceClient.submitSparkTask(
                        ServiceUrlProvider.sparkJobMgrService(ServiceReqClient.SPARK_SQL_EXECUTE_CTX),
                        serviceClient.sqlHiveEntity(nodeRunSql, appId),
                        brief);
                    
                    srm.setSparkCtxUnusedJobScope(brief.getJobCode());
                }
                else
                {
                    serviceClient.submitSparkTask(
                        ServiceUrlProvider.sparkJobMgrService(ServiceReqClient.SPARK_SQL_EXECUTE),
                        serviceClient.sqlHiveEntity(nodeRunSql),
                        brief);
                }
            }
            else
            {
                serviceClient.submitSparkTask(ServiceUrlProvider.sparkJobMgrService(ServiceReqClient.SPARK_SQL_EXECUTE),
                    serviceClient.sqlHiveEntity(nodeRunSql),
                    brief);
            }
            
        }
        
    }
}
