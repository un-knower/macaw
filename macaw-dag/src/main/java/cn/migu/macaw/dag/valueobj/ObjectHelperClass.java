package cn.migu.macaw.dag.valueobj;

import static cn.migu.macaw.dag.util.StrLib.format;
import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Objects;

import com.google.common.collect.ImmutableMap;

import cn.migu.macaw.dag.errors.Bug;
import cn.migu.macaw.dag.util.StrLib;

/**
 * 对象帮助类实现
 * 
 * @author soy
 */
class ObjectHelperClass implements ObjectHelper
{
    
    private final Class<?> objectClass;
    
    private final Object[] fieldNamesAndValues;
    
    public ObjectHelperClass(Class<?> objectClass, Object[] fieldNamesAndValues)
    {
        
        this.objectClass = requireNonNull(objectClass);
        this.fieldNamesAndValues = requireNonNull(fieldNamesAndValues);
        
        if (fieldNamesAndValues.length % 2 != 0)
        {
            throw new Bug(
                "ObjectHelper fieldNamesAndValues must be an even length: alternating pairs of names and values");
        }
    }
    
    private int fieldCount()
    {
        return fieldNamesAndValues.length / 2;
    }
    
    // ===========================================================================
    // 类和属性
    // ===========================================================================
    
    @Override
    public Object[] classAndFieldValues()
    {
        if (classAndFieldValues == null)
        {
            
            classAndFieldValues = new Object[fieldCount() + 1];
            
            classAndFieldValues[0] = objectClass;
            
            for (int i = 0; i < fieldCount(); i++)
            {
                classAndFieldValues[i + 1] = fieldNamesAndValues[i * 2 + 1];
            }
        }
        
        return classAndFieldValues;
    }
    
    // 内部缓存
    private Object[] classAndFieldValues;
    
    // ===========================================================================
    // 属性
    // ===========================================================================
    
    @Override
    public Object[] fieldValues()
    {
        if (fieldValues == null)
        {
            
            fieldValues = new Object[fieldCount()];
            
            for (int i = 0; i < fieldCount(); i++)
            {
                fieldValues[i] = fieldNamesAndValues[i * 2 + 1];
            }
        }
        
        return fieldValues;
    }
    
    // 内部缓存
    private Object[] fieldValues;
    
    // ===========================================================================
    // 属性map
    // ===========================================================================
    
    @Override
    public ImmutableMap<String, Object> fieldMap()
    {
        if (fieldMap == null)
        {
            
            ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
            
            for (int i = 0; i < fieldNamesAndValues.length; i += 2)
            {
                String name = (String)fieldNamesAndValues[i];
                Object value = fieldNamesAndValues[i + 1];
                builder.put(name, value);
            }
            
            this.fieldMap = builder.build();
        }
        
        return fieldMap;
    }
    
    // 内部缓存
    private ImmutableMap<String, Object> fieldMap;
    
    // ===========================================================================
    // 对象字符串
    // ===========================================================================
    
    @Override
    public String classAndStateString()
    {
        return StrLib.classAndStateString(objectClass, fieldNamesAndValues);
    }
    
    // ===========================================================================
    // 类和属性比较
    // ===========================================================================
    
    @Override
    public boolean classAndStateEquals(Object otherObject)
    {
        
        if (otherObject == null)
        {
            return false;
        }
        
        if (!(otherObject instanceof HasObjectHelper))
        {
            return false;
        }
        
        ObjectHelper otherHelper = ((HasObjectHelper)otherObject).objectHelper();
        
        return Arrays.equals(classAndFieldValues(), otherHelper.classAndFieldValues());
    }
    
    // ===========================================================================
    // hashCode
    // ===========================================================================
    
    /**
     * 根据类和属性hash
     */
    @Override
    public int classAndStateHash()
    {
        if (classAndStateHash == null)
        {
            classAndStateHash = Objects.hash(classAndFieldValues());
        }
        return classAndStateHash;
    }
    
    // 内部缓存
    private Integer classAndStateHash;
    
    // ===========================================================================
    // 属性 比较
    // ===========================================================================
    
    @Override
    public void assertStateEquals(HasObjectHelper other)
    {
        assertStateEquals(other.objectHelper());
    }
    
    private void assertStateEquals(ObjectHelper other)
    {
        
        if (Arrays.equals(fieldValues(), other.fieldValues()))
        {
            return;
        }
        
        throw new AssertionError(format("Object states were not equal. Expecting: %s, but got %s",
            fieldValues(),
            other.fieldValues()));
    }
    
    // ===================================
    
    @Override
    public void assertStateNotEquals(HasObjectHelper other)
    {
        assertStateNotEquals(other.objectHelper());
    }
    
    private void assertStateNotEquals(ObjectHelper other)
    {
        if (!Arrays.equals(fieldValues(), other.fieldValues()))
        {
            return;
        }
        
        throw new AssertionError(format("Object states were equal, both were: %s", fieldValues()));
    }
    
}
