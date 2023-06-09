package com.sw.xyz.springframework;

import com.sw.xyz.springframework.cache.annotations.EnableRedis;
import com.sw.xyz.springframework.cache.annotations.EnableRedisson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/19 14:38
 */
@SpringBootApplication(scanBasePackages = {"com.sw"})
@EnableAsync
@Slf4j
@EnableRedisson
@EnableRedis
public class App {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(App.class, args);
       /* RabbitMqBaseListen bean = run.getBean(RabbitMqBaseListen.class);
        bean.subscribe(new RabbitMqSubscribeBody()
                .setQueue("delay.queue.demo")
                .setType(String.class)
                .setRunnable(msg -> log.info("de-queue:[{}]", msg.getData()))
        );*/
    }
}
