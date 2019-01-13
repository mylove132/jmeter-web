package com.okjiaoyu.jmeter.response;

public enum ErrorCode {
    // 成功
    SUCCESS(0),
    //获取dubbo接口失败
    DUBBO_SERVICE_FAIL(1001, "dubbo接口获取失败"),
    //上传文件为空
    AUTO_GENERATE_JMX_FILE_FAIL(3001, "自动生成jmx文件失败"),
    //上传文件为空
    UPLOAD_FILE_EMPTY(2001, "上传的文件为空，请检查"),
    //上传文件转换异常
    UPLOAD_FILE_TRANSFER_FAIL(2002, "上传文件转换失败"),
    //未知错误
    FAIL(-1),

    // 未认证（签名错误）
    UNAUTHORIZED(401),

    // 接口不存在
    NOT_FOUND(404),

    // 服务器内部错误
    INTERNAL_SERVER_ERROR(500);

    private int code;
    private String msg;

    ErrorCode(int code) {
        this.code = code;
    }
    ErrorCode(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}