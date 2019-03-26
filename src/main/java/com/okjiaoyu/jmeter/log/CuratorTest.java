package com.okjiaoyu.jmeter.log;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.WatchedEvent;

import java.util.List;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-03-26:10:34
 * Modify date: 2019-03-26:10:34
 */
public class CuratorTest {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("10.10.1.7:2181", new RetryNTimes(5, 2000));
        client.start();
        List<String> children = client.getChildren().usingWatcher(new CuratorWatcher() {
            @Override
            public void process(WatchedEvent event) throws Exception {
                System.out.println("监控： " + event);
            }
        }).forPath("/dubbo");
        System.out.println(children);
    }
}
