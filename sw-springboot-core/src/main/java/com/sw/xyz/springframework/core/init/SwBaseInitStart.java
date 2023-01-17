package com.sw.xyz.springframework.core.init;

import com.sw.xyz.springframework.utils.spring.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 初始化类执行器
 * 时间: 2023/1/17 9:53
 */
@Component
@Order(-1)
@Slf4j
public class SwBaseInitStart implements InitializingBean {

    @Autowired
    private SpringContextUtils springContextUtils;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("===========================初始化数据加载器,开始执行初始化任务===========================");
        Map<String,SwBaseInit> beans=springContextUtils.getBeans(SwBaseInit.class);
        if (!CollectionUtils.isEmpty(beans)) {
            for (String name : beans.keySet()) {
                log.debug("加载bean名称 :[{}]",name);
                beans.get(name).initialize();
            }
        }
        log.info("===========================初始化任务执行完毕===========================");
    }
}
