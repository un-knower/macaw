package cn.migu.macaw.schedule.task.util;

import static com.cronutils.model.CronType.QUARTZ;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.google.common.collect.Maps;

/**
 * quartz cron表达式解析工具类
 * 
 * @author soy
 */
public class QuartzCronUtil
{
    /**
     * 解析结果字符串格式
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 解析器对象
     */
    private static final CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(QUARTZ));
    
    /**
     * 解析表达式返回上次和下次触发时间
     * @param quartzStr cron表达式
     * @return [上次触发时间,下次触发时间]
     */
    public static String[] parseTriggerRangeToStr(String quartzStr)
    {
        Cron cron = parser.parse(quartzStr);
        
        ZonedDateTime now = ZonedDateTime.now();
        
        ExecutionTime executionTime = ExecutionTime.forCron(cron);
        
        ZonedDateTime lastExecTime = executionTime.lastExecution(now);
        
        ZonedDateTime nextExecTime = executionTime.nextExecution(now);
        
        return new String[] {lastExecTime.format(formatter), nextExecTime.format(formatter)};
        
    }

    /**
     * 解析表达式返回上次和下次触发时间
     * @param quartzStr cron表达式
     * @return [上次触发时间,下次触发时间]
     */
    public static Date[] parseTriggerRangeToDate(String quartzStr)
    {
        Cron cron = parser.parse(quartzStr);

        ZonedDateTime now = ZonedDateTime.now();

        ExecutionTime executionTime = ExecutionTime.forCron(cron);

        ZonedDateTime lastExecTime = executionTime.lastExecution(now);

        ZonedDateTime nextExecTime = executionTime.nextExecution(now);

        return new Date[] {Date.from(lastExecTime.toInstant()), Date.from(nextExecTime.toInstant())};

    }
    
    public static void main(String[] args)
    {
        String[] tm = parseTriggerRangeToStr("0 0 10,14,16 * * ?");
        Arrays.stream(tm).forEach(x -> {
            System.out.println(x);
        });
        
        Map<String, String[]> jobCronStr = Maps.newHashMap();
        jobCronStr.put("1", tm);
        String msg =
            jobCronStr.entrySet()
                .stream()
                .map(e -> StringUtils.join(e.getKey(), ":", e.getValue()[0], "-", e.getValue()[1]))
                .collect(Collectors.joining(","));
        System.out.println(msg);
        
        //select * from unify_job_log where start_time between to_date('2017-03-15 21:17:00','yyyy-mm-dd hh24:mi:ss') and to_date('2017-03-17 21:17:00','yyyy-mm-dd hh24:mi:ss') order by start_time desc
    }
}
