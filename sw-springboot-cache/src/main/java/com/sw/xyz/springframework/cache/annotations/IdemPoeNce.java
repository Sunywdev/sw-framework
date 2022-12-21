package com.sw.xyz.springframework.cache.annotations;

import java.lang.annotation.*;

/**
 * 名称: IdemPoeNce
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 幂等性注解,使用SpEl表达式进行解析,目的是为了解决同一时间节点的重复请求
 * 时间: 2022/12/21 0021 17:58
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IdemPoeNce {

    /**
     * 解析的数据值
     */
    String[] spElValue() default "";

    /**
     * 时间,时间范围,时间秒
     */
    long time() default 1;
}
