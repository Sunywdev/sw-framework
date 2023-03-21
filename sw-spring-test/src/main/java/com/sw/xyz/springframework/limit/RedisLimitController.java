package com.sw.xyz.springframework.limit;

import com.sw.xyz.springframework.bean.response.BaseResponse;
import com.sw.xyz.springframework.cache.annotations.RateLimit;
import com.sw.xyz.springframework.core.annocation.Log;
import com.sw.xyz.springframework.core.log.LogLevel;
import com.sw.xyz.springframework.model.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;


/**
 * 名称: RedisLimit
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/12/2 0002 15:05
 */
@RestController
@Api(tags = "限流器使用控制层")
@RequestMapping("/limit")
public class RedisLimitController {


    @PostMapping("/normal")
    @Log(value = "limit限流普通方法", level = LogLevel.INFO)
    @ApiOperation(value = "limit限流普通方法", notes = "limit限流普通方法", response = BaseResponse.class, httpMethod = "POST")
    @RateLimit
    public BaseResponse limitNormal(@RequestBody OrderVo vo) {
        return BaseResponse.success();
    }


    @PostMapping("/spel")
    @Log(value = "limit限流spel", level = LogLevel.INFO)
    @ApiOperation(value = "limit限流spel", notes = "limit限流spel", response = BaseResponse.class, httpMethod = "POST")
    @RateLimit(spElValue = "#vo.userId")
    public BaseResponse limitSpEl(@RequestBody OrderVo vo) {
        return BaseResponse.success();
    }
}
