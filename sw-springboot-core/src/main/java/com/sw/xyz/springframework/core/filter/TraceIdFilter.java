package com.sw.xyz.springframework.core.filter;

import cn.hutool.core.util.IdUtil;
import com.sw.xyz.springframework.bean.constants.SystemConstants;
import com.sw.xyz.springframework.utils.web.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/27 11:03
 */
@Slf4j
@WebFilter(filterName="traceIdFilter", urlPatterns="/*")
@Order(0)
@Component
public class TraceIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest,ServletResponse servletResponse,FilterChain filterChain) throws IOException, ServletException {
        WebUtils.bindContext((HttpServletRequest) servletRequest,(HttpServletResponse) servletResponse);
        MDC.put(SystemConstants.TRACE_ID_MDC_FIELD,IdUtil.objectId());
        try{
            filterChain.doFilter(servletRequest,servletResponse);
        } finally {
            WebUtils.clearContext();
        }
    }

    @Override
    public void destroy() {
        MDC.clear();
    }
}
