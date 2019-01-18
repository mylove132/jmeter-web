package com.okjiaoyu.jmeter.util;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.registry.RegistryService;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.okjiaoyu.jmeter.cache.CacheEntity;
import com.okjiaoyu.jmeter.cache.CacheManager;
import com.okjiaoyu.jmeter.cache.CacheManagerImpl;
import com.okjiaoyu.jmeter.entity.DubboEntity;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-01-02:10:56
 * Modify date: 2019-01-02:10:56
 */
public class ZkServiceUtil {

    private CacheManager cacheManager = new CacheManagerImpl();
    private final String cacheName = "dubbo_services_method_cache";

    private Map<String, String[]> getInterfaceMethods(String address) {
        Map<String, String[]> serviceMap = new HashMap<>();
        try {
            ApplicationConfig applicationConfig = new ApplicationConfig();
            applicationConfig.setName("qa-dubbo-jmeter-web");

            RegistryConfig registry = new RegistryConfig();
            registry.setAddress(address);
            registry.setProtocol("zookeeper");
            registry.setGroup(null);

            ReferenceConfig referenceConfig = new ReferenceConfig();
            referenceConfig.setApplication(applicationConfig);
            referenceConfig.setRegistry(registry);
            referenceConfig.setInterface("com.alibaba.dubbo.registry.RegistryService");
            ReferenceConfigCache cache = ReferenceConfigCache.getCache();
            RegistryService registryService = (RegistryService) cache.get(referenceConfig);

            RegistryServerSync registryServerSync = RegistryServerSync.get(address + "_");
            registryService.subscribe(RegistryServerSync.SUBSCRIBE, registryServerSync);
            ConcurrentMap<String, ConcurrentMap<String, Map<String, URL>>> map = registryServerSync.getRegistryCache();
            ConcurrentMap<String, Map<String, URL>> providers = map.get(Constants.PROVIDERS_CATEGORY);
            if (providers == null || providers.isEmpty()) {
                throw new RuntimeException("zookeeper连接失败");
            } else {
                for (String url : providers.keySet()) {
                    Map<String, URL> provider = providers.get(url);
                    for (String str : provider.keySet()) {
                        String methodName = provider.get(str).getParameter(Constants.METHODS_KEY);
                        String[] methods = methodName.split(",");
                        serviceMap.put(url, methods);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        CacheEntity entity = new CacheEntity(serviceMap, (1000*60*60*24L), System.currentTimeMillis());
        cacheManager.putCache(cacheName,entity);
        return serviceMap;
    }

    /**
     * 获取所有的services
     * @param address
     * @return
     */
    public Set<String> getServices(String address){
        if (cacheManager.isExistCacheKey(cacheName)){
            CacheEntity cacheEntity = cacheManager.getCacheEntityByCacheKey(cacheName);
            Map<String, String[]> servicesMap = (Map<String, String[]>) cacheEntity.getCacheObject();
            return servicesMap.keySet();
        }
        return getInterfaceMethods(address).keySet();
    }

    /**
     * 获取services对应的方法
     * @param serviceName
     * @return
     */
    public String[] getMethods(String serviceName){
        if (cacheManager.isExistCacheKey(cacheName)){
            CacheEntity cacheEntity = cacheManager.getCacheEntityByCacheKey(cacheName);
            Map<String, String[]> servicesMap = (Map<String, String[]>) cacheEntity.getCacheObject();
            return servicesMap.get(serviceName);
        }

        return null;
    }

    public GenericService getGenericService(DubboEntity entity) {
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress(entity.getAddress());
        registry.setProtocol(entity.getProtocol());
        registry.setGroup(entity.getGroup());
        ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
        reference.setApplication(new ApplicationConfig("qa-dubbo-jmeter-web"));
        reference.setInterface(entity.getInterfaceName());
        reference.setProtocol("dubbo");
        reference.setTimeout(entity.getTimeOut());
        reference.setVersion(entity.getVersion());
        reference.setGroup(entity.getGroup());
        reference.setRegistry(registry);
        reference.setGeneric(true);
        GenericService genericService = reference.get();
        return genericService;
    }
}
