package cn.migu.macaw.hadoop.rest;

import javax.servlet.http.HttpServletRequest;

import cn.migu.macaw.common.RequestKey;
import cn.migu.macaw.common.SysRetCode;
import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.common.message.Response;
import cn.migu.macaw.hadoop.api.service.HadoopService;
import cn.migu.macaw.hadoop.model.SqlParam;
import cn.migu.macaw.hadoop.service.IHugetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * hadoop服务
 *
 * @author soy
 */
@RestController
public class HadoopRestImpl implements HadoopService
{
    @Autowired
    private IHugetableService hugeTable;

    @Override
    public Response hugetableSql(HttpServletRequest request)
    {
        SqlParam sqlParam = new SqlParam(request.getParameter(RequestKey.SQL),request.getParameter(RequestKey.DATA_BASE_NAME),request.getParameter(RequestKey.USER_NAME),request.getParameter(RequestKey.PASSWORD));
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
    
    @Override
    public Response dataBaseSync(HttpServletRequest request)
    {
        return null;
    }
}
