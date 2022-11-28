package com.holly.top.springframework.core.filter;

import com.holly.top.springframework.core.constants.SystemConstants;
import org.slf4j.MDC;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 多线程链路追踪
 * 时间: 2022/9/27 11:03
 */
public class ThreadPoolMDCFilter {

    public static void setSleuthTraceId(){
        if (!ObjectUtils.isEmpty(MDC.get(SystemConstants.TRACE_ID_MDC_FIELD))){
            MDC.put(SystemConstants.TRACE_ID_HTTP_FIELD, MDC.get(SystemConstants.TRACE_ID_MDC_FIELD));//设置X-B3-TraceId
        }
        if (!ObjectUtils.isEmpty(MDC.get(SystemConstants.SPAN_ID_MDC_FIELD))){
            MDC.put(SystemConstants.PARENT_SPAN_ID_HTTP_FIELD,MDC.get(SystemConstants.SPAN_ID_MDC_FIELD));
            MDC.put(SystemConstants.PARENT_SPAN_ID_MDC_FIELD,MDC.get(SystemConstants.SPAN_ID_MDC_FIELD));
        }
        String spanIdNew = UUID.randomUUID().toString().replace("-","").substring(0,16);
        MDC.put(SystemConstants.SPAN_ID_HTTP_FIELD, spanIdNew);//设置X-B3-SpanId供外部使用
        MDC.put(SystemConstants.SPAN_ID_MDC_FIELD, spanIdNew);//设置X-B3-SpanId供外部使用
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setSleuthTraceId();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setSleuthTraceId();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

}
