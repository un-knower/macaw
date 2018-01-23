package cn.migu.macaw.sparkdrivermgr.common;

/**
 * spark driver类型
 * @author soy
 */
public interface DriverType
{
    /**
     * 该类型spark drievr主要负责spark sql运行
     */
    String CALC = "calc";

    /**
     * 该类型spark drievr主要负责机器学习算法运行
     */
    String ALGO = "algo";
}
