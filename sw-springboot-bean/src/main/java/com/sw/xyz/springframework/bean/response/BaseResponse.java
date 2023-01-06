package com.sw.xyz.springframework.bean.response;

import com.sw.xyz.springframework.bean.entity.enums.SystemRespCodeEnums;
import lombok.Data;

import java.io.Serializable;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/20 9:07
 */
@Data
public class BaseResponse<T> implements Serializable {


    private Integer code= SystemRespCodeEnums.OK.getCode();


    private String message=SystemRespCodeEnums.OK.getMessage();


    private Boolean success=true;


    private T data;


    /**
     * 错误请求
     *
     * @param message
     * @return
     */
    public static BaseResponse error(String message) {
        BaseResponse commonResp=new BaseResponse();
        commonResp.setCode(SystemRespCodeEnums.FAIL.getCode());
        commonResp.setSuccess(false);
        commonResp.setMessage(message);
        return commonResp;
    }

    /**
     * 错误请求
     *
     * @param message
     * @return
     */
    public static BaseResponse error(Integer code,String message) {
        BaseResponse commonResp=new BaseResponse();
        commonResp.setCode(code);
        commonResp.setSuccess(false);
        commonResp.setMessage(message);
        return commonResp;
    }

    /**
     * 错误请求
     *
     * @return
     */
    public static BaseResponse error() {
        BaseResponse commonResp=new BaseResponse();
        commonResp.setCode(SystemRespCodeEnums.FAIL.getCode());
        commonResp.setMessage(SystemRespCodeEnums.FAIL.getMessage());
        commonResp.setSuccess(false);
        return commonResp;
    }

    /**
     * 成功请求
     *
     * @param message
     * @return
     */
    @SuppressWarnings("unchecked")
    public static BaseResponse success(String message,Object data) {
        BaseResponse commonResp=new BaseResponse();
        commonResp.setCode(SystemRespCodeEnums.OK.getCode());
        commonResp.setMessage(message);
        commonResp.setData(data);
        return commonResp;
    }

    /**
     * 成功请求
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static BaseResponse success(Object data) {
        BaseResponse commonResp=new BaseResponse();
        commonResp.setCode(SystemRespCodeEnums.OK.getCode());
        commonResp.setMessage(SystemRespCodeEnums.OK.getMessage());
        commonResp.setData(data);
        return commonResp;
    }

    /**
     * 成功请求
     *
     * @return
     */
    public static BaseResponse success() {
        BaseResponse commonResp=new BaseResponse();
        commonResp.setCode(SystemRespCodeEnums.OK.getCode());
        commonResp.setMessage(SystemRespCodeEnums.OK.getMessage());
        return commonResp;
    }

}

