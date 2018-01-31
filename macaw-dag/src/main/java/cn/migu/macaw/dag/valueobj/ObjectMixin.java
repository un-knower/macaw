package cn.migu.macaw.dag.valueobj;

/**
 * 引用对象
 * 
 * @author soy
 */
public abstract class ObjectMixin implements HasObjectHelper
{
    
    // ===========================================================================
    // 抽象回调接口
    // ===========================================================================
    
    /**
     * 是对象引用还是值
     */
    protected abstract boolean isEntity();
    
    /**
     * 属性和值的数组
     */
    protected abstract Object[] fields();
    
    // ===========================================================================
    // 通用方法
    // ===========================================================================
    
    /**
     * Convenience method so subclasses can implement fields() by returning array("field1", field1,
     * "field2", field2, ...). The longer alternative is to return new Object[]{"field1", field1,
     * "field2", field2, ...}.
     * 
     * http://rethinktheworld.blogspot.com/2010/06/literal-arrays-and-lists-in-java.html
     */
    protected Object[] array(Object... items)
    {
        return items;
    }
    
    // ===========================================================================
    // 创建 objectHelper
    // ===========================================================================
    
    @Override
    public ObjectHelper objectHelper()
    {
        
        if (isEntity())
        {
            return uncachedHelper();
        }
        
        if (cachedHelper == null)
        {
            cachedHelper = uncachedHelper();
        }
        
        return cachedHelper;
    }
    
    private ObjectHelper uncachedHelper()
    {
        return ObjectHelperLib.helper(getClass(), fields());
    }
    
    private ObjectHelper cachedHelper;
    
    // ===========================================================================
    // objectHelper代理
    // ===========================================================================
    
    @Override
    public String toString()
    {
        return objectHelper().classAndStateString();
    }
    
    // ValueMixin also delegates equals() and hashCode()
    
}
