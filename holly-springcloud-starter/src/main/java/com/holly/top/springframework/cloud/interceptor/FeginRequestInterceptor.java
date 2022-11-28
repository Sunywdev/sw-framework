package com.holly.top.springframework.cloud.interceptor;

import cn.hutool.core.util.IdUtil;
import com.holly.top.springframework.core.constants.SystemConstants;
import com.holly.top.springframework.core.utils.WebUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: Feign调用增加TriceId
 * 时间: 2022/9/27 11:03
 */
public class FeginRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String triceId=MDC.get(SystemConstants.TRACE_ID_MDC_FIELD);
        if (null == triceId) {
            HttpServletRequest request=WebUtils.getContext().getRequest();
            if (null != request) {
                triceId=request.getHeader(SystemConstants.TRACE_ID_MDC_FIELD);
            } else {
                triceId=IdUtil.objectId();
            }
            MDC.put(SystemConstants.TRACE_ID_MDC_FIELD,triceId);
        }
        requestTemplate.header(SystemConstants.TRACE_ID_MDC_FIELD,triceId);
    }

}
