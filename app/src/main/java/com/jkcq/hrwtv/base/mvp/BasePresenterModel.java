package com.jkcq.hrwtv.base.mvp;

import android.content.Context;

/**
 * Created by Administrator on 2017/10/16.
 */
public abstract class BasePresenterModel<T extends BaseView> {

    protected Context context;

    protected T baseView;

    public BasePresenterModel(Context context, T baseView) {
        this.context = context;
        this.baseView = baseView;
    }
}
