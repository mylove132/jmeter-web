package com.okjiaoyu.jmeter.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-01-25:14:30
 * Modify date: 2019-01-25:14:30
 */
public class TimerEntity implements Serializable {

    private static final long serialVersionUID = -2567457932227227212L;

    private Integer id;
    private int fileId;
    private Date timer;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public Date getTimer() {
        return timer;
    }

    public void setTimer(Date timer) {
        this.timer = timer;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
