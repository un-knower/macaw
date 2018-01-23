package cn.migu.macaw.dag.alg;

import java.util.*;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

import cn.migu.macaw.dag.util.MultimapLib;

/**
 * 
 * 拓扑排序
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年5月27日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class TopsortLib
{
    // 算法原理参考
    // http://en.wikipedia.org/wiki/Topological_sorting
    //
    
    /**
     * 排序方法
     * @param idSet 有向图顶点集
     * @param id__parents 有向图边集合
     * @return 排序后的顶点集合
     * @see [类、类#方法、类#成员]
     */
    public static <Id> Optional<ImmutableList<Id>> sort(ImmutableSet<Id> idSet, ImmutableSetMultimap<Id, Id> id__parents)
    {
        
        // 根据子节点方向遍历
        ImmutableMultimap<Id, Id> id__children = id__parents.inverse();
        
        // 创建结果list
        ImmutableList.Builder<Id> sorted = ImmutableList.builder();
        
        Set<Id> closed = new HashSet<>();
        
        Stack<Id> open = new Stack<>();
        
        Map<Id, Integer> id__parentCount = new HashMap<>();
        for (Id id : idSet)
        {
            
            int parentCount = id__parents.get(id).size();
            id__parentCount.put(id, parentCount);
            
            if (parentCount == 0)
            {
                open.push(id);
            }
        }
        
        while (!open.isEmpty())
        {
            
            Id id = open.pop();
            
            sorted.add(id);
            closed.add(id);
            
            for (Id child : id__children.get(id))
            {
                
                int parentCount = id__parentCount.get(child) - 1;
                id__parentCount.put(child, parentCount);
                
                if (parentCount == 0)
                {
                    open.push(child);
                }
            }

        }
        
        // 存在环,返回absent
        if (idSet.size() != closed.size())
        {
            return Optional.absent();
        }
        
        // 返回排序结果
        return Optional.of(sorted.build());
    }
    
    /**
     * 排序方法
     * @param id__parents 有向图边集合
     * @return 排序后的顶点集合
     * @see [类、类#方法、类#成员]
     */
    public static <Id> Optional<ImmutableList<Id>> sort(ImmutableSetMultimap<Id, Id> id__parents)
    {
        
        ImmutableSet<Id> idSet = MultimapLib.keysAndValues(id__parents);
        
        return sort(idSet, id__parents);
    }
    
}
