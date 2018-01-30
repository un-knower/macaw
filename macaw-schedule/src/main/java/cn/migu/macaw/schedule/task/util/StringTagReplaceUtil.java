package cn.migu.macaw.schedule.task.util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.cache.ProcContextCache;
import cn.migu.macaw.schedule.workflow.DataConstants;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * sql替换
 * 
 * @author soy
 */
@Component("stringTagReplaceUtil")
public class StringTagReplaceUtil implements LabelTag
{

    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private ProcContextCache procContextCache;
    
    @Resource
    private JobTasksCache jobTasksCache;
    
    /**
     * 替换指定运行批次号
     * @param origStr
     * @param batchNo
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String replaceBatchNo(String origStr, String batchNo)
    {
        Preconditions.checkNotNull(origStr, "输入原始字符串为空");
        
        return StringUtils.replace(origStr, "${batch_no}", batchNo);
    }
    
    /**
     * 替换指定数据库名
     * @param origStr
     * @param dataBase
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String replaceDatabase(String origStr, String dataBase)
    {
        Preconditions.checkNotNull(origStr, "输入原始字符串为空");
        
        return StringUtils.replace(origStr, "${database}", dataBase);
        
    }
    
    /**
     * 替换sql中的列集合
     * @param origStr
     * @param colSet
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String replaceColSet(String origStr, String colSet)
    {
        Preconditions.checkNotNull(origStr, "输入原始字符串为空");
        
        return StringUtils.replace(origStr, "${column_set}", colSet);
    }
    
    /**
     * 替换正则表达式指定的内容
     * 如果替换标签中带有需要转移符号如$,{,}等
     * 需先调用StringUtil.escapeExprSpecialWord(str)方法,然后再替换;
     * @author zhaocan
     * @param origStr 传入原始sql
     * @param regEx   替换正则
     * @param replaceStr 替换内容
     * @return 替换后的sql字符串
     */
    public String replaceSpecRegEx(String origStr, String regEx, String replaceStr)
    {
        Preconditions.checkNotNull(origStr, "输入原始字符串为空");
        
        Preconditions.checkNotNull(regEx, "替换的正则表达式为空");
        
        AbstractRegxReplaceByGroup tripler = new AbstractRegxReplaceByGroup(regEx, replaceStr)
        {
            @Override
            public String replacement()
            {
                return this.getDstObj();
            }
        };
        
        return tripler.rewrite(origStr);
    }
    
    /**
     * 环境中的替换标签
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getReplaceLabels(TaskNodeBrief brief)
    {
        //1.节点标签参数配置
        Map<String, String> nLabels = configParamUtil.getJobNodeSqlLabelParams(brief);
        
        nLabels = (null == nLabels) ? Maps.newHashMap() : nLabels;
        
        //2.job标签参数配置
        Map<String, String> jLabels = configParamUtil.getJobLabelParams(brief);
        
        jLabels = (null == jLabels) ? Maps.newHashMap() : jLabels;
        
        jLabels.putAll(nLabels);
        
        return jLabels;
    }
    
    /**
     * 节点内标签替换
     * @param origStr
     * @param labels
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String replaceLabelsInNode(final String origStr, Map<String, String> labels)
    {
        String rstStr = origStr;
        
        if (MapUtils.isNotEmpty(labels))
        {
            for (Map.Entry<String, String> entry : labels.entrySet())
            {
                
                String key = entry.getKey();
                String value = entry.getValue();
                
                if (this.isRepLabelValid(key) && StringUtils.isNotEmpty(value))
                {
                    rstStr = StringUtils.replace(rstStr, key, value);
                }
                
            }
        }
        
        return rstStr;
    }
    
    /**
     * 根据调度节点中的配置标签参数进行标签替换
     * @param origStr 原始字符串
     * @param brief 调度中心上下文
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String replaceLabelsInNode(final String origStr, TaskNodeBrief brief)
    {
        
        Map<String, String> labels = getReplaceLabels(brief);
        
        return replaceLabelsInNode(origStr, labels);
        
    }
    
    /**
     * 替换指定参数key对应的标签
     * @param dataMap
     * @param brief
     * @param filterKeys
     * @see [类、类#方法、类#成员]
     */
    public void replaceLabelsSpecParam(final Map<String, String> dataMap, TaskNodeBrief brief, String[] filterKeys)
    {
        if (ArrayUtils.isEmpty(filterKeys))
        {
            return;
        }
        
        Map<String, String> labels = getReplaceLabels(brief);
        
        if (MapUtils.isNotEmpty(labels))
        {
            Stream<String> fks = Stream.of(filterKeys);
            
            Map<String, String> filterDataMap =
                dataMap.entrySet()
                    .stream()
                    .filter(e -> StringUtils.isNotEmpty(e.getValue())
                        && fks.anyMatch(x -> StringUtils.equals(x, e.getKey())))
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> replaceLabelsInNode(e.getValue(), labels)));
            
            dataMap.putAll(filterDataMap);
        }
        
    }
    
    /**
     * 替换指定map中的sql语句
     * @param dataMap 数据map
     * @param brief   上下文
     * @see [类、类#方法、类#成员]
     */
    public void replaceLabelsInSqls(final Map<String, String> dataMap, TaskNodeBrief brief)
    {
        Map<String, String> newDataMap =
            dataMap.entrySet()
                .stream()
                .filter(entry -> StringUtils.startsWithIgnoreCase(entry.getValue().trim(), "select"))
                .collect(HashMap::new, (m, sp) -> {
                    m.put(sp.getKey(), sp.getValue());
                }, HashMap::putAll);
        
        if (MapUtils.isEmpty(newDataMap))
        {
            return;
        }
        
        Map<String, String> labels = getReplaceLabels(brief);
        
        if (MapUtils.isNotEmpty(labels))
        {
            Map<String, String> replaceMap =
                newDataMap.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> replaceLabelsInNode(e.getValue(), labels)));
            
            dataMap.putAll(replaceMap);
        }
        
    }
    
    /**
     * 有效替换标签格式为${X....}
     * @param key
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean isRepLabelValid(String key)
    {
        if (StringUtils.isEmpty(key))
        {
            return false;
        }
        
        if (StringUtils.length(key) < LABEL_MIN_LEN)
        {
            return false;
        }
        
        if (!StringUtils.startsWith(key, LABEL_PREFIX))
        {
            return false;
        }
        
        if (!StringUtils.endsWith(key, LABEL_SUFFIX))
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * 根据上下文进行标签替换
     * @param jobCode 任务编码
     * @param origStr 原始字符串
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String replaceLabelsInCtx(String jobCode, final String origStr)
    {
        Map<String, String> vars = procContextCache.getProcVars(jobCode);
        
        String rstStr = origStr;
        
        if (MapUtils.isNotEmpty(vars))
        {
            for (Map.Entry<String, String> entry : vars.entrySet())
            {
                
                String k = entry.getKey();
                String v = entry.getValue();
                
                if (this.isRepLabelValid(k) && StringUtils.isNotEmpty(v))
                {
                    rstStr = StringUtils.replace(rstStr, k, v);
                }
                
            }
        }
        
        //job上下文中的循环变量
        String factorName = jobTasksCache.get(jobCode, "-", DataConstants.VAR_NAME_FOR_JOB_LOOP);
        String factorVal = jobTasksCache.get(jobCode, "-", DataConstants.VAR_VAL_FOR_JOB_LOOP);
        if (StringUtils.isNotEmpty(factorName) && StringUtils.isNotEmpty(factorVal))
        {
            rstStr = StringUtils.replace(rstStr, factorName, factorVal);
        }
        
        return rstStr;
        
    }
    
    public static void main(String[] args)
    {
        
        String abc = StringUtils.replace("${i} == 3 || ${i} == 4", "${i}", "3");
        System.out.println(abc);
    }
    
}
