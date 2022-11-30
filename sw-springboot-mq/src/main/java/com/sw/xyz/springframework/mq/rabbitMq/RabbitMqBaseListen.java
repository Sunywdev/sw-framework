package com.sw.xyz.springframework.mq.rabbitMq;

import com.sw.xyz.springframework.core.utils.GsonUtils;
import com.sw.xyz.springframework.mq.base.Message;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 名称: RabbitMqBaseListen
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/29 0029 18:09
 */
@Component
@Slf4j
public class RabbitMqBaseListen {

    @Autowired
    private ConnectionFactory connectionFactory;


    public void subscribe(RabbitMqSubscribeBody subscribeBody) {
        Connection connection = null;
        Channel channel = null;
        try {
            connection = connectionFactory.createConnection();
            channel = connection.createChannel(false);
            // 消费消息
            channel.basicConsume(subscribeBody.getQueue(), false, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body)
                        throws IOException {
                    String routingKey = envelope.getRoutingKey();
                    String exchange = envelope.getExchange();
                    long deliveryTag = envelope.getDeliveryTag();
                    String messageId = properties.getMessageId();
                    String message = new String(body);
                    try {
                        if (subscribeBody.getType() == String.class) {
                            subscribeBody.getRunnable().run(new Message(messageId, exchange, routingKey, message));
                        } else {
                            subscribeBody.getRunnable().run(new Message(messageId, exchange, routingKey, GsonUtils.fromJson(message, subscribeBody.getType())));
                        }
                        this.getChannel().basicAck(deliveryTag, false);
                    } catch (Exception ex) {
                        this.getChannel().basicReject(envelope.getDeliveryTag(), false);
                    }
                }
            });

        } catch (IOException ex) {
            // 释放资源
            try {
                connection.close();
                channel.close();
            } catch (Exception ignored) {
            }
        }
    }
}