package com.holly.top.springframework.core.utils;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class WebUtils {
    private static final ThreadLocal<WebContext> threadContext = new ThreadLocal<>();

    public static WebContext getContext() {
        return threadContext.get();
    }

    public static HttpServletRequest getRequest() {
        WebContext webContext = getContext();
        return webContext == null ? null : webContext.request;
    }

    public static HttpServletResponse getResponse() {
        WebContext webContext = getContext();
        return webContext == null ? null : webContext.response;
    }

    public static void bindContext(HttpServletRequest request,HttpServletResponse response) {
        threadContext.set(new WebContext(request, response));
    }

    public static void clearContext() {
        threadContext.remove();
    }

    public static class WebContext {
        @Getter
        private HttpServletRequest request;
        @Getter
        private HttpServletResponse response;
        @Getter
        @Setter
        private Object controller;
        @Getter
        @Setter
        private Method method;

        public WebContext(HttpServletRequest request,HttpServletResponse response) {
            this.request = request;
            this.response = response;
        }

    }
}
