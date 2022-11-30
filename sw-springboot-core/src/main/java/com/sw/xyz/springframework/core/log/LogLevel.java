package com.sw.xyz.springframework.core.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/27 9:47
 */
@AllArgsConstructor
@Getter
public enum LogLevel {

    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR
}
