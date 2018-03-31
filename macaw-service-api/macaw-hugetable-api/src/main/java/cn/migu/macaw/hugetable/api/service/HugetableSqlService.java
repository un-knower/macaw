package cn.migu.macaw.hugetable.api.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.migu.macaw.common.message.Response;

/**
 * hugetable sql服务
 *
 * @author soy
 */
@RequestMapping("/hugetable-sql")
public interface HugetableSqlService
{
    /**
     * jdbc方式执行hugetable sql
     * @param request http request
     * @return Response - 返回消息
     */
    @PostMapping("execute")
    Response hugetableSql(HttpServletRequest request);

    /**
     * 查询hugetable并返回
     * @param request
     * @return
     */
    @PostMapping("query")
    Response hugetableSqlSelect(HttpServletRequest request);
}
