package cn.migu.macaw.common.message;

/**
 * 模块之间交互消息
 * 
 * @author  soy
 */
public class Response
{
    private Entity response;
    
    public Response()
    {
        
    }
    
    public Response(Entity response)
    {
        this.response = response;
    }
    
    public Entity getResponse()
    {
        return response;
    }
    
    public void setResponse(Entity response)
    {
        this.response = response;
    }
    
}
