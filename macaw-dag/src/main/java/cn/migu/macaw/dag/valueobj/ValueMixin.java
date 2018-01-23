package cn.migu.macaw.dag.valueobj;

/**
 * 值对象
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年5月30日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class ValueMixin extends ObjectMixin
{
    
    @Override
    protected boolean isEntity()
    {
        return false;
    }
    
    @Override
    public boolean equals(Object other)
    {
        return objectHelper().classAndStateEquals(other);
    }
    
    @Override
    public int hashCode()
    {
        return objectHelper().classAndStateHash();
    }
    
}
