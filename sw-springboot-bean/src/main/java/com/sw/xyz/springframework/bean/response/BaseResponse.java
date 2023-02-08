package com.sw.xyz.springframework.bean.response;

import com.sw.xyz.springframework.bean.constants.SystemConstants;
import com.sw.xyz.springframework.bean.entity.enums.SystemRespCodeEnums;
import lombok.Data;
import org.slf4j.MDC;

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
//@NoArgsConstructor
public class BaseResponse<T> implements Serializable {

    /**
     * 响应码
     */
    private String code=SystemRespCodeEnums.OK.getCode();

    /**
     * 响应信息
     */
    private String message=SystemRespCodeEnums.OK.getMessage();

    /**
     * 成功标志
     */
    private Boolean success=true;

    /**
     * 系统链路跟踪标识
     */
    private String triceId;

    /**
     * 返回信息
     */
    private T data;

    /**
     * 错误请求
     *
     * @param message
     * @return
     */
    public static <T> BaseResponse<T> error(String message) {
        BaseResponse<T> commonResp = new BaseResponse<>();
        commonResp.setCode(SystemRespCodeEnums.FAIL.getCode());
        commonResp.setSuccess(false);
        commonResp.setMessage(message);
        commonResp.setTriceId(getTriceId());
        return commonResp;
    }

    /**
     * 错误请求
     *
     * @param message
     * @return
     */
    public static <T> BaseResponse<T> error(String code, String message) {
        BaseResponse<T> commonResp = new BaseResponse<>();
        commonResp.setCode(code);
        commonResp.setSuccess(false);
        commonResp.setMessage(message);
        commonResp.setTriceId(getTriceId());
        return commonResp;
    }

    /**
     * 错误请求
     *
     * @return
     */
    public static <T> BaseResponse<T> error() {
        BaseResponse<T> commonResp = new BaseResponse<>();
        commonResp.setCode(SystemRespCodeEnums.FAIL.getCode());
        commonResp.setMessage(SystemRespCodeEnums.FAIL.getMessage());
        commonResp.setSuccess(false);
        commonResp.setTriceId(getTriceId());
        return commonResp;
    }

    /**
     * 成功请求
     *
     * @param message
     * @return
     */
    public static <T> BaseResponse<T> success(String message, T data) {
        BaseResponse<T> commonResp = new BaseResponse<>();
        commonResp.setCode(SystemRespCodeEnums.OK.getCode());
        commonResp.setMessage(message);
        commonResp.setData(data);
        commonResp.setTriceId(getTriceId());
        return commonResp;
    }

    /**
     * 成功请求
     *
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> commonResp = new BaseResponse<>();
        commonResp.setCode(SystemRespCodeEnums.OK.getCode());
        commonResp.setMessage(SystemRespCodeEnums.OK.getMessage());
        commonResp.setData(data);
        commonResp.setTriceId(getTriceId());
        return commonResp;
    }

    /**
     * 成功请求
     *
     * @return
     */
    public static <T> BaseResponse<T> success() {
        BaseResponse<T> commonResp = new BaseResponse<>();
        commonResp.setCode(SystemRespCodeEnums.OK.getCode());
        commonResp.setMessage(SystemRespCodeEnums.OK.getMessage());
        commonResp.setTriceId(getTriceId());
        return commonResp;
    }

    protected static String getTriceId() {
        return MDC.get(SystemConstants.TRACE_ID_MDC_FIELD);
    }
}

