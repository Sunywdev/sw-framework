package com.sw.xyz.springframework.cache.config;

import cn.hutool.core.lang.Validator;
import com.sw.xyz.springframework.bean.entity.enums.RespCodeEnums;
import com.sw.xyz.springframework.bean.exceptions.BaseException;
import com.sw.xyz.springframework.cache.annotations.IdemPoeNce;
import com.sw.xyz.springframework.cache.annotations.RateLimit;
import com.sw.xyz.springframework.cache.redisson.RedissonBaseUtils;
import com.sw.xyz.springframework.utils.spel.SpElUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 名称: IdemPoeNceAspect
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 在一段时间节点内防止重复点击
 * 时间: 2022/12/21 0021 18:01
 */
@Aspect
@Component
@Slf4j
public class IdemPoeNceAspect {

    @Autowired
    private RedissonBaseUtils<String> baseUtils;

    @Autowired
    private SpElUtils spElUtils;


    @Pointcut(value = "@annotation(com.sw.xyz.springframework.cache.annotations.IdemPoeNce)")
    public void cut() {
    }

    @Before(value = "cut()")
    public void check(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        IdemPoeNce poeNce = method.getAnnotation(IdemPoeNce.class);
        if (null == poeNce) {
            return;
        }
        String value = buildKey(poeNce, method, joinPoint.getArgs());
        boolean result = baseUtils.TrySaveObject(value, value, poeNce.time(), TimeUnit.SECONDS);
        if (!result) {
            throw new BaseException(RespCodeEnums.REPEATABLE_REQUESTS.getCode(), RespCodeEnums.REPEATABLE_REQUESTS.getMessage());
        }
    }

    /**
     * 构建限流器的Key,由Key值加动态参数值构成
     *
     * @param rateLimit {@link RateLimit}
     * @param method    {@link Method}
     * @return String
     */
    private String buildKey(IdemPoeNce rateLimit, Method method, Object[] args) {
        String value = method.getDeclaringClass().getName() + "." + method.getName();
        String spElValue = spElUtils.parseSpEl(method, rateLimit.spElValue(), args);
        if (Validator.isNotEmpty(spElValue)) {
            value = value + "#" + spElValue;
        }
        return value;
    }
}
