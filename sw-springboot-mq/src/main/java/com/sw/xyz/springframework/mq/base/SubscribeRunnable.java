package com.sw.xyz.springframework.mq.base;

/**
 * 名称: SubscribeRunnable
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/29 0029 18:13
 */
public interface SubscribeRunnable<T> {

    void run(Message<T> msg);
}
