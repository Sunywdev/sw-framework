package com.sw.xyz.springframework.mq.rabbitMq;

import cn.hutool.core.lang.Validator;
import com.sw.xyz.springframework.core.utils.GsonUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 名称: RabbitMqSendUtils
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/29 0029 17:14
 */

@Component
@Slf4j
public class RabbitMqSendMsg {

    @Autowired
    private ConnectionFactory connectionFactory;


    /**
     * 消息发送
     *
     * @param message {@link RabbitMessageSendBody}
     */
    public void sendMessage(RabbitMessageSendBody message) {
        try (Connection connection = connectionFactory.createConnection()) {
            try (Channel channel = connection.createChannel(false)) {
                bindingQueueWithExchange(channel, message);
                String msg = GsonUtils.toJsonWtihNullField(message.getBody());
                if (Validator.isNotEmpty(message.getDelayTimestamp()) && 0 < message.getDelayTimestamp()) {
                    //延迟队列
                    log.info("延迟队列:[{}],延迟时间[{}]秒",message.getQueue(),message.getDelayTimestamp());
                    Map<String, Object> headers = new HashMap<>(1);
                    headers.put("x-delay", message.getDelayTimestamp() * 1000);
                    AMQP.BasicProperties.Builder props = new AMQP.BasicProperties.Builder().headers(headers);
                    channel.basicPublish(message.getExchange(), message.getRoutingKey(), true, props.build(), msg.getBytes(StandardCharsets.UTF_8));
                } else {
                    channel.basicPublish(message.getExchange(), message.getRoutingKey(), true, null, msg.getBytes(StandardCharsets.UTF_8));
                }
            }
        } catch (Exception e) {
            log.error("发送Message失败");
        }
    }


    /**
     * 路由绑定类型
     *
     * @param channel {@link Channel}
     * @param message {@link RabbitMessageSendBody}
     * @throws IOException 创建channel异常
     */
    private void bindingQueueWithExchange(Channel channel, RabbitMessageSendBody message) throws IOException {
        ExchangeTypeEnum exchangeType = message.getExchangeTypeEnum();
        if (exchangeType == ExchangeTypeEnum.DIRECT) {
            channel.exchangeDeclare(message.getExchange(), BuiltinExchangeType.DIRECT, true, false, null);
        } else if (exchangeType == ExchangeTypeEnum.TOPIC) {
            channel.exchangeDeclare(message.getExchange(), BuiltinExchangeType.TOPIC, true, false, null);
        } else if (exchangeType == ExchangeTypeEnum.FANOUT) {
            channel.exchangeDeclare(message.getExchange(), BuiltinExchangeType.FANOUT, true, false, null);
        } else if (exchangeType == ExchangeTypeEnum.DELAYED) {
            Map<String, Object> args = new HashMap<>(1);
            args.put("x-delayed-type", "direct");
            channel.exchangeDeclare(message.getExchange(), BuiltinExchangeType.HEADERS, true, false, args);
        }
        Map<String, Object> argsMap = new HashMap<>(2);
        argsMap.put("x-dead-letter-exchange", "dead.letter.exchange");
        argsMap.put("x-dead-letter-routing-key", "dead.letter.routing.key");
        Map<String, Object> args = message.getEnableDeadQueue() ? argsMap : null;
        channel.queueDeclare(message.getQueue(), true, false, false, args);
        channel.queueBind(message.getQueue(), message.getExchange(), message.getRoutingKey());
    }
}
