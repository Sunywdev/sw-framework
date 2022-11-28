package com.holly.top.springframework;

import com.holly.top.springframework.cache.annotations.EnableRedisson;
import org.redisson.RedissonQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
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
@SpringBootApplication(scanBasePackages={"com.holly"})
@EnableAspectJAutoProxy
@EnableAsync
@EnableRedisson
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }
}
