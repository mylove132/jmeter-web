package com.okjiaoyu.jmeter.entity;

import java.io.Serializable;

public class AutoGenerateJmxEntity implements Serializable {

    private String userName;
    //jmeter版本
    private String jmeterVersion;
    //接口超时时间
    private String timeOut;
    //压测时间
    private Integer preTime;
    //压测并发数
    private Integer preNumber;
    //压测文件名
    private String preName;
    //zookeeper
    private String zkAddress;
    //压测接口名
    private String dubboInterfaceName;
    //压测方法名
    private String methodName;
    //方法参数类型
    private String requestBeanRenfence;
    //方法参数
    private String param;
    //断言结果
    private String assertText;

    public String getJmeterVersion() {
        return jmeterVersion;
    }

    public void setJmeterVersion(String jmeterVersion) {
        this.jmeterVersion = jmeterVersion;
    }

    public Integer getPreTime() {
        return preTime;
    }

    public void setPreTime(Integer preTime) {
        this.preTime = preTime;
    }

    public Integer getPreNumber() {
        return preNumber;
    }

    public void setPreNumber(Integer preNumber) {
        this.preNumber = preNumber;
    }

    public String getPreName() {
        return preName;
    }

    public void setPreName(String preName) {
        this.preName = preName;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getDubboInterfaceName() {
        return dubboInterfaceName;
    }

    public void setDubboInterfaceName(String dubboInterfaceName) {
        this.dubboInterfaceName = dubboInterfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRequestBeanRenfence() {
        return requestBeanRenfence;
    }

    public void setRequestBeanRenfence(String requestBeanRenfence) {
        this.requestBeanRenfence = requestBeanRenfence;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAssertText() {
        return assertText;
    }

    public void setAssertText(String assertText) {
        this.assertText = assertText;
    }
}
