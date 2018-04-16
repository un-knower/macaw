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

import cn.migu.macaw.common.RestTemplateProvider;
import cn.migu.macaw.common.SysRetCode;
import cn.migu.macaw.common.message.Response;
import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.util.ConfigParamUtil;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;

/**
 * 爬虫组件
 *
 * @author soy
 */
@Component("crawlerTask")
public class CrawlerTask implements ITask
{
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

        if (StringUtils.isEmpty(remoteAddr))
        {
            return;
        }

        Map<String, String> formParams = sysParams.entrySet()
            .stream()
            .filter(map -> !StringUtils.equalsIgnoreCase(map.getKey(), ConfigParamUtil.REMOTE_HOST))
            .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

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
