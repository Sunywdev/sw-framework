package com.holly.top.springframework.test;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/25 23:20
 */
@Slf4j
public class TakeMessage implements Runnable{

    private RBlockingDeque<String> blockingDeque;

    public TakeMessage(RBlockingDeque<String> blockingDeque) {
        this.blockingDeque=blockingDeque;
    }

    @Override
    public void run() {
        String take=null;
        try{
            take=blockingDeque.take();
            //sleepFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
