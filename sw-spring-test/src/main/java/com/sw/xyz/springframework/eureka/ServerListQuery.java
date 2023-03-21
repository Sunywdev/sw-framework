package com.sw.xyz.springframework.eureka;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/01/31 17:19
 */
@Data
@ApiModel(value = "服务列表查询", description = "服务列表查询API返回接口")
public class ServerListQuery {


    @ApiModelProperty(value = "服务名称", dataType = "String")
    private String name;


    @ApiModelProperty(value = "服务总数", dataType = "Integer")
    private Integer serverCount;


    @ApiModelProperty(value = "服务详情", dataType = "Integer")
    private List<ServerDetail> instance;

}
