package cn.migu.macaw.schedule.task.bean;

import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.base.Joiner;

import cn.migu.macaw.common.RestTemplateProvider;
import cn.migu.macaw.common.SysRetCode;
import cn.migu.macaw.common.message.Response;
import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.api.model.NodeParam;
import cn.migu.macaw.schedule.dao.NodeParamMapper;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.util.ConfigParamUtil;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import tk.mybatis.mapper.entity.Example;

/**
 * 爬虫组件
 *
 * @author soy
 */
@Component("crawlerTask")
public class CrawlerTask implements ITask
{
    @Autowired
    private NodeParamMapper nodeParamDao;

    @Autowired
    private ConfigParamUtil configParamUtil;

    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;

    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        Map<String, String> sysParams = configParamUtil.getJobNodeSysParams(brief);

        String remoteAddr = sysParams.get(ConfigParamUtil.REMOTE_HOST);

        if (StringUtils.isBlank(remoteAddr))
        {
            throw new IllegalArgumentException("没有配置服务地址");
        }

        Map<String, String> formParams = sysParams.entrySet()
            .stream()
            .filter(map -> !StringUtils.equalsIgnoreCase(map.getKey(), ConfigParamUtil.REMOTE_HOST))
            .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

        formParams.put("job_code",brief.getJobCode());
        formParams.put("task_code",brief.getTaskCode());
        formParams.put("node_code",brief.getNodeId());

        Example exam = new Example(NodeParam.class);
        exam.createCriteria().andLike("pkey", "_datasource_%").andEqualTo("jobCode", brief.getJobCode()).andEqualTo("nodeCode",brief.getNodeId());

        NodeParam nps = nodeParamDao.selectOneByExample(exam);
        formParams.put("database_type",nps.getValue());

        String postFormParam = "";
        if(null != formParams && formParams.size() > 0)
        {
            postFormParam = Joiner.on(",").withKeyValueSeparator("=").join(formParams);
        }

        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("请求参数:", postFormParam));

        String respStr = RestTemplateProvider.postFormForEntity(restTemplate, remoteAddr, String.class, formParams);

        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("请求返回消息:", respStr));

        Response response = JSON.parseObject(respStr, Response.class, Feature.InitStringFieldAsEmpty);

        if(!StringUtils.equals(response.getResponse().getCode(),SysRetCode.SUCCESS))
        {
            String defaultErr = "爬虫功能异常";
            if(StringUtils.isNotBlank(response.getResponse().getErrorStack()))
            {
                defaultErr = String.format("%s:%s",defaultErr,response.getResponse().getErrorStack());
            }
            throw new IllegalStateException(defaultErr);
        }

    }
}
