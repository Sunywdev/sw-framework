package com.sw.xyz.springframework.webflux.security.exceptions;

import cn.dev33.satoken.exception.SaTokenException;
import com.sw.xyz.springframework.bean.entity.enums.SystemRespCodeEnums;
import com.sw.xyz.springframework.bean.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/22 16:39
 */
@RestControllerAdvice
@Order(1)
public class NotLoginExceptionHandler {


    private Logger log=LoggerFactory.getLogger(NotLoginExceptionHandler.class);



    /**
     * 未授权
     *
     * @param e NotLoginException
     * @return BaseResponse
     */
    @ExceptionHandler(SaTokenException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse notLoginException(SaTokenException e) {
        log.error("MissingServletRequestParameterException异常信息,未授权:{}", e.getMessage(), e);
        return BaseResponse.error(SystemRespCodeEnums.UNAUTHORIZED.getCode(), SystemRespCodeEnums.UNAUTHORIZED.getMessage());
    }
}
