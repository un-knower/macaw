package cn.migu.macaw.dag.idgraph;

import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

import cn.migu.macaw.dag.util.LambdaLib.Fn1;

/**
 * 有向图数据结构
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年5月27日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IdGraph<Id>
{
    
    // ===========================================================================
    // 顶点集
    // ===========================================================================
    
    /**
     * 顶点集合
     */
    ImmutableSet<Id> idSet();
    
    /**
     * 顶点数
     */
    int idSize();
    
    /**
     * 顶点集相等判断
     */
    void assertIdsEqual(ImmutableSet<Id> ids);
    
    /**
     * 顶点集相等判断
     */
    void assertIdsEqual(Id[] ids);
    
    /**
     * 顶点及关系过滤,仅保留指定顶点和边
     */
    IdGraph<Id> filterIdGraph(Set<Id> ids);
    
    // ===========================================================================
    // 父节点关系
    // ===========================================================================
    
    /**
     * 父关系判定
     */
    boolean isParentOf(Id id, Id potentialChild);
    
    /**
     * 所有顶点的父节点集合
     */
    ImmutableSetMultimap<Id, Id> id__parentIds();
    
    /**
     * 指定节点的父节点集合
     */
    ImmutableSet<Id> parentIdSet(Id id);
    
    // ===========================================================================
    // 子节点关系
    // ===========================================================================
    
    /**
     * 子节点关系判定
     */
    boolean isChildOf(Id id, Id potentialParent);
    
    /**
     * 所有节点的子节点集合
     */
    ImmutableSetMultimap<Id, Id> id__childIds();
    
    /**
     * 指定节点的子节点集合
     */
    ImmutableSet<Id> childIdSet(Id id);
    
    // ===========================================================================
    // 祖先
    // ===========================================================================
    
    /**
     * 祖先节点关系判定
     */
    boolean isAncestorOf(Id id, Id potentialDescendant, boolean inclusive);
    
    /**
     * 指定节点的祖先节点的迭代器
     */
    Iterable<Id> ancestorIdIterable(Id id, boolean inclusive);
    
    /**
     * 指定节点集合的祖先节点的迭代器
     */
    Iterable<Id> ancestorIdIterable(Set<Id> ids, boolean inclusive);
    
    /**
     * 指定节点的所有祖先节点的节点集合
     * @param id 指定节点
     * @param inclusive 返回集合是否包含指定节点
     * @return
     * @see [类、类#方法、类#成员]
     */
    ImmutableSet<Id> ancestorIdSet(Id id, boolean inclusive);
    
    /**
     * 指定节点集合的所有祖先节点集
     * @param ids 指定节点集合
     * @param inclusive 是否包含指定节点集合
     * @return
     * @see [类、类#方法、类#成员]
     */
    ImmutableSet<Id> ancestorIdSet(Set<Id> ids, boolean inclusive);
    
    /**
     * 指定节点的所有祖先节点的构成的有向图
     * @param id 指定节点
     * @param inclusive 是否包含指定节点
     * @return
     * @see [类、类#方法、类#成员]
     */
    IdGraph<Id> ancestorIdGraph(Id id, boolean inclusive);
    
    /**
     * 指定节点集的所有祖先节点的构成的有向图
     * @param ids 指定节点集
     * @param inclusive 是否包含指定节点集
     * @return
     * @see [类、类#方法、类#成员]
     */
    IdGraph<Id> ancestorIdGraph(Set<Id> ids, boolean inclusive);
    
    // ===========================================================================
    // 后代
    // ===========================================================================
    
    /**
     * 判断一节点是否为另一节点的后代
     * @param id
     * @param potentialAncestor
     * @param inclusive
     * @return
     * @see [类、类#方法、类#成员]
     */
    boolean isDescendantOf(Id id, Id potentialAncestor, boolean inclusive);
    
    /**
     * 指定节点的所有后代节点构成的迭代器
     * @param id 指定节点
     * @param inclusive 是否包含指定节点
     * @return
     * @see [类、类#方法、类#成员]
     */
    Iterable<Id> descendantIdIterable(Id id, boolean inclusive);
    
    /**
     * 指定节点集的所有后代节点构成的迭代器
     * @param ids 指定节点集
     * @param inclusive 是否包含指定节点集
     * @return
     * @see [类、类#方法、类#成员]
     */
    Iterable<Id> descendantIdIterable(Set<Id> ids, boolean inclusive);
    
    /**
     * 指定节点的所有后代节点集
     * @param id 指定节点
     * @param inclusive 是否包含指定节点
     * @return
     * @see [类、类#方法、类#成员]
     */
    ImmutableSet<Id> descendantIdSet(Id id, boolean inclusive);
    
    /**
     * 指定节点集的所有后代节点集
     * @param ids 指定节点集
     * @param inclusive 是否包含指定节点集
     * @return
     * @see [类、类#方法、类#成员]
     */
    ImmutableSet<Id> descendantIdSet(Set<Id> ids, boolean inclusive);
    
    /**
     * 指定节点的所有后代节点构成图
     * @param id 指定节点
     * @param inclusive 是否包含指定节点
     * @return
     * @see [类、类#方法、类#成员]
     */
    IdGraph<Id> descendantIdGraph(Id id, boolean inclusive);
    
    /**
     * 指定节点集的所有后代节点构成图
     * @param ids 指定节点集合
     * @param inclusive 是否包含指定节点集合
     * @return
     * @see [类、类#方法、类#成员]
     */
    IdGraph<Id> descendantIdGraph(Set<Id> ids, boolean inclusive);
    
    // ===========================================================================
    // roots (sources)
    // ===========================================================================
    
    /**
     * 是否为根节点
     * @param id
     * @return
     * @see [类、类#方法、类#成员]
     */
    boolean isRoot(Id id);
    
    /**
     * 有向图的所有根节点集合
     * @return
     * @see [类、类#方法、类#成员]
     */
    ImmutableSet<Id> rootIdSet();
    
    // ===========================================================================
    // 叶子节点
    // ===========================================================================
    
    /**
     * 是否为叶子节点
     * @param id
     * @return
     * @see [类、类#方法、类#成员]
     */
    boolean isLeaf(Id id);
    
    /**
     * 有向图中所有叶子节点集合
     * @return
     * @see [类、类#方法、类#成员]
     */
    ImmutableSet<Id> leafIdSet();
    
    // ===========================================================================
    // 拓扑排序
    // ===========================================================================
    
    /**
     * 是否存在环路
     * @return
     * @see [类、类#方法、类#成员]
     */
    boolean containsCycle();
    
    /**
     * 拓扑排序后生成的节点序列集合,如果存在环则返回absent
     * @return
     * @see [类、类#方法、类#成员]
     */
    Optional<ImmutableList<Id>> optionalTopsortIdList();
    
    // ===========================================================================
    // 遍历
    // ===========================================================================
    
    /**
     * 遍历并返回节点集合的迭代器
     * @param depthFirst true为深搜,false为广搜
     * @param inclusive 在遍历时是否包含startId
     * @param startId 初始节点
     * @param expand 节点间映射关系
     * @return 遍历过的节点的迭代器
     * @see [类、类#方法、类#成员]
     */
    Iterable<Id> traverseIdIterable(boolean depthFirst, boolean inclusive, Id startId, Fn1<Id, List<Id>> expand);
    
    /**
     * 遍历并返回节点集合的迭代器
     * @param depthFirst true为深搜,false为广搜
     * @param inclusive 在遍历时是否包含startIds
     * @param startIds 初始节点集合
     * @param expand 节点间映射关系
     * @return
     * @see [类、类#方法、类#成员]
     */
    Iterable<Id> traverseIdIterable(boolean depthFirst, boolean inclusive, ImmutableList<Id> startIds,
        Fn1<Id, List<Id>> expand);
    
    /**
     * 遍历并返回节点集合的列表
     * @param depthFirst true为深搜,false为广搜
     * @param inclusive 在遍历时是否包含startId
     * @param startId 初始节点
     * @param expand 节点间映射关系
     * @return 遍历过的节点的列表
     * @see [类、类#方法、类#成员]
     */
    ImmutableList<Id> traverseIdList(boolean depthFirst, boolean inclusive, Id startId, Fn1<Id, List<Id>> expand);
    
    /**
     * 遍历并返回节点集合的列表
     * @param depthFirst true为深搜,false为广搜
     * @param inclusive 在遍历时是否包含startIds
     * @param startIds 初始节点集合
     * @param expand 节点间映射关系
     * @return
     * @see [类、类#方法、类#成员]
     */
    ImmutableList<Id> traverseIdList(boolean depthFirst, boolean inclusive, ImmutableList<Id> startIds,
        Fn1<Id, List<Id>> expand);
    
}
