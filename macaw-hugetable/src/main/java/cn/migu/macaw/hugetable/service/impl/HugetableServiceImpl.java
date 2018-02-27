package cn.migu.macaw.hugetable.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.migu.macaw.common.SysRetCode;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.hugetable.common.HtJdbcTemplateGenerator;
import cn.migu.macaw.hugetable.model.SqlParam;
import cn.migu.macaw.hugetable.service.IHugetableService;

/**
 * hadoop上表项查询服务
 *
 * @author soy
 */
@Service("hugetableServiceImpl")
public class HugetableServiceImpl implements IHugetableService
{
    private static final Log logger = LogFactory.getLog(HugetableServiceImpl.class);

    @Autowired
    private JdbcTemplate hugetable;
    

    @Override
    public Entity executeSql(SqlParam param)
    {
        Entity res = new Entity();
        try
        {
            if (StringUtils.isBlank(param.getSql()))
            {
                res.setCode(SysRetCode.PARAM_INCOMPLETE);
                res.setDesc("SQL语句不能为空");
                return res;
            }
            
            if (StringUtils.isEmpty(param.getUrl()))
            {
                hugetable.execute(param.getSql());
            }
            else
            {
                String url = param.getUrl();
                String userName = param.getUsername();
                String password = param.getPassword();
                HtJdbcTemplateGenerator htJdTemplate = new HtJdbcTemplateGenerator(url, userName, password);
                htJdTemplate.executeSql(param.getSql());
            }
            
        }
        catch (Throwable e)
        {
            LogUtils.runLogError(e);
            res.setCode(SysRetCode.HT_EXCEPTION);
            res.setErrorStack(ExceptionUtils.getStackTrace(e));
            res.setDesc("执行sql语句失败");
        }
        return res;
    }

    @Override
    public Entity executeQuery(SqlParam param)
    {
        Entity res = new Entity();
        try
        {
            if (StringUtils.isBlank(param.getSql()))
            {
                res.setCode(SysRetCode.PARAM_INCOMPLETE);
                res.setDesc("SQL语句不能为空");
                return res;
            }
            
            if (StringUtils.isEmpty(param.getUrl()))
            {
                List<Map<String, Object>> lists = hugetable.queryForList(param.getSql());
                
                res.setContent(JSON.toJSONString(lists));
            }
            else
            {
                String url = param.getUrl();
                String userName = param.getUsername();
                String password = param.getPassword();
                HtJdbcTemplateGenerator htJdTemplate = new HtJdbcTemplateGenerator(url, userName, password);
                List<Map<String, Object>> lists = htJdTemplate.queryForList(param.getSql());
                
                res.setContent(JSON.toJSONString(lists));
            }
            
        }
        catch (Exception e)
        {
            res.setCode(SysRetCode.SPARK_APP_FAILED);
            res.setErrorStack(e.toString());
            res.setDesc("查询失败！");
            e.printStackTrace();
        }
        return res;
    }
    
}
