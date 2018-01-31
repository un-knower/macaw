package cn.migu.macaw.dag.util;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Set数据结构工具类
 * 
 * @author soy
 */
public abstract class SetLib
{
    
    /**
     * 并集
     * 
     * @param sets 
     * @return 
     */
    @SafeVarargs
    public static <Item> Set<Item> union(Set<Item>... sets)
    {
        
        Set<Item> items = new HashSet<>();
        
        for (Set<Item> set : sets)
        {
            items = Sets.union(items, set);
        }
        
        return items;
    }
    
    /**
     * 集合相等判断
     * 
     * @param expected 期望的集合
     * @param provided 判定集合
     * @throws AssertionError
     */
    public static <Item> void assertEquals(Set<Item> expected, Set<Item> provided)
        throws AssertionError
    {
        
        if (expected.equals(provided))
        {
            return;
        }
        
        Set<Item> missing = Sets.difference(expected, provided);
        
        Set<Item> unexpected = Sets.difference(provided, expected);
        
        String message = "Sets were not equal.";
        
        if (!missing.isEmpty())
        {
            message += " Missing items: " + missing + ".";
        }
        
        if (!unexpected.isEmpty())
        {
            message += " Unexpected items: " + unexpected + ".";
        }
        
        throw new AssertionError(message);
        
    }
}
