package com.sw.xyz.springframework.swagger.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/22 14:51
 */

@Configuration
@ConfigurationProperties(
        prefix="sw.swagger"
)
public class SwaggerParams {

    private String title;

    private String description;

    private String termsOfServiceUrl;

    private SwaggerParams.Contact contact;

    private String version="1.0.0";

    private boolean enable=true;

    private String basePackage = "com.sw";

    public static class Contact {
        private String name;
        private String url;
        private String email;

        public Contact() {
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name=name;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url=url;
        }

        public String getEmail() {
            return this.email;
        }

        public void setEmail(String email) {
            this.email=email;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl=termsOfServiceUrl;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact=contact;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version=version;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable=enable;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
