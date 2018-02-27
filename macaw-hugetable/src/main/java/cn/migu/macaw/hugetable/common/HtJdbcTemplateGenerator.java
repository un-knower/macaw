package cn.migu.macaw.hugetable.common;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.chinamobile.cmss.ht.Driver;

/**
 * jdbc template生成器
 * 
 * @author soy
 */
public class HtJdbcTemplateGenerator
{
    private JdbcTemplate htJdbc;
    
    public HtJdbcTemplateGenerator(String url, String userName, String password)
    {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        
        htJdbc = new JdbcTemplate(dataSource);
        
    }
    
    /**
     * 根据sql查询并返回结果
     * @param sql
     * @return
     */
    public List<Map<String, Object>> queryForList(String sql)
    {
        return this.htJdbc.queryForList(sql);
    }
    
    /**
     * 执行sql
     * @param sql
     * @see [类、类#方法、类#成员]
     */
    public void executeSql(String sql)
    {
        this.htJdbc.execute(sql);
    }
}
