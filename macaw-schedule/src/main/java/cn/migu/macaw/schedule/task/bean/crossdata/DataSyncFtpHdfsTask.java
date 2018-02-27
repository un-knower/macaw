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
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.util.ConfigParamUtil;
import cn.migu.macaw.schedule.task.util.ServiceReqClient;

/**
 * ftp下载文件至hdfs文件系统
 * 
 * @author soy
 */
@Component("dataSyncFtpHdfsTask")
public class DataSyncFtpHdfsTask extends CrossDataLogger implements ITask
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
        Sets.newHashSet("DelimiterFtp", "DelimiterHdfs", "fileName", "ftpDirLocation", "hdfsFileLocation");
        
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
        
        if (StringUtils.equals(dsAttr.getDsType(), "ftp") && StringUtils.equals(dsAttr.getTargetDsType(), "hugetable"))
        {
            runParams.put("dataSource", dsAttr.getDsType());
            runParams.put("t_dataSource", dsAttr.getTargetDsType());
            
            runParams.put("locationFtp",
                StringUtils.join("sftp://",
                    dsAttr.getUsername(),
                    ":",
                    dsAttr.getPassword(),
                    "@",
                    dsAttr.getConnectAddr(),
                    runParams.get("ftpDirLocation")));
            runParams.remove("ftpDirLocation");
            
            runParams.put("locationHdfs",
                StringUtils.join(dsAttr.getTargetHdfsPrefixSchema(), runParams.get("hdfsFileLocation")));
                
            runParams.remove("hdfsFileLocation");
        }
        
        if (StringUtils.equals(dsAttr.getDsType(), "hugetable") && StringUtils.equals(dsAttr.getTargetDsType(), "ftp"))
        {
            runParams.put("dataSource", dsAttr.getDsType());
            runParams.put("t_dataSource", dsAttr.getTargetDsType());
            
            runParams.put("locationHdfs",
                StringUtils.join(dsAttr.getHdfsPrefixSchema(), runParams.get("hdfsFileLocation")));
                
            runParams.remove("hdfsFileLocation");
            
            runParams.put("locationFtp",
                StringUtils.join("sftp://",
                    dsAttr.getTargetUsername(),
                    ":",
                    dsAttr.getTargetPassword(),
                    "@",
                    dsAttr.getTargetConnectAddr(),
                    runParams.get("ftpDirLocation")));
            runParams.remove("ftpDirLocation");
            
        }
        
    }
    
}
