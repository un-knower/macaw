package cn.migu.macaw.common;

/**
 * 在线数据源类型
 *
 * @author soy
 */
public enum OnlineDataSourceType
{
    /**
     * 内部kafka
     */
    IDOT_KAFKA(1),
    /**
     * hive
     */
    HIVE(2),
    /**
     * oracle
     */
    ORACLE(3),
    /**
     * redis
     */
    REDIS(4),
    /**
     * mysql
     */
    MYSQL(5),
    /**
     * ftp
     */
    FTP(6),
    /**
     * hbase
     */
    HBASE(7),
    /**
     * hdfs
     */
    HDFS(8),
    /**
     * kafka
     */
    KAFAK(9);


    private int value;

    private OnlineDataSourceType(int value)
    {
        this.value = value;
    }
}
