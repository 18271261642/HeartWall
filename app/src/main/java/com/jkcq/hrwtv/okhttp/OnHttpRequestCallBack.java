package com.jkcq.hrwtv.okhttp;

public abstract class OnHttpRequestCallBack<T> {

    public abstract void onSuccess(T bean);

    public abstract void onGetErrorCode(T bean);

    public abstract void onFailure(int errorCode, String msg);

    /**
     * 下载进度条
     *
     * @param progress
     */
    public void inProgress(final float progress) {

    }
}
