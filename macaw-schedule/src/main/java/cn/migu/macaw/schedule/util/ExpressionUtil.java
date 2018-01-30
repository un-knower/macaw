package cn.migu.macaw.schedule.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 表达式工具类
 * 
 * @author soy
 */
public class ExpressionUtil
{
    private static ExpressionUtil instance = new ExpressionUtil();
    
    private ExpressionUtil()
    {
        
    }
    
    public static ExpressionUtil getInstance()
    {
        return instance;
    }
    
    /**
     * 布尔表达式判定
     * @param expr
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean evalBool(String expr)
    {
        try
        {
            
            ScriptEngineManager sem = new ScriptEngineManager();
            ScriptEngine se = sem.getEngineByName("JavaScript");
            
            return (boolean)se.eval(expr);
            
        }
        catch (ScriptException e)
        {
            return false;
        }
    }

    public static void main(String[] args)
    {
        System.out.println(ExpressionUtil.getInstance().evalBool("6 > 5 || 1 >= 3"));
    }
}
