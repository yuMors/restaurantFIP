package com.aurora.util.qiniuYunOss.other;

/**
 * 自定义异常类
 * 好像就是闪电符号
 */
public class SystemException extends RuntimeException {

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
}
