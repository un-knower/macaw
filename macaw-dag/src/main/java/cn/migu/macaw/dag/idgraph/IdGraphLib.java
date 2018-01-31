package cn.migu.macaw.dag.idgraph;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;

import cn.migu.macaw.dag.util.MultimapLib;

/**
 * 创建有向图的lib
 * 
 * @author soy
 */
public abstract class IdGraphLib
{
    
    // ===========================================================================
    // 根据指定父节点关系创建
    // ===========================================================================
    
    /**
     * 根据节点集合和父节点的关系创建有向图
     * @param idSet 节点集合
     * @param id__parentIds 与父节点的关系
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static <Id> IdGraph<Id> fromParentMap(Set<Id> idSet, Multimap<Id, Id> id__parentIds)
    {
        return new IdGraphClass<Id>(ImmutableSet.copyOf(idSet), ImmutableSetMultimap.copyOf(id__parentIds));
    }
    
    /**
     * 根据父节点的关系创建有向图
     * 所有节点集都在关系中表示
     * @param id__parentIds 关系
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static <Id> IdGraph<Id> fromParentMap(Multimap<Id, Id> id__parentIds)
    {
        return fromParentMap(MultimapLib.keysAndValues(id__parentIds), id__parentIds);
    }
    
    /**
     * 根据节点集合和父节点关系创建有向图
     * @param idSet
     * @param alternatingIdsAndParentIds
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SafeVarargs
    public static <Id> IdGraph<Id> fromParentMap(Set<Id> idSet, Id... alternatingIdsAndParentIds)
    {
        return fromParentMap(idSet, MultimapLib.of(alternatingIdsAndParentIds));
    }
    
    /**
     * 根据父节点关系创建有向图
     * @param alternatingIdsAndParentIds
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SafeVarargs
    public static <Id> IdGraph<Id> fromParentMap(Id... alternatingIdsAndParentIds)
    {
        return fromParentMap(MultimapLib.of(alternatingIdsAndParentIds));
    }
    
    // ===========================================================================
    // 根据子节点关系创建有向图
    // ===========================================================================
    
    /**
     * 根据节点集合和子节点的关系创建有向图
     * @param idSet
     * @param id__childIds
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static <Id> IdGraph<Id> fromChildMap(Set<Id> idSet, Multimap<Id, Id> id__childIds)
    {
        
        ImmutableSetMultimap<Id, Id> id__parentIds = ImmutableSetMultimap.copyOf(id__childIds).inverse();
        
        return fromParentMap(idSet, id__parentIds);
    }
    
    /**
     * 根据与子节点的关系创建有向图
     * 图中所有节点都在关系中呈现
     * @param id__childIds
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static <Id> IdGraph<Id> fromChildMap(Multimap<Id, Id> id__childIds)
    {
        return fromChildMap(MultimapLib.keysAndValues(id__childIds), id__childIds);
    }
    
    /**
     * 根据节点集合和节点关系创建有向图
     * @param idSet 节点集合
     * @param alternatingIdsAndChildIds 按节点-子节点-节点-子节点...顺序出现
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SafeVarargs
    public static <Id> IdGraph<Id> fromChildMap(Set<Id> idSet, Id... alternatingIdsAndChildIds)
    {
        return fromChildMap(idSet, MultimapLib.of(alternatingIdsAndChildIds));
    }
    
    /**
     * 根据节点之间关系创建有向图
     * @param alternatingIdsAndChildIds 按节点-子节点-节点-子节点...顺序出现
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SafeVarargs
    public static <Id> IdGraph<Id> fromChildMap(Id... alternatingIdsAndChildIds)
    {
        return fromChildMap(MultimapLib.of(alternatingIdsAndChildIds));
    }
    
}
