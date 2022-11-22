package com.jkcq.hrwtv.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jkcq.hrwtv.AllocationApi;
import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.app.BaseApp;
import com.jkcq.hrwtv.util.NetUtils;
import com.jkcq.hrwtv.util.ToastUtils;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment {

    public static final String TAG = BaseFragment.class.getSimpleName();

    protected Context context;

    protected BaseApp app;

    private int mLayoutId;

    protected View mView;

    protected LayoutInflater mInflater;

    protected BaseFragment() {
        this.mLayoutId = getLayoutId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.mInflater = inflater;

        context = getActivity();

        app = (BaseApp) context.getApplicationContext();

        if (null == mView) {
            mView = inflater.inflate(mLayoutId, container, false);
            ButterKnife.bind(this, mView);
            initView(mView);
            initData();
            initEvent();
        } else {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        return mView;
    }

    /**
     * 获取layout Id
     *
     * @return layout Id
     */
    protected abstract int getLayoutId();

    /**
     * 所有初始化View的地方
     */
    protected abstract void initView(View view);

    /**
     * View绑定事件
     */
    protected abstract void initEvent();

    /**
     * 初始化赋值操作
     */
    protected abstract void initData();

    /**
     * 获取当前View
     *
     * @return 当前窗口View
     */
    public View getCurrentView() {
        return mView;
    }

    /**
     * 重新设置View Params，设置不同缩放
     *
     * @param layoutParams
     */
    public void setCurrentViewPararms(FrameLayout.LayoutParams layoutParams) {
        if (null == mView) {
            return;
        }
        mView.setLayoutParams(layoutParams);
    }

    /**
     * 获取当前View配置Params
     *
     * @return
     */
    public FrameLayout.LayoutParams getCurrentViewParams() {
        if (null == mView) {
            return null;
        }
        return (FrameLayout.LayoutParams) mView.getLayoutParams();
    }

    @Override
    public void startActivity(Intent intent) {
        getActivity().startActivity(intent);
    }

    /**
     * onActivityResult 回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void netError() {
        if (!AllocationApi.isNetwork && !AllocationApi.isShowHint) {

            // 显示Dialog
            AllocationApi.isShowHint = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    getActivity());
            builder.setMessage("提示 \n\n目前处于无网络状态，是否立即开启网络！")
                    .setCancelable(false)
                    .setPositiveButton("设置",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    NetUtils.openNet(getActivity());
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * 检查网络状态
     *
     * @return
     */
    public boolean checkNet() {
        if (NetUtils.hasNetwork(BaseApp.getApp())) {
            return true;
        } else {
            ToastUtils.showToast(context, R.string.please_check_that_your_network_is_connected);
            return false;
        }
    }
}
