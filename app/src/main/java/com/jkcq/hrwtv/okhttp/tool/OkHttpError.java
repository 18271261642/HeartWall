package com.jkcq.hrwtv.okhttp.tool;

/*
 * 
 * classes : com.xingye.project.net.okhttp
 * @author 苗恒聚
 * V 1.0.0
 * Create at 2016-1-20 14:59
 */
public class OkHttpError extends Exception {

    private int code;

    public OkHttpError() {
    }

    public OkHttpError(String detailMessage) {
        super(detailMessage);
    }

    public OkHttpError(int code, String detailMessage) {
        super(detailMessage);
        this.code = code;
    }

    public OkHttpError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public OkHttpError(Throwable throwable) {
        super(throwable);
    }

    public int getCode() {
        return code;
    }
}
