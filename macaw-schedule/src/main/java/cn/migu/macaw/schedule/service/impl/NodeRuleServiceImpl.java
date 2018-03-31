package cn.migu.macaw.schedule.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import cn.migu.macaw.schedule.api.model.NodeRule;
import cn.migu.macaw.schedule.cache.ProcContextCache;
import cn.migu.macaw.schedule.dao.NodeRuleMapper;
import cn.migu.macaw.schedule.service.INodeRuleService;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.util.StringTagReplaceUtil;

/**
 * 节点规则业务实现类
 * 
 * @author  soy
 */
@Service("nodeRuleService")
public class NodeRuleServiceImpl implements INodeRuleService
{
    @Resource
    private NodeRuleMapper nodeRuleDao;
    
    @Resource
    private StringTagReplaceUtil stringTagTool;
    
    @Resource
    private ProcContextCache procContextCache;

    
    /**
     * 获取节点对应的规则表达式
     * 每条规则按顺序存放
     * @param brief 节点简明信息
     * @return
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Map<String, String> getRuleExpr(TaskNodeBrief brief)
    {
        String jobCode = brief.getJobCode();
        String nodeCode = brief.getNodeId();
        // 查询节点规则
        NodeRule nr = new NodeRule();
        nr.setJobCode(jobCode);
        nr.setLocalNode(nodeCode);
        
        List<NodeRule> nrs = nodeRuleDao.select(nr);
        if (CollectionUtils.isEmpty(nrs))
        {
            return null;
        }

        Map<String, String> ruleExprs = Maps.newHashMap();
        
        for (NodeRule inr : nrs)
        {
            if(StringUtils.isEmpty(inr.getNote()))
            {
                continue;
            }

            String afterReplaceExpr = stringTagTool.replaceLabelsInCtx(jobCode,inr.getNote());

            afterReplaceExpr = stringTagTool.replaceLabelsInNode(afterReplaceExpr,brief);

            if (StringUtils.isNotEmpty(afterReplaceExpr))
            {
                ruleExprs.put(inr.getNextNode(), afterReplaceExpr);
            }
            
        }
        
        return ruleExprs;
    }
    
}
