package com.holly.top.springframework.core.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 跨域配置
 * 时间: 2022/9/27 11:03
 */
@Slf4j
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res=(HttpServletResponse) response;
        HttpServletRequest req=(HttpServletRequest) request;
        if (res.containsHeader("Access-Control-Allow-Headers"))
            return;

        res.addHeader("Access-Control-Allow-Credentials","true");
        res.addHeader("Access-Control-Allow-Methods","*");
        res.addHeader("Access-Control-Allow-Headers","*");
        res.addHeader("Access-Control-Allow-Origin","*");
        if ("OPTIONS".equals(req.getMethod().toUpperCase())) {
            res.setStatus(HttpStatus.NO_CONTENT.value());
            return;
        }
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
