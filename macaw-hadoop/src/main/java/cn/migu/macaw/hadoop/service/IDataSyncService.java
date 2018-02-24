package cn.migu.macaw.hadoop.service;

import cn.migu.macaw.common.ReturnCode;
import cn.migu.macaw.common.log.InterfaceLogBean;
import cn.migu.macaw.common.message.Entity;

import javax.servlet.http.HttpServletRequest;

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
    ReturnCode dataSync(HttpServletRequest request,Entity entity,InterfaceLogBean logBean) throws Exception;
}
