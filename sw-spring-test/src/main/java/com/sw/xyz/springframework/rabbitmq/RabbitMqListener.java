package com.sw.xyz.springframework.rabbitmq;

import cn.hutool.core.date.DateTime;
import com.rabbitmq.client.Channel;
import com.sw.xyz.springframework.model.OrderVo;
import com.sw.xyz.springframework.utils.json.FastJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


/**
 * 名称: RabbitMqListen
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/29 0029 15:54
 */
@Component
@Slf4j
public class RabbitMqListener {

    /**
     * 普通队列消费
     *
     * @param orderVo {@link OrderVo}
     * @param headers {@link Map}
     * @param channel {@link Channel}
     */
    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pay_queue", durable = "true"),
            exchange = @Exchange(name = "pay_exchange"),
            key = "pay-routing-*"
    )
    )
    public void listenPayRouting1(@Payload OrderVo orderVo, @Headers Map<String, Object> headers, Channel channel) {
        try {
            log.info("pay_queue监听到消息:[{}]", FastJsonUtil.tojson(orderVo));
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 延迟队列消费,手动ACK场景
     *
     * @param orderVo {@link OrderVo}
     * @param headers {@link Map}
     * @param channel {@link Channel}
     * @apiNote delayed = "true" 是否为延迟队列,使用时需要确保RabbitMq已经安装了延迟队列插件
     */
    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "delays_queue", durable = "true"),
            exchange = @Exchange(name = "delays_exchange", delayed = "true")
    )
    )
    public void direct(@Payload OrderVo orderVo, @Headers Map<String, Object> headers, Channel channel) {
        try {
            log.info("--------------------delays_queue监听到消息:开始时间:[{}],消息发送时间[{}]-------------------", DateTime.now(), orderVo.getUserId());
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            log.error("消息消费失败", e);
        }
    }
}
