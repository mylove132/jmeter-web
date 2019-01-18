package com.okjiaoyu.jmeter.entity;

import com.okjiaoyu.jmeter.util.JsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: liuzhanhui
 * @Decription:
 * @Date: Created in 2019-01-17:11:39
 * Modify date: 2019-01-17:11:39
 */
public class DubboEntity implements Serializable {

    private String protocol;
    private String address;
    private String interfaceName;
    private String methodName;
    private String version;
    private String group;
    private Integer timeOut;
    private List<RequestTypeArgments> requestParamTypeArgs;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getVersion() {
        return version == null ? "" : version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group != null ? group : "";
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getTimeOut() {
        return timeOut != null ? timeOut : 0;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public List<RequestTypeArgments> getRequestParamTypeArgs() {
        return requestParamTypeArgs;
    }

    public void setRequestParamTypeArgs(List<RequestTypeArgments> requestParamTypeArgs) {
        this.requestParamTypeArgs = requestParamTypeArgs;
    }

    public static void main(String[] args) {
        DubboEntity dubboEntity = new DubboEntity();
        dubboEntity.setGroup(null);
        dubboEntity.setVersion(null);
        dubboEntity.setTimeOut(null);
        dubboEntity.setAddress("172.18.4.48:2181");
        dubboEntity.setProtocol("zookeeper");
        dubboEntity.setInterfaceName("com.noriental.adminsvr.service.teaching.ChapterService");
        dubboEntity.setMethodName("findByIds");
        List<RequestTypeArgments> requestTypeArgments = new ArrayList<>();
        RequestTypeArgments typeArgments = new RequestTypeArgments("com.noriental.adminsvr.response.ResponseEntity","{\"entity\":[200,201,202]}");
        requestTypeArgments.add(typeArgments);
        dubboEntity.setRequestParamTypeArgs(requestTypeArgments);
        System.out.println(JsonUtils.toJson(dubboEntity));
    }
}
