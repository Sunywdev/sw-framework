//package com.holly.top.springframework.test;
//
//import com.alibaba.fastjson.JSONObject;
//import com.riven.redisson.annotation.RedissonListener;
//import com.riven.redisson.exception.MessageConversionException;
//import com.riven.redisson.message.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.handler.annotation.Payload;
//
//import java.util.Map;
//
///**
// * 名称: XX定义
// * 功能: <功能详细描述>
// * 方法: <方法简述-方法描述>
// * 版本: 1.0
// * 作者: sunyw
// * 说明: 说明描述
// * 时间: 2022/11/26 0:14
// */
//@Configuration
//public class TestLis {
//    @Bean("myMessageConverter")
//    public MessageConverter messageConverter() {
//        return new MessageConverter() {
//            @Override
//            public QueueMessage<?> toMessage(Object object,Map<String, Object> headers) throws MessageConversionException {
//                //do something you want, eg:
//                headers.put("my_header", "my_header_value");
//                return QueueMessageBuilder.withPayload(object).headers(headers).build();
//            }
//
//            @Override
//            public Object fromMessage(RedissonMessage redissonMessage) throws MessageConversionException {
//                byte[] payload = redissonMessage.getPayload();
//                String payloadStr = new String(payload);
//                return JSONObject.parseObject(payloadStr, Map.class);
//            }
//        };
//    }
//
//    @RedissonListener(queues = "riven", messageConverter = "myMessageConverter")
//    public void handler(@Header(value = RedissonHeaders.MESSAGE_ID, required = false) String messageId,
//                        @Header(RedissonHeaders.DELIVERY_QUEUE_NAME) String queue,
//                        @Header(RedissonHeaders.SEND_TIMESTAMP) long sendTimestamp,
//                        @Header(RedissonHeaders.EXPECTED_DELAY_MILLIS) long expectedDelayMillis,
//                        @Header(value = "my_header", required = false, defaultValue = "test") String myHeader,
//                        @Payload Map carLbsDto) {
//        System.out.println(messageId);
//        System.out.println(queue);
//        System.out.println(myHeader);
//        long actualDelay = System.currentTimeMillis() - (sendTimestamp + expectedDelayMillis);
//        System.out.println("receive " + carLbsDto + ", delayed " + actualDelay + " millis");
//    }
//}
