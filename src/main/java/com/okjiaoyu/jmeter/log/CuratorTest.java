package com.okjiaoyu.jmeter.log;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.registry.RegistryService;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.okjiaoyu.jmeter.entity.DubboEntity;
import com.okjiaoyu.jmeter.entity.RequestTypeArgments;
import com.okjiaoyu.jmeter.util.JsonUtils;
import com.okjiaoyu.jmeter.util.ZkServiceUtil;
import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-03-26:10:34
 * Modify date: 2019-03-26:10:34
 */
public class CuratorTest implements Watcher {

    public void test() throws Exception {
        CuratorZookeeperClient client = new CuratorZookeeperClient("10.10.6.3:2181",5000,
                5000,this,new RetryNTimes(5, 2000));
        client.start();
        List<String> services = client.getZooKeeper().getChildren("/xdfapp", this);
        System.out.println(services);
    }

    public static void main(String[] args) throws Exception {
        ZkServiceUtil util = new ZkServiceUtil();
        DubboEntity dubboEntity = new DubboEntity();
        dubboEntity.setMethodName("makeResPackage");
        dubboEntity.setInterfaceName("com.noriental.lessonsvr.rservice.ResPackageService");
        dubboEntity.setProtocol("zookeeper");
        dubboEntity.setAddress("172.18.4.48:2181");
        dubboEntity.setTimeOut(1000);
        dubboEntity.setVersion(null);
        dubboEntity.setGroup(null);
        GenericService genericService = util.getGenericService(dubboEntity);
        Map<String, Object> params = new HashMap<>();
       String json = "{\"year\":0,\"publishType\":0,\"directoryId\":0}";
        params = JsonUtils.formJson(json, Map.class);
        Object deleteCusDirResource = genericService.$invoke("makeResPackage", new String[]{"com.noriental.lessonsvr.entity.request.PackageInfoSimpleRequest"}, new Object[]{params});
        System.out.println(deleteCusDirResource);
//        CuratorTest test = new CuratorTest();
//        test.test();
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
    }
}
