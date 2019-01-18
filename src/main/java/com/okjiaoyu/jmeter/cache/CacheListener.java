package com.okjiaoyu.jmeter.cache;

import org.apache.log4j.Logger;

public class CacheListener{
    Logger logger = Logger.getLogger(CacheListener.class);
    private CacheManagerImpl cacheManagerImpl;
    public CacheListener(CacheManagerImpl cacheManagerImpl) {
        this.cacheManagerImpl = cacheManagerImpl;
    }
    public void startListen() {
        new Thread(){
            public void run() {
                while (true) {
                    for(String key : cacheManagerImpl.getAllCacheKeys()) {
                        if (cacheManagerImpl.isTimeout(key)) {
                            cacheManagerImpl.deleteCacheByKey(key);
                            logger.info(key + "缓存被清除");
                        }
                    }
                }
            }
        }.start();

    }
}
