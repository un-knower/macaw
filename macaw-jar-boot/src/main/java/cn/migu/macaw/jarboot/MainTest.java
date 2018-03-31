package cn.migu.macaw.jarboot;

import cn.migu.macaw.common.message.Response;
import cn.migu.macaw.jarboot.common.JarFuncType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 *
 * author  zhaocan
 * version  [版本号, 2018/2/14]
 * see  [相关类/方法]
 * since  [产品/模块版本]
 */
public class MainTest
{
    public static void main(String[] args)
    {
        /*String result = "{\"response\":{\"code\":\"0000\",\"appname\":\"zhaocan_test3-n1_192.168.129.153_80a8a53a13d14441a0937fc270e0f2b5\",\"appid\":\"app-20180308165956-0080\",\"desc\":\"操作成功\"}}";
        Response response = JSON.parseObject(result, Response.class, Feature.InitStringFieldAsEmpty);

        System.out.println(response);*/

        /*Map<String,String[]> params = Maps.newHashMap();
        params.put("111",new String[]{"a"});
        params.put("123",new String[]{});
        params.put("222",new String[]{"b"});

        Map<String, String> nMapParams = params.entrySet()
            .stream()
            .filter(e -> null != e.getValue() && e.getValue().length > 0 && StringUtils.isNotEmpty(e.getValue()[0]))
            .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()[0]));

        String postFormParam = null;
        if (null != nMapParams && nMapParams.size() > 0)
        {
            postFormParam = Joiner.on(",").withKeyValueSeparator("=").join(nMapParams);
        }

        System.out.println(postFormParam);*/
        JarFuncType type = JarFuncType.jarType(3);

        System.out.println(type);
    }
    

}
