package com.jkcq.hrwtv.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jkcq.hrwtv.AllocationApi;
import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.app.BaseApp;
import com.jkcq.hrwtv.service.observe.NetProgressObservable;
import com.jkcq.hrwtv.util.DisplayUtils;
import com.jkcq.hrwtv.util.NetUtils;
import com.jkcq.hrwtv.util.SystemBarTintManager;
import com.jkcq.hrwtv.util.ToastUtils;

import java.util.Observable;
import java.util.Observer;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements Observer {

    public final String TAG = this.getClass().getSimpleName();

    protected Context context;

    protected BaseApp app;

//    protected View contentView;

    private SystemBarTintManager tintManager;

    /**
     * 记录上次点击返回键的时间，用于控制退出操作
     */
    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // fullScreen(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        getWindow()
                .getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        initBase();

        ActivityManager.getInstance().putActivity(TAG, this);
    }

    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }


    /**
     * 初始化资源layout，初始化布局加载器，初始化imgLoader，Activity入栈，查找控件，设置监听
     */
    private void initBase() {
        context = this;
        app = (BaseApp) getApplication();
        NetProgressObservable.getInstance().addObserver(this);
//        initBaseView();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
//        initView(contentView);
        initView();
        initBaseData();
        initData();
        initEvent();
        initHeader();
    }

//    protected void initBaseView() {
//        contentView = setBodyView();
//        if (null == contentView) {
//            finish();
//            return;
//        }
//
//        setContentView(contentView);
//      ButterKnife.bind(this, contentView);
//    }


    /**
     * 获取layout Id
     *
     * @return layout Id
     */

    protected abstract int getLayoutId();
    protected void initBaseData() { }
    /**
     * find控件
     */
    protected  void initView(View view){};

    protected abstract void initView();

    /**
     * 设置中间容器View
     *
     * @return 返回
     */
    public View setBodyView() {
        return inflate(getLayoutId());
    }

    /**
     * 数据处理
     */
    protected abstract void initData();

    /**
     * 设置监听
     */
    protected  void initEvent(){};

    private void initHeader() {
//        view = findViewById(R.id.view_head);
//
//        View netError = findViewById(R.id.netError);
//        /**
//         * 判断网络
//         */
//        if (netError != null) {
//            if (!NetUtils.hasNetwork(BaseApp.getApp())) {//网络异常
//                netError.setVisibility(View.VISIBLE);
//            }
//        }

        // setTranslucentStatus(getStatusBarTintColor());
        fullScreen(this);

    }
    /**
     * 填充布局
     *
     * @param resId 控件的id
     */
    public View inflate(int resId) {
        if (resId <= 0) {
            return null;
        }
        return LayoutInflater.from(this).inflate(resId, null);
    }

    public View view;

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(com.jkcq.managersetting.R.anim.push_in_right, com.jkcq.managersetting.R.anim.push_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(com.jkcq.managersetting.R.anim.push_in_right, com.jkcq.managersetting.R.anim.push_out_left);
    }

    @Override
    public void finish() {
        overridePendingTransition(com.jkcq.managersetting.R.anim.push_in_left, com.jkcq.managersetting.R.anim.push_out_right);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        dismissProgressBar();
        NetProgressObservable.getInstance().deleteObserver(this);
        ActivityManager.getInstance().removeActivity(TAG);
        super.onDestroy();
    }

    public void netError(final Activity activity) {
        if (!AllocationApi.isNetwork && !AllocationApi.isShowHint) {
            // 显示Dialog
            AllocationApi.isShowHint = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("提示 \n\n目前处于无网络状态，是否立即开启网络！").setCancelable(false)
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            NetUtils.openNet(activity);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof NetProgressObservable && arg instanceof Message) {
            Message msg = (Message) arg;
            switch (msg.what) {
                case NetProgressObservable.SHOW_PROGRESS_BAR:
                    if (msg.obj instanceof String) {
                        showProgress((String) msg.obj);
                    } else {
                        showProgress(getResources().getString(R.string.please_wait));
                    }
                    break;
                case NetProgressObservable.DISMISS_PORGRESS_BAR:
                    dismissProgressBar();
                    break;
                default:
                    break;
            }
        } else {
            onObserverChange(o, arg);
        }
    }

    public void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    public void showToast(int toast) {
        Toast.makeText(this, getResources().getString(toast), Toast.LENGTH_SHORT).show();
    }


    private ProgressDialog netReqProgressBar;

    private void showProgress(final String desc) {
        if (null == netReqProgressBar) {
            netReqProgressBar = new ProgressDialog(this);
            netReqProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        netReqProgressBar.setMessage(desc);
        netReqProgressBar.show();
        // 设置让返回按键失效，dialog以外的空间失效！
        netReqProgressBar.setCancelable(false);
        netReqProgressBar.setCanceledOnTouchOutside(false);
    }

    private void dismissProgressBar() {
        if (netReqProgressBar != null && netReqProgressBar.isShowing()) {
            netReqProgressBar.dismiss();
        }
    }

    public void onObserverChange(Observable o, Object arg) {
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

    /**
     * 设置Titlebar padding
     */
    public void setViewPadding(View view) {
        if (null == view) {
            return;
        }
        int height = DisplayUtils.getStatusBarHeight(this);

        if (0 == height) {
            return;
        }
        view.setPadding(view.getPaddingLeft(), height,
                view.getPaddingRight(),
                view.getPaddingBottom());
    }
}