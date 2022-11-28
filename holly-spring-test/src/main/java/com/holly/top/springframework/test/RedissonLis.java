package com.holly.top.springframework.test;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.RedisCommand;
import org.redisson.client.protocol.decoder.MultiDecoder;
import org.redisson.command.CommandAsyncExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
public class RedissonLis implements CommandLineRunner {

    @Autowired
    private RedissonClient redissonClient;


    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private Asyncu asyncu;



    @Override
    public void run(String... args) throws Exception {
        RBlockingDeque<String> blockingDeque=redissonClient.getBlockingDeque("test");
        new Thread(() -> {
            while (true) {
                //threadPoolTaskExecutor.execute(new TakeMessage(blockingDeque));
                String take=null;
                try{
                    //take=blockingDeque.take();
                    RFuture<String> stringRFuture=blockingDeque.takeAsync();
                    take=stringRFuture.get();
                    //sleepFor();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                asyncu.setf(take);

            }

        }).start();
    }

    private static class MessageDecoder<T> implements MultiDecoder<T> {

        @Override
        public Decoder<Object> getDecoder(Codec codec,int paramNum,State state) {
            return codec.getValueDecoder();
        }

        @Override
        public T decode(List<Object> parts,State state) {
            if (parts == null) {
                return null;
            }
            return (T) parts.get(0);
        }
    }
}
