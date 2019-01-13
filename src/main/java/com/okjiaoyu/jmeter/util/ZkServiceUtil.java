package com.okjiaoyu.jmeter.util;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.registry.RegistryService;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-01-02:10:56
 * Modify date: 2019-01-02:10:56
 */
public class ZkServiceUtil {


    public static Map<String, String[]> getInterfaceMethods(String address) {
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
        return serviceMap;
    }

    public static GenericService getGenericService(String address, String interfaceName) {
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress(address);
        registry.setProtocol("zookeeper");
        registry.setGroup(null);
        registry.setTimeout(10000);
        ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
        reference.setApplication(new ApplicationConfig("qa-dubbo-jmeter-web"));
        reference.setInterface(interfaceName);
        reference.setProtocol("dubbo");
        reference.setTimeout(5000);
        reference.setVersion("3.0.0");
        reference.setGroup(null);
        reference.setRegistry(registry);
        reference.setGeneric(true);
        GenericService genericService = reference.get();
        return genericService;
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        final ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        GenericService service = getGenericService("172.18.4.48:2181","com.noriental.lessonsvr.rservice.ResPackageService");
        Map<String,Object> map = new HashMap<>();
        map.put("id","36019");
        Object result = null;
                try {
                    service.$invoke("findResPackageDetail", new String[]{"com.noriental.lessonsvr.entity.request.LongRequest"}, new Object[]{map});
                }catch (Exception e){
                    System.out.println("zk连接超时");
                    return;
                }
                System.out.println(result.toString().contains("success"));
    }
}
