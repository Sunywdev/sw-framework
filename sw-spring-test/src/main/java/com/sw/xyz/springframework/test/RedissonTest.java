package com.sw.xyz.springframework.test;

import cn.hutool.core.util.IdUtil;
import com.sw.xyz.springframework.bean.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2022/11/25 14:35
 */
@RestController
@Slf4j
public class RedissonTest {
    @Autowired
    private Asyncu asyncu;

    private int total = 0;

    private long time = 0;

    @RequestMapping("/add/{value}")
    public BaseResponse addBaseResponse(@PathVariable String value) {
        asyncu.set(value);
        total++;
        return BaseResponse.success();
    }

    private boolean flag=true;



    @RequestMapping("/start")
    public void start() {
        flag=true;
        time = System.currentTimeMillis();
        while (flag) {
            addBaseResponse(IdUtil.objectId());
        }
    }

    @RequestMapping("/stop")
    public void stop() {
        flag=false;
        log.info("总计发送:[{}]条,耗时:[{}]ms",total,System.currentTimeMillis()-time);
        time = 0;
        total = 0;
    }
}
