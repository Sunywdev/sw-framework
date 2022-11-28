package com.holly.top.springframework.cache.enums;


/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 锁类型
 * 时间: 2021/4/13 9:49
 */
public enum LockEnum {
    /**
     * 可重入锁
     */
    REENTRANT,
    /**
     * 公平锁
     */
    FAIR,
    /**
     * 联锁
     */
    MULTIPLE,
    /**
     * 红锁
     */
    REDLOCK,
    /**
     * 读锁
     */
    READ,
    /**
     * 写锁
     */
    WRITE
}
