package cn.migu.macaw.crossdata.service;

import javax.servlet.http.HttpServletRequest;

import cn.migu.macaw.common.ReturnCode;
import cn.migu.macaw.common.log.InterfaceLogBean;
import cn.migu.macaw.common.message.Entity;

/**
 * 数据同步服务层
 *
 * @author soy
 */
public interface IDataSyncService
{
    /**
     * 数据同步服务
     * @param request http请求
     * @return ReturnCode - 返回码
     * @throws Exception 系统抛出异常
     */
    ReturnCode dataSyncStart(HttpServletRequest request, Entity entity, InterfaceLogBean logBean) throws Exception;

    /**
     * 停止数据同步服务
     * @param jobId 任务id
     * @return ReturnCode - 返回码
     */
    ReturnCode dataSyncStop(String jobId);
}
