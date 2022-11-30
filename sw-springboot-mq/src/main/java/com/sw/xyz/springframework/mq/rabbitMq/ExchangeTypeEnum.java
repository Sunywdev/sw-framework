package com.sw.xyz.springframework.mq.rabbitMq;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 名称: ExchangeTypeEnum
 * 功能: <交换机枚举>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/29 0029 16:39
 */
@AllArgsConstructor
@Getter
public enum ExchangeTypeEnum  {

    TOPIC("topic", "主题-topic"),
    FANOUT("fanout", "广播-fanout"),
    DIRECT("direct", "点对点-direct"),
    DELAYED("x-delayed-message", "延迟消息-delayed");

    private String code;

    private String desc;


}
