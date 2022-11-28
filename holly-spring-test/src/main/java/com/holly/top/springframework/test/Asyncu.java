package com.holly.top.springframework.test;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/25 23:02
 */
@Component
@Slf4j

public class Asyncu {

    @Autowired
    private RedissonClient redissonClient;

    @Async
    public void set(String value) {
        RBlockingDeque<Object> test=redissonClient.getBlockingDeque("test");
        RDelayedQueue<Object> delayedQueue=redissonClient.getDelayedQueue(test);
        delayedQueue.offer(value,10,TimeUnit.SECONDS);
        log.info("添加[{}]成功",value);
    }




    @Async
    public void setf(String valie) {
        log.info("取出ID为:{}",valie);
    }
}
