package cn.migu.macaw.hugetable.rest;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import cn.migu.macaw.common.RequestKey;
import cn.migu.macaw.common.log.ReqRespLog;
import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.common.message.Response;
import cn.migu.macaw.hugetable.api.service.HugetableSqlService;
import cn.migu.macaw.hugetable.model.SqlParam;
import cn.migu.macaw.hugetable.service.IHugetableService;

/**
 * hugetable sql服务
 *
 * @author soy
 */
@RestController
public class HugetableRestImpl implements HugetableSqlService
{
    private static final Log crossDataLog = LogFactory.getLog("hugetable-sql");
    
    @Autowired
    private IHugetableService hugeTable;
    
    @Autowired
    private ReqRespLog reqRespLog;
    
    @Override
    public Response hugetableSql(HttpServletRequest request)
    {
        SqlParam sqlParam =
            new SqlParam(request.getParameter(RequestKey.SQL), request.getParameter(RequestKey.DATA_BASE_NAME),
                request.getParameter(RequestKey.USER_NAME), request.getParameter(RequestKey.PASSWORD));
        Response response = new Response();
        Entity res = hugeTable.executeSql(sqlParam);
        response.setResponse(res);
        
        return response;
    }
    
    @Override
    public Response hugetableSqlSelect(HttpServletRequest request)
    {
        return null;
    }
    
}
