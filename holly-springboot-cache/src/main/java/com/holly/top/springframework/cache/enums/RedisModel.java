package com.holly.top.springframework.cache.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/8/22 9:36
 */
@Getter
@AllArgsConstructor
public enum RedisModel {


    SINGLE("SINGLE","单机"),
    CLUSTER("CLUSTER","集群"),
    SENTINEL("SENTINEL","哨兵");

    private String code;

    private String desc;

    /**
     * 获取构建模式
     *
     * @return String
     */
    public static RedisModel buildModel(RedisProperties redisProperties) {
        if (null != redisProperties.getCluster()) {
            return RedisModel.CLUSTER;
        }
        if (null != redisProperties.getSentinel()) {
            return RedisModel.SENTINEL;
        }
        return RedisModel.SINGLE;
    }
}
