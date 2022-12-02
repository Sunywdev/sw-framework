package com.sw.xyz.springframework.cache.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 名称: Redisson 操作 Redis
 * 功能: 获取操作Redis的数据对象
 * Redisson 获取连接的数据对象， 可直接对Redis数据进行操作
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2021/12/7 10:05
 */
@Component
@Slf4j
public class RedissonBaseUtils<T> {

    private final RedissonClient redissonClient;

    public RedissonBaseUtils(RedissonClient redissonClient) {
        this.redissonClient=redissonClient;
    }


    /**
     * 获取自增序列
     *
     * @param key 键
     * @return String
     */
    public long getSequence(String key) {

        RAtomicLong bucket=redissonClient.getAtomicLong(key);
        long seq=bucket.incrementAndGet();
        if (seq == 1) {
            //初始化，设定过期时间为 1 天
            log.debug("初始化序列key:{}，设定有效期1天!",key);
            bucket.expire(1,TimeUnit.DAYS);
        }
        return seq;
    }

    /**
     * 获取自增序列,自定义过期时间
     *
     * @param key 键
     * @return String
     */
    public long getSequence(String key,Long time,TimeUnit timeUnit) {

        RAtomicLong bucket=redissonClient.getAtomicLong(key);
        long seq=bucket.incrementAndGet();
        if (seq == 1) {
            bucket.expire(time,timeUnit);
        }
        return seq;
    }

    /**
     * 保存Object
     *
     * @param key      键
     * @param value    值
     * @param time     过期时间
     * @param timeUnit 过期时间单位
     * @return true success
     */
    public boolean saveObject(String key,T value,Long time,TimeUnit timeUnit) {
        try{
            RBucket<Object> bucket=redissonClient.getBucket(key);
            bucket.set(value,time,timeUnit);
            return true;
        } catch (Exception e) {
            log.error("saveValue时异常",e);
        }
        return false;
    }
    /**
     * 尝试保存Object,当已经存在一个值时,返回false
     *
     * @param key      键
     * @param value    值
     * @param time     过期时间
     * @param timeUnit 过期时间单位
     * @return true success
     */
    public boolean TrySaveObject(String key,T value,Long time,TimeUnit timeUnit) {
        try{
            RBucket<Object> bucket=redissonClient.getBucket(key);
            return bucket.trySet(value,time,timeUnit);
        } catch (Exception e) {
            log.error("saveValue时异常",e);
        }
        return false;
    }

    /**
     * 保存Object 无过期时间
     *
     * @param key   键
     * @param value 值
     * @return true success
     */
    public boolean saveObject(String key,T value) {
        try{
            RBucket<T> bucket=redissonClient.getBucket(key);
            bucket.set(value);
            return true;
        } catch (Exception e) {
            log.error("saveValue时异常",e);
        }
        return false;
    }

    /**
     * 获取值
     *
     * @param key key
     * @return Object
     */
    public T getValue(String key) {
        return (T) redissonClient.getBucket(key).get();
    }



    /**
     * 获取Map集合数据
     *
     * @param key key
     * @return Map<String,T>
     */
    public Map<String,T> getMapValue(String key) {
        return redissonClient.getMapCache(key);
    }


    /**
     * 获取Map集合数据
     *
     * @param key key
     * @return Map<String,T>
     */
    public T getMapValueOne(String key,String mapKey) {
        RMapCache<Object,Object> mapCache=redissonClient.getMapCache(key);
        if (null == mapCache) {
            return null;
        }
        return (T) mapCache.get(mapKey);
    }

    /**
     * 删除指定的key值
     *
     * @param keys key
     * @return true成功
     */
    public long deleteKey(String... keys) {
        return redissonClient.getKeys().delete(keys);
    }


    /**
     * 异步删除指定的key值
     *
     * @param keys keys
     */
    public void deleteKeyAsync(String... keys) {
        redissonClient.getKeys().deleteAsync(keys);
    }


    /**
     * 获取list集合
     *
     * @param key key
     * @return List<T>
     */
    public List<T> getListValue(String key) {
        return redissonClient.getList(key);
    }


    /**
     * 是否包含指定keys
     *
     * @param keys
     * @return true存在
     */
    public boolean containsKey(String... keys) {
        return redissonClient.getKeys().countExists(keys) > 0;
    }
}
