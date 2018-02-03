package cn.migu.macaw.hadoop.api.service;

import cn.migu.macaw.common.message.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 大数据平台底层服务
 *
 * @author soy
 */
@RequestMapping("/hadoop")
public interface HadoopService
{
    /**
     * jdbc方式执行hugetable sql
     * @param request http request
     * @return Response - 返回消息
     */
    @PostMapping("hugetable-sql")
    Response hugetableSql(HttpServletRequest request);

    /**
     * 查询hugetable并返回
     * @param request
     * @return
     */
    @PostMapping("hugetable-sql-select")
    Response hugetableSqlSelect(HttpServletRequest request);

    /**
     * 数据同步服务
     * @param request http request
     * @return  Response - 返回消息
     */
    @PostMapping("crossdata")
    Response dataBaseSync(HttpServletRequest request);
}
