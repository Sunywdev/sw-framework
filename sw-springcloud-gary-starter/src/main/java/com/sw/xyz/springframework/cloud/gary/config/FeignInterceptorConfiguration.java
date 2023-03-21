package com.sw.xyz.springframework.cloud.gary.config;

import com.sw.xyz.springframework.cloud.gary.interceptor.FeignInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: feign拦截器注册
 * 时间: 2023/03/03 9:42
 */
@Configuration
public class FeignInterceptorConfiguration {


    @Bean("feignInterceptor")
    public RequestInterceptor requestInterceptor() {
        return new FeignInterceptor();
    }
}
