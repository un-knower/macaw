package cn.migu.macaw.dag.util;

import com.google.common.base.Function;

/**
 * 匿名lambda接口
 * 
 * @author soy
 */
public abstract class LambdaLib
{
    
    // ===========================================================================
    // 函数
    // ===========================================================================
    
    /**
     * 0个输入,1个输出函数
     * 
     * @author soy
     */
    public interface Fn0<Out>
    {
        Out apply();
    }
    
    /**
     * 1个输入,1个输出函数
     * 
     * @author soy
     */
    public interface Fn1<A, Out> extends Function<A, Out>
    {
        @Override
        Out apply(A a);
    }
    
    /**
     * 2个输入,1个输出函数
     * 
     * @author soy
     */
    public interface Fn2<A, B, Out>
    {
        Out apply(A a, B b);
    }
    
    /**
     * 3个输入,1个输出函数
     * 
     * @author soy
     */
    public static interface Fn3<A, B, C, Out>
    {
        Out apply(A a, B b, C c);
    }
    
    /**
     * 4个输入,1个输出函数
     * 
     * @author soy
     */
    public interface Fn4<A, B, C, D, Out>
    {
        Out apply(A a, B b, C c, D d);
    }
    
    /**
     * 5个输入,1个输出函数
     * 
     * @author soy
     */
    public interface Fn5<A, B, C, D, E, Out>
    {
        Out apply(A a, B b, C c, D d, E e);
    }
    
    /**
     * 6个输入,1个输出函数
     * 
     * @author soy
     */
    public interface Fn6<A, B, C, D, E, F, Out>
    {
        Out apply(A a, B b, C c, D d, E e, F f);
    }
    
    /**
     * 7个输入,1个输出函数
     * 
     * @author soy
     */
    public interface Fn7<A, B, C, D, E, F, G, Out>
    {
        Out apply(A a, B b, C c, D d, E e, F f, G g);
    }
    
    /**
     * 8个输入,1个输出函数
     * 
     * @author soy
     */
    public interface Fn8<A, B, C, D, E, F, G, H, Out>
    {
        Out apply(A a, B b, C c, D d, E e, F f, G g, H h);
    }
    
    /**
     * 9个输入,1个输出函数
     * 
     * @author soy
     */
    public interface Fn9<A, B, C, D, E, F, G, H, I, Out>
    {
        Out apply(A a, B b, C c, D d, E e, F f, G g, H h, I i);
    }
    
    // ===========================================================================
    // 动作(无输出)
    // ===========================================================================
    
    /**
     * 0个输入,0个输出
     * 
     * @author soy
     */
    public interface Act0
    {
        void apply();
    }
    
    /**
     * 1个输入,0个输出
     * 
     * @author soy
     */
    public interface Act1<A>
    {
        void apply(A a);
    }
    
    /**
     * 2个输入,0个输出
     * 
     * @author soy
     */
    public interface Act2<A, B>
    {
        void apply(A a, B b);
    }
    
    /**
     * 3个输入,0个输出
     * 
     * @author soy
     */
    public interface Act3<A, B, C>
    {
        void apply(A a, B b, C c);
    }
    
    /**
     * 4个输入,0个输出
     * 
     * @author soy
     */
    public interface Act4<A, B, C, D>
    {
        void apply(A a, B b, C c, D d);
    }
    
    /**
     * 5个输入,0个输出
     * 
     * @author soy
     */
    public interface Act5<A, B, C, D, E>
    {
        void apply(A a, B b, C c, D d, E e);
    }
    
    /**
     * 6个输入,0个输出
     * 
     * @author soy
     */
    public interface Act6<A, B, C, D, E, F>
    {
        void apply(A a, B b, C c, D d, E e, F f);
    }
    
    /**
     * 7个输入,0个输出
     * 
     * @author soy
     */
    public interface Act7<A, B, C, D, E, F, G>
    {
        void apply(A a, B b, C c, D d, E e, F f, G g);
    }
    
    /**
     * 8个输入,0个输出
     * 
     * @author soy
     */
    public interface Act8<A, B, C, D, E, F, G, H>
    {
        void apply(A a, B b, C c, D d, E e, F f, G g, H h);
    }
    
    /**
     * 9个输入,0个输出
     * 
     * @author soy
     */
    public interface Act9<A, B, C, D, E, F, G, H, I>
    {
        void apply(A a, B b, C c, D d, E e, F f, G g, H h, I i);
    }
    
}
