package com.sw.xyz.springframework.redisson;

import cn.hutool.core.date.DateTime;
import com.sw.xyz.springframework.bean.response.BaseResponse;
import com.sw.xyz.springframework.core.annocation.Log;
import com.sw.xyz.springframework.core.log.LogLevel;
import com.sw.xyz.springframework.model.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/25 14:35
 */
@RestController
@Slf4j
@Api(tags = "redisson测试控制层")
@RequestMapping("/redisson")
public class RedissonController {


    @Autowired
    private RedissonClient redissonClient;


    @GetMapping("/delay/{value}")
    @Log(value = "redisson延迟消息", level = LogLevel.INFO)
    @ApiOperation(value = "redisson延迟消息", notes = "redisson延迟消息", response = BaseResponse.class, httpMethod = "GET")
    public BaseResponse redissonDelay(@PathVariable String value) {
        RBlockingDeque<Object> test = redissonClient.getBlockingDeque("redisson");
        RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(test);
        OrderVo build = OrderVo.builder()
                .orderAmount(new BigDecimal(2))
                .orderId(value)
                .userId(DateTime.now().toString()).build();
        delayedQueue.offer(build, 10, TimeUnit.SECONDS);
        return BaseResponse.success();
    }

    @GetMapping("/normal/{value}")
    @Log(value = "redisson普通消息", level = LogLevel.INFO)
    @ApiOperation(value = "redisson普通消息", notes = "redisson普通消息", response = BaseResponse.class, httpMethod = "GET")
    public BaseResponse redissonNormal(@PathVariable String value) {
        RBlockingDeque<Object> test = redissonClient.getBlockingDeque("redissonQueue");
        OrderVo build = OrderVo.builder()
                .orderAmount(new BigDecimal(2))
                .orderId(value)
                .userId(DateTime.now().toString()).build();
        test.offer(build);
        return BaseResponse.success();
    }
}
