package com.sw.xyz.springframework.core.thread;

import com.sw.xyz.springframework.core.filter.ThreadPoolMDCFilter;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/9/27 11:57
 */
public class ThreadPoolExecutorMdcUtil extends ThreadPoolTaskExecutor {
    //重写执行方法，将MDC塞入
    @Override
    public void execute(Runnable task) {
        super.execute(ThreadPoolMDCFilter.wrap(task,MDC.getCopyOfContextMap()));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(ThreadPoolMDCFilter.wrap(task,MDC.getCopyOfContextMap()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(ThreadPoolMDCFilter.wrap(task,MDC.getCopyOfContextMap()));
    }
}
