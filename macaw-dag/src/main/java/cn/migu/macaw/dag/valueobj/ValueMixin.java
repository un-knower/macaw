package cn.migu.macaw.dag.valueobj;

/**
 * 值对象
 * 
 * @author soy
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
