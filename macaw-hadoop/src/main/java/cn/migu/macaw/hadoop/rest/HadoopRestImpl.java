package cn.migu.macaw.hadoop.rest;

import javax.servlet.http.HttpServletRequest;

import cn.migu.macaw.common.RequestKey;
import cn.migu.macaw.common.ReturnCode;
import cn.migu.macaw.common.ServiceName;
import cn.migu.macaw.common.log.InterfaceLogBean;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.common.log.ReqRespLog;
import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.common.message.Response;
import cn.migu.macaw.hadoop.api.service.HadoopService;
import cn.migu.macaw.hadoop.model.SqlParam;
import cn.migu.macaw.hadoop.service.IDataSyncService;
import cn.migu.macaw.hadoop.service.IHugetableService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private static final Log crossDataLog = LogFactory.getLog("crossdata");

    @Autowired
    private IHugetableService hugeTable;

    @Autowired
    private IDataSyncService dataSyncService;

    @Autowired
    private ReqRespLog reqRespLog;

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
    public Response dataBaseSyncStart(HttpServletRequest request)
    {
        InterfaceLogBean logBean = reqRespLog.initLogBean(request, ServiceName.HADOOP_SERVICE);
        reqRespLog.requestLog(request,crossDataLog,logBean);

        Entity entity = new Entity();
        try
        {
            ReturnCode retCode = dataSyncService.dataSyncStart(request,entity,logBean);
            entity.setCode(retCode.getCode());
            entity.setDesc(retCode.getName());
        }
        catch (Exception e)
        {
            entity.setCode(ReturnCode.FAILED.getCode());
            entity.setDesc(ReturnCode.FAILED.getName());
            entity.setErrorStack(ExceptionUtils.getStackTrace(e));
            LogUtils.runLogError(e);
        }

        reqRespLog.responseLog(crossDataLog,logBean,JSON.toJSONString(entity));

        Response response = new Response();
        response.setResponse(entity);

        return response;
    }

    @Override
    public Response dataBaseSyncStop(HttpServletRequest request)
    {
        InterfaceLogBean logBean = reqRespLog.initLogBean(request, ServiceName.HADOOP_SERVICE);
        reqRespLog.requestLog(request,crossDataLog,logBean);

        Entity entity = new Entity();

        String jobId = request.getParameter(RequestKey.CROSS_DATA_JOB_ID);

        if(StringUtils.isNotEmpty(jobId))
        {
            ReturnCode ret = dataSyncService.dataSyncStop(jobId);
            entity.setCode(ret.getCode());
            entity.setDesc(ret.getName());
        }
        else
        {
            entity.setCode(ReturnCode.DATA_SYNC_JOB_ID_EMPTY.getCode());
            entity.setDesc(ReturnCode.DATA_SYNC_JOB_ID_EMPTY.getName());
        }

        reqRespLog.responseLog(crossDataLog,logBean,JSON.toJSONString(entity));
        Response response = new Response();
        response.setResponse(entity);

        return response;
    }
}
