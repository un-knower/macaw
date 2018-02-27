package cn.migu.macaw.hugetable.model;

/**
 * sql请求参数
 *
 * @author soy
 */
public class SqlParam
{
    private String sql;

    private String url;

    private String username;

    private String password;

    public SqlParam()
    {

    }

    public SqlParam(String sql, String url, String username, String password)
    {
        this.sql = sql;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getSql()
    {
        return sql;
    }

    public void setSql(String sql)
    {
        this.sql = sql;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
