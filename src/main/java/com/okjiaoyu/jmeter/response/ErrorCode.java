package com.okjiaoyu.jmeter.response;

public enum ErrorCode {
    // 成功
    SUCCESS(0),
    //获取dubbo接口失败
    DUBBO_SERVICE_FAIL(1001, "dubbo接口获取service失败"),
    //获取dubbo接口失败
    DUBBO_METHOD_FAIL(1002, "dubbo接口获取method失败"),
    //上传文件为空
    AUTO_GENERATE_JMX_FILE_FAIL(3001, "自动生成jmx文件失败"),
    //请求参数为空
    REQUEST_PARAM_NULL(5000),
    //上传文件为空
    UPLOAD_FILE_EMPTY(2001, "上传的文件为空，请检查"),
    //上传文件转换异常
    UPLOAD_FILE_TRANSFER_FAIL(2002, "上传文件转换失败"),
    SCRIPT_RENAME_FAIL(2003, "脚本重命名失败"),
    //未知错误
    FAIL(-1),
    //用户名不存在
    LOGIN_UNKNOW_NAME_FAIL(6001,"未知的用户名"),
    //密码错误
    LOGIN_PASSWORD_FAIL(6002,"密码错误"),
    //账号锁定
    LOGIN_ACCOUNT_ISLOCK(6003,"账号不可用"),
    //账号认证失败
    LOGIN_PASSWORD_AUTHERTICATION_FAIL(6004,"账号密码认证失败"),
    TIMER_TASK_EXIST(7001,"定时任务已存在"),
    TIMER_LITTLE_CURRENT_TIME(7002,"定时时间不能小于当前时间"),
    TIMER_TASK_CANCEL_FAIL(7003,"定时任务取消失败"),
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

    public ErrorCode setMsg(String msg){
        this.msg = msg;
        return this;
    }
}
