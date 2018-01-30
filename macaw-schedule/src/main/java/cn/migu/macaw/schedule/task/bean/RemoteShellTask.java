package cn.migu.macaw.schedule.task.bean;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import cn.migu.macaw.common.SSHManager;
import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.datasource.DataSourceAdapter;
import cn.migu.macaw.schedule.task.datasource.DataSourceFlatAttr;
import cn.migu.macaw.schedule.task.util.ConfigParamUtil;
import cn.migu.macaw.schedule.task.util.TaskTraceLogUtil;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;

/**
 * 操作系统命令执行task
 * 
 * 
 * @author soy
 */
@Component("remoteShellTask")
public class RemoteShellTask implements ITask
{
    
    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private TaskTraceLogUtil taskTraceLogUtil;
    
    @Resource
    private DataSourceAdapter dataSourceAdapter;
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        
        DataSourceFlatAttr dsAttr = dataSourceAdapter.getNodeDataSourceConf(brief.getJobCode(), brief.getNodeId());
        
        //task级运行时参数
        List<String> shellCommand = configParamUtil.getJobNodeRunShellParams(brief);
        
        this.executeCommand(shellCommand, dsAttr, brief);
        
    }
    
    /**
     * 执行命令
     * @param shellCommand
     * @throws Exception 
     * @see [类、类#方法、类#成员]
     */
    private void executeCommand(List<String> shellCommand, DataSourceFlatAttr dsAttr, TaskNodeBrief brief)
        throws Exception
    {
        
        if (CollectionUtils.isEmpty(shellCommand))
        {
            return;
        }
        
        String ip = dsAttr.getSshHost();
        String username = dsAttr.getSshUser();
        String password = dsAttr.getSshPassword();

        String shells = shellCommand.stream().collect(Collectors.joining(";"));
        
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("远程系统命令开始执行:", shells));
        
        String output = SSHManager.execCommand(ip, username, password, shells);
        
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("远程系统命令结束,输出:", output));

    }
}
