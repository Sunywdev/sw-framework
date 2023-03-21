package com.sw.xyz.springframework.cache;

import com.sw.xyz.springframework.bean.response.BaseResponse;
import com.sw.xyz.springframework.cache.local.LocalCacheUtils;
import com.sw.xyz.springframework.cache.redis.RedisUtils;
import com.sw.xyz.springframework.cache.redisson.RedissonBaseUtils;
import com.sw.xyz.springframework.core.annocation.Log;
import com.sw.xyz.springframework.core.log.LogLevel;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 名称: CacheController
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/12/2 0002 16:22
 */
@RestController
@Api(tags = "缓存测试控制层")
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private RedissonBaseUtils redissonBaseUtils;


    @PostMapping("/redis/{id}")
    @Log(value = "redis-cache", level = LogLevel.INFO)
    public BaseResponse redisCache(@PathVariable String id) {
        RedisUtils.saveValue(id, id);
        return BaseResponse.success(RedisUtils.getHashValue(id));
    }


    @PostMapping("/redisson/{id}")
    @Log(value = "redisson-cache", level = LogLevel.INFO)
    public BaseResponse redissonCache(@PathVariable String id) {
        redissonBaseUtils.saveObject(id, id);
        return BaseResponse.success(redissonBaseUtils.getValue(id));
    }

    @PostMapping("/localCache/{id}")
    @Log(value = "local-cache", level = LogLevel.INFO)
    public BaseResponse localCache(@PathVariable String id) {
        LocalCacheUtils.put(id, id);
        return BaseResponse.success(LocalCacheUtils.get(id));
    }
}
