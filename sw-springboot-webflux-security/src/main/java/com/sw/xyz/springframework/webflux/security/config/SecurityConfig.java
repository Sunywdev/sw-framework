package com.sw.xyz.springframework.webflux.security.config;

import cn.dev33.satoken.config.SaTokenConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/02/09 14:34
 */
@Configuration
public class SecurityConfig {

    private final SecurityProperties securityProperties;

    public SecurityConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Bean
    public SaTokenConfig saTokenConfig() {
        SaTokenConfig saTokenConfig = new SaTokenConfig();
        saTokenConfig.setTokenName(securityProperties.getTokenName());
        saTokenConfig.setTimeout(securityProperties.getTimeout());
        saTokenConfig.setTokenStyle(securityProperties.getTokenStyle());
        saTokenConfig.setActivityTimeout(securityProperties.getActivityTimeout());
        saTokenConfig.setIsConcurrent(securityProperties.getIsConcurrent());
        saTokenConfig.setIsShare(securityProperties.getIsShare());
        saTokenConfig.setMaxLoginCount(securityProperties.getMaxLoginCount());
        saTokenConfig.setIsReadBody(securityProperties.getIsReadBody());
        saTokenConfig.setIsReadHeader(securityProperties.getIsReadHeader());
        saTokenConfig.setIsReadCookie(securityProperties.getIsReadCookie());
        saTokenConfig.setIsWriteHeader(securityProperties.getIsWriteHeader());
        saTokenConfig.setDataRefreshPeriod(securityProperties.getDataRefreshPeriod());
        saTokenConfig.setTokenSessionCheckLogin(securityProperties.getTokenSessionCheckLogin());
        saTokenConfig.setAutoRenew(securityProperties.getAutoRenew());
        saTokenConfig.setIsPrint(securityProperties.getIsPrint());
        saTokenConfig.setTokenPrefix(securityProperties.getTokenPrefix());
        saTokenConfig.setIsLog(securityProperties.getIsLog());
        saTokenConfig.setLogLevel(securityProperties.getLogLevel());
        return saTokenConfig;
    }
}
