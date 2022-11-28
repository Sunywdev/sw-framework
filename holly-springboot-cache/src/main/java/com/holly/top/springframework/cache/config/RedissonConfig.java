package com.holly.top.springframework.cache.config;

import com.holly.top.springframework.cache.enums.RedisModel;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;


/**
 * 名称: 功能定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: Yef
 * 说明: 说明描述
 * 时间: 2021/12/6 下午6:45
 */
@Slf4j
public class RedissonConfig {

    /**
     * 构造器注入
     */
    @Autowired
    private RedisProperties redisProperties;

    /*public RedissonConfig(RedisProperties redisProperties) {
        this.redisProperties=redisProperties;
    }*/

    /**
     * 编码：默认值org.redisson.codec.JsonJacksonCodec
     * 修改可见:org.redisson.codec包下
     */
    private static final String codec="org.redisson.codec.JsonJacksonCodec";

    @Bean("redissonClient")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient() {
        log.info("加载Redisson配置：[ ==============================BEGIN============================== ]");
        String password=redisProperties.getPassword();
        Config config=new Config();
        try{
            config.setCodec((Codec) Class.forName(codec).newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        config.setLockWatchdogTimeout(30000L);
        RedisModel model=RedisModel.buildModel(redisProperties);
        switch (model) {
            case SINGLE:
                log.info("Redisson服务配置为==============================:[ 单机 ]");
                SingleServerConfig singleServerConfig=config.useSingleServer();
                singleServerConfig.setPingConnectionInterval(1000);
                singleServerConfig.setAddress(prefixAddress(redisProperties.getHost().concat(":") + redisProperties.getPort()));
                singleServerConfig.setDatabase(redisProperties.getDatabase());
                singleServerConfig.setPassword(password);
                if (null != redisProperties.getTimeout()) {
                    singleServerConfig.setTimeout(Integer.parseInt(String.valueOf(redisProperties.getTimeout().toMillis())));
                }
                if (null != redisProperties.getLettuce()) {
                    RedisProperties.Lettuce lettuce=redisProperties.getLettuce();
                    if (null !=lettuce.getPool()){
                        singleServerConfig.setConnectionPoolSize(lettuce.getPool().getMaxIdle());
                        singleServerConfig.setConnectionMinimumIdleSize(lettuce.getPool().getMinIdle());
                    }
                }
                break;
            case CLUSTER:
                log.info("Redis服务配置为==============================:[ 集群 ]");
                ClusterServersConfig clusterServersConfig=config.useClusterServers();
                clusterServersConfig.setPingConnectionInterval(1000);
                clusterServersConfig.setReadMode(ReadMode.SLAVE);
                List<String> clusterNodes=redisProperties.getCluster().getNodes();
                clusterServersConfig.setPassword(password);
                for (String nodeAddress : clusterNodes) {
                    clusterServersConfig.addNodeAddress(prefixAddress(nodeAddress));
                }
                break;
            case SENTINEL:
                log.info("Redis服务配置为==============================:[ 哨兵 ]");
                SentinelServersConfig sentinelServersConfig=config.useSentinelServers();
                sentinelServersConfig.setMasterName(redisProperties.getSentinel().getMaster());
                sentinelServersConfig.setReadMode(ReadMode.SLAVE);
                List<String> nodes=redisProperties.getSentinel().getNodes();
                for (String nodeAddress : nodes) {
                    sentinelServersConfig.addSentinelAddress(prefixAddress(nodeAddress));
                }
                if (null != redisProperties.getLettuce()) {
                    RedisProperties.Lettuce lettuce=redisProperties.getLettuce();
                    if (null !=lettuce.getPool()){
                        sentinelServersConfig.setMasterConnectionPoolSize(lettuce.getPool().getMaxIdle());
                        sentinelServersConfig.setSlaveConnectionPoolSize(lettuce.getPool().getMaxActive());
                    }
                }
                sentinelServersConfig.setDatabase(redisProperties.getDatabase());
                sentinelServersConfig.setClientName(redisProperties.getClientName());
                sentinelServersConfig.setPassword(password);
                break;
            default:
                break;
        }
        RedissonClient redissonClient=Redisson.create(config);
        log.info("加载Redisson配置：[ ==============================END============================== ]");
        return redissonClient;
    }

    private String prefixAddress(String address) {
        if (!StringUtils.isEmpty(address) && !address.startsWith("redis")) {
            return "redis://" + address;
        }
        return address;
    }

}
