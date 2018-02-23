package cn.migu.macaw.hadoop.service.impl;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.hugetable.crossdata.config.Column;
import com.hugetable.crossdata.config.FileInfo;
import com.hugetable.crossdata.config.JobConfig;

import cn.migu.macaw.common.ReturnCode;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.hadoop.PlatformAttribute;
import cn.migu.macaw.hadoop.common.DataSourceType;
import cn.migu.macaw.hadoop.common.DataSynMode;
import cn.migu.macaw.hadoop.common.DbTool;
import cn.migu.macaw.hadoop.model.CrossDataParam;

/**
 * 数据同步服务实现
 *
 * @author soy
 */
@Service
public class DataSyncServiceImpl
{
    @Autowired
    private DbTool dbTool;
    
    @Autowired
    private PlatformAttribute platformAttri;
    
    public ReturnCode dataSync(HttpServletRequest request)
    {
        CrossDataParam params = parseDataSyncParam(request);
        if (null == params)
        {
            return ReturnCode.DATA_SYNC_PARAM_PARSE_ERROR;
        }
        
        //特殊同步功能,暂不支持
        if (StringUtils.isNotBlank(params.getTransferType()))
        {
            return ReturnCode.DATA_SYNC_FUNC_NOT_SUPPORT;
        }
        
        //源数据类型
        DataSourceType sData = params.getDataSource();
        //目标数据类型
        DataSourceType tData = params.getT_dataSource();
        
        JobConfig config = null;
        
        switch (sData)
        {
            case HUGETABLE:
                switch (tData)
                {
                    case ORACLE:
                    case MYSQL:
                        genJobConfigForHiveToDb(params);
                        break;
                    case FTP:
                        break;
                    default:
                        break;
                }
                break;
            case FTP:
                switch (tData)
                {
                    case HUGETABLE:
                        break;
                    default:
                        break;
                }
                break;
            case ORACLE:
            case MYSQL:
            case GREENPLUM:
                if (DataSourceType.HUGETABLE == tData)
                {
                    
                }
                break;
            default:
                if (StringUtils.isBlank(params.getTableName()))
                {
                    
                }
                break;
        }

        return ReturnCode.SUCCESS;
        
    }

    /**
     * 关系数据库数据同步到HIVE配置
     * @param params 请求参数
     * @return JobConfig - hadoop job配置对象
     */
    private JobConfig genJobConfigForDbToHive(CrossDataParam params)
    {
        //其他情况则为 DB-》HT
        JobConfig config = null;
        Connection sConn = dbTool.createConnection(params.getDataSource().getDriverClass(),
            params.getConnectUrl(),
            params.getAuth()[0],
            params.getAuth()[1]);
        Connection tConn = dbTool.createConnection(params.getT_dataSource().getDriverClass(),
            params.getT_connectUrl(),
            params.getT_auth()[0],
            params.getT_auth()[1]);
        
        try
        {
            config = new JobConfig(JobConfig.ResourceType.DB, JobConfig.ResourceType.HIVE,
                dbTool.getColumnInfo(sConn, params.getTableName()),
                dbTool.getColumnInfo(tConn, params.getT_tableName()));
            
            if (StringUtils.isNotBlank(params.getWhereSql()))
            {
                config.getDbInput().setWhere(params.getWhereSql().trim());
            }
            
            if (StringUtils.isNotBlank(params.getPartition()))
            {
                config.getDbInput().setDbPartition(params.getPartition());
            }
            
            //配置Input/output参数
            config.getDbInput()
                .setConnectUrl(params.getConnectUrl())
                .setPassword(params.getAuth()[1])
                .setUsername(params.getAuth()[0])
                .setMapNum(params.getMapNum());
            
            //目标数据源是oracle 则表名大写 其余情况小写
            if (DataSourceType.ORACLE == params.getDataSource())
            {
                config.getDbOutput().setTableName(params.getTableName().trim().toUpperCase());
            }
            else
            {
                config.getDbOutput().setTableName(params.getTableName().toLowerCase());
            }
            
            config.getHiveOutput()
                .setHiveServer2Url(params.getT_connectUrl())
                .setConnectionUserName(params.getT_auth()[0])
                .setConnectionUserPassword(params.getT_auth()[1])
                .setDatabaseName(params.getT_databaseName())
                .setTableName(params.getT_tableName());
            
            if (DataSynMode.OVERWRITE == params.getWriteType())
            {
                config.getDbOutput().setIsOverwrite(true);
            }
            else if (DataSynMode.APPEND == params.getWriteType())
            {
                config.getDbOutput().setIsOverwrite(false);
            }
        }
        finally
        {
            dbTool.close(sConn);
            dbTool.close(tConn);
        }
        
        return config;
    }
    
    /**
     * sql加载数据同步到hdfs配置
     * @param params 请求参数
     * @return JobConfig - hadoop job配置对象
     */
    private JobConfig genJobConfigForSqlToHdfs(CrossDataParam params)
    {
        JobConfig config = new JobConfig(JobConfig.ResourceType.SQL, JobConfig.ResourceType.FILE,
            Lists.<Column> newArrayList(), Lists.<Column> newArrayList());
        
        //配置Intput参数
        config.getSqlInput()
            .setConnectUrl(params.getConnectUrl())
            .setPassword(params.getAuth()[1])
            .setUsername(params.getAuth()[0])
            .setSql(params.getSql())
            .setMapNum(params.getMapNum());
        
        //配置Output参数
        config.getFileOutput()
            .setFilesystem(FileInfo.FSType.HDFS)
            .setSerDeType(FileInfo.SerDeType.DELIMITED)
            .setFieldDelimiter(params.getFieldDelimiter())
            .setMakeDir(true)
            .setLocation(platformAttri.getCrossdataDefaultFtpDir());
        
        if (DataSynMode.OVERWRITE == params.getWriteType())
        {
            params.setLastSql(StringUtils.join("LOAD DATA INPATH '",
                platformAttri.getCrossdataDefaultFtpDir(),
                "' OVERWRITE  INTO TABLE ",
                params.getT_databaseName(),
                ".",
                params.getT_tableName()));
        }
        else if (DataSynMode.APPEND == params.getWriteType())
        {
            params.setLastSql(StringUtils.join("LOAD DATA INPATH '",
                platformAttri.getCrossdataDefaultFtpDir(),
                "'   INTO TABLE ",
                params.getT_databaseName(),
                ".",
                params.getT_tableName()));
        }
        
        return config;
    }
    
    /**
     * Hive表同步到关系型数据库配置
     * @param params 请求参数
     * @return JobConfig - hadoop job配置对象
     */
    private JobConfig genJobConfigForHiveToDb(CrossDataParam params)
    {
        JobConfig config = null;
        Connection sConn = dbTool.createConnection(params.getDataSource().getDriverClass(),
            params.getConnectUrl(),
            params.getAuth()[0],
            params.getAuth()[1]);
        Connection tConn = dbTool.createConnection(params.getT_dataSource().getDriverClass(),
            params.getT_connectUrl(),
            params.getT_auth()[0],
            params.getT_auth()[1]);
        try
        {
            config = new JobConfig(JobConfig.ResourceType.HIVE, JobConfig.ResourceType.DB,
                dbTool.getColumnInfo(sConn, params.getTableName()),
                dbTool.getColumnInfo(tConn, params.getT_tableName()));
            config.getHiveInput()
                .setMapNum(params.getMapNum())
                .setHiveServer2Url(params.getConnectUrl())
                .setConnectionUserPassword(params.getAuth()[1])
                .setConnectionUserName(params.getAuth()[0])
                .setDatabaseName(params.getDatabaseName())
                .setTableName(params.getTableName().toLowerCase());
            
            if (StringUtils.isNotBlank(params.getWhereSql()))
            {
                config.getHiveInput().setWhere(params.getWhereSql().trim());
            }
            config.getDbOutput()
                .setConnectUrl(params.getT_connectUrl())
                .setTableName(params.getT_tableName())
                .setPassword(params.getT_auth()[1])
                .setUsername(params.getT_auth()[0])
                .setRecordsPerStatement(params.getRecordPerStatement());
            
            if (DataSourceType.ORACLE == params.getDataSource())
            {
                config.getDbOutput().setTableName(params.getTableName().trim().toUpperCase());
            }
            else
            {
                config.getDbOutput().setTableName(params.getTableName().toLowerCase());
            }
            
            if (DataSynMode.OVERWRITE == params.getWriteType())
            {
                config.getDbOutput().setIsOverwrite(true);
            }
            else if (DataSynMode.APPEND == params.getWriteType())
            {
                config.getDbOutput().setIsOverwrite(false);
            }
            
            if (StringUtils.isNotBlank(params.getT_partition()))
            {
                config.getDbOutput().setDbPartition(params.getT_partition());
            }
        }
        finally
        {
            dbTool.close(sConn);
            dbTool.close(tConn);
        }
        
        return config;
    }
    
    /**
     * ftp文件同步到hive表配置
     * @param params 请求参数
     * @return JobConfig - hadoop job配置对象
     */
    private JobConfig genJobConfigForFtpToHive(CrossDataParam params)
    {
        //初始化JobConfig，四个参数是源数据源类型（有file和db两种），目的数据源类型（有file和db两种），输入列信息，输出列信息
        JobConfig config = new JobConfig(JobConfig.ResourceType.FILE, JobConfig.ResourceType.HIVE, null, null);
        
        String extName = StringUtils.substringAfter(params.getFileName(), ".");
        
        //配置Input参数（有FileInput和DbInput）
        config.getFileInput()
            .setFilesystem(FileInfo.FSType.FTP)
            .setCompression(false)
            .setLocation(params.getLocationFtp())
            .setFieldDelimiter(params.getDelimiterFtp())
            .setFilePattern(params.getFileName())
            .setEmptyWarn(true)
            .setRecursive(true)
            .setDelSourceFile(false);
        
        if (StringUtils.equalsIgnoreCase(extName, "csv"))
        {
            config.getFileInput().setSerDeType(FileInfo.SerDeType.CSV);
        }
        else
        {
            config.getFileInput().setSerDeType(FileInfo.SerDeType.DELIMITED);
        }
        
        //配置Output参数（有FileOutput和DbOutput）
        config.getHiveOutput()
            .setOverwrite(true)
            .setHiveServer2Url(params.getHiveServer2Url())
            .setDatabaseName(params.getDatabaseName())
            .setTableName(params.getTableName());
        
        return config;
    }
    
    /**
     * ftp文件同步到hugetable中的hdfs文件配置
     * @param params 请求参数
     * @return JobConfig - hadoop job配置对象
     */
    private JobConfig genJobConfigForFtpToHdfs(CrossDataParam params)
    {
        JobConfig config = new JobConfig(JobConfig.ResourceType.FILE, JobConfig.ResourceType.FILE, null, null);
        
        config.getFileInput()
            .setFilesystem(FileInfo.FSType.SFTP)
            .setCompression(false)
            //sftp路径,sftp://testftp:testftp@192.168.129.150/apps/testftp
            .setLocation(params.getLocationFtp())
            .setSerDeType(FileInfo.SerDeType.DELIMITED)
            //分隔符
            .setFieldDelimiter(params.getDelimiterFtp())
            //匹配文件名
            .setFilePattern(params.getFileName())
            //没有文件会告警
            .setEmptyWarn(true)
            //迭代子目录文件
            .setRecursive(true)
            //删除ftp源文件
            .setDelSourceFile(true)
            //acsii传输方式
            .setTransmissionType("ascii");
        
        //配置Output参数（有FileOutput和DbOutput）
        config.getFileOutput()
            .setFilesystem(FileInfo.FSType.HDFS)
            .setCompression(false)
            .setSerDeType(FileInfo.SerDeType.DELIMITED)
            //hdfs文件系统分隔符
            .setFieldDelimiter(params.getDelimiterHdfs())
            //指定的hdfs目录不存在则自动创建
            .setMakeDir(true)
            .setLocation(params.getLocationHdfs());
        
        return config;
    }
    
    /**
     * hugetable中的hdfs文件同步到ftp文件配置
     * @param params 请求参数
     * @return JobConfig - hadoop job配置对象
     */
    private JobConfig genJobConfigForHdfsToFtp(CrossDataParam params)
    {
        //定义输入和输出列信息
        List<Column> inputColumns = Lists.newArrayList();
        List<Column> outputColumns = Lists.newArrayList();
        
        inputColumns.add(new Column("1x", "int"));
        outputColumns.add(new Column("1x", "int"));
        
        JobConfig config =
            new JobConfig(JobConfig.ResourceType.FILE, JobConfig.ResourceType.FILE, inputColumns, outputColumns);
        
        //ht2ftp 是hdfs文件与ftp文件之间的同步
        config.getFileInput()
            .setFilesystem(FileInfo.FSType.HDFS)
            .setCompression(false)
            .setSerDeType(FileInfo.SerDeType.DELIMITED)
            //字段分隔符
            .setFieldDelimiter(params.getDelimiterHdfs())
            //遍历
            .setRecursive(true)
            //acsii方式全量数据拷贝
            .setTransmissionType("acsii")
            //hive 表在hdfs的路径,如hdfs://hadoop/apps/hugetable/warehouse/hbase_test.db/qcc_test_hbase
            .setLocation(params.getLocationHdfs());
        
        //配置Output参数（有FileOutput和DbOutput）
        config.getFileOutput()
            .setFilesystem(FileInfo.FSType.SFTP)
            .setCompression(false)
            //路径,sftp://testftp:testftp@192.168.129.150/apps/testftp
            .setLocation(params.getLocationFtp())
            .setSerDeType(FileInfo.SerDeType.DELIMITED)
            //文件名匹配表达式,在hdfs上生成的文件名以ht_hive-开头，如ht_hive-000,[例子ht_hive2-%id%]
            .setFileNamePattern(params.getFileName())
            .setFileNameIdLength(3)
            //字段分隔符
            .setFieldDelimiter(params.getDelimiterFtp());
        
        return config;
    }
    
    /**
     * 解析数据同步参数
     * @param request http请求对象
     * @return CrossDataParam - 数据同步参数
     */
    private CrossDataParam parseDataSyncParam(HttpServletRequest request)
    {
        try
        {
            CrossDataParam bean = new CrossDataParam();
            List<String> keys = BeanUtils.describe(bean)
                .entrySet()
                .stream()
                .map(e -> e.getKey())
                .filter(k -> !StringUtils.equals("class", k))
                .collect(Collectors.toList());
            Map<String, String> paramMap = request.getParameterMap()
                .entrySet()
                .stream()
                .filter(e -> StringUtils.isNotBlank(e.getKey()) && keys.contains(e.getKey()))
                .filter(e -> null != e.getValue() && e.getValue().length > 0)
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()[0]));
            BeanUtils.populate(bean, paramMap);
            
            return bean;
        }
        catch (Exception e)
        {
            LogUtils.runLogError(e);
        }
        
        return null;
        
    }
}
