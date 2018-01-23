/*
 * 文 件 名:  IResourceSchedulersService.java
 * 版    权:  Copyright 2015 咪咕互动娱乐有限公司,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  wufeng
 * 创建时间:  2016年1月22日
 
 */
package cn.migu.macaw.sparkdrivermgr.service;

import javax.servlet.http.HttpServletRequest;

import cn.migu.macaw.common.log.InterfaceLogBean;
import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.common.message.Response;
import cn.migu.macaw.sparkdrivermgr.model.SparkJobMetaData;

/**
 * spark任务管理
 * @author  soy
 */
public interface ISparkJobMgrService
{
    /**
     * spark session初始化
     * @param request http请求对象
     * @param resCtx spark job信息
     * @param entity response对象
     * @param logBean 日志信息
     */
    void initSparkCtx(HttpServletRequest request, SparkJobMetaData resCtx, Entity entity, InterfaceLogBean logBean);
    
    /**
     * 资源分配
     *
     * @param request http请求对象
     * @param resCtx spark job信息
     * @return String 分配spark session结果
     */
    String allocResource(HttpServletRequest request, SparkJobMetaData resCtx);
    
    /**
     * 资源释放
     * @param resCtx spark job信息
     */
    void freeResource(SparkJobMetaData resCtx);
    
    /**
     * 停止spark session
     * @param request request http请求对象
     * @param resCtx spark job信息
     * @param logBean 日志信息
     */
    void stopSparkCtx(HttpServletRequest request, SparkJobMetaData resCtx, InterfaceLogBean logBean);
    
    /**
     * 提交任务到spark driver服务器
     * @param request request http请求对象
     * @param resCtx spark job信息
     * @param logBean 日志记录
     * @return String spark job提交结果
     */
    String submit(HttpServletRequest request, SparkJobMetaData resCtx, InterfaceLogBean logBean);
    
    /**
     * 主动kill spark app
     * @param appName spark job名称
     * @param appId spark job id
     * @return String 停止spark job结果
     */
    String killSparkApp(String appName, String appId);
    
    /**
     * spark app资源核数,内存大小校验
     * @param coreNum spark核数
     * @param memSize spark executor内存大小
     * @return String 检测spark资源结果
     */
    String checkResParam(String coreNum, String memSize);
    
    /**
     * 查询缓存中的resource context
     * @param resCtx spark job信息
     * @return 获取spark job提交上下文结果
     */
    String getResourceCtxCache(SparkJobMetaData resCtx);

    /**
     * 查询spark job状态
     * @param ip driver地址
     * @param port driver端口
     * @param appId spark job appid
     * @return
     */
    Response querySparkApp(String ip, String port, String appId);
    
}
