package cn.migu.macaw.dag.errors;

/**
 * 方法未实现异常
 * 
 * @author soy
 */
public class NotImplemented extends ErrorMixin
{
    private static final long serialVersionUID = 1L;
    
    public NotImplemented()
    {
        super();
    }
    
    public NotImplemented(String template, Object... parts)
    {
        super(template, parts);
    }
}
