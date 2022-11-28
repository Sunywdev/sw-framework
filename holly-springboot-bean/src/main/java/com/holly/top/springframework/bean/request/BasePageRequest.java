package com.holly.top.springframework.bean.request;

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
public class BasePageRequest implements Serializable {

    /**
     * 页码数
     */
    private Integer pageNo=1;

    /**
     * 分页查询条数
     */
    private Integer pageSize=10;
}
