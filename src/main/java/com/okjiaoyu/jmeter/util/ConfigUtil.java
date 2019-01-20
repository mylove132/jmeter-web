package com.okjiaoyu.jmeter.util;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.util.Properties;

public class ConfigUtil {

    private Logger log = Logger.getLogger(ConfigUtil.class);
    private static ConfigUtil configUtil;
    private ConfigUtil(){}

    public static ConfigUtil getInstance(){
        if (configUtil == null){
            configUtil = new ConfigUtil();
        }
        return configUtil;
    }

    public String getValue(String key){
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("加载config配置文件失败");
        }
        return prop.getProperty(key);
    }

    public static void main(String[] args) {
        System.out.println(ConfigUtil.getInstance().getValue("fileDownLoadPath"));
    }

}
