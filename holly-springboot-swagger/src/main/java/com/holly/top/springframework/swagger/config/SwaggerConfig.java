package com.holly.top.springframework.swagger.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ObjectUtils;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/22 15:06
 */
@Configuration
@EnableSwagger2WebMvc
@EnableKnife4j
public class SwaggerConfig {

    @Autowired
    private SwaggerParams swaggerParams;

    @Bean
    public Docket createRestApi() {
        return (new Docket(DocumentationType.SWAGGER_2)).apiInfo(this.apiInfo()).enable(swaggerParams.isEnable()).
                select().apis(RequestHandlerSelectors.basePackage("com.holly")).paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        if (!ObjectUtils.isEmpty(this.swaggerParams.getTermsOfServiceUrl()) && this.swaggerParams.getContact() != null) {
            return (new ApiInfoBuilder()).title(this.swaggerParams.getTitle()).description(this.swaggerParams.getDescription()).termsOfServiceUrl(this.swaggerParams.getTermsOfServiceUrl()).contact(new Contact(this.swaggerParams.getContact().getName(), this.swaggerParams.getContact().getUrl(), this.swaggerParams.getContact().getEmail())).version(this.swaggerParams.getVersion()).build();
        } else if (!ObjectUtils.isEmpty(this.swaggerParams.getTermsOfServiceUrl()) && this.swaggerParams.getContact() == null) {
            return (new ApiInfoBuilder()).title(this.swaggerParams.getTitle()).description(this.swaggerParams.getDescription()).termsOfServiceUrl(this.swaggerParams.getTermsOfServiceUrl()).version(this.swaggerParams.getVersion()).build();
        } else {
            return ObjectUtils.isEmpty(this.swaggerParams.getTermsOfServiceUrl()) && this.swaggerParams.getContact() != null ? (new ApiInfoBuilder()).title(this.swaggerParams.getTitle()).description(this.swaggerParams.getDescription()).contact(new Contact(this.swaggerParams.getContact().getName(), this.swaggerParams.getContact().getUrl(), this.swaggerParams.getContact().getEmail())).version(this.swaggerParams.getVersion()).build() : (new ApiInfoBuilder()).title(this.swaggerParams.getTitle()).description(this.swaggerParams.getDescription()).version(this.swaggerParams.getVersion()).build();
        }
    }
}
