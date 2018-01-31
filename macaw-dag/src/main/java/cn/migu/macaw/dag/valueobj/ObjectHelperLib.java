package cn.migu.macaw.dag.valueobj;

/**
 * 对象帮助类lib
 * 
 * @author soy
 */
public abstract class ObjectHelperLib
{
    
    /**
     * 创建 object helper.
     */
    public static ObjectHelper helper(Class<?> objectClass, Object... fieldNamesAndValues)
    {
        return new ObjectHelperClass(objectClass, fieldNamesAndValues);
    }
    
    // ===========================================================================
    
    /**
     * 对象属性是否一致断言
     * @param a
     * @param b
     * @see [类、类#方法、类#成员]
     */
    public static void assertStateEquals(HasObjectHelper a, HasObjectHelper b)
    {
        a.objectHelper().assertStateEquals(b);
    }
    
    /**
     * 对象是否有不一致属性断言
     * @param a
     * @param b
     * @see [类、类#方法、类#成员]
     */
    public static void assertStateNotEquals(HasObjectHelper a, HasObjectHelper b)
    {
        a.objectHelper().assertStateNotEquals(b);
    }
    
}
