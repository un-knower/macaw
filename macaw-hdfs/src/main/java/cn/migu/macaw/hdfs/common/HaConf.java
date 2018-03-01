package cn.migu.macaw.hdfs.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;

/**
 * hdfs HA配置
 *
 * @author soy
 */
public class HaConf
{
    private static final String HDFS_CLUSTER_NAME_KEY = "clustername";

    private static final String NAMENODES_KEY = "namenodes";

    private static final String HOSTNAME_KEY = "hostname";

    private static final String PORT_KEY = "port";

    /**
     * 创建配置对象
     *
     * @param confStr
     * @return
     */
    public final static Configuration createConf(String confStr)
    {
        if (StringUtils.isNotEmpty(confStr))
        {
            //解析hdfs配置参数
            JSONObject jsonObj = JSONObject.parseObject(confStr);
            String clusterName = jsonObj.getString(HDFS_CLUSTER_NAME_KEY);

            JSONArray namenodes = jsonObj.getJSONArray(NAMENODES_KEY);

            int bsize = namenodes.size();

            String[] hostnames = new String[bsize];
            String[] ports = new String[bsize];
            for (int i = 0; i < namenodes.size(); i++)
            {
                JSONObject nn = (JSONObject)namenodes.get(i);
                hostnames[i] = nn.getString(HOSTNAME_KEY);
                ports[i] = nn.getString(PORT_KEY);
            }

            Configuration pConf = makeHaConf(clusterName, hostnames, ports);
            if (null == pConf)
            {
                throw new IllegalArgumentException("hdfs配置参数无效:" + confStr);
            }
            return pConf;
        }

        return null;
    }

    /**
     * 解析ha配置
     * @param clusterName
     * @param hostnames
     * @param ports
     * @return
     */
    private static Configuration makeHaConf(String clusterName, String[] hostnames, String[] ports)
    {
        if (StringUtils.isEmpty(clusterName))
        {
            return null;
        }

        if (hostnames.length != ports.length)
        {
            return null;
        }

        Configuration conf = new Configuration(false);
        conf.set("fs.defaultFS", StringUtils.join("hdfs://", clusterName));
        conf.set("fs.default.name", conf.get("fs.defaultFS"));
        conf.set("dfs.nameservices", clusterName);

        int len = hostnames.length;
        String nns = "";
        for (int i = 0; i < len; i++)
        {
            if (StringUtils.isEmpty(hostnames[i]) || StringUtils.isEmpty(ports[i]))
            {
                return null;
            }

            if (!StringUtils.isNumeric(ports[i]))
            {
                return null;
            }

            //自定义namenode名字
            String nnn = StringUtils.join("nn", String.valueOf(i + 1));

            nns = StringUtils.join(nns, nnn, ",");

            conf.set(StringUtils.join("dfs.namenode.rpc-address.", clusterName, ".", nnn),
                StringUtils.join(hostnames[i], ":", ports[i]));
        }

        nns = StringUtils.substring(nns, 0, nns.length() - 1);

        conf.set(StringUtils.join("dfs.ha.namenodes.", clusterName), nns);

        conf.set(StringUtils.join("dfs.client.failover.proxy.provider.", clusterName),
            "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

        conf.set("dfs.support.append", "true");

        return conf;
    }
}
