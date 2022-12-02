package com.sw.xyz.springframework.cache.annotations;

import com.sw.xyz.springframework.cache.enums.LockEnum;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/5 16:24
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {


    /**
     * 加锁前缀默认为lock:
     *
     * @return
     */
    String lockPrefix() default "lock:";

    /**
     * 加锁参数值,默认为对象的路径信息
     *
     * @return
     */
    String name() default "";

    /**
     * spEl表达式
     *
     * @return
     */
    String[] spElValue() default "";

    /**
     * 加锁时间类型默认为秒
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 加锁时间默认三十秒
     *
     * @return
     */
    long lockTime() default 30;

    /**
     * 加锁时间默认三十秒
     *
     * @return
     */
    long waitLockTime() default 5;

    /**
     * 锁类型默认可重入锁
     *
     * @return
     */
    LockEnum model() default LockEnum.REENTRANT;
}
