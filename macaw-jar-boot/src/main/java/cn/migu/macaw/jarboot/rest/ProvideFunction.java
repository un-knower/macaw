package cn.migu.macaw.jarboot.rest;

import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.jarboot.api.model.ProcessLog;
import cn.migu.macaw.jarboot.common.JarFuncType;
import cn.migu.macaw.jarboot.common.JarStatus;
import cn.migu.macaw.jarboot.common.RemainCode;
import cn.migu.macaw.jarboot.api.model.Process;
import cn.migu.macaw.jarboot.dao.ProcessLogMapper;
import cn.migu.macaw.jarboot.dao.ProcessMapper;
import cn.migu.macaw.jarboot.model.BaseResponse;
import cn.migu.macaw.jarboot.model.BaseResponseEntity;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 提供第三方服务功能
 *
 * @author soy
 */
@RestController
public class ProvideFunction
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProcessMapper processDao;

    @Autowired
    private ProcessLogMapper processLogDao;

    /**
     * sql执行
     * @param request http请求
     * @return BaseResponse - 返回消息
     */
    @RequestMapping("/sql/execute")
    public BaseResponse execute(HttpServletRequest request)
    {
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
        return res;

    }

    /**
     * 执行sql
     * @param request http请求
     * @return BaseResponse - 返回消息
     */
    @RequestMapping("/sql/query")
    public BaseResponse queryForList(HttpServletRequest request)
    {
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
        return res;
    }
}
