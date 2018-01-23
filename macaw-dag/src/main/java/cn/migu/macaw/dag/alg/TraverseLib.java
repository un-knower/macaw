package cn.migu.macaw.dag.alg;

import java.util.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import cn.migu.macaw.dag.errors.NotImplemented;
import cn.migu.macaw.dag.util.LambdaLib.Fn1;

/**
 * 有向图遍历类
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年5月27日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class TraverseLib
{
    
    /**
     * 遍历方法
     * @param depthFirst 深度优先
     * @param inclusive  是否包含开始的节点
     * @param startIds 初始化节点
     * @param expand 节点映射函数
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static <Id> Iterable<Id> idIterable(final boolean depthFirst, final boolean inclusive,
        final ImmutableList<Id> startIds, final Fn1<Id, List<Id>> expand)
    {
        
        Fn1<Id, Id> lookup = new Fn1<Id, Id>()
        {
            @Override
            public Id apply(Id id)
            {
                return id;
            }
        };
        
        return new IterableClass<Id, Id>(depthFirst, inclusive, startIds, expand, lookup);
    }
    
    /**
     * 遍历方法
     * @param depthFirst 深度优先
     * @param inclusive  是否包含开始的节点
     * @param startIds 初始化节点
     * @param expand 节点映射函数
     * @return 节点集合迭代器
     * @see [类、类#方法、类#成员]
     */
    public static <Id, Node> Iterable<Node> nodeIterable(boolean depthFirst, boolean inclusive,
        ImmutableList<Id> startIds, Fn1<Node, List<Id>> expand, Fn1<Id, Node> lookup)
    {
        
        return new IterableClass<Id, Node>(depthFirst, inclusive, startIds, expand, lookup);
    }
    
    // ===========================================================================
    // IterableClass
    // ===========================================================================
    
    private static class IterableClass<Id, Node> implements Iterable<Node>
    {
        
        private final boolean depthFirst;
        
        private final boolean inclusive;
        
        private final ImmutableList<Id> startIds;
        
        private final Fn1<Node, List<Id>> expand;
        
        private final Fn1<Id, Node> lookup;
        
        public IterableClass(boolean depthFirst, boolean inclusive, ImmutableList<Id> startIds,
            Fn1<Node, List<Id>> expand, Fn1<Id, Node> lookup)
        {
            
            this.depthFirst = depthFirst;
            this.inclusive = inclusive;
            this.startIds = startIds;
            this.expand = expand;
            this.lookup = lookup;
        }
        
        @Override
        public Iterator<Node> iterator()
        {
            return TraverseLib.nodeIterator(depthFirst, inclusive, startIds, expand, lookup);
        }
        
    }
    
    // ===========================================================================
    // iterator
    // ===========================================================================
    
    /**
     * 图遍历迭代器
     * @param depthFirst 是否为深度优先,否则为广度优先
     * @param inclusive  是否包含开始节点
     * @param startIds   初始化节点集合
     * @param expand     节点隐射函数
     * @return 遍历节点迭代器
     * @see [类、类#方法、类#成员]
     */
    public static <Id> Iterator<Id> idIterator(boolean depthFirst, boolean inclusive, ImmutableList<Id> startIds,
        Fn1<Id, List<Id>> expand)
    {
        
        Fn1<Id, Id> lookup = new Fn1<Id, Id>()
        {
            @Override
            public Id apply(Id id)
            {
                return id;
            }
        };
        
        return new IteratorClass<Id, Id>(depthFirst, inclusive, startIds, expand, lookup);
    }
    
    /**
     * 
     * 图遍历迭代器
     * @param depthFirst
     * @param inclusive
     * @param startIds
     * @param expand
     * @param lookup
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static <Id, Node> Iterator<Node> nodeIterator(boolean depthFirst, boolean inclusive,
        ImmutableList<Id> startIds, Fn1<Node, List<Id>> expand, Fn1<Id, Node> lookup)
    {
        
        return new IteratorClass<Id, Node>(depthFirst, inclusive, startIds, expand, lookup);
    }
    
    // ===========================================================================
    // IteratorClass
    // ===========================================================================
    
    private static class IteratorClass<Id, Node> implements Iterator<Node>
    {
        
        // =================================
        // inputs
        // =================================
        
        private final boolean depthFirst;
        
        private final boolean inclusive;
        
        private final Fn1<Node, List<Id>> expand;
        
        private final Fn1<Id, Node> lookup;
        
        private final ImmutableSet<Id> startSet;
        
        // =================================
        // state
        // =================================
        
        // http://stackoverflow.com/a/25207657
        private final LinkedList<Id> open = new LinkedList<Id>();
        
        private final Set<Id> closed = new HashSet<>();
        
        private Node nextNode = null;
        
        // =================================
        // constructor
        // =================================
        
        public IteratorClass(boolean depthFirst, boolean inclusive, ImmutableList<Id> startIds,
            Fn1<Node, List<Id>> expand, Fn1<Id, Node> lookup)
        {
            
            this.depthFirst = depthFirst;
            this.inclusive = inclusive;
            this.expand = expand;
            this.lookup = lookup;
            
            startSet = ImmutableSet.copyOf(startIds);
            
            pushN(startIds);
        }
        
        // =================================
        // hasNext
        // =================================
        
        @Override
        public boolean hasNext()
        {
            
            findNext();
            
            return nextNode != null;
        }
        
        // =================================
        // next
        // =================================
        
        @Override
        public Node next()
        {
            
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }
            
            Node node = nextNode;
            
            nextNode = null;
            
            return node;
        }
        
        // =================================
        // findNext
        // =================================
        
        private void findNext()
        {
            
            while (nextNode == null && !open.isEmpty())
            {
                
                Id id = open.removeFirst();
                
                Node node = lookup.apply(id);
                
                expand(node);
                
                if (!inclusive && startSet.contains(id))
                {
                    continue;
                }
                
                nextNode = node;
            }
        }
        
        // =================================
        // expand
        // =================================
        
        private void expand(Node node)
        {
            
            List<Id> expanded = expand.apply(node);
            
            pushN(expanded);
        }
        
        // =================================
        // pushN
        // =================================
        
        private void pushN(List<Id> ids)
        {
            
            if (depthFirst)
            {    // 由于需要ids.first来作为open.first结束所以需要反序
                for (int i = ids.size() - 1; i >= 0; i--)
                {
                    push1(ids.get(i));
                }
            }
            else
            {    // 需要ids.first来作为open.last结束
                for (Id id : ids)
                {
                    push1(id);
                }
            }
        }
        
        // =================================
        // push1
        // =================================
        
        private void push1(Id id)
        {
            
            if (closed.contains(id))
            {
                return;
            }
            
            closed.add(id);
            
            if (depthFirst)
            {
                open.addFirst(id);
            }
            else
            {
                open.addLast(id);
            }
        }
        
        // =================================
        // remove
        // =================================
        
        @Override
        public void remove()
        {
            throw new NotImplemented();
        }
        
    }
}
