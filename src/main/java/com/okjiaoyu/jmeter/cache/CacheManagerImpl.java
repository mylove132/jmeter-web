package com.okjiaoyu.jmeter.cache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-01-16:13:29
 * Modify date: 2019-01-16:13:29
 */
public class CacheManagerImpl implements CacheManager {

    private Map<String, CacheEntity> caches = new ConcurrentHashMap<String, CacheEntity>();

    @Override
    public void putCache(String cacheKey, Object cacheObject, Long timeOut) {
        timeOut = timeOut > 0? timeOut : 0L;
        putCache(cacheKey, new CacheEntity(cacheObject, timeOut, System.currentTimeMillis()));
    }

    @Override
    public void putCache(String cacheKey, CacheEntity cacheEntity) {
        caches.put(cacheKey, cacheEntity);
    }

    @Override
    public void deleteCacheByKey(String cacheKey) {
        if (isExistCacheKey(cacheKey)){
            caches.remove(cacheKey);
        }
    }

    @Override
    public void deleteAllCache() {
        caches.clear();
    }

    @Override
    public Object getCacheObjectByCacheKey(String cacheKey) {
        if (isExistCacheKey(cacheKey)){
            return caches.get(cacheKey);
        }
        return null;
    }

    @Override
    public CacheEntity getCacheEntityByCacheKey(String cacheKey) {
        if (isExistCacheKey(cacheKey)){
            return caches.get(cacheKey);
        }
        return null;
    }

    @Override
    public Object getAllCache() {
        return caches;
    }

    @Override
    public boolean isExistCacheKey(String cacheKey) {
        return caches.containsKey(cacheKey);
    }

    @Override
    public boolean isTimeout(String cacheKey) {
        if (!isExistCacheKey(cacheKey)){
            return true;
        }
        CacheEntity entity = caches.get(cacheKey);
        long timeOut = entity.getTimeOut();
        long lastReflushTime = entity.getLastRefeshTime();
        if (timeOut == 0 || System.currentTimeMillis() - lastReflushTime >= timeOut){
            return true;
        }
        return false;
    }

    @Override
    public Set<String> getAllCacheKeys() {
        return caches.keySet();
    }

}
