package cn.migu.macaw.jarboot.rest;

import cn.migu.macaw.common.log.InterfaceLogBean;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.common.log.ReqRespLog;
import cn.migu.macaw.jarboot.api.model.DataFileLog;
import cn.migu.macaw.jarboot.api.model.HdfsLogTmp;
import cn.migu.macaw.jarboot.api.model.ProcessLog;
import cn.migu.macaw.jarboot.common.JarFuncType;
import cn.migu.macaw.jarboot.common.JarStatus;
import cn.migu.macaw.jarboot.common.RemainCode;
import cn.migu.macaw.jarboot.api.model.Process;
import cn.migu.macaw.jarboot.dao.DataFileLogMapper;
import cn.migu.macaw.jarboot.dao.HdfsLogTmpMapper;
import cn.migu.macaw.jarboot.dao.ProcessLogMapper;
import cn.migu.macaw.jarboot.dao.ProcessMapper;
import cn.migu.macaw.jarboot.model.BaseResponse;
import cn.migu.macaw.jarboot.model.BaseResponseEntity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 提供第三方服务功能
 *
 * @author soy
 */
@RestController
@RequestMapping("/jar-boot")
public class ProvideFunction extends PreLogger
{
    private static final Log provideFuncLog = LogFactory.getLog("jar-boot-provide-func");
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProcessMapper processDao;

    @Autowired
    private ProcessLogMapper processLogDao;

    @Autowired
    private DataFileLogMapper dataFileLogDao;

    @Autowired
    private HdfsLogTmpMapper hdfsLogTmpDao;

    @Autowired
    private ReqRespLog reqRespLog;

    /**
     * sql执行
     * @param request http请求
     * @return BaseResponse - 返回消息
     */
    @RequestMapping("/sql-execute")
    public BaseResponse execute(HttpServletRequest request)
    {
        InterfaceLogBean logBean = createLogBean(reqRespLog,request);

        reqRespLog.requestLog(request,provideFuncLog,logBean);

        String sql = request.getParameter("sql");
        String param = request.getParameter("param");
        BaseResponseEntity content = new BaseResponseEntity();

        try
        {
            if (param != null && sql != null)
            {
                Object[] obj = JSONObject.parseArray(param).toArray();
                jdbcTemplate.update(sql, obj);
            }
            else
            {
                jdbcTemplate.execute(sql);
            }
        }
        catch (Exception e)
        {
            content.setCode(RemainCode.FAILED);
            content.setDesc(e.toString());
            LogUtils.runLogError(e);
        }
        BaseResponse res = new BaseResponse(content);

        reqRespLog.responseLog(provideFuncLog,logBean, JSON.toJSONString(content));

        return res;
    }

    /**
     * 执行sql
     * @param request http请求
     * @return BaseResponse - 返回消息
     */
    @RequestMapping("/sql-query")
    public BaseResponse queryForList(HttpServletRequest request)
    {
        InterfaceLogBean logBean = createLogBean(reqRespLog,request);

        reqRespLog.requestLog(request,provideFuncLog,logBean);

        String sql = request.getParameter("sql");
        String param = request.getParameter("param");
        BaseResponseEntity content = new BaseResponseEntity();

        List<Map<String, Object>> ls = null;

        try
        {
            if (param != null)
            {
                Object[] obj = JSONObject.parseArray(param).toArray();
                ls = jdbcTemplate.queryForList(sql, obj);
            }
            else
            {
                ls = jdbcTemplate.queryForList(sql);
            }
            content.setContent(JSONObject.toJSONString(ls));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            content.setCode(RemainCode.FAILED);
            content.setDesc(e.toString());
            LogUtils.runLogError(e);
        }
        BaseResponse res = new BaseResponse(content);

        reqRespLog.responseLog(provideFuncLog,logBean, JSON.toJSONString(content));
        return res;
    }

    /**
     * 批量处理sql
     * @param request
     * @return
     */
    @RequestMapping("/sql-batchUpdate")
    public BaseResponse executeFlumeSqls(HttpServletRequest request)
    {
        InterfaceLogBean logBean = createLogBean(reqRespLog,request);
        reqRespLog.requestLog(request,provideFuncLog,logBean);

        String sql = request.getParameter("sql");
        BaseResponseEntity content = new BaseResponseEntity();
        try
        {
            String[] sqls = sql.split("~");
            jdbcTemplate.batchUpdate(sqls);
        }
        catch (Exception e)
        {
            content.setCode(RemainCode.FAILED);
            content.setDesc(e.toString());
            LogUtils.runLogError(e);
        }
        BaseResponse res = new BaseResponse(content);

        reqRespLog.responseLog(provideFuncLog,logBean, JSON.toJSONString(content));
        return res;
    }

    /**
     * 进程启动成功回调(flume jar启动使用)
     * @param request
     * @return
     */
    @RequestMapping("/insertProcess")
    public BaseResponse insertProcess(HttpServletRequest request)
    {
        InterfaceLogBean logBean = createLogBean(reqRespLog,request);
        reqRespLog.requestLog(request,provideFuncLog,logBean);

        BaseResponseEntity content = new BaseResponseEntity();
        String serverId = request.getParameter("sid");
        String appId = request.getParameter("aid");
        String userId = request.getParameter("uid");
        String jarId = request.getParameter("jid");
        String pid = request.getParameter("pid");
        String port = request.getParameter("port");
        int metricsPort = Integer.valueOf(port) * 2;
        try
        {
            Process up = new Process();
            up.setAppId(appId);
            up.setJarId(jarId);
            up.setServerId(serverId);
            up.setProcessNo(Integer.valueOf(pid));
            up.setDealUser(userId);
            up.setPort(Integer.valueOf(port));
            up.setKind(String.valueOf(JarFuncType.FLUME.ordinal()));

            up.setNote("http://" + serverId + ":" + metricsPort + "/metrics");
            processDao.insertSelective(up);
            ProcessLog upLog = new ProcessLog();
            upLog.setAppId(appId);
            upLog.setDealUser(userId);
            upLog.setJarId(jarId);
            upLog.setKind(up.getKind());
            upLog.setServerId(serverId);
            upLog.setNote(up.getNote());
            upLog.setPort(up.getPort());
            upLog.setProcessNo(up.getProcessNo());
            upLog.setStatus(String.valueOf(JarStatus.RUNNING.ordinal()));
            processLogDao.insertSelective(upLog);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            content.setCode(RemainCode.FAILED);
            content.setDesc(e.toString());
            LogUtils.runLogError(e);
        }

        BaseResponse res = new BaseResponse(content);
        reqRespLog.responseLog(provideFuncLog,logBean, JSON.toJSONString(content));
        return res;
    }

    /**
     * 更新日志
     * @param request
     * @return
     */
    @RequestMapping("/updateDataFileLog")
    public BaseResponse updateDataFileLog(HttpServletRequest request)
    {
        InterfaceLogBean logBean = createLogBean(reqRespLog,request);
        reqRespLog.requestLog(request,provideFuncLog,logBean);

        BaseResponseEntity content = new BaseResponseEntity();
        String fn = request.getParameter("fn");

        try
        {

            DataFileLog dataFileLog = new DataFileLog();
            dataFileLog.setCollectTime(new Date());
            Example example = new Example(DataFileLog.class);
            example.createCriteria().andEqualTo("bname",fn);
            dataFileLogDao.updateByExampleSelective(dataFileLog,example);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            content.setCode(RemainCode.FAILED);
            content.setDesc(e.toString());
            LogUtils.runLogError(e);
        }

        BaseResponse res = new BaseResponse(content);
        reqRespLog.responseLog(provideFuncLog,logBean, JSON.toJSONString(content));
        return res;
    }

    /**
     * hdfs文件临时日志表
     * @param request
     * @return
     */
    @RequestMapping("/addHdfsLogTmp")
    public BaseResponse addHdfsLogTmp(HttpServletRequest request)
    {
        InterfaceLogBean logBean = createLogBean(reqRespLog,request);
        reqRespLog.requestLog(request,provideFuncLog,logBean);

        BaseResponseEntity content = new BaseResponseEntity();
        String sid = request.getParameter("sid");
        String aid = request.getParameter("aid");
        String jid = request.getParameter("jid");
        String path = request.getParameter("path");
        try
        {
            HdfsLogTmp hdfsLogTmp = new HdfsLogTmp();
            hdfsLogTmp.setAppId(aid);
            hdfsLogTmp.setServerId(sid);
            hdfsLogTmp.setJarId(jid);
            hdfsLogTmp.setHdfsPath(path);
            hdfsLogTmpDao.insertSelective(hdfsLogTmp);
        }
        catch (Exception e)
        {
            LogUtils.runLogError(e);
            content.setCode(RemainCode.FAILED);
            content.setDesc(e.toString());
        }

        BaseResponse res = new BaseResponse(content);
        reqRespLog.responseLog(provideFuncLog,logBean, JSON.toJSONString(content));
        return res;
    }

}
