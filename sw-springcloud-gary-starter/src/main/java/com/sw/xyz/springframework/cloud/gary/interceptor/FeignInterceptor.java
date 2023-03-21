package com.sw.xyz.springframework.cloud.gary.interceptor;

import com.sw.xyz.springframework.cloud.gary.loadbalance.GaryLoadBalancer;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 说明描述
 * 时间: 2023/03/02 15:30
 */
@Slf4j
public class FeignInterceptor implements RequestInterceptor {



    @Override
    public void apply(RequestTemplate requestTemplate) {
        String header = null;
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null ==requestAttributes){
                return;
            }
            header=requestAttributes.getRequest().getHeader(GaryLoadBalancer.GRAY);
            if (null == header || header.isEmpty()) {
                return;
            }
        } catch (Exception e) {
            log.info("请求头获取失败, 错误信息为: {}", e.getMessage());
        }
        requestTemplate.header(GaryLoadBalancer.GRAY, header);
    }
}
