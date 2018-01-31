package cn.migu.macaw.dag.errors;

/**
 * 存在不期望的顶点集异常
 * 
 * @author soy
 */
public class GraphHadUnexpectedIds extends ErrorMixin
{
    private static final long serialVersionUID = 1L;
    
    public GraphHadUnexpectedIds()
    {
        super();
    }
    
    public GraphHadUnexpectedIds(String template, Object... parts)
    {
        super(template, parts);
    }
}