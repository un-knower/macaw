package cn.migu.macaw.schedule.task.util;

/**
 * 请求服务uri
 *
 * @author
 */
public interface RequestServiceUri
{
    /**
     * 执行1条spark sql
     */
    String SPARK_SQL_EXECUTE = "rs/SparkSQL/executeSql.do";

    /**
     * 在已创建spark session中执行spark sql
     */
    String SPARK_SQL_EXECUTE_CTX = "rs/SparkSQL/doSessionSql.do";

    /**
     * 加载文件数据到数据库
     */
    String LOADL_FILE_TO_DATABASE = "rs/SparkSQL/loadFileToDatabase.do";

    /**
     * 执行spark sql后并将数据加载至内存表
     */
    String EXECUTE_SQL_DATAFRAME = "rs/SparkSQL/executeSqldataFrame.do";

    /**
     * 执行spark sql并将数据保存到hdfs文件
     */
    String SPARK_SELECT_TO_FILE = "rs/SparkSQL/selectToFile.do";

    /**
     * 执行spark sql并将数据保存到hdfs一个文本文件中
     */
    String SPARK_SELECT_TO_TEXT = "rs/SparkSQL/selectToSingleTextFile.do";

    /**
     * 将sql的查询数据保存到redis
     */
    String SAVE_TO_REDIS_BYSQL = "rs/SparkSQL/saveToRedisBySql.do";

    /**
     * 执行多条sql
     */
    String SPARK_SQLLIST_PATH = "rs/SparkSQL/executeSqlList.do";

    /**
     * 将json数据保存到内存表
     */
    String SPARK_JSON_TO_DB = "rs/SparkSQL/jsonToDbBySql.do";

    /**
     * 算法训练
     */
    String ALGO_TRAIN = "rs/algo/scalaTrain.do";

    /**
     * 算法使用
     */
    String ALGO_USE = "rs/algo/scalaUse.do";

    //////////////////////////////////////////////
    /**
     * jdbc方式查询HT
     */
    String JDBC_EXECUTE_QUERY = "hugetable-sql-select";

    /**
     *
     */
    String SINGLE_DATASOURCE_TABLE = "singleDataSourceTable.do";

    /**
     * 数据同步
     */
    String COMMON_DB_CROSSDATA = "crossdata-start";

    /**
     * 停止数据同步服务
     */
    String DB_CROSSDATA_KILL = "crossdata-stop";

    /**
     * 创建spark session
     */
    String SPARK_DRIVER_INIT = "initSparkDriver.do";

    /**
     * 释放spark session
     */
    String SPARK_DRIVER_FREE = "freeSparkDriver.do";

    /**
     * 在已创建的spark session中执行一条查询sql
     */
    String SPARK_SELECT_QUERY = "doSessionSelectSql.do";

    /**
     * 停止spark job
     */
    String SPARK_STOP_APP = "stopSparkApp.do";

    /**
     * 查询spark job状态
     */
    String SPARK_JOB_STATUS_QUERY = "querySparkApp.do";

    //////////////////////////////////////////////////////////////////
}
