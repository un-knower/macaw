package cn.migu.macaw.schedule.util;

import org.apache.commons.lang3.StringUtils;

import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 任务调度日志
 * 
 * @author soy
 */
public class ScheduleLogTrace
{

    private static Log log = LogFactory.getLog("scheduler");


    /**
     * INFO日志
     * @param brief
     * @param info
     * @see [类、类#方法、类#成员]
     */
    public static void scheduleInfoLog(TaskNodeBrief brief, String info)
    {
        LogUtils.info(log,StringUtils.join("job[",
            brief.getJobCode(),
            "]-->{node[",
            brief.getNodeId(),
            "],task[",
            brief.getTaskClassType(),
            "]} ",
            info));
    }

    /**
     * 调度日志
     * @param info
     */
    public static void scheduleInfoLog(String info)
    {
        LogUtils.info(log,info);
    }
    
    /**
     * INFO日志
     * @param jobCode
     * @param nodeCode
     * @param taskType
     * @param info
     * @see [类、类#方法、类#成员]
     */
    public static void scheduleInfoLog(String jobCode, String nodeCode, String taskType, String info)
    {
        LogUtils.info(log,StringUtils.join("job[",
            jobCode,
            "]-->{node[",
            nodeCode,
            "],task[",
            taskType,
            "]} ",
            info));
    }

    /**
     * 警告日志
     * @param info
     */
    public static void scheduleWarnLog(String info)
    {
        LogUtils.warn(log,info);
    }
    
    /**
     * 警告日志
     * @param brief
     * @param info
     * @see [类、类#方法、类#成员]
     */
    public static void scheduleWarnLog(TaskNodeBrief brief, String info)
    {
        LogUtils.warn(log,StringUtils.join("job[",
            brief.getJobCode(),
            "]-->{node[",
            brief.getNodeId(),
            "],task[",
            brief.getTaskClassType(),
            "]} ",
            info));
    }
    
    /**
     * 警告日志
     * @param jobCode
     * @param nodeCode
     * @param taskType
     * @param info
     * @see [类、类#方法、类#成员]
     */
    public static void scheduleWarnLog(String jobCode, String nodeCode, String taskType, String info)
    {
        LogUtils.warn(log,StringUtils.join("job[",
            jobCode,
            "]-->{node[",
            nodeCode,
            "],task[",
            taskType,
            "]} ",
            info));
    }
    
}
