package com.sw.xyz.springframework.utils.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 获取spring管理bean
 * 时间: 2021/12/16 10:31
 */
@Component
@Lazy
public final class SpringContextUtils implements  ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(SpringContextUtils.class);
    private static SpringContextUtils instance;
    private ApplicationContext applicationContext;

    public SpringContextUtils() {
    }

    public static SpringContextUtils getInstance() {
        if (instance == null) {
            instance = new SpringContextUtils();
        }

        return instance;
    }

    public <T> T getBean(Class<T> clazz) {
        return this.applicationContext == null ? null : this.applicationContext.getBean(clazz);
    }

    public Object getBean(String name) {
        return this.applicationContext == null ? null : this.applicationContext.getBean(name);
    }

    public <T> Map<String, T> getBeans(Class<T> beanClass) {
        return this.applicationContext == null ? null : this.applicationContext.getBeansOfType(beanClass);
    }

    public <T> T getBean(String name, Class<T> clazz) {
        return this.applicationContext == null ? null : this.applicationContext.getBean(name, clazz);
    }

    public void initContext(ApplicationContext ctx) throws BeansException {
        if (ctx == null) {
            log.warn("ApplicationContext is null");
        } else {
            getInstance().setApplicationContext(ctx);
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        if (instance == null) {
            instance = (SpringContextUtils)applicationContext.getBean("springContextUtils");
        }

    }
}
