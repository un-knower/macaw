/*
 * 文 件 名:  DataSyncHdfsToHdfsTask.java
 * 版    权:  Copyright 2015 咪咕互动娱乐有限公司,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  qiancuicui
 * 创建时间:  2016年11月24日
 
 */
package cn.migu.macaw.schedule.task.bean.crossdata;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.migu.macaw.common.ServiceUrlProvider;
import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.schedule.dao.CrossdataBatchLogMapper;
import cn.migu.macaw.schedule.task.datasource.DataSourceAdapter;
import cn.migu.macaw.schedule.task.datasource.DataSourceFlatAttr;
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
 *
 * @author zhaocan
 */
@Component("dataSyncHdfsToHdfsTask")
public class DataSyncHdfsToHdfsTask extends CrossDataLogger implements ITask
{
    @Resource
    private ServiceReqClient client;
    
    @Resource
    private ConfigParamUtil configParamUtil;

    @Resource
    private CrossdataBatchLogMapper crossDataBatchLogDao;

    @Resource
    private DataSourceAdapter dataSourceMgr;

    Set<String> confParams =
        Sets.newHashSet("hdfsSourceDirLocation", "hdfsTargetDirLocation", "transferType", "tableName", "t_tableName");
        
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
     * @param runParams
     * @param brief
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    private void dataSourceTranslate(Map<String, String> runParams, TaskNodeBrief brief)
        throws Exception
    {
        DataSourceFlatAttr dsAttr = dataSourceMgr.getNodeDataSourceConf(brief.getJobCode(), brief.getNodeId());
        
        String sDsType = dsAttr.getDsType();
        String tDsType = dsAttr.getTargetDsType();
        
        runParams.put("dataSource", sDsType);
        runParams.put("t_dataSource", tDsType);
        
        runParams.put("hdfsSource",
            StringUtils.join(dsAttr.getHdfsPrefixSchema(), runParams.get("hdfsSourceDirLocation")));
        runParams.remove("hdfsSourceDirLocation");
        
        runParams.put("hdfsTarget",
            StringUtils.join(dsAttr.getTargetHdfsPrefixSchema(), runParams.get("hdfsTargetDirLocation")));
        runParams.remove("hdfsTargetDirLocation");
        
        //runParams.put("transferType", "hdfs2hdfs");
    }
}
