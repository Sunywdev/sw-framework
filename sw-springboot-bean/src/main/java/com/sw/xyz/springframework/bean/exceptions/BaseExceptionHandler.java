package com.sw.xyz.springframework.bean.exceptions;

import com.sw.xyz.springframework.bean.entity.enums.RespCodeEnums;
import com.sw.xyz.springframework.bean.response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

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
public class BaseExceptionHandler {


    private Logger log=LoggerFactory.getLogger(BaseExceptionHandler.class);

    /**
     * 捕获Exception
     *
     * @param e Exception
     * @return CommonResponse
     */
    @ExceptionHandler(value=Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse invoke(Exception e) {
        log.error("Exception异常信息:{}",e.getMessage(),e);
        return BaseResponse.error();
    }


    /**
     * 捕获CommonException
     *
     * @param e CommonException
     * @return CommonResponse
     */
    @ExceptionHandler(value=BaseException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse invoke(BaseException e) {
        log.error("BaseException 异常信息:{}",e.getMessage(),e);
        return BaseResponse.error(e.getCode(),e.getMessage());
    }


    /**
     * 参数验证异常处理
     *
     * @param ex Exception
     * @return CommonResponse
     */
    @ResponseBody
    @ExceptionHandler({BindException.class,MethodArgumentNotValidException.class,ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse argumentValidationHandler(Exception ex) {
        String errorMsg="参数验证错误";
        BindingResult bindingResult=null;
        if (ex instanceof BindException) {
            BindException bindException=(BindException) ex;
            bindingResult=bindException.getBindingResult();
        }
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException=(MethodArgumentNotValidException) ex;
            bindingResult=methodArgumentNotValidException.getBindingResult();
        }
        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException=(ConstraintViolationException) ex;
            errorMsg=constraintViolationException.getMessage();
        }
        if (bindingResult != null) {
            for (FieldError filedError : bindingResult.getFieldErrors()) {
                errorMsg=filedError.getDefaultMessage();
            }
        }
        log.error("参数验证异常信息:{}",errorMsg,ex);
        return BaseResponse.error(RespCodeEnums.BAD_REQUEST.getCode(),errorMsg);
    }

    /**
     * http请求的方法不正确
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public BaseResponse httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException异常信息,请求的方法不正确:{}",e.getMessage(),e);
        return BaseResponse.error(RespCodeEnums.BAD_REQUEST.getCode(),"HTTP请求方式错误");
    }

    /**
     * 请求参数缺少
     *
     * @param e MissingServletRequestParameterException
     * @return CommonResponse
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public BaseResponse missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException异常信息,请求参数缺失:{}",e.getMessage(),e);
        return BaseResponse.error(RespCodeEnums.BAD_REQUEST.getCode(),"请求缺少参数");
    }
}
