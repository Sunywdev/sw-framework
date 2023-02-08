package com.sw.xyz.springframework.bean.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/22 14:10
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseException extends RuntimeException {

    private String code;

    private String message;
}
