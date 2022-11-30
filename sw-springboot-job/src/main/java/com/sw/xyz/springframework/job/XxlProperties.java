package com.sw.xyz.springframework.job;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 名称: XxlProperties
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/28 0028 15:23
 */
@ConfigurationProperties(prefix = "sw.job")
@Data
public class XxlProperties {

    /**
     * 接入服务名称
     */
    @Value("${spring.application.name:}")
    private String applicationName;


}
