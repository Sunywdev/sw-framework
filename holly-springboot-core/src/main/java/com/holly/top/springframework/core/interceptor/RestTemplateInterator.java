package com.holly.top.springframework.core.interceptor;

import com.holly.top.springframework.core.constants.SystemConstants;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.UUID;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: restTemplate请求的拦截器，将traceId及parentSpanId塞入
 * 时间: 2022/9/27 11:03
 */
public class RestTemplateInterator implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest,byte[] bytes,ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        String traceId=MDC.get(SystemConstants.TRACE_ID_HTTP_FIELD);
        if (!ObjectUtils.isEmpty(traceId)) {
            httpRequest.getHeaders().add(SystemConstants.TRACE_ID_HTTP_FIELD,traceId);
        }
        String spanIdNew=UUID.randomUUID().toString().replace("-","").substring(0,16);
        httpRequest.getHeaders().add(SystemConstants.SPAN_ID_HTTP_FIELD,spanIdNew);//设置X-B3-SpanId供外部使用
        if (!ObjectUtils.isEmpty(MDC.get(SystemConstants.SPAN_ID_MDC_FIELD))) {
            httpRequest.getHeaders().add(SystemConstants.PARENT_SPAN_ID_HTTP_FIELD,MDC.get(SystemConstants.SPAN_ID_MDC_FIELD));
        }
        return clientHttpRequestExecution.execute(httpRequest,bytes);
    }
}
