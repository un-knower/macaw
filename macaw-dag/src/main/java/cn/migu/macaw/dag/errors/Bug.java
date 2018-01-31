package cn.migu.macaw.dag.errors;

/**
 * 使用异常
 * 
 * @author soy
 */
public class Bug extends ErrorMixin
{
    private static final long serialVersionUID = 1L;
    
    public Bug()
    {
        super();
    }
    
    public Bug(String template, Object... parts)
    {
        super(template, parts);
    }
}
