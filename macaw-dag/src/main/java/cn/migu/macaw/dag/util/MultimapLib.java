package cn.migu.macaw.dag.util;

import java.util.Map.Entry;

import com.google.common.base.Predicate;
import com.google.common.collect.*;

/**
 * Multimap工具类
 */
public abstract class MultimapLib
{
    
    /**
     * 创建multimap
     * 
     * @param entries keg-value顺序交替出现
     * @return The multimap
     */
    @SafeVarargs
    public static <Item> ImmutableSetMultimap<Item, Item> of(Item... entries)
    {
        
        CollectLib.assertSizeIsEven(entries);
        
        ImmutableSetMultimap.Builder<Item, Item> key__values = ImmutableSetMultimap.builder();
        
        for (int i = 0; i < entries.length; i += 2)
        {
            
            Item key = entries[i];
            Item value = entries[i + 1];
            
            key__values.put(key, value);
        }
        
        return key__values.build();
    }
    
    /**
     * multimap的键值集合
     * 
     * @param key__values The multimap.
     * @return 键值集合
     */
    public static <Item> ImmutableSet<Item> keysAndValues(Multimap<Item, Item> key__values)
    {
        
        ImmutableSet.Builder<Item> items = ImmutableSet.builder();
        
        items.addAll(key__values.keySet());
        
        items.addAll(key__values.values());
        
        return items.build();
    }
    
    /**
     * 过滤获取指定键值的multimap的键值集合
     * 
     * @param key__values multimap.
     * @param predicate 过滤条件
     * @return 过滤后的multimap
     */
    public static <Item> SetMultimap<Item, Item> filterKeysAndValues(SetMultimap<Item, Item> key__values,
        final Predicate<Item> predicate)
    {
        
        return Multimaps.filterEntries(key__values, new Predicate<Entry<Item, Item>>()
        {
            @Override
            public boolean apply(Entry<Item, Item> entry)
            {
                
                if (!predicate.apply(entry.getKey()))
                {
                    return false;
                }
                
                if (!predicate.apply(entry.getValue()))
                {
                    return false;
                }
                
                return true;
            }
        });
    }
    
}
