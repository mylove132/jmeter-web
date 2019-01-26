package com.okjiaoyu.jmeter.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-01-22:09:50
 * Modify date: 2019-01-22:09:50
 */
public class FileEntity implements Serializable {

    private static final long serialVersionUID = -2567457932227227262L;
    private Integer id;
    private String name;
    private Integer uid;
    private Date createTime;
    private Date execTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getExecTime() {
        return execTime;
    }

    public void setExecTime(Date execTime) {
        this.execTime = execTime;
    }
}
