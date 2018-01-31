package cn.migu.macaw.dag.idgraph;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;

import cn.migu.macaw.dag.util.MultimapLib;

/**
 * 有向图创建库
 * 
 * @author soy
 */
public abstract class IdDagLib
{
    
    // ===========================================================================
    // 根据parent关系创建
    // ===========================================================================
    
    /**
     * 根据顶点集合和关系创建有向图
     * @param idSet 顶点集合
     * @param id__parentIds 关系
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static <Id> IdDag<Id> fromParentMap(Set<Id> idSet, Multimap<Id, Id> id__parentIds)
    {
        return new IdDagClass<Id>(ImmutableSet.copyOf(idSet), ImmutableSetMultimap.copyOf(id__parentIds));
    }
    
    /**
     * 根据顶点关系创建有向图,假设所有顶点已在关系中表示
     * @param id__parentIds 关系
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static <Id> IdDag<Id> fromParentMap(Multimap<Id, Id> id__parentIds)
    {
        return fromParentMap(MultimapLib.keysAndValues(id__parentIds), id__parentIds);
    }
    
    /**
     * 根据顶点和父节点的关系创建图
     * @param idSet 顶点集
     * @param alternatingIdsAndParentIds 关系
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SafeVarargs
    public static <Id> IdDag<Id> fromParentMap(Set<Id> idSet, Id... alternatingIdsAndParentIds)
    {
        return fromParentMap(idSet, MultimapLib.of(alternatingIdsAndParentIds));
    }
    
    /**
     * 根据父节点关系创建有向图
     * @param alternatingIdsAndParentIds 关系
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SafeVarargs
    public static <Id> IdDag<Id> fromParentMap(Id... alternatingIdsAndParentIds)
    {
        return fromParentMap(MultimapLib.of(alternatingIdsAndParentIds));
    }
    
    // ===========================================================================
    // 根据子节点关系创建有向图
    // ===========================================================================
    
    /**
     * 根据节点集合与子节点关系创建有向图
     * @param idSet
     * @param id__childIds
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static <Id> IdDag<Id> fromChildMap(Set<Id> idSet, Multimap<Id, Id> id__childIds)
    {
        
        ImmutableSetMultimap<Id, Id> id__parentIds = ImmutableSetMultimap.copyOf(id__childIds).inverse();
        
        return fromParentMap(idSet, id__parentIds);
    }
    
    /**
     * 根据子节点关系创建有向图
     * @param id__childIds 关系
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static <Id> IdDag<Id> fromChildMap(Multimap<Id, Id> id__childIds)
    {
        return fromChildMap(MultimapLib.keysAndValues(id__childIds), id__childIds);
    }
    
    /**
     * 根据节点集合与子节点关系创建有向图
     * @param idSet 顶点集合
     * @param alternatingIdsAndChildIds 关系
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SafeVarargs
    public static <Id> IdDag<Id> fromChildMap(Set<Id> idSet, Id... alternatingIdsAndChildIds)
    {
        return fromChildMap(idSet, MultimapLib.of(alternatingIdsAndChildIds));
    }
    
    /**
     * 根据子节点的关系创建有向图
     * @param alternatingIdsAndChildIds 关系
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SafeVarargs
    public static <Id> IdDag<Id> fromChildMap(Id... alternatingIdsAndChildIds)
    {
        return fromChildMap(MultimapLib.of(alternatingIdsAndChildIds));
    }
    
}
