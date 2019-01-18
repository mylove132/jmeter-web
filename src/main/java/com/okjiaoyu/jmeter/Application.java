package com.okjiaoyu.jmeter;

import com.okjiaoyu.jmeter.cache.CacheListener;
import com.okjiaoyu.jmeter.cache.CacheManagerImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

/**
 * start class
 */
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class Application {
    public static void main(String[] args) {
        CacheListener listener = new CacheListener(new CacheManagerImpl());
        listener.startListen();
        SpringApplication.run(Application.class, args);
    }
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("10240KB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("102400KB");
        return factory.createMultipartConfig();
    }
}
