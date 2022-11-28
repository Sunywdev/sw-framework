package com.holly.top.springframework.core.health;

import com.holly.top.springframework.bean.response.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/27 16:50
 */
@RestController
@Api(tags="健康检测")
public class Health {


    /**
     * 健康检查
     * @return BaseResponse
     */
    @GetMapping("/checkAlive")
    @ApiOperation(value="健康检测接口", notes="健康检查", response=BaseResponse.class, httpMethod="GET")
    public BaseResponse health() {
        return BaseResponse.success();
    }
}
