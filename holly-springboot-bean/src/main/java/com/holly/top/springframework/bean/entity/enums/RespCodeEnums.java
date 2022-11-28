package com.holly.top.springframework.bean.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 响应码按照HTTP状态码进行扩展,长度定为5为 成功状态内的提示吗,按照200XX设定,支持自定义状态码导入缓存,提供扩展
 * 时间: 2022/9/20 9:15
 */
@AllArgsConstructor
@Getter
public enum RespCodeEnums implements BaseEnums {

    /**
     * 按照五位进行扩展,以HTTP状态码为基准进行后续的扩展
     */
    OK(20000,"成功"),
    BAD_REQUEST(40000,"错误的请求"),
    UNAUTHORIZED(40100,"请求失败,未被允许的请求"),
    NOT_FOUND(40400,"请求失败,无法识别的请求"),
    TIMEOUT(40800,"请求失败,请求超时"),
    METHOD_NOT_ALLOWED(40500,"请求失败,错误的请求方式"),
    TOO_MANY_REQUESTS(42900,"请求失败,请求太快了"),
    FAIL(50000,"请求失败");


    private Integer code;

    private String message;

}
