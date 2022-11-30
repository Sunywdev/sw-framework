package com.sw.xyz.springframework.mq.rabbitMq;

import com.sw.xyz.springframework.mq.base.BaseBody;
import com.sw.xyz.springframework.mq.base.SubscribeRunnable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 名称: RabbitMqSubscribeBody
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/29 0029 18:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class RabbitMqSubscribeBody<T> extends BaseBody {

    /**
     * 队列名称
     */
    private String queue;

    /**
     * 回调函数
     */
    SubscribeRunnable<T> runnable;

    /**
     * 返回参数类型
     */
    Class<T> type;
}
