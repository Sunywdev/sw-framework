package com.sw.xyz.springframework.cache.config;

import cn.hutool.core.lang.Validator;
import com.sw.xyz.springframework.bean.entity.enums.SystemRespCodeEnums;
import com.sw.xyz.springframework.bean.exceptions.BaseException;
import com.sw.xyz.springframework.cache.annotations.RateLimit;
import com.sw.xyz.springframework.utils.spel.SpElUtils;
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

    @Autowired
    private SpElUtils spElUtils;


    @Pointcut(value = "@annotation(com.sw.xyz.springframework.cache.annotations.RateLimit)")
    public void cut() {
    }

    @Before(value = "cut()")
    public void check(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RateLimit routeLimiter = method.getAnnotation(RateLimit.class);
        if (null == routeLimiter) {
            return;
        }
        String value = buildRateKey(routeLimiter, method, joinPoint.getArgs());
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(value);
        rateLimiter.setRate(routeLimiter.rateType(), routeLimiter.rate(), routeLimiter.rateInterval(), routeLimiter.unit());
        boolean ac;
        if (0 == routeLimiter.timeout()) {
            ac = rateLimiter.tryAcquire();
        } else {
            ac = rateLimiter.tryAcquire(routeLimiter.timeout(), routeLimiter.timeoutUnit());
        }
        if (!ac) {
            throw new BaseException(SystemRespCodeEnums.TOO_MANY_REQUESTS.getCode(), SystemRespCodeEnums.TOO_MANY_REQUESTS.getMessage());
        }
    }

    /**
     * 构建限流器的Key,由Key值加动态参数值构成
     *
     * @param rateLimit {@link RateLimit}
     * @param method    {@link Method}
     * @return String
     */
    private String buildRateKey(RateLimit rateLimit, Method method, Object[] args) {
        String value = rateLimit.value();
        if (ObjectUtils.isEmpty(value)) {
            log.warn("限流器没有指定keyName,将使用默认方法路径");
            value = method.getDeclaringClass().getName() + "." + method.getName();
        }
        String spElValue = spElUtils.parseSpEl(method, rateLimit.spElValue(), args);
        if (Validator.isNotEmpty(spElValue)) {
            value = value + "#" + spElValue;
        }
        log.info("限流Key:[{}]", value);
        return value;
    }

}
