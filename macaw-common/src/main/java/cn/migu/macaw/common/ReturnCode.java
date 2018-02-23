package cn.migu.macaw.common;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * 返回码
 *
 * @author soy
 */
public enum ReturnCode
{
    SUCCESS(SysRetCode.SUCCESS, "操作成功"),

    DATA_SYNC_FUNC_NOT_SUPPORT(SysRetCode.DATA_SYNC_FUNC_NOT_SUPPORT,"数据同步功能不支持"),
    DATA_SYNC_PARAM_PARSE_ERROR(SysRetCode.DATA_SYNC_PARAM_PARSE_ERROR,"数据同步参数解析错误"),

    CUSTOM_ETL_JAR_EXT_FUNC_NOT_EXISTED(SysRetCode.CUSTOM_ETL_JAR_EXT_FUNC_NOT_EXISTED,"自定义ETL jar外部扩展功能不存在"),
    JAR_ID_EMPTY(SysRetCode.JAR_ID_EMPTY,"部署jar id为空"),
    DEPLOY_APP_ID_EMPTY(SysRetCode.DEPLOY_APP_ID_EMPTY,"部署服务应用id为空"),
    DEPLOY_SERVER_ID_EMPTY(SysRetCode.DEPLOY_SERVER_ID_EMPTY,"部署服务器id为空"),
    DEPLOY_PATH_EMPTY(SysRetCode.DEPLOY_PATH_EMPTY,"部署路径为空"),
    DEPLOY_JAR_TYPE_ERROR(SysRetCode.DEPLOY_JAR_TYPE_ERROR,"部署jar类型错误"),
    DEPLOY_JAR_NAME_EMPTY(SysRetCode.DEPLOY_JAR_NAME_EMPTY,"部署jar名称为空"),
    DEPLOY_USER_EMPTY(SysRetCode.DEPLOY_USER_EMPTY,"服务部署操作人为空"),
    DEPLOY_JAR_PORT_EMPTY(SysRetCode.DEPLOY_JAR_PORT_EMPTY,"部署服务端口号为空"),
    DATA_COLLECT_TIME_EMPTY(SysRetCode.DATA_COLLECT_TIME_EMPTY,"数据采集时间为空"),
    BOOT_PARAM_PARSE_ERROR(SysRetCode.BOOT_PARAM_PARSE_ERROR,"启动参数解析错误"),
    DEPLOY_HOST_NOT_EXISTED(SysRetCode.DEPLOY_HOST_NOT_EXISTED,"部署主机信息不存在"),
    LOGIN_USERNAME_OR_PASSWD_ERROR(SysRetCode.LOGIN_USERNAME_OR_PASSWD_ERROR,"登录主机用户名或密码错误"),
    EXECUTE_SHELL_EXCEPTION(SysRetCode.EXECUTE_SHELL_EXCEPTION,"执行shell命令异常"),
    PORT_ALREADY_USED(SysRetCode.PORT_ALREADY_USED,"启动端口被占用"),
    SOURCE_PATH_ERROR(SysRetCode.SOURCE_PATH_ERROR,"原始文件路径错误"),
    COLLECT_PATH_ERROR(SysRetCode.COLLECT_PATH_ERROR,"采集文件路径错误"),
    PARAMS_LESS_EIGHT_ERROR(SysRetCode.PARAMS_LESS_EIGHT_ERROR,"输入参数少于8个"),
    BOOT_FAILED(SysRetCode.BOOT_FAILED,"功能服务启动失败"),
    PARAMS_LESS_FOUR_ERROR(SysRetCode.PARAMS_LESS_FOUR_ERROR,"参数个数不能小于4个,顺序为appID,serviceID,port,jarID"),
    DEPLOY_PATH_ERROR(SysRetCode.DEPLOY_PATH_ERROR,"部署路径错误"),
    PERMISSION_DENY(SysRetCode.PERMISSION_DENY,"系统权限不足"),
    NOT_ANY_RESOURCES(SysRetCode.NOT_ANY_RESOURCES,"无系统资源可用"),
    NOT_SUPPORT_BOOT(SysRetCode.NOT_SUPPORT_BOOT,"服务应用不支持启动"),
    NEXT_LINE_PARSE(SysRetCode.NEXT_LINE_PARSE,"继续分析下一行"),
    STOP_FAILED(SysRetCode.STOP_FAILED,"停止服务失败"),

    FAILED(SysRetCode.ERROR,"操作失败"),
    ;
    
    private String code;
    
    private String name;
    
    ReturnCode(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
