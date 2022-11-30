package com.sw.xyz.springframework.rabbitmq;

import cn.hutool.core.date.DateTime;
import com.sw.xyz.springframework.model.OrderVo;
import com.sw.xyz.springframework.bean.response.BaseResponse;
import com.sw.xyz.springframework.core.log.Log;
import com.sw.xyz.springframework.core.log.LogLevel;
import com.sw.xyz.springframework.mq.rabbitMq.RabbitMessageSendBody;
import com.sw.xyz.springframework.mq.rabbitMq.RabbitMqSend;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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
@RequestMapping("/rabbit")
public class RabbitMQController {

    @Autowired
    private RabbitMqSend rabbitMqSend;

    @GetMapping("/delay/{msg}")
    @Log(value = "rabbitMq延迟消息", level = LogLevel.INFO)
    @ApiOperation(value = "rabbitMq延迟消息", notes = "rabbitMq延迟消息", response = BaseResponse.class, httpMethod = "GET")
    public BaseResponse sendMsg(@PathVariable String msg) {
        RabbitMessageSendBody<OrderVo> message = new RabbitMessageSendBody<>();
        message.setExchange("delays_exchange");
        message.setQueue("delays_queue");
        message.setDelayTimestamp(10);
        OrderVo build = OrderVo.builder()
                .orderAmount(new BigDecimal(2))
                .orderId(msg)
                .userId(DateTime.now().toString()).build();
        message.setBody(build);
        rabbitMqSend.sendMessage(message);
        return BaseResponse.success();
    }


    @GetMapping("/normal/{msg}")
    @Log(value = "rabbitMq普通消息", level = LogLevel.INFO)
    @ApiOperation(value = "rabbitMq普通消息", notes = "rabbitMq普通消息", response = BaseResponse.class, httpMethod = "GET")
    public BaseResponse rabbit_base(@PathVariable String msg) {
        RabbitMessageSendBody<OrderVo> message = new RabbitMessageSendBody<>();
        message.setExchange("pay_exchange");
        message.setQueue("pay_queue");
        message.setRoutingKey("pay-routing-1");
        OrderVo build = OrderVo.builder()
                .orderAmount(new BigDecimal(2))
                .orderId(msg)
                .userId(DateTime.now().toString()).build();
        message.setBody(build);
        rabbitMqSend.sendMessage(message);
        return BaseResponse.success();
    }
}
