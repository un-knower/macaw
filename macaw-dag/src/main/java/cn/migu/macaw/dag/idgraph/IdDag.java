package cn.migu.macaw.dag.idgraph;

import java.util.Set;

import com.google.common.collect.ImmutableList;

/**
 * 有向图顶点集
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年5月27日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IdDag<Id> extends IdGraph<Id>
{
    
    // ===========================================================================
    // 顶点集
    // ===========================================================================
    
    @Override
    IdDag<Id> filterIdGraph(Set<Id> ids);
    
    // ===========================================================================
    // 祖先
    // ===========================================================================
    
    @Override
    IdDag<Id> ancestorIdGraph(Id id, boolean inclusive);
    
    @Override
    IdDag<Id> ancestorIdGraph(Set<Id> ids, boolean inclusive);
    
    // ===========================================================================
    // 后代
    // ===========================================================================
    
    @Override
    IdDag<Id> descendantIdGraph(Id id, boolean inclusive);
    
    @Override
    IdDag<Id> descendantIdGraph(Set<Id> ids, boolean inclusive);
    
    // ===========================================================================
    // 拓扑排序
    // ===========================================================================
    
    /**
     * 列表排序
     */
    ImmutableList<Id> topsortIdList();
    
    // ===========================================================================
    // 深度优先
    // ===========================================================================
    
    /**
     * 深度优先迭代
     */
    Iterable<Id> depthIdIterable();
    
    /**
     * 深度优先顶点集
     */
    ImmutableList<Id> depthIdList();
    
    // ===========================================================================
    // 广度优先
    // ===========================================================================
    
    /**
     * 广度优先顶点集迭代器
     */
    Iterable<Id> breadthIdIterable();
    
    /**
     * 广度优先顶点集
     */
    ImmutableList<Id> breadthIdList();
    
}
