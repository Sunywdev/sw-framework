package com.sw.xyz.springframework.init;

import com.sw.xyz.springframework.core.init.SwBaseInit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/1/17 9:54
 */
@Slf4j
@Component
public class InitConfig implements SwBaseInit {


    public static final Map<String,String> map=new ConcurrentHashMap<>();


    @Override
    public void initialize() {
        log.info("基础参数数据开始加载----------------------------------");
        map.put("keys","values");

    }
}
