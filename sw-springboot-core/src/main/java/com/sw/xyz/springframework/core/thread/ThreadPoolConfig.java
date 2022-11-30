package com.sw.xyz.springframework.core.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/27 11:58
 */
@Configuration
@Slf4j
public class ThreadPoolConfig {

    @Bean({"threadPoolTaskExecutor"})
    public ThreadPoolTaskExecutor init() {
        log.info("<===============================线程池初始化配置开始===============================>");
        ThreadPoolExecutorMdcUtil executor = new ThreadPoolExecutorMdcUtil();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(2147483647);
        executor.setThreadNamePrefix("taskThread----");
        executor.setKeepAliveSeconds(3000);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        log.info("<===============================线程池初始化配置结束===============================>");
        return executor;
    }
}
