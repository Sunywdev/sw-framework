package com.sw.xyz.springframework.mq.annotations;

import com.sw.xyz.springframework.mq.rabbitMq.RabbitMqConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/8/22 9:23
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RabbitMqConfig.class})
public @interface EnableRabbitMq {
}
