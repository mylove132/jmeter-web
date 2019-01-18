package com.okjiaoyu.jmeter.cache;

import java.io.Serializable;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-01-16:13:18
 * Modify date: 2019-01-16:13:18
 */
public class CacheEntity implements Serializable {

    private Object cacheObject;

    private Long lastRefeshTime;

    private Long timeOut;

    public Object getCacheObject() {
        return cacheObject;
    }

    public CacheEntity(Object cacheObject, Long timeOut, Long lastRefeshTime) {
        this.cacheObject = cacheObject;
        this.lastRefeshTime = lastRefeshTime;
        this.timeOut = timeOut;
    }

    public void setCacheObject(Object cacheObject) {
        this.cacheObject = cacheObject;
    }

    public Long getLastRefeshTime() {
        return lastRefeshTime;
    }

    public void setLastRefeshTime(Long lastRefeshTime) {
        this.lastRefeshTime = lastRefeshTime;
    }

    public Long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Long timeOut) {
        this.timeOut = timeOut;
    }
}
