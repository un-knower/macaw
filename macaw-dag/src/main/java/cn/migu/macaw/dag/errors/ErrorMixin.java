package cn.migu.macaw.dag.errors;

import static cn.migu.macaw.dag.util.StrLib.formatN;

/**
 * 运行时异常扩展
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年5月27日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public abstract class ErrorMixin extends RuntimeException
{
    
    private static final long serialVersionUID = 1L;
    
    public ErrorMixin()
    {
        super();
    }
    
    public ErrorMixin(String template, Object... parts)
    {
        super(formatN(template, parts));
    }
    
    public ErrorMixin(Exception e)
    {
        this("Caused by %s", e);
    }
    
}
