package com.holly.top.springframework.cache.config;

import com.holly.top.springframework.bean.entity.enums.RespCodeEnums;
import com.holly.top.springframework.bean.exceptions.BaseException;
import com.holly.top.springframework.cache.annotations.RateLimit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/23 9:35
 */
@Aspect
@Component
@Slf4j
public class RateLimitAspect {


    @Autowired
    private RedissonClient redissonClient;

    @Pointcut(value="@annotation(com.holly.top.springframework.cache.annotations.RateLimit)")
    public void cut() {
    }

    @Before(value="cut()")
    public void check(JoinPoint joinPoint) {
        Method method=((MethodSignature) joinPoint.getSignature()).getMethod();
        RateLimit routeLimiter=method.getAnnotation(RateLimit.class);
        if (null == routeLimiter) {
            return;
        }
        if (ObjectUtils.isEmpty(routeLimiter.value())) {
            log.warn("限流器没有指定keyName");
            throw new BaseException(RespCodeEnums.FAIL.getCode(),RespCodeEnums.FAIL.getMessage());
        }
        RRateLimiter rateLimiter=redissonClient.getRateLimiter(routeLimiter.value());
        rateLimiter.setRate(routeLimiter.rateType(),routeLimiter.rate(),routeLimiter.rateInterval(),routeLimiter.unit());
        boolean ac;
        if (0 == routeLimiter.timeout()) {
            ac=rateLimiter.tryAcquire();
        } else {
            ac=rateLimiter.tryAcquire(routeLimiter.timeout(),routeLimiter.timeoutUnit());
        }
        if (!ac) {
            throw new BaseException(RespCodeEnums.TOO_MANY_REQUESTS.getCode(),RespCodeEnums.TOO_MANY_REQUESTS.getMessage());
        }
    }
}
