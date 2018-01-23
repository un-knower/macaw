package cn.migu.macaw.dag.util;

/**
 * 
 * 数字通用工具类
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年5月30日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class NumLib
{
    
    // 如有需要可尝试位操作实现
    // http://stackoverflow.com/questions/7342237/check-whether-number-is-even-or-odd
    
    /**
     * 是否为偶数
     * @param num
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isEven(int num)
    {
        return num % 2 == 0;
    }
    
    /**
     * 是否为奇数
     * @param num
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static boolean isOdd(int num)
    {
        return num % 2 != 0;
    }
    
}
