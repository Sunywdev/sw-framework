package com.holly.top.springframework.cache.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 名称: Redis批量方法处理
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: Redis异步批量方法处理
 * 时间: 2021/4/14 16:54
 */
@Component
public class RedisPipelinedUtils<T> {

    private Logger log=LoggerFactory.getLogger(RedisPipelinedUtils.class);

    /**
     * String RedisTemplate
     *
     * @see StringRedisTemplate
     */
    @Autowired
    private StringRedisTemplate strRedisTemplate;

    /**
     * RedisTemplate
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 批量数据插入到redis中,不要使用单个插入,多个数据时,使用管道来节省网络链接的开销
     *
     * @param listKey 插入的Key
     * @param lists   待插入的集合
     */
    public void batchInsertStringListToRedis(String listKey,List<String> lists) {
        long startTime=System.currentTimeMillis();
        strRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            StringRedisConnection stringRedisConn=(StringRedisConnection) connection;
            lists.forEach(bigDecimal -> stringRedisConn.lPush(listKey,bigDecimal));
            return null;
        });
        log.info("Redis->batchInsertStringListToRedis批量插入耗时:{}",(System.currentTimeMillis() - startTime));
    }

    /**
     * 批量数据插入到redis中,不要使用单个插入,多个数据时,使用管道来节省网络链接的开销
     *
     * @param mapKey mapKey
     * @param all    map集合
     */
    public void batchInsertMapToRedis(String mapKey,Map<String,String> all) {
        long startTime=System.currentTimeMillis();
        strRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            StringRedisConnection stringRedisConn=(StringRedisConnection) connection;
            all.forEach((key,value) -> stringRedisConn.hSet(mapKey,key,value));
            return null;
        });
        log.info("Redis->batchInsertMapToRedis批量插入耗时:{}",(System.currentTimeMillis() - startTime));
    }

    /**
     * 批量插入数据到List集合中,重载泛型方法
     *
     * @param key  listKeys
     * @param list 集合信息
     */
    public void batchInsertObjectListToRedis(String key,List<T> list) {
        long startTime=System.currentTimeMillis();
        redisTemplate.executePipelined(new SessionCallback<List<Object>>() {
            @Override
            public <K,V> List<Object> execute(final RedisOperations<K,V> operations) throws DataAccessException {
                final BoundListOperations boundListOperations=operations.boundListOps((K) key);
                if (!CollectionUtils.isEmpty(list)) {
                    list.stream().filter(t -> !ObjectUtils.isEmpty(t)).forEachOrdered(boundListOperations::leftPush);
                }
                return null;
            }
        });
        log.info("Redis->batchInsertObjectListToRedis批量插入耗时:{}",(System.currentTimeMillis() - startTime));
    }

    /**
     * 批量插入数据到List集合中,重载泛型方法
     *
     * @param key  listKeys
     * @param list 集合信息
     */
    public void batchInsbjectListToRedis(String key,List<T> list,long date,TimeUnit timeUnit) {
        long startTime=System.currentTimeMillis();
        redisTemplate.executePipelined(new SessionCallback<List<Object>>() {
            @Override
            public <K,V> List<Object> execute(final RedisOperations<K,V> operations) throws DataAccessException {
                final BoundListOperations boundListOperations=operations.boundListOps((K) key);
                Boolean expire=boundListOperations.expire(date,timeUnit);
                if (null != expire && expire) {
                    if (!CollectionUtils.isEmpty(list)) {
                        list.stream().filter(t -> !ObjectUtils.isEmpty(t)).forEachOrdered(boundListOperations::leftPush);
                    }
                }
                return null;
            }
        });
        log.info("Redis->batchInsertObjectListToRedis批量插入耗时:{}",(System.currentTimeMillis() - startTime));
    }
}
