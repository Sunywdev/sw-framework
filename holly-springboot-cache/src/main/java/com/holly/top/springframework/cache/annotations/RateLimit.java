package com.holly.top.springframework.cache.annotations;

import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 基于Redisson的限流
 * 时间: 2022/9/23 9:28
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 实例名称
     */
    String value();

    /**
     * OVERALL,             //所有客户端加总限流
     * PER_CLIENT;          //每个客户端单独计算流量
     */
    RateType rateType() default RateType.PER_CLIENT;

    /**
     * 产生的令牌数量
     */
    int rate() default 1;

    /**
     * 多久产生令牌
     */
    int rateInterval() default 1;

    /**
     * 令牌生成的时间单位
     */
    RateIntervalUnit unit() default RateIntervalUnit.SECONDS;

    /**
     * 阻塞时间 0 代表不生效 -1 代表阻塞等待
     */
    long timeout() default 0;

    /**
     * 阻塞时间单位
     */
    TimeUnit timeoutUnit() default TimeUnit.SECONDS;

}
