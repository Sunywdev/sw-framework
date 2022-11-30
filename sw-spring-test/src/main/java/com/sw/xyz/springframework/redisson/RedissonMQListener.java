package com.sw.xyz.springframework.redisson;

import cn.hutool.core.date.DateTime;
import com.sw.xyz.springframework.model.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.decoder.MultiDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/25 14:39
 */
@Component
@Slf4j
@Lazy
public class RedissonMQListener implements CommandLineRunner {

    @Autowired
    private RedissonClient redissonClient;


    @Override
    public void run(String... args) throws Exception {
        RBlockingDeque<OrderVo> blockingDeque = redissonClient.getBlockingDeque("redisson");
        new Thread(() -> {
            while ( true ) {
                OrderVo take;
                try {
                    RFuture<OrderVo> stringRFuture = blockingDeque.takeAsync();
                    take = stringRFuture.get();
                    log.info("--------------------延时消息监听到消息,当前时间:[{}],消息发送时间[{}]-------------------", DateTime.now(), take.getUserId());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        RBlockingDeque<OrderVo> normalDeque = redissonClient.getBlockingDeque("redissonQueue");
        new Thread(() -> {
            while ( true ) {
                OrderVo take;
                try {
                    RFuture<OrderVo> stringRFuture = normalDeque.takeAsync();
                    take = stringRFuture.get();
                    log.info("--------------------普通消息监听到消息,当前时间:[{}],消息发送时间[{}]-------------------", DateTime.now(), take.getUserId());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
