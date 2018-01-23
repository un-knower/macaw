package cn.migu.macaw.dag.alg;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import cn.migu.macaw.dag.idgraph.IdDag;

/**
 * 图路径查找
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年6月5日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class PathsLib
{
    /**
     * 查找指定两个节点之间的所有路径
     * @param source
     * @param destination
     * @param dag
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static List<List<String>> getAllPaths(String source, String destination, final IdDag<String> dag)
    {
        List<List<String>> paths = new ArrayList<List<String>>();
        
        recursive(source, destination, paths, new LinkedHashSet<String>(), dag);
        
        return paths;
    }
    
    /**
     * 递归路径查找
     * @param current
     * @param destination
     * @param paths
     * @param path
     * @param dag
     * @see [类、类#方法、类#成员]
     */
    private static void recursive(String current, String destination, List<List<String>> paths,
        LinkedHashSet<String> path, final IdDag<String> dag)
    {
        path.add(current);
        
        if (StringUtils.equals(current, destination))
        {
            paths.add(new ArrayList<String>(path));
            path.remove(current);
            return;
        }
        
        final Set<String> edges = dag.childIdSet(current);
        
        for (String t : edges)
        {
            if (!path.contains(t))
            {
                recursive(t, destination, paths, path, dag);
            }
        }
        
        path.remove(current);
    }
}
