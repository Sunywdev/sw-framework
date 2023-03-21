package com.sw.xyz.springframework.eureka;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/01/31 17:30
 */
@Data
@ApiModel(value = "服务治理时间相关", description = "服务治理时间相关")
public class LeaseInfo {

    @ApiModelProperty(value = "服务检测时间间隔默认30", dataType = "Long")
    private long renewalIntervalInSecs;

    @ApiModelProperty(value = "设置客户端指定的逐出设置（例如等待多长时间无更新事件默认90", dataType = "Long")
    private long durationInSecs;

    @ApiModelProperty(value = "服务注册时间戳", dataType = "Long")
    private long registrationTimestamp;

    @ApiModelProperty(value = "上次续订时间戳", dataType = "Long")
    private long lastRenewalTimestamp;

    @ApiModelProperty(value = "驱逐时间戳", dataType = "Long")
    private long evictionTimestamp;

    @ApiModelProperty(value = "服务启动时间戳", dataType = "Long")
    private long serviceUpTimestamp;
}
