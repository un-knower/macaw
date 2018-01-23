package cn.migu.macaw.dag.util;

import java.util.Collection;

/**
 * 集合工具类
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年5月30日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class CollectLib
{
    
    // ===========================================================================
    // 判空操作
    // ===========================================================================
    
    public static <Item> void assertNotEmpty(Item[] items)
    {
        if (items.length == 0)
        {
            throw new AssertionError("array was empty");
        }
    }
    
    public static <Item> void assertNotEmpty(Collection<Item> items)
    {
        if (items.isEmpty())
        {
            throw new AssertionError("collection was empty");
        }
    }
    
    public static <Item> void assertIsEmpty(Item[] items)
    {
        if (items.length != 0)
        {
            throw new AssertionError("array not empty");
        }
    }
    
    public static <Item> void assertIsEmpty(Collection<Item> items)
    {
        if (!items.isEmpty())
        {
            throw new AssertionError("collection not empty");
        }
    }
    
    // ===========================================================================
    // 长度奇偶判断
    // ===========================================================================
    
    public static <Item> boolean isSizeEven(Item[] items)
    {
        return NumLib.isEven(items.length);
    }
    
    public static <Item> boolean isSizeEven(Collection<Item> items)
    {
        return NumLib.isEven(items.size());
    }
    
    public static <Item> boolean isSizeOdd(Item[] items)
    {
        return NumLib.isOdd(items.length);
    }
    
    public static <Item> boolean isSizeOdd(Collection<Item> items)
    {
        return NumLib.isOdd(items.size());
    }
    
    public static <Item> void assertSizeIsEven(Item[] items)
    {
        if (!isSizeEven(items))
        {
            throw new AssertionError("array size was not even, size was " + items.length);
        }
    }
    
    public static <Item> void assertSizeIsEven(Collection<Item> items)
    {
        if (!isSizeEven(items))
        {
            throw new AssertionError("collection size was not even, size was " + items.size());
        }
    }
    
    public static <Item> void assertSizeIsOdd(Item[] items)
    {
        if (!isSizeOdd(items))
        {
            throw new AssertionError("array size was not odd, size was " + items.length);
        }
    }
    
    public static <Item> void assertSizeIsOdd(Collection<Item> items)
    {
        if (!isSizeOdd(items))
        {
            throw new AssertionError("collection size was not odd, size was " + items.size());
        }
    }
    
}
