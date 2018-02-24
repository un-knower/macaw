package cn.migu.macaw.hadoop.common;

import java.sql.*;
import java.util.List;

import com.google.common.collect.Lists;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hugetable.crossdata.config.Column;

import cn.migu.macaw.common.log.LogUtils;

/**
 * 数据库通用类
 *
 * @author soy
 */
@Service
public class DbTool
{
    /**
     * 创建数据库连接
     * @param driver 驱动名
     * @param url 连接信息
     * @param user 用户名
     * @param password 密码
     * @return Connection - 连接对象
     */
    public Connection createConnection(String driver, String url, String user, String password)
    {
        Connection connect = null;
        try
        {
            if (DbUtils.loadDriver(driver))
            {
                connect = DriverManager.getConnection(url, user, password);
            }
            else
            {
                LogUtils.runLogError(String.format("load driver error:%s", driver));
            }
        }
        catch (Exception e)
        {
            LogUtils.runLogError(e);
        }
        
        return connect;
    }

    /**
     * 执行sql
     * @param driver 驱动名
     * @param url 连接地址
     * @param user 用户名
     * @param password 密码
     * @param sql 执行sql语句
     */
    public void execute(String driver, String url, String user, String password,String sql)
    {
        Connection conn = createConnection(driver,url,user,password);
        if(null != conn)
        {
            try
            {
                new QueryRunner().update(conn, sql);
            }
            catch (SQLException e)
            {
                LogUtils.runLogError(e);
            }
            finally
            {
                close(conn);
            }
        }
    }

    /**
     * 清空表项内容
     * 如果有分区则仅清空分区
     * @param conn 数据库连接对象
     * @param tableName 表名
     * @param partition 分区
     */
    public void truncate(Connection conn,String tableName,String partition)
    {
        String sql;
        if (StringUtils.isBlank(partition))
        {
            sql = String.format("truncate table %s",tableName);
        }
        else
        {
            sql = String.format("alter table %s truncate partition %s",tableName,partition);
        }

        try
        {
            new QueryRunner().update(conn, sql);
        }
        catch (Exception e)
        {
            LogUtils.runLogError(e);
        }
    }

    /**
     * 关闭数据库连接
     * @param conn 数据库连接对象
     */
    public void close(Connection conn)
    {
        try
        {
            DbUtils.close(conn);
        }
        catch (Exception e)
        {
            LogUtils.runLogError(e);
        }
    }

    /**
     * 获取指定表列信息
     * @param conn 数据库连接对象
     * @param tableName 表名
     * @return List<Column> - 列信息
     */
    public List<Column> getColumnInfo(Connection conn, String tableName)
    {
        try
        {
            DatabaseMetaData metadata = conn.getMetaData();
            ResultSet resultSet = metadata.getColumns(null, null, tableName, null);

            List<Column> cols = Lists.newArrayList();

            while (resultSet.next())
            {
                String id = StringUtils.lowerCase(resultSet.getString("COLUMN_NAME"));
                String type = resultSet.getString("TYPE_NAME");
                cols.add(new Column(id,type));
            }

            return cols;
        }
        catch (Exception e)
        {
            LogUtils.runLogError(e);
        }

        return null;
        
    }
}
