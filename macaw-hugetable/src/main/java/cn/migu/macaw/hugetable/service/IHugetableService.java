package cn.migu.macaw.hugetable.service;

import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.hugetable.model.SqlParam;

/**
 * hugetable sql操作
 *
 * @author soy
 */
public interface IHugetableService
{
    /**
     * 执行一条hugetable sql
     * @param param sql参数
     * @return Entity - 执行结果
     */
    Entity executeSql(SqlParam param);

    /**
     * 查询一条hugetable sql
     * @param param sql参数
     * @return Entity - 执行结果
     */
    Entity executeQuery(SqlParam param);
    
}
