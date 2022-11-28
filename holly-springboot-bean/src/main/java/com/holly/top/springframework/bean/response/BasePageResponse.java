package com.holly.top.springframework.bean.response;

import com.holly.top.springframework.bean.entity.enums.RespCodeEnums;
import lombok.Data;

import java.io.Serializable;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/20 9:08
 */
@Data
public class BasePageResponse<T> extends BaseResponse implements Serializable {

    /**
     * 页码数
     */
    private Integer pageNo;

    /**
     * 每页显示个数
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 错误请求
     */
    public static BasePageResponse error(String message) {
        BasePageResponse commonResp=new BasePageResponse();
        commonResp.setCode(RespCodeEnums.FAIL.getCode());
        commonResp.setSuccess(false);
        commonResp.setMessage(message);
        return commonResp;
    }

    /**
     * 错误请求
     */
    public static BasePageResponse error(Integer code,String message) {
        BasePageResponse commonResp=new BasePageResponse();
        commonResp.setCode(code);
        commonResp.setSuccess(false);
        commonResp.setMessage(message);
        return commonResp;
    }

    /**
     * 错误请求
     */
    public static BasePageResponse error() {
        BasePageResponse commonResp=new BasePageResponse();
        commonResp.setCode(RespCodeEnums.FAIL.getCode());
        commonResp.setMessage(RespCodeEnums.FAIL.getMessage());
        commonResp.setSuccess(false);
        return commonResp;
    }

    /**
     * 成功请求
     */

    public static BasePageResponse success(String message,Object data,Integer pageNo,Integer pageSize,Integer totalPages) {
        return successPageResp(message,data,pageNo,pageSize,totalPages);
    }


    /**
     * 成功请求
     */
    public static BasePageResponse success(Object data,Integer pageNo,Integer pageSize,Integer totalPages) {
        return successPageResp(RespCodeEnums.OK.getMessage(),data,pageNo,pageSize,totalPages);
    }

    /**
     * 成功请求
     */
    public static BasePageResponse success(Integer pageNo,Integer pageSize,Integer totalPages) {
        BasePageResponse commonResp=new BasePageResponse();
        commonResp.setCode(RespCodeEnums.OK.getCode());
        commonResp.setMessage(RespCodeEnums.OK.getMessage());
        commonResp.setPageNo(pageNo);
        commonResp.setPageSize(pageSize);
        commonResp.setTotalPages(totalPages);
        return commonResp;
    }


    @SuppressWarnings("unchecked")
    private static BasePageResponse successPageResp(String message,Object data,Integer pageNo,Integer pageSize,Integer totalPages) {
        BasePageResponse commonResp=new BasePageResponse();
        commonResp.setCode(RespCodeEnums.OK.getCode());
        commonResp.setMessage(message);
        commonResp.setData(data);
        commonResp.setPageNo(pageNo);
        commonResp.setPageSize(pageSize);
        commonResp.setTotalPages(totalPages);
        return commonResp;
    }
}
