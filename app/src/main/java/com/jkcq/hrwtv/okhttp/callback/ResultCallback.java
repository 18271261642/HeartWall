package com.jkcq.hrwtv.okhttp.callback;


import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;

import java.lang.reflect.Type;


public abstract class ResultCallback<T> {
    public Type mType;

    public Class<T> clazz;

    public ResultCallback() {
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public ResultCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void onStart(Request request) {

    }

    public void onEnd() {

    }

    public void inProgress(float progress) {

    }

    /**
     * 请求结果拦截器
     *
     * @param response true对结果重定向 false不处理
     * @return
     */
    public boolean onResponseIntercept(T response) {
        return false;
    }

    public abstract void onError(Request request, Exception e);

    public abstract void onResponse(T response);

    public void onHeaders(Headers headers) {

    }

    public static final ResultCallback<String> DEFAULT_RESULT_CALLBACK = new ResultCallback<String>() {
        @Override
        public void onError(Request request, Exception e) {

        }

        @Override
        public void onResponse(String response) {

        }
    };
}