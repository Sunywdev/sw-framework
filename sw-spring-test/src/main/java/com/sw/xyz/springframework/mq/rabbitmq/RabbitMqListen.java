package com.sw.xyz.springframework.mq.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

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
public class RabbitMqListen {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "vb-queues", durable = "true"),
            exchange = @Exchange(name = "vb-exchange", durable = "true", type = "direct"), key = "vb-queues"
    )
    )
    @RabbitHandler
    public void listen(String msg) {
        log.info("listen监听到消息:[{}]", msg);
    }

   /* @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "de-queues", durable = "true"),
            exchange = @Exchange(name = "de-exchange", durable = "true", type = "headers"), key = "de-queues"
    )
    )
    @RabbitHandler
    public void deListen(String msg) {
        log.info("deListen监听到消息:[{}]", msg);
    }*/
}
