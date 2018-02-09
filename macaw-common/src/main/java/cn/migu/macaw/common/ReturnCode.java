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
    /**
     * 成功
     */
    SUCCESS(SysRetCode.SUCCESS, "操作成功"),
    CUSTOM_ETL_JAR_EXT_FUNC_NOT_EXISTED(SysRetCode.CUSTOM_ETL_JAR_EXT_FUNC_NOT_EXISTED,"自定义ETL jar外部扩展功能不存在"),
    FAILED(SysRetCode.ERROR,"操作失败"),
    ;
    
    private String code;
    
    private String name;
    
    private ReturnCode(String code, String name)
    {
        this.code = code;
        this.name = name;
    }
    
    public static final String getName(String code)
    {
        return Stream.of(ReturnCode.values()).filter(c -> StringUtils.equals(c.code, code)).map(e -> e.name).collect(
            Collectors.joining(""));
    }

    public static void main(String[] args)
    {
        System.out.println(ReturnCode.getName(SysRetCode.HT_EXCEPTION));
    }
}
