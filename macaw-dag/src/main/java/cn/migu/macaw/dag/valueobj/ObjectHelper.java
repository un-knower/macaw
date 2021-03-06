package cn.migu.macaw.dag.valueobj;

import com.google.common.collect.ImmutableMap;

/**
 * 对象帮助类接口
 * 
 * @author soy
 */
public interface ObjectHelper
{
    
    // ===========================================================================
    // 方法
    // ===========================================================================
    
    /**
     * toString的实现
     */
    String classAndStateString();
    
    /**
     * equals的实现
     */
    boolean classAndStateEquals(Object otherObject);
    
    /**
     * hashCode的实现
     */
    int classAndStateHash();
    
    // ===========================================================================
    // 获取状态
    // ===========================================================================
    
    /**
     * 对象内容
     */
    Object[] classAndFieldValues();
    
    /**
     * 属性和值
     */
    Object[] fieldValues();
    
    /**
     * 属性map
     */
    ImmutableMap<String, Object> fieldMap();
    
    // ===========================================================================
    // 内容比较
    // ===========================================================================
    
    /**
     * 是否相等
     */
    void assertStateEquals(HasObjectHelper other);
    
    /**
     * 是否不相等
     */
    void assertStateNotEquals(HasObjectHelper other);
}
