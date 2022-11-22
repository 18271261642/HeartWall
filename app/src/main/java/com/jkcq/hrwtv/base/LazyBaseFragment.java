package com.jkcq.hrwtv.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * 延迟加载
 * classes : com.snscity.globalexchange.base
 * @author 苗恒聚
 * V 1.0.0
 * Create at 2016-2-29 18:44
 */
public abstract class LazyBaseFragment extends BaseFragment {

    /**
     * Fragment是否已经初始化完成
     */
    protected boolean isPrepared;

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    /**
     * Fragment是否已经加载过
     */
    protected boolean isLoaded;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        isPrepared = true;

        if (isVisible) {
            initLazyData();
            isLoaded = true;
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        if (isLoaded || !isPrepared) {
            return;
        }
        isLoaded = true;
        initLazyData();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {

    }

    protected abstract void initLazyData();
}
