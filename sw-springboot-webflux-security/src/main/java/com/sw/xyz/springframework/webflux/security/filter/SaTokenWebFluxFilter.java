package com.sw.xyz.springframework.webflux.security.filter;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.sw.xyz.springframework.bean.entity.enums.SystemRespCodeEnums;
import com.sw.xyz.springframework.bean.response.BaseResponse;
import com.sw.xyz.springframework.webflux.security.config.SecurityConfig;
import com.sw.xyz.springframework.webflux.security.config.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/02/13 13:01
 */
@Configuration
@Slf4j
public class SaTokenWebFluxFilter {

    private final SecurityProperties securityProperties;

    public SaTokenWebFluxFilter(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    /**
     * 注册 [Sa-Token全局过滤器]
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        SaReactorFilter saReactorFilter = new SaReactorFilter();
        saReactorFilter.addInclude("/**");
        String[] split = securityProperties.getIgnorePath().split(",");
        for (String url : split) {
            saReactorFilter.addExclude(url);
        }
        saReactorFilter.setAuth(r -> {
            log.info(">>>>>>>>>>>>>>>>>>网关全局登录验证");
            StpUtil.checkLogin();
        })// 指定[异常处理函数]：每次[认证函数]发生异常时执行此函数
                .setError(e -> {
                    SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
                    SaHolder.getResponse().setStatus(HttpStatus.UNAUTHORIZED.value());
                    log.error("网关验证权限异常", e);
                    return JSONUtil.toJsonStr(BaseResponse.error(SystemRespCodeEnums.UNAUTHORIZED.getCode(), SystemRespCodeEnums.UNAUTHORIZED.getMessage()));
                });
        return saReactorFilter;
    }
}
