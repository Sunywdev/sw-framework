package com.sw.xyz.springframework.security.filter;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ArrayUtil;
import com.sw.xyz.springframework.security.config.SecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;


/**
 * 名称: SecurityAutoConfig
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/12/8 0008 15:06
 */
@Configuration
public class SecurityFilter implements WebMvcConfigurer {

    private final SecurityProperties securityProperties;

    public SecurityFilter(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        if (!securityProperties.getEnableSecurityFilter()) {
            return;
        }
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**");

        interceptorRegistration.excludePathPatterns("/**/**.css")
                .excludePathPatterns("/**/**.js")
                .excludePathPatterns("/**/**.html")
                .excludePathPatterns("/**/**.ico")
                .excludePathPatterns("/**/swagger-resources/**")
                .excludePathPatterns("/**/checkAlive");
        if (StringUtils.isNotEmpty(securityProperties.getIgnorePath())) {
            String[] split = securityProperties.getIgnorePath().split(",");
            if (ArrayUtil.isNotEmpty(split)) {
                Arrays.stream(split).forEach(interceptorRegistration::excludePathPatterns);
            }
        }
    }
}
