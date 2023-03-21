package com.sw.xyz.springframework.cloud.gary.loadbalance;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 名称: XX定义
 * 功能: <功能详细描述>
 * 方法: <方法简述-方法描述>
 * 版本: 1.0
 * 作者: sunyw
 * 说明: 灰度负载均衡策略,默认使用Ribbon的轮询策略,存在灰度服务时,则对灰度服务使用轮询策略
 * 时间: 2023/03/02 15:52
 */
public class GaryLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private static final Logger log = LoggerFactory.getLogger(GaryLoadBalancer.class);


    /**
     * 灰度META 值
     */
    public static final String GRAY_META = "GRAY_META";


    /**
     * 灰度版本
     */
    public static final String GRAY = "GRAY-VERSION";

    /**
     *
     */
    public static final String NORMAL = "normal";


    private final AtomicInteger position;

    private final String serviceId;

    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    /**
     * @param serviceInstanceListSupplierProvider a provider of
     *                                            {@link ServiceInstanceListSupplier} that will be used to get available instances
     * @param serviceId                           id of the service for which to choose an instance
     */
    public GaryLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                            String serviceId) {
        this(serviceInstanceListSupplierProvider, serviceId, new Random().nextInt(1000));
    }

    /**
     * @param serviceInstanceListSupplierProvider a provider of
     *                                            {@link ServiceInstanceListSupplier} that will be used to get available instances
     * @param serviceId                           id of the service for which to choose an instance
     * @param seedPosition                        Round Robin element position marker
     */
    public GaryLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                            String serviceId, int seedPosition) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.position = new AtomicInteger(seedPosition);
    }

    @SuppressWarnings("rawtypes")
    @Override
    // see original
    // https://github.com/Netflix/ocelli/blob/master/ocelli-core/
    // src/main/java/netflix/ocelli/loadbalancer/RoundRobinLoadBalancer.java
    public Mono<Response<ServiceInstance>> choose(Request request) {
        RequestDataContext context = (RequestDataContext) request.getContext();
        List<String> strings = context.getClientRequest().getHeaders().get(GRAY);
        final String garyVersion = CollectionUtils.isEmpty(strings) ? "" : strings.get(0);
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next()
                .map(serviceInstances -> processInstanceResponse(garyVersion, supplier, serviceInstances));
    }

    private Response<ServiceInstance> processInstanceResponse(String garyVersion, ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(garyVersion, serviceInstances);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(String garyVersion, List<ServiceInstance> instances) {
        log.debug(">>>>>>>>>>负载均衡,请求头携带版本号为: " + garyVersion);
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + serviceId);
            }
            return new EmptyResponse();
        }
        List<ServiceInstance> garyInstance = new ArrayList<>();
        List<ServiceInstance> normalInstance = new ArrayList<>();
        for (ServiceInstance instance : instances) {
            Map<String, String> metadata = instance.getMetadata();
            if (metadata.containsKey(GRAY_META)) {
                //判断实例中的灰度版本是否和请求头中的是否一致
                String instanceVersion = metadata.get(GRAY_META);
                if (NORMAL.equals(instanceVersion)){
                    normalInstance.add(instance);
                    continue;
                }
                garyInstance.add(instance);
                continue;
            }
            normalInstance.add(instance);
        }
        if (!garyInstance.isEmpty()&&StringUtils.isNotEmpty(garyVersion)) {
            int pos = Math.abs(this.position.incrementAndGet());
            ServiceInstance instance = garyInstance.get(pos % garyInstance.size());
            log.info(">>>>>>>>>>路由至灰度服务,service: " + serviceId + ",请求地址为: " + instance.getUri());
            return new DefaultResponse(instance);
        }else {
            if (garyInstance.isEmpty()){
                log.info(">>>>>>>>>>目标灰度service: " + serviceId + ",不存在实例,开始转发至正常服务");
            }
        }
        int pos = Math.abs(this.position.incrementAndGet());

        ServiceInstance instance = normalInstance.get(pos % normalInstance.size());
        log.info(">>>>>>>>>>路由至正常服务,service: " + serviceId + ",请求地址为: " + instance.getUri());
        return new DefaultResponse(instance);
    }
}
