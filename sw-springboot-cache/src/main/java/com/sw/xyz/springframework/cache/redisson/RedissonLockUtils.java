package com.sw.xyz.springframework.cache.redisson;

import com.sw.xyz.springframework.bean.entity.enums.RespCodeEnums;
import com.sw.xyz.springframework.bean.exceptions.BaseException;
import com.sw.xyz.springframework.cache.enums.LockEnum;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2021/6/16 14:47
 */
@Component
@Slf4j
public class RedissonLockUtils {


    @Value(value="${lock.prefix:sw.lock}")
    private String lockPrefix;


    private static final String LOCK="LOCK::%s";

    /**
     * 加锁超时时间默认2秒
     */
    private static final Long attemptTimeout=2000L;

    /**
     * 锁持有时间默认30秒
     */
    private static final Long leaseTime=30000L;

    public RedissonLockUtils(@Qualifier(value="redissonClient") RedissonClient redissonClient) {
        this.redissonClient=redissonClient;
    }

    private final RedissonClient redissonClient;

    /**
     * 加锁，一直等待获取锁，无自动释放锁时间 默认可重入锁,watchDog默认过期时间为30S,若超时业务未执行完毕,则会自动延期,延期时间为30S
     *
     * @param keys 键，根据模式生成list或string
     * @return 锁
     */
    public RLock tryLock(String... keys) {
        if (keys.length > 1) {
            return tryLock(LockEnum.MULTIPLE,null,null,keys);
        }
        return tryLock(LockEnum.REENTRANT,null,null,keys);
    }

    /**
     * 加锁，默认加锁时间2S,自动释放时间30S， 需指定锁类型
     *
     * @param lockModel 锁的模式
     * @param keys      键，根据模式生成list或string
     * @return 锁
     */
    public RLock tryLock(LockEnum lockModel,String... keys) {
        return tryLock(lockModel,attemptTimeout,leaseTime,keys);
    }

    /**
     * 加锁，设置等待加锁超时时间，无自动释放锁时间
     *
     * @param lockModel      锁的模式
     * @param attemptTimeout 等待加锁超时时间
     * @param keys           键，根据模式生成list或string
     * @return 锁
     */
    public RLock tryLock(LockEnum lockModel,Long attemptTimeout,String... keys) {
        return tryLock(lockModel,attemptTimeout,null,keys);
    }

    /**
     * 加锁，设置等待加锁超时时间，设置自动释放锁时间
     *
     * @param lockModel      锁的模式
     * @param attemptTimeout 等待加锁超时时间
     * @param leaseTime      自动释放锁的时间
     * @param keys           键，根据模式生成list或string
     * @return 锁
     */
    public RLock tryLock(LockEnum lockModel,Long attemptTimeout,Long leaseTime,String... keys) {
        RLock lock=getLock(lockModel,keys);
        boolean res;
        if (leaseTime == null) {
            leaseTime=-1L;
        }
        if (attemptTimeout == null || attemptTimeout <= -1) {
            lock.lock();
            res=true;
        } else {
            try{
                res=lock.tryLock(attemptTimeout,leaseTime,TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                log.error("获取锁失败:{}",e.getMessage(),e);
                Thread.currentThread().interrupt();
                throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),"获取锁异常");
            }
        }
        if (!res) {
            throw new BaseException(RespCodeEnums.BAD_REQUEST.getCode(),"流程执行中,请稍后重试");
        }
        return lock;
    }


    /**
     * 释放锁
     */
    public void unlock(RLock rLock) {
        if (rLock.isHeldByCurrentThread()) {
            rLock.unlock();
            log.info("锁释放成功--------------->{}",rLock.getName());
        }
    }

    private RLock getLock(LockEnum lockEnum,String... args) {
        List<String> keyList=Arrays.asList(args);
        String key=String.format("%s:%s",lockPrefix,String.format(LOCK,String.join(":",keyList)));
        List<String> prefixList=keyList.stream().map(m -> String.format("%s:%s",lockPrefix,String.format(LOCK,m))).collect(Collectors.toList());
        return buildLock(lockEnum,key,prefixList);
    }


    /**
     * 构建锁
     */
    private RLock buildLock(LockEnum lockEnum,String key,List<String> prefixList) {
        RLock rLock;
        switch (lockEnum) {
            case FAIR:
                rLock=fairLock(key);
                break;
            case REDLOCK:
                rLock=redLock(prefixList);
                break;
            case MULTIPLE:
                rLock=multiLock(prefixList);
                break;
            case REENTRANT:
                rLock=reentrantLock(key);
                break;
            case WRITE:
                rLock=writeLock(key);
                break;
            default:
                rLock=readLock(key);
                break;
        }
        if (rLock == null) {
            log.error("获取锁类型失败---> rLock ==null");
            throw new RuntimeException("获取锁类型失败");
        }
        return rLock;
    }


    /**
     * 公平锁
     */
    private RLock fairLock(String key) {
        return redissonClient.getFairLock(key);
    }

    /**
     * 可重入锁
     */
    private RLock reentrantLock(String key) {
        return redissonClient.getLock(key);
    }

    /**
     * 红锁
     */
    private RLock redLock(List<String> keys) {
        RLock[] locks=keys.stream().filter(f -> f != null && !"".equals(f)).map(redissonClient::getLock).toArray(RLock[]::new);
        return new RedissonRedLock(locks);
    }

    /**
     * 联锁
     */
    private RLock multiLock(List<String> keys) {
        RLock[] locks=keys.stream().filter(f -> f != null && !"".equals(f)).map(redissonClient::getLock).toArray(RLock[]::new);
        return new RedissonMultiLock(locks);
    }

    /**
     * 读锁
     */
    private RLock readLock(String key) {
        return redissonClient.getReadWriteLock(key).readLock();
    }

    /**
     * 写锁
     */
    private RLock writeLock(String key) {
        return redissonClient.getReadWriteLock(key).writeLock();
    }


}
