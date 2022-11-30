package com.sw.xyz.springframework.cache.config;

import cn.hutool.json.JSONUtil;
import com.sw.xyz.springframework.cache.enums.RedisModel;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: Redis配置类
 * 时间: 2022/8/22 9:25
 */
@Slf4j
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfig {

    /**
     * 构造器注入
     */
    private RedisProperties redisProperties;


    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties=redisProperties;
    }

    private GenericObjectPoolConfig<?> genericObjectPoolConfig(RedisProperties.Pool properties) {
        GenericObjectPoolConfig<?> config=new GenericObjectPoolConfig<>();
        if (null !=properties){
            System.out.println(JSONUtil.toJsonStr(properties));
            config.setMaxTotal(properties.getMaxActive());
            config.setMaxIdle(properties.getMaxIdle());
            config.setMinIdle(properties.getMinIdle());
            config.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxWait().toMillis());
            return config;
        }
        System.out.println(JSONUtil.toJsonStr(config));
        return config;
    }

    @Bean("lettuceConnectionFactory")
    public LettuceConnectionFactory lettuceConnectionFactory() {
        //构建方式
        RedisModel model=RedisModel.buildModel(redisProperties);
        log.info("===========================Redis[{}]模式注册(BEGIN)===========================",model.getDesc());
        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions=ClusterTopologyRefreshOptions.builder()
                // 开启全部自适应刷新
                .enableAllAdaptiveRefreshTriggers() // 开启自适应刷新,自适应刷新不开启,Redis集群变更时将会导致连接异常
                // 自适应刷新超时时间(默认30秒)
                .adaptiveRefreshTriggersTimeout(Duration.ofSeconds(15)) //默认关闭开启后时间为30秒
                // 开周期刷新
                .enablePeriodicRefresh(Duration.ofSeconds(15))  // 默认关闭开启后时间为60秒 ClusterTopologyRefreshOptions.DEFAULT_REFRESH_PERIOD 60  .enablePeriodicRefresh(Duration.ofSeconds(2)) = .enablePeriodicRefresh().refreshPeriod(Duration.ofSeconds(2))
                .build();
        ClientOptions clientOptions=ClusterClientOptions.builder()
                .topologyRefreshOptions(clusterTopologyRefreshOptions)
                .build();
        Duration timeOut=Duration.ofSeconds(RedisURI.DEFAULT_TIMEOUT);
        if (null != redisProperties.getTimeout()) {
            timeOut=redisProperties.getTimeout();
        }
        LettuceClientConfiguration clientConfig=LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig(redisProperties.getLettuce().getPool()))
                .readFrom(ReadFrom.MASTER_PREFERRED)
                .clientOptions(clientOptions)
                .commandTimeout(timeOut) //默认RedisURI.DEFAULT_TIMEOUT 60
                .build();
        RedisConfiguration clusterConfiguration;
        if (model.equals(RedisModel.CLUSTER)) {
            clusterConfiguration=redisClusterConfiguration();
        } else if (model.equals(RedisModel.SENTINEL)) {
            clusterConfiguration=redisSentinelConfiguration();
        } else {
            clusterConfiguration=standaloneConfiguration();
        }
        LettuceConnectionFactory lettuceConnectionFactory=new LettuceConnectionFactory(clusterConfiguration,clientConfig);
        lettuceConnectionFactory.setShareNativeConnection(true); //是否允许多个线程操作共用同一个缓存连接，默认true，false时每个操作都将开辟新的连接
        lettuceConnectionFactory.afterPropertiesSet();
        // 重置底层共享连接, 在接下来的访问时初始化
        lettuceConnectionFactory.resetConnection();
        return lettuceConnectionFactory;
    }

    /**
     * 哨兵模式
     *
     * @return RedisSentinelConfiguration
     */
    private RedisSentinelConfiguration redisSentinelConfiguration() {
        log.info("===========================Redis哨兵模式注册开始===========================");
        RedisProperties.Sentinel sentinel=redisProperties.getSentinel();
        log.info("===========================Redis哨兵模式注册节点:{}===========================",sentinel.getNodes());
        String master=sentinel.getMaster();
        RedisSentinelConfiguration redisSentinelConfiguration=new RedisSentinelConfiguration();
        redisSentinelConfiguration.setMaster(master);
        redisSentinelConfiguration.setDatabase(redisProperties.getDatabase());
        if (null != redisProperties.getSentinel().getNodes()) {
            List<RedisNode> sentinelNode=new ArrayList<>();
            for (String sen : redisProperties.getSentinel().getNodes()) {
                String[] arr=sen.split(":");
                sentinelNode.add(new RedisNode(arr[0],Integer.parseInt(arr[1])));
            }
            redisSentinelConfiguration.setPassword(redisProperties.getPassword());
            redisSentinelConfiguration.setSentinels(sentinelNode);
            redisSentinelConfiguration.setSentinelPassword(redisProperties.getPassword());
        }
        log.info("===========================Redis哨兵模式注册结束===========================");
        return redisSentinelConfiguration;
    }


    /**
     * 单机模式配置
     *
     * @return RedisStandaloneConfiguration
     */
    private RedisStandaloneConfiguration standaloneConfiguration() {
        log.info("===========================Redis单机模式注册开始===========================");
        log.info("===========================Redis单机模式注册节点:{}===========================",redisProperties.getHost());
        RedisStandaloneConfiguration redisStandaloneConfiguration=new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setUsername(redisProperties.getUsername());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        log.info("===========================Redis单机模式注册结束===========================");
        return redisStandaloneConfiguration;
    }

    /**
     * 集群模式配置
     *
     * @return RedisClusterConfiguration
     */
    private RedisClusterConfiguration redisClusterConfiguration() {
        //开启 自适应集群拓扑刷新和周期拓扑刷新
        log.info("===========================Redis集群模式注册开始===========================");
        Set<RedisNode> nodes=new HashSet<>();
        List<String> clusterNodes=redisProperties.getCluster().getNodes();
        log.info("===========================Redis集群模式注册节点:{}===========================",clusterNodes);
        clusterNodes.forEach(address -> nodes.add(new RedisNode(address.split(":")[0].trim(),Integer.valueOf(address.split(":")[1]))));
        RedisClusterConfiguration clusterConfiguration=new RedisClusterConfiguration();
        clusterConfiguration.setClusterNodes(nodes);
        clusterConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        clusterConfiguration.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());
        log.info("===========================Redis集群模式注册结束===========================");
        return clusterConfiguration;
    }

    /**
     * RedisTemplate
     *
     * @return RedisTemplate
     */
    @Bean(name="redisTemplate")
    public RedisTemplate redisTemplate(RedisConnectionFactory lettuceConnectionFactory) {
        log.info("===========================RedisTemplate配置(BEGIN)===========================");
        RedisTemplate<String,Serializable> template=new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(lettuceConnectionFactory);
        log.info("===========================RedisTemplate配置(END)===========================");
        return template;
    }

    /**
     * stringRedisTemplate
     *
     * @return StringRedisTemplate
     */
    @Bean(name="stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory lettuceConnectionFactory) {
        log.info("===========================StringRedisTemplate(BEGIN)===========================");
        StringRedisTemplate template=new StringRedisTemplate();
        template.setKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(lettuceConnectionFactory);
        log.info("===========================StringRedisTemplate(END)===========================");
        return template;
    }

}
