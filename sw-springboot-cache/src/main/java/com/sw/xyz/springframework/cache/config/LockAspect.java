package com.sw.xyz.springframework.cache.config;

import cn.hutool.core.lang.Validator;
import com.sw.xyz.springframework.bean.entity.enums.SystemRespCodeEnums;
import com.sw.xyz.springframework.bean.exceptions.BaseException;
import com.sw.xyz.springframework.cache.annotations.Lock;
import com.sw.xyz.springframework.cache.enums.LockEnum;
import com.sw.xyz.springframework.cache.redisson.RedissonLockUtils;
import com.sw.xyz.springframework.utils.spel.SpElUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/5 16:32
 */
@Aspect
@Component
@Slf4j
public class LockAspect {

    @Autowired
    private RedissonLockUtils lockUtils;

    @Autowired
    private SpElUtils spElUtils;


    @Pointcut("@annotation(com.sw.xyz.springframework.cache.annotations.Lock)")
    private void pointCut() {

    }

    @Around(value = "pointCut()")
    public Object point(ProceedingJoinPoint point) throws Throwable {

        Method method = ((MethodSignature) point.getSignature()).getMethod();
        Lock lock = method.getAnnotation(Lock.class);
        if (null == lock) {
            return point.proceed();
        }
        String lockKey = buildLockKey(lock, method, point.getArgs());

        LockEnum model = lock.model();
        long lockTime = lock.lockTime();
        long waitLockTime = lock.waitLockTime();
        TimeUnit timeUnit = lock.timeUnit();
        RLock rLock = null;
        Object proceed;
        try {
            rLock = lockUtils.tryLock(model, timeUnit.toMillis(waitLockTime), timeUnit.toMillis(lockTime), lockKey);
            if (rLock.isLocked()) {
                proceed = point.proceed();
            } else {
                throw new BaseException(SystemRespCodeEnums.SYSTEM_IN_PROCESSING.getCode(), SystemRespCodeEnums.SYSTEM_IN_PROCESSING.getMessage());
            }
        } finally {
            if (null != rLock && rLock.isLocked()) {
                rLock.unlock();
                log.info("unlock key: [{}] success", lockKey);
            }
        }
        return proceed;
    }

    /**
     * 构建LockKey
     *
     * @param lock   {@link Lock}
     * @param method {@link Method}
     * @param args   {@link Object}
     * @return String
     */
    private String buildLockKey(Lock lock, Method method, Object[] args) {
        StringBuilder sbl = new StringBuilder();
        String prefix = lock.lockPrefix();
        sbl.append(prefix).append("#");
        String name = lock.name();
        if (Validator.isEmpty(name)) {
            name = method.getDeclaringClass().getName() + "." + method.getName();
        }
        sbl.append(name);
        String spElValue = spElUtils.parseSpEl(method, lock.spElValue(), args);
        if (Validator.isNotEmpty(spElValue)) {
            sbl.append("#").append(spElValue);
        }
        return sbl.toString();
    }


}
