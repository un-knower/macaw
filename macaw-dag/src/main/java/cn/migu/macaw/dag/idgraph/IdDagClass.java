package cn.migu.macaw.dag.idgraph;

import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

import cn.migu.macaw.dag.errors.DagCannotHaveCycle;

/**
 * 有向图实现
 * 
 * @author soy
 */
public class IdDagClass<Id> extends IdGraphClass<Id> implements IdDag<Id>
{
    
    // ===========================================================================
    // 构造函数
    // ===========================================================================
    
    public IdDagClass(ImmutableSet<Id> idSet, ImmutableSetMultimap<Id, Id> id__parentIds)
    {
        
        super(idSet, id__parentIds);
        
        validate();
    }
    
    // ===========================================================================
    // 验证
    // ===========================================================================
    
    private void validate()
        throws DagCannotHaveCycle
    {
        
        if (containsCycle())
        {
            throw new DagCannotHaveCycle();
        }
    }
    
    // ===========================================================================
    // 顶点集
    // ===========================================================================
    
    @Override
    public IdDag<Id> filterIdGraph(Set<Id> ids)
    {
        return IdDagLib.fromParentMap(ids, filterParentMap(ids));
    }
    
    // ===========================================================================
    // 祖先
    // ===========================================================================
    
    @Override
    public IdDag<Id> ancestorIdGraph(Id id, boolean inclusive)
    {
        return ancestorIdGraph(ImmutableSet.of(id), inclusive);
    }
    
    @Override
    public IdDag<Id> ancestorIdGraph(Set<Id> ids, boolean inclusive)
    {
        return filterIdGraph(ancestorIdSet(ids, inclusive));
    }
    
    // ===========================================================================
    // 后代
    // ===========================================================================
    
    @Override
    public IdDag<Id> descendantIdGraph(Id id, boolean inclusive)
    {
        return descendantIdGraph(ImmutableSet.of(id), inclusive);
    }
    
    @Override
    public IdDag<Id> descendantIdGraph(Set<Id> ids, boolean inclusive)
    {
        return filterIdGraph(descendantIdSet(ids, inclusive));
    }
    
    // ===========================================================================
    // 拓扑排序
    // ===========================================================================
    
    @Override
    public ImmutableList<Id> topsortIdList()
    {
        return optionalTopsortIdList().get();
    }
    
    // ===========================================================================
    // 深度优先
    // ===========================================================================
    
    @Override
    public Iterable<Id> depthIdIterable()
    {
        return traverseIdIterable(true, true, ImmutableList.copyOf(rootIdSet()), childIdListLambda());
    }
    
    // ===================================
    
    @Override
    public ImmutableList<Id> depthIdList()
    {
        if (depthIdList == null)
        {
            depthIdList = ImmutableList.copyOf(depthIdIterable());
        }
        return depthIdList;
    }
    
    private ImmutableList<Id> depthIdList;
    
    // ===========================================================================
    // 广度优先
    // ===========================================================================
    
    @Override
    public Iterable<Id> breadthIdIterable()
    {
        return traverseIdIterable(false, true, ImmutableList.copyOf(rootIdSet()), childIdListLambda());
    }
    
    // ===================================
    
    @Override
    public ImmutableList<Id> breadthIdList()
    {
        if (breadthIdList == null)
        {
            breadthIdList = ImmutableList.copyOf(breadthIdIterable());
        }
        return breadthIdList;
    }
    
    private ImmutableList<Id> breadthIdList;
    
}
