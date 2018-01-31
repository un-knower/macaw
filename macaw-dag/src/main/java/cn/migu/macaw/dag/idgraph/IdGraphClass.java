package cn.migu.macaw.dag.idgraph;

import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.*;

import cn.migu.macaw.dag.alg.TopsortLib;
import cn.migu.macaw.dag.alg.TraverseLib;
import cn.migu.macaw.dag.errors.GraphHadUnexpectedIds;
import cn.migu.macaw.dag.util.MultimapLib;
import cn.migu.macaw.dag.util.SetLib;
import cn.migu.macaw.dag.util.LambdaLib.Fn1;
import cn.migu.macaw.dag.valueobj.ValueMixin;

/**
 * 有向图实现
 * 
 * @author soy
 */
public class IdGraphClass<Id> extends ValueMixin implements IdGraph<Id>
{
    
    // ===========================================================================
    // 状态
    // ===========================================================================
    
    @Override
    public Object[] fields()
    {
        return array("idSet", idSet(), "id__parentIds", id__parentIds());
    }
    
    // ===========================================================================
    // 构造方法
    // ===========================================================================
    
    public IdGraphClass(ImmutableSet<Id> idSet, ImmutableSetMultimap<Id, Id> id__parentIds)
    {
        
        this.idSet = idSet;
        this.id__parentIds = id__parentIds;
        
        validate();
    }
    
    // ===========================================================================
    // 有效性验证
    // ===========================================================================
    
    private void validate()
        throws GraphHadUnexpectedIds
    {
        
        ImmutableSet<Id> mapIds = MultimapLib.keysAndValues(id__parentIds());
        
        Set<Id> unexpectedIds = Sets.difference(mapIds, idSet());
        
        if (!unexpectedIds.isEmpty())
        {
            throw new GraphHadUnexpectedIds("unexpectedIds = %s, mapIds = %s, idSet = %s", unexpectedIds, mapIds,
                idSet());
        }
    }
    
    // ===========================================================================
    // 节点集
    // ===========================================================================
    
    @Override
    public ImmutableSet<Id> idSet()
    {
        return idSet;
    }
    
    private ImmutableSet<Id> idSet;
    
    // ===================================
    
    @Override
    public int idSize()
    {
        return idSet().size();
    }
    
    // ===================================
    
    @Override
    public void assertIdsEqual(ImmutableSet<Id> ids)
    {
        SetLib.assertEquals(idSet(), ids);
    }
    
    @Override
    public void assertIdsEqual(Id[] ids)
    {
        assertIdsEqual(ImmutableSet.copyOf(ids));
    }
    
    // ===================================
    
    @Override
    public IdGraph<Id> filterIdGraph(Set<Id> ids)
    {
        return IdGraphLib.fromParentMap(ids, filterParentMap(ids));
    }
    
    // ===========================================================================
    // 父节点操作
    // ===========================================================================
    
    @Override
    public boolean isParentOf(Id id, Id potentialChild)
    {
        return childIdSet(id).contains(potentialChild);
    }
    
    // ===================================
    
    @Override
    public ImmutableSetMultimap<Id, Id> id__parentIds()
    {
        return id__parentIds;
    }
    
    private ImmutableSetMultimap<Id, Id> id__parentIds;
    
    // ===================================
    
    @Override
    public ImmutableSet<Id> parentIdSet(Id id)
    {
        return id__parentIds().get(id);
    }
    
    // ===================================
    
    protected Fn1<Id, List<Id>> parentIdListLambda()
    {
        if (parentIdListLambda == null)
        {
            parentIdListLambda = new Fn1<Id, List<Id>>()
            {
                @Override public List<Id> apply(Id id)
                {
                    // 不确定的顺序
                    return ImmutableList.copyOf(parentIdSet(id));
                }
            };
        }
        
        return parentIdListLambda;
    }
    
    private Fn1<Id, List<Id>> parentIdListLambda;
    
    // ===================================
    
    protected SetMultimap<Id, Id> filterParentMap(final Set<Id> ids)
    {
        return MultimapLib.filterKeysAndValues(id__parentIds(), new Predicate<Id>()
        {
            @Override
            public boolean apply(Id id)
            {
                return ids.contains(id);
            }
        });
    }
    
    // ===========================================================================
    // 子节点操作
    // ===========================================================================
    
    @Override
    public boolean isChildOf(Id id, Id potentialParent)
    {
        return parentIdSet(id).contains(potentialParent);
    }
    
    // ===================================
    
    @Override
    public ImmutableSetMultimap<Id, Id> id__childIds()
    {
        if (id__childIds == null)
        {
            
            ImmutableSetMultimap.Builder<Id, Id> builder = ImmutableSetMultimap.builder();
            
            for (Id id : idSet())
            {
                for (Id parentId : id__parentIds().get(id))
                {
                    builder.put(parentId, id);
                }
            }
            
            id__childIds = builder.build();
        }
        
        return id__childIds;
    }
    
    private ImmutableSetMultimap<Id, Id> id__childIds;
    
    // ===================================
    
    @Override
    public ImmutableSet<Id> childIdSet(Id id)
    {
        return id__childIds().get(id);
    }
    
    // ===================================
    
    protected Fn1<Id, List<Id>> childIdListLambda()
    {
        if (childIdListLambda == null)
        {
            childIdListLambda = new Fn1<Id, List<Id>>()
            {
                @Override public List<Id> apply(Id id)
                {
                    // 不确定顺序
                    return ImmutableList.copyOf(childIdSet(id));
                }
            };
        }
        
        return childIdListLambda;
    }
    
    private Fn1<Id, List<Id>> childIdListLambda;
    
    // ===========================================================================
    // 祖先节点操作
    // ===========================================================================
    
    @Override
    public boolean isAncestorOf(Id id, Id potentialDescendant, boolean inclusive)
    {
        return isDescendantOf(potentialDescendant, id, inclusive);
    }
    
    @Override
    public Iterable<Id> ancestorIdIterable(Id id, boolean inclusive)
    {
        return traverseIdIterable(true, inclusive, id, parentIdListLambda());
    }
    
    @Override
    public Iterable<Id> ancestorIdIterable(Set<Id> ids, boolean inclusive)
    {
        return traverseIdIterable(true, inclusive, ImmutableList.copyOf(ids), parentIdListLambda());
    }
    
    @Override
    public ImmutableSet<Id> ancestorIdSet(Id id, boolean inclusive)
    {
        return ImmutableSet.copyOf(ancestorIdIterable(id, inclusive));
    }
    
    @Override
    public ImmutableSet<Id> ancestorIdSet(Set<Id> ids, boolean inclusive)
    {
        return ImmutableSet.copyOf(ancestorIdIterable(ids, inclusive));
    }
    
    @Override
    public IdGraph<Id> ancestorIdGraph(Id id, boolean inclusive)
    {
        return ancestorIdGraph(ImmutableSet.of(id), inclusive);
    }
    
    @Override
    public IdGraph<Id> ancestorIdGraph(Set<Id> ids, boolean inclusive)
    {
        return filterIdGraph(ancestorIdSet(ids, inclusive));
    }
    
    // ===========================================================================
    // 后代节点操作
    // ===========================================================================
    
    @Override
    public boolean isDescendantOf(Id id, Id potentialAncestor, boolean inclusive)
    {
        return ancestorIdSet(id, inclusive).contains(potentialAncestor);
    }
    
    @Override
    public Iterable<Id> descendantIdIterable(Id id, boolean inclusive)
    {
        return traverseIdIterable(true, inclusive, id, childIdListLambda());
    }
    
    @Override
    public Iterable<Id> descendantIdIterable(Set<Id> ids, boolean inclusive)
    {
        return traverseIdIterable(true, inclusive, ImmutableList.copyOf(ids), childIdListLambda());
    }
    
    @Override
    public ImmutableSet<Id> descendantIdSet(Id id, boolean inclusive)
    {
        return ImmutableSet.copyOf(descendantIdIterable(id, inclusive));
    }
    
    @Override
    public ImmutableSet<Id> descendantIdSet(Set<Id> ids, boolean inclusive)
    {
        return ImmutableSet.copyOf(descendantIdIterable(ids, inclusive));
    }
    
    @Override
    public IdGraph<Id> descendantIdGraph(Id id, boolean inclusive)
    {
        return descendantIdGraph(ImmutableSet.of(id), inclusive);
    }
    
    @Override
    public IdGraph<Id> descendantIdGraph(Set<Id> ids, boolean inclusive)
    {
        return filterIdGraph(descendantIdSet(ids, inclusive));
    }
    
    // ===========================================================================
    // 根节点操作
    // ===========================================================================
    
    @Override
    public boolean isRoot(Id id)
    {
        return rootIdSet().contains(id);
    }
    
    // ===================================
    
    @Override
    public ImmutableSet<Id> rootIdSet()
    {
        if (rootIds == null)
        {
            
            ImmutableSet.Builder<Id> builder = ImmutableSet.builder();
            
            for (Id id : idSet)
            {
                if (parentIdSet(id).isEmpty())
                {
                    builder.add(id);
                }
            }
            
            rootIds = builder.build();
        }
        
        return rootIds;
    }
    
    private ImmutableSet<Id> rootIds;
    
    // ===========================================================================
    // 叶子节点操作
    // ===========================================================================
    
    @Override
    public boolean isLeaf(Id id)
    {
        return leafIdSet().contains(id);
    }
    
    // ===================================
    
    @Override
    public ImmutableSet<Id> leafIdSet()
    {
        if (leafIds == null)
        {
            
            ImmutableSet.Builder<Id> builder = ImmutableSet.builder();
            
            for (Id id : idSet)
            {
                if (childIdSet(id).isEmpty())
                {
                    builder.add(id);
                }
            }
            
            leafIds = builder.build();
        }
        
        return leafIds;
    }
    
    private ImmutableSet<Id> leafIds;
    
    // ===========================================================================
    // 拓扑排序
    // ===========================================================================
    
    @Override
    public Optional<ImmutableList<Id>> optionalTopsortIdList()
    {
        if (optionalTopsortIdList == null)
        {
            optionalTopsortIdList = TopsortLib.sort(idSet(), id__parentIds());
        }
        return optionalTopsortIdList;
    }
    
    private Optional<ImmutableList<Id>> optionalTopsortIdList;
    
    // ===================================
    
    @Override
    public boolean containsCycle()
    {
        return !optionalTopsortIdList().isPresent();
    }
    
    // ===========================================================================
    // 遍历操作
    // ===========================================================================
    
    @Override
    public Iterable<Id> traverseIdIterable(boolean depthFirst, boolean inclusive, Id startId, Fn1<Id, List<Id>> expand)
    {
        
        return traverseIdIterable(depthFirst, inclusive, ImmutableList.of(startId), expand);
    }
    
    @Override
    public Iterable<Id> traverseIdIterable(boolean depthFirst, boolean inclusive, ImmutableList<Id> startIds,
        Fn1<Id, List<Id>> expand)
    {
        
        return TraverseLib.idIterable(depthFirst, inclusive, startIds, expand);
    }
    
    @Override
    public ImmutableList<Id> traverseIdList(boolean depthFirst, boolean inclusive, Id startId, Fn1<Id, List<Id>> expand)
    {
        
        return traverseIdList(depthFirst, inclusive, ImmutableList.of(startId), expand);
    }
    
    @Override
    public ImmutableList<Id> traverseIdList(boolean depthFirst, boolean inclusive, ImmutableList<Id> startIds,
        Fn1<Id, List<Id>> expand)
    {
        
        return ImmutableList.copyOf(traverseIdIterable(depthFirst, inclusive, startIds, expand));
    }
    
}
