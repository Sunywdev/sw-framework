package com.sw.xyz.springframework.mq.rabbitMq;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 名称: RabbitMqSend
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/30 0030 14:53
 */
@Slf4j
@Component
public class RabbitMqSend {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqSend(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    /**
     * 发送消息
     *
     * @param message {@link RabbitMessageSendBody}
     */
    public void sendMessage(RabbitMessageSendBody message) {
        if (Validator.isEmpty(message.getMessageId())) {
            message.setMessageId(IdUtil.objectId());
        }
        CorrelationData correlationData = new CorrelationData(message.getMessageId());
        if (Validator.isNotEmpty(message.getDelayTimestamp())
                && 0 < message.getDelayTimestamp()) {
            //延迟消息发送
            log.info("延迟消息发送,延迟时间[{}]秒,MSG-ID:[{}]", message.getDelayTimestamp(), message.getMessageId());
            rabbitTemplate.convertAndSend(message.getExchange(), message.getRoutingKey(), message.getBody(), properties -> {
                properties.getMessageProperties().setDelay(message.getDelayTimestamp() * 1000);
                properties.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return properties;
            }, correlationData);
        } else {
            log.info("普通消息发送,MSG-ID:[{}]", message.getMessageId());
            rabbitTemplate.convertAndSend(message.getExchange(), message.getRoutingKey(), message.getBody(), correlationData);
        }
    }
}
