package com.okjiaoyu.jmeter.response;

public class CommonResponse<T> {
    private final static String SUCCESS = "success";

    public static <T> Response<T> makeOKRsp() {
        return new Response<T>().setCode(ErrorCode.SUCCESS).setMsg(SUCCESS);
    }

    public static <T> Response<T> makeOKRsp(T data) {
        return new Response<T>().setCode(ErrorCode.SUCCESS).setMsg(SUCCESS).setData(data);
    }

    public static <T> Response<T> makeErrRsp(String message) {
        return new Response<T>().setCode(ErrorCode.FAIL).setMsg(SUCCESS);
    }

    public static <T> Response<T> makeRsp(ErrorCode errorCode) {
        return new Response<T>().setCode(errorCode.getCode()).setMsg(errorCode.getMsg());
    }

    public static <T> Response<T> makeRsp(int code, String msg) {
        return new Response<T>().setCode(code).setMsg(msg);
    }

    public static <T> Response<T> makeRsp(int code, String msg, T data) {
        return new Response<T>().setCode(code).setMsg(msg).setData(data);
    }
}