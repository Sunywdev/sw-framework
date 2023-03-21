package com.sw.xyz.springframework.lock;

import com.sw.xyz.springframework.bean.response.BaseResponse;
import com.sw.xyz.springframework.cache.annotations.Lock;
import com.sw.xyz.springframework.core.annocation.Log;
import com.sw.xyz.springframework.core.log.LogLevel;
import com.sw.xyz.springframework.model.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 名称: RedisLock
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/12/2 0002 16:03
 */
@RestController
@Api(tags = "分布式锁使用控制层")
@RequestMapping("/lock")
public class RedisLockController {

    @PostMapping("/normal")
    @Log(value = "lock分布式锁普通方法", level = LogLevel.INFO)
    @ApiOperation(value = "lock分布式锁普通方法", notes = "lock分布式锁普通方法", response = BaseResponse.class, httpMethod = "POST")
    @Lock
    public BaseResponse lockNormal(@RequestBody OrderVo vo) {
        return BaseResponse.success();
    }


    @PostMapping("/spel")
    @Log(value = "lock分布式锁spel", level = LogLevel.INFO)
    @ApiOperation(value = "lock分布式锁spel", notes = "lock分布式锁spel", response = BaseResponse.class, httpMethod = "POST")
    @Lock(lockPrefix = "lock:fd", spElValue = "#vo.orderId")
    public BaseResponse lockSpEl(@RequestBody OrderVo vo) {
        return BaseResponse.success();
    }
}
