package com.sw.xyz.springframework.mq.base;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 名称: Message
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/29 0029 18:13
 */
@Data
@AllArgsConstructor
public class Message<T> implements Serializable {

    private static final long serialVersionUID = 601755029278159274L;
    private String msgId;
    private String topic;
    private String tag;
    private T data;
}
