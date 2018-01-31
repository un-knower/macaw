package cn.migu.macaw.dag.errors;

/**
 * 有向图存在环异常
 * 
 * @author soy
 */
public class DagCannotHaveCycle extends ErrorMixin
{
    private static final long serialVersionUID = 1L;
    
    public DagCannotHaveCycle()
    {
        super();
    }
    
    public DagCannotHaveCycle(String template, Object... parts)
    {
        super(template, parts);
    };
}