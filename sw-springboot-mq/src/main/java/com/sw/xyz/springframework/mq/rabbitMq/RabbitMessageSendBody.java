package com.sw.xyz.springframework.mq.rabbitMq;

import com.sw.xyz.springframework.mq.base.BaseBody;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 名称: RabbitMessageSendBody
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/29 0029 16:39
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RabbitMessageSendBody<T> extends BaseBody {

    /**
     * 消息体
     */
    private T body;


    /**
     * 队列名称
     */
    private String queue;

    /**
     * 消息唯一ID
     */
    private String messageId;

    /**
     * 消息主题
     */
    private String exchange;

    /**
     * 消息路由规则
     */
    private String routingKey;

    /**
     * 延迟的时间,默认为0不延迟 单位秒,首先需要先创建好延迟队列
     */
    private Integer delayTimestamp;

    /**
     * 消息失败是否放入死信队列中,默认不放
     */
    private Boolean enableDeadQueue = false;
}
