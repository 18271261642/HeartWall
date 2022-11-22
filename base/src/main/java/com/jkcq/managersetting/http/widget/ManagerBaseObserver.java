package com.jkcq.managersetting.http.widget;

import android.text.TextUtils;
import android.util.Log;

import com.jkcq.managersetting.BaseView;
import com.jkcq.managersetting.bean.ManagerBaseResponse;
import com.blankj.utilcode.util.ToastUtils;

import io.reactivex.observers.ResourceObserver;
import retrofit2.HttpException;

/**
 * created by wq on 2019/5/9
 */
public abstract class ManagerBaseObserver<T extends ManagerBaseResponse> extends ResourceObserver<T> {
    private BaseView mView;
    private String mErrorMsg;
    private boolean isShowError = true;

    protected ManagerBaseObserver() {
    }
    protected ManagerBaseObserver(BaseView view) {
        this.mView = view;
    }

    protected ManagerBaseObserver(BaseView view, String errorMsg) {
        this.mView = view;
        this.mErrorMsg = errorMsg;
    }

    protected ManagerBaseObserver(BaseView view, boolean isShowError) {
        this.mView = view;
        this.isShowError = isShowError;
    }

    protected ManagerBaseObserver(BaseView view, String errorMsg, boolean isShowError) {
        this.mView = view;
        this.mErrorMsg = errorMsg;
        this.isShowError = isShowError;
    }


    @Override
    public void onNext(T t) {
        if (t != null) {
            Log.e("httpResult","result="+t.toString());
            if (t.getCode() == 2000) {
                if (t.getObj() != null) {
                    onSuccess(t);
                } else {
                    onErrorMessage("暂无数据");
                }
            } else {
                onErrorMessage(t.getMessage());
            }
        } else {
            onErrorMessage("服务器异常");
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        if (mView == null) {
            ToastUtils.showShort(e.getMessage());
            return;
        }
        mView.onRespondError(e.getMessage());
        //doError(e);
    }

    private void doError(Throwable e) {

        if (mErrorMsg != null && !TextUtils.isEmpty(mErrorMsg)) {
            mView.onRespondError(mErrorMsg);
        }else if (e instanceof HttpException) {
            mView.onRespondError("网络错误");
        } else {
            mView.onRespondError("未知错误");
        }
    }

    /**
     * 请求成功
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    public void onErrorMessage(String message) {
        mView.onRespondError(message);
    }
}


