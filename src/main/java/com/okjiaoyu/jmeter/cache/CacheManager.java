package com.okjiaoyu.jmeter.cache;

import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-01-16:13:19
 * Modify date: 2019-01-16:13:19
 */
@Service("cacheService")
public interface CacheManager {

   void putCache(String cacheKey,Object cacheObject, Long timeOut);

   void putCache(String cacheKey, CacheEntity cacheEntity);

   void deleteCacheByKey(String cacheKey);

   void deleteAllCache();

   Object getCacheObjectByCacheKey(String cacheKey);

   CacheEntity getCacheEntityByCacheKey(String cacheKey);

   Object getAllCache();

   boolean isExistCacheKey(String cacheKey);

   boolean isTimeout(String cacheKey);

   Set<String> getAllCacheKeys();

}
