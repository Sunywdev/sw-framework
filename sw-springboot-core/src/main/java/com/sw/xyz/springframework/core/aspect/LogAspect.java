package com.sw.xyz.springframework.core.aspect;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.xyz.springframework.core.log.Log;
import com.sw.xyz.springframework.core.log.LogLevel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/27 9:53
 */
@Aspect
@Component
public class LogAspect {


    protected static final String LEFT="=======================";

    protected static final String RIGHT="=======================";

    private ObjectMapper objectMapper=new ObjectMapper();


    @Pointcut("@annotation(com.sw.xyz.springframework.core.log.Log)")
    public void pointCut() {

    }

    @Around(value="pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        Signature signature=joinPoint.getSignature();
        MethodSignature methodSignature=(MethodSignature) signature;
        Method method=methodSignature.getMethod();
        Log annotation=method.getAnnotation(Log.class);
        if (null == annotation) {
            return joinPoint.proceed();
        }
        final long startTime=System.currentTimeMillis();
        Logger logger=LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String reqMsg=objectMapper.writeValueAsString(joinPoint.getArgs());
        print(logger,LEFT + "接口:[" + annotation.value() + "]开始执行,请求参数:" + reqMsg + RIGHT,annotation.level());
        Object proceed=joinPoint.proceed();
        print(logger,LEFT + "接口:[" + annotation.value() + "]执行完毕,响应参数:[" + objectMapper.writeValueAsString(proceed) + "]耗时:" + DateUtil.spendMs(startTime) + "ms" + RIGHT,annotation.level());
        return proceed;
    }


    private void print(Logger logger, String msg, LogLevel logLevel) {
        switch (logLevel) {
            case WARN:
                logger.warn(msg);
                break;
            case INFO:
                logger.info(msg);
                break;
            case ERROR:
                logger.error(msg);
                break;
            case TRACE:
                logger.trace(msg);
                break;
            default:
                logger.debug(msg);
                break;
        }
    }

}
