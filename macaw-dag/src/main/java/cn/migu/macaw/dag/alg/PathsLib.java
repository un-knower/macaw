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
 * @author soy
 */
public class PathsLib
{
    /**
     * 查找指定两个节点之间的所有路径(DFS)
     * @param source 源节点
     * @param destination 目的节点
     * @param dag 有向图
     * @return  - 路径信息
     * @see [类、类#方法、类#成员]
     */
    public static List<List<String>> getAllPaths(String source, String destination, final IdDag<String> dag)
    {
        List<List<String>> paths = new ArrayList<>();
        
        recursive(source, destination, paths, new LinkedHashSet<>(), dag);
        
        return paths;
    }
    
    /**
     * 递归路径查找(DFS)
     * @param current 当前节点
     * @param destination 目的节点
     * @param paths 所有路径集合
     * @param path 已访问节点
     * @param dag 有向图
     * @see [类、类#方法、类#成员]
     */
    private static void recursive(String current, String destination, List<List<String>> paths,
        LinkedHashSet<String> path, final IdDag<String> dag)
    {
        path.add(current);
        
        if (StringUtils.equals(current, destination))
        {
            paths.add(new ArrayList<>(path));
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
