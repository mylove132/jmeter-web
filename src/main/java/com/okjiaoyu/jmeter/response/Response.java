package com.okjiaoyu.jmeter.response;

import java.io.Serializable;

/**
 * 请求返回类
 * Created by Tiger on 2018/10/9.
 */
public class Response<T> implements Serializable {
    public int code;

    private String msg;

    private T data;

    public Response<T> setCode(ErrorCode retCode) {
        this.code = retCode.getCode();
        return this;
    }

    public int getCode() {
        return code;
    }

    public Response<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Response<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public Response<T> setData(T data) {
        this.data = data;
        return this;
    }

}