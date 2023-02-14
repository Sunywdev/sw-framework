package com.sw.xyz.springframework.core.annocation;

import com.sw.xyz.springframework.core.log.LogLevel;

import java.lang.annotation.*;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/27 9:45
 */
@Documented
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {


    String value() default "";


    LogLevel level() default LogLevel.DEBUG;
}
