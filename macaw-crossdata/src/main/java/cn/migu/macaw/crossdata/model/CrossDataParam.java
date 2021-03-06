package cn.migu.macaw.crossdata.model;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import cn.migu.macaw.crossdata.common.DataSourceType;
import cn.migu.macaw.crossdata.common.DataSynMode;

/**
 * 数据同步参数
 *
 * @author soy
 */
public class CrossDataParam
{
    private String crossDataIp;

    private Integer crossDataPort;

    private int recordPerStatement;

    private String whereSql;

    private int columnNum;

    private DataSynMode writeType;

    private String lastSql;

    private String sql;

    private String fieldDelimiter;

    private int mapNum;

    private String transferType;

    private String hdfsSource;

    private String hdfsTarget;

    private String hadoopNode;

    private String locationHdfs;

    private String locationFtp;

    private String fileName;

    private String DelimiterFtp;

    private String DelimiterHdfs;

    private String hiveServer2Url;

    private String batchNo;

    private String jobCode;

    private String taskCode;

    private String nodeId;

    private DataSourceType dataSource;

    private String partition;

    private String connectUrl;

    private String tableName;

    private String databaseName;

    private String[] auth;

    private DataSourceType t_dataSource;

    private String t_connectUrl;

    private String t_partition;

    private String t_tableName;

    private String t_databaseName;

    private String[] t_auth;

    public String getCrossDataIp()
    {
        return crossDataIp;
    }

    public void setCrossDataIp(String crossDataIp)
    {
        this.crossDataIp = crossDataIp;
    }

    public Integer getCrossDataPort()
    {
        return crossDataPort;
    }

    public void setCrossDataPort(String crossDataPort)
    {
        this.crossDataPort = StringUtils.isNotBlank(crossDataPort) ? Integer.valueOf(crossDataPort) : null;
    }

    public int getRecordPerStatement()
    {
        return recordPerStatement;
    }

    public void setRecordPerStatement(String recordPerStatement)
    {
        this.recordPerStatement = StringUtils.isBlank(recordPerStatement) ? 1000 : Integer.valueOf(recordPerStatement.trim());
    }

    public String getWhereSql()
    {
        return whereSql;
    }

    public void setWhereSql(String whereSql)
    {
        this.whereSql = whereSql;
    }

    public String getSql()
    {
        return sql;
    }

    public void setSql(String sql)
    {
        this.sql = sql;
    }

    public int getColumnNum()
    {
        return columnNum;
    }

    public void setColumnNum(String columnNum)
    {
        this.columnNum = StringUtils.isBlank(columnNum) ? 0 : Integer.valueOf(columnNum.trim());
    }

    public DataSynMode getWriteType()
    {
        return writeType;
    }

    public void setWriteType(String writeType)
    {
        this.writeType = DataSynMode.OVERWRITE;
        if(StringUtils.isBlank(writeType))
        {
            this.writeType = DataSynMode.APPEND;
        }
        else
        {
            if(DataSynMode.APPEND.ordinal() == Integer.valueOf(writeType))
            {
                this.writeType = DataSynMode.APPEND;
            }
            else
            {
                this.writeType = DataSynMode.OVERWRITE;
            }
        }
    }

    public String getLastSql()
    {
        return lastSql;
    }

    public void setLastSql(String lastSql)
    {
        this.lastSql = lastSql;
    }

    public String getFieldDelimiter()
    {
        return fieldDelimiter;
    }

    public void setFieldDelimiter(String fieldDelimiter)
    {
        this.fieldDelimiter = StringUtils.isBlank(fieldDelimiter) ? "001" : fieldDelimiter.trim();
    }

    public int getMapNum()
    {
        return mapNum;
    }

    public void setMapNum(String mapNum)
    {
        this.mapNum = StringUtils.isBlank(mapNum) ? 5 : Integer.valueOf(mapNum.trim());
    }

    public String getTransferType()
    {
        return transferType;
    }

    public void setTransferType(String transferType)
    {
        this.transferType = transferType;
    }

    public String getHdfsSource()
    {
        return hdfsSource;
    }

    public void setHdfsSource(String hdfsSource)
    {
        this.hdfsSource = hdfsSource;
    }

    public String getHdfsTarget()
    {
        return hdfsTarget;
    }

    public void setHdfsTarget(String hdfsTarget)
    {
        this.hdfsTarget = hdfsTarget;
    }

    public String getHadoopNode()
    {
        return hadoopNode;
    }

    public void setHadoopNode(String hadoopNode)
    {
        this.hadoopNode = hadoopNode;
    }

    public String getLocationHdfs()
    {
        return locationHdfs;
    }

    public void setLocationHdfs(String locationHdfs)
    {
        this.locationHdfs = locationHdfs;
    }

    public String getLocationFtp()
    {
        return locationFtp;
    }

    public void setLocationFtp(String locationFtp)
    {
        this.locationFtp = locationFtp;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getDelimiterFtp()
    {
        return DelimiterFtp;
    }

    public void setDelimiterFtp(String delimiterFtp)
    {
        DelimiterFtp = delimiterFtp;
    }

    public String getDelimiterHdfs()
    {
        return DelimiterHdfs;
    }

    public void setDelimiterHdfs(String delimiterHdfs)
    {
        DelimiterHdfs = StringUtils.isBlank(delimiterHdfs) ? "\\u0001" : delimiterHdfs.trim();
    }

    public String getHiveServer2Url()
    {
        return hiveServer2Url;
    }

    public void setHiveServer2Url(String hiveServer2Url)
    {
        this.hiveServer2Url = hiveServer2Url;
    }

    public String getBatchNo()
    {
        return batchNo;
    }

    public void setBatchNo(String batchNo)
    {
        this.batchNo = batchNo;
    }

    public String getJobCode()
    {
        return jobCode;
    }

    public void setJobCode(String jobCode)
    {
        this.jobCode = jobCode;
    }

    public String getTaskCode()
    {
        return taskCode;
    }

    public void setTaskCode(String taskCode)
    {
        this.taskCode = taskCode;
    }

    public String getNodeId()
    {
        return nodeId;
    }

    public void setNodeId(String nodeId)
    {
        this.nodeId = nodeId;
    }

    public DataSourceType getDataSource()
    {
        return dataSource;
    }

    public void setDataSource(String dataSource)
    {
        this.dataSource = parseDataSourceType(dataSource);
    }

    public String getPartition()
    {
        return partition;
    }

    public void setPartition(String partition)
    {
        this.partition = partition;
    }

    public String getConnectUrl()
    {
        return connectUrl;
    }

    public void setConnectUrl(String connectUrl)
    {
        this.connectUrl = connectUrl;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getDatabaseName()
    {
        return databaseName;
    }

    public void setDatabaseName(String databaseName)
    {
        this.databaseName = databaseName;
    }

    public DataSourceType getT_dataSource()
    {
        return t_dataSource;
    }

    public void setT_dataSource(String t_dataSource)
    {
        this.t_dataSource = parseDataSourceType(t_dataSource);
    }

    public String getT_connectUrl()
    {
        return t_connectUrl;
    }

    public void setT_connectUrl(String t_connectUrl)
    {
        this.t_connectUrl = t_connectUrl;
    }

    public String getT_partition()
    {
        return t_partition;
    }

    public void setT_partition(String t_partition)
    {
        this.t_partition = t_partition;
    }

    public String getT_tableName()
    {
        return t_tableName;
    }

    public void setT_tableName(String t_tableName)
    {
        this.t_tableName = t_tableName;
    }

    public String getT_databaseName()
    {
        return t_databaseName;
    }

    public void setT_databaseName(String t_databaseName)
    {
        this.t_databaseName = t_databaseName;
    }

    public String[] getAuth()
    {
        return auth;
    }

    public void setAuth(String auth)
    {
        this.auth = StringUtils.splitByWholeSeparator(auth,"/");
    }

    public String[] getT_auth()
    {
        return t_auth;
    }

    public void setT_auth(String t_auth)
    {
        this.t_auth = StringUtils.splitByWholeSeparator(t_auth,"/");
    }

    private DataSourceType parseDataSourceType(String dataSource)
    {
        switch(dataSource)
        {
            case "hugetable":
                return DataSourceType.HIVE;
            case "oracle":
                return DataSourceType.ORACLE;
            case "mysql":
                return DataSourceType.MYSQL;
            case "gp":
                return DataSourceType.GREENPLUM;
            case "ftp":
                return DataSourceType.FTP;
            default:
                return DataSourceType.ERROR;
        }
    }

    @Override public String toString()
    {
        return "CrossDataParam{" + "crossDataIp='" + crossDataIp + '\'' + ", crossDataPort=" + crossDataPort
            + ", recordPerStatement=" + recordPerStatement + ", whereSql='" + whereSql + '\'' + ", columnNum="
            + columnNum + ", writeType=" + writeType + ", lastSql='" + lastSql + '\'' + ", sql='" + sql + '\''
            + ", fieldDelimiter='" + fieldDelimiter + '\'' + ", mapNum=" + mapNum + ", transferType='" + transferType
            + '\'' + ", hdfsSource='" + hdfsSource + '\'' + ", hdfsTarget='" + hdfsTarget + '\'' + ", hadoopNode='"
            + hadoopNode + '\'' + ", locationHdfs='" + locationHdfs + '\'' + ", locationFtp='" + locationFtp + '\''
            + ", fileName='" + fileName + '\'' + ", DelimiterFtp='" + DelimiterFtp + '\'' + ", DelimiterHdfs='"
            + DelimiterHdfs + '\'' + ", hiveServer2Url='" + hiveServer2Url + '\'' + ", jobCode='" + jobCode + '\''
            + ", taskCode='" + taskCode + '\'' + ", nodeId='" + nodeId + '\'' + ", dataSource=" + dataSource
            + ", partition='" + partition + '\'' + ", connectUrl='" + connectUrl + '\'' + ", tableName='" + tableName
            + '\'' + ", databaseName='" + databaseName + '\'' + ", auth=" + Arrays.toString(auth) + ", t_dataSource="
            + t_dataSource + ", t_connectUrl='" + t_connectUrl + '\'' + ", t_partition='" + t_partition + '\''
            + ", t_tableName='" + t_tableName + '\'' + ", t_databaseName='" + t_databaseName + '\'' + ", t_auth="
            + Arrays.toString(t_auth) + '}';
    }
}
