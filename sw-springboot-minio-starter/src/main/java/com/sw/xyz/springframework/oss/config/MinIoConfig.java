package com.sw.xyz.springframework.oss.config;

import io.minio.MinioClient;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/02/03 13:38
 */
@Data
@Configuration
@ConfigurationProperties(
        prefix = "sw.oss.minio"
)
public class MinIoConfig {

    /**
     * 端点
     */
    private String endpoint;

    /**
     * 默认的桶名称
     */
    private String defaultBucketName;

    /**
     * 访问key
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 域名：可选配置
     * 配置后获得URL时，会将host替换为domainName
     * 用作域名指定，通常台配置的endpoint是本地地址，如果不指定域名外部系统通常无法访问
     */
    private String domainName;

    @SneakyThrows
    @ConditionalOnMissingBean
    @Bean
    public MinioClient minioClient(MinIoConfig minIoConfig) {
        return new MinioClient(minIoConfig.getEndpoint(), minIoConfig.getAccessKey(), minIoConfig.getSecretKey());
    }
}
