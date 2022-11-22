package com.jkcq.hrwtv.http.widget;

import android.text.TextUtils;
import android.util.Log;

import com.jkcq.hrwtv.base.mvp.BaseView;
import com.jkcq.hrwtv.http.interceptor.ExceptionHandle;
import com.jkcq.hrwtv.http.bean.BaseResponse;

import io.reactivex.observers.ResourceObserver;
import retrofit2.HttpException;

/**
 * created by wq on 2019/5/9
 */
public abstract class BaseObserver<T extends BaseResponse> extends ResourceObserver<T> {
    private BaseView mView;
    private String mErrorMsg;
    private boolean isShowError = true;

    private String LOG_TAG = "http_result";

    protected BaseObserver() {
    }

    protected BaseObserver(BaseView view) {
        this.mView = view;
    }

    protected BaseObserver(BaseView view, String errorMsg) {
        this.mView = view;
        this.mErrorMsg = errorMsg;
    }

    protected BaseObserver(BaseView view, boolean isShowError) {
        this.mView = view;
        this.isShowError = isShowError;
    }

    protected BaseObserver(BaseView view, String errorMsg, boolean isShowError) {
        this.mView = view;
        this.mErrorMsg = errorMsg;
        this.isShowError = isShowError;
    }

    public abstract void onSuccess(T t);

    public void onNullData() {
        dealError("暂无数据");
    }

    @Override
    public void onNext(T t) {
        Log.e(LOG_TAG, t.toString());
        if (t != null) {
            if (t.getCode() == 0) {
                if (t.getData() != null) {
                    onSuccess(t);
                } else {
                    onNullData();
                }
            } else {
                dealError(t.getMsg());
            }
        } else {
            dealError("服务器异常");
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        if (mView == null) {
            return;
        }
        mView.onRespondError(e.getMessage());
        //doError(e);
    }

    private void doError(Throwable e) {

        if (mErrorMsg != null && !TextUtils.isEmpty(mErrorMsg)) {
            mView.onRespondError(mErrorMsg);
        } else if (e instanceof ExceptionHandle.ServerException) {
            mView.onRespondError(e.toString());
        } else if (e instanceof HttpException) {
            mView.onRespondError("网络错误");
        } else {
            mView.onRespondError("未知错误");
        }
    }

    public void dealError(String msg) {
        if (mView != null) {
            mView.onRespondError(msg);
        }
    }
}


