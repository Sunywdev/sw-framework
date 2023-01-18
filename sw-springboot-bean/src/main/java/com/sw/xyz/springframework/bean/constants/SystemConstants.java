package com.sw.xyz.springframework.bean.constants;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/27 11:41
 */
public class SystemConstants {


    public static final String TRACE_ID_MDC_FIELD = "traceId";
    public static final String SPAN_ID_MDC_FIELD = "spanId";
    public static final String PARENT_SPAN_ID_MDC_FIELD = "parentSpanId";
    public static final String TRACE_ID_HTTP_FIELD = "X-B3-TraceId";
    public static final String SPAN_ID_HTTP_FIELD = "X-B3-SpanId";
    public static final String PARENT_SPAN_ID_HTTP_FIELD = "X-B3-ParentSpanId";

}
