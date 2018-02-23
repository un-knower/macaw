package cn.migu.macaw.hadoop.common;

/**
 * 数据源类型
 *
 * @author soy
 */
public enum DataSourceType
{
    /**
     * 大表
     */
    HUGETABLE("com.chinamobile.cmss.ht.Drive"),
    /**
     * mysql
     */
    MYSQL("com.mysql.jdbc.Driver"),
    /**
     * oracle
     */
    ORACLE("oracle.jdbc.driver.OracleDriver"),
    /**
     * ftp
     */
    FTP("ftp"),
    /**
     * green plum
     */
    GREENPLUM("org.postgresql.Driver"),
    /**
     * 错误类型
     */
    ERROR("")
    ;
    DataSourceType(String driverClass)
    {
        this.driverClass = driverClass;
    }

    private String driverClass;

    public String getDriverClass()
    {
        return driverClass;
    }
}
