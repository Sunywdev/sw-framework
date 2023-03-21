package com.sw.xyz.springframework.eureka;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/01/31 17:23
 */
@Data
@ApiModel(value = "服务详情", description = "服务详情API")
public class ServerDetail {

    @ApiModelProperty(value = "服务实例ID", dataType = "String")
    private String instanceId;

    @ApiModelProperty(value = "服务地址名称", dataType = "String")
    private String hostName;

    @ApiModelProperty(value = "服务名称", dataType = "String")
    private String app;

    @ApiModelProperty(value = "服务IP地址", dataType = "String")
    private String ipAddr;

    @ApiModelProperty(value = "服务端口号", dataType = "long")
    private long port;

    @ApiModelProperty(value = "服务状态", dataType = "String")
    private String status;

    @ApiModelProperty(value = "服务状态描述", dataType = "String")
    private String statusDesc;

    @ApiModelProperty(value = "服务时间相关", dataType = "LeaseInfo")
    private LeaseInfo leaseInfo;

    @ApiModelProperty(value = "服务首页地址", dataType = "String")
    private String homePageUrl;

    @ApiModelProperty(value = "服务状态查询地址", dataType = "String")
    private String statusPageUrl;

    @ApiModelProperty(value = "服务健康检查地址", dataType = "String")
    private String healthCheckUrl;

    @ApiModelProperty(value = "服务元数据", dataType = "String")
    private Map<String, Object> metaData;
}
