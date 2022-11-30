package com.sw.xyz.springframework.mq.rabbitmq;

import com.sw.xyz.springframework.bean.response.BaseResponse;
import com.sw.xyz.springframework.core.log.Log;
import com.sw.xyz.springframework.core.log.LogLevel;
import com.sw.xyz.springframework.mq.rabbitMq.ExchangeTypeEnum;
import com.sw.xyz.springframework.mq.rabbitMq.RabbitMessageSendBody;
import com.sw.xyz.springframework.mq.rabbitMq.RabbitMqSendMsg;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 名称: RabbitMQWeb
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/29 0029 15:43
 */
@RestController
@Api(tags = "RabbitMQ测试控制层")
public class RabbitMQWeb {

    @Autowired
    private RabbitMqSendMsg rabbitMqSendMsg;


    @GetMapping("/rabbit/{msg}")
    @Log(value = "rabbitMq", level = LogLevel.INFO)
    public BaseResponse sendMsg(@PathVariable String msg){
        RabbitMessageSendBody<String> message = new RabbitMessageSendBody<>();
        message.setExchange("delay-exchange");
        message.setQueue("delay.queue.demo");
        message.setBody(msg);
        message.setRoutingKey("delay-rk-demo");
        message.setDelayTimestamp(5);
        message.setExchangeTypeEnum(ExchangeTypeEnum.DELAYED);
        rabbitMqSendMsg.sendMessage(message);
        return BaseResponse.success();
    }
}
