package com.jkcq.hrwtv.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.app.BaseApp;
import com.jkcq.hrwtv.http.widget.BaseObserver;
import com.jkcq.hrwtv.util.DeviceUtil;
import com.jkcq.hrwtv.util.LogUtil;
import com.jkcq.hrwtv.http.RetrofitHelper;
import com.jkcq.hrwtv.http.bean.BaseResponse;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DialogLogin extends Dialog {
    private Context mContext;
    public OnClickShowView onClickShowView;

    public DialogLogin(Context context, OnClickShowView onClickShowView) {

        this(context, R.style.MyDialogStyle, onClickShowView);
    }

    public DialogLogin(Context context, int themeResId, OnClickShowView onClickShowView) {
        super(context, themeResId);
        this.mContext = context;
        this.onClickShowView = onClickShowView;
        initView();
    }

    protected DialogLogin(Context context, boolean cancelable, OnCancelListener cancelListener, OnClickShowView onClickShowView) {
        super(context, cancelable, cancelListener);
        this.onClickShowView = onClickShowView;
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(com.jkcq.managersetting.R.layout.dialog_login, null);
        setContentView(view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        //设置Dialog大小位置
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
//        int desity = (int) ScreenUtils.getScreenDensity();
//        lp.width= LinearLayout.LayoutParams.MATCH_PARENT;
//        lp.height=LinearLayout.LayoutParams.WRAP_CONTENT;
       /* int width = (int) (ScreenUtils.getScreenWidth() * 0.4);
        int height = (int) (width * 0.6);*/
        int width = (int) (mContext.getResources().getDimension(com.jkcq.hrwtv.base.R.dimen.dp360));
        int height = (int) (mContext.getResources().getDimension(com.jkcq.hrwtv.base.R.dimen.dp237));
        lp.width = width; // 宽度
        lp.height = height; // 高度
        dialogWindow.setAttributes(lp);

        final TextView btn_cancel = view.findViewById(com.jkcq.managersetting.R.id.btn_cancel);
        final TextView btn_sure = view.findViewById(com.jkcq.managersetting.R.id.btn_sure);
        final EditText et_pwd = view.findViewById(com.jkcq.managersetting.R.id.et_pwd);
        final LinearLayout layout_pwd = view.findViewById(com.jkcq.managersetting.R.id.layout_pwd);

        this.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //if (keyCode == KeyEvent.KEYCODE_BACK) {
                   /* warningsDialog = new WarningsDialog(mContext);
                    warningsDialog.setOnSureClick(new WarningsDialog.OnSureClick() {
                        @Override
                        public void OnSureClickListener() {
                            warningsDialog.dismiss();
                            mDialog.dismiss();
                        }
                    });

                    warningsDialog.show();*/
                    hideDialog(false);

                } else {
                }
                return false;
            }
        });


        et_pwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    layout_pwd.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_selected_bg);
                } else {
                    layout_pwd.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_unselected_bg);
                }
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(et_pwd.getText().toString());
            }
        });
        btn_sure.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    btn_sure.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_selected_bg);
                } else {
                    btn_sure.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_unselected_bg);
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog(false);
            }
        });
        btn_cancel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    btn_cancel.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_selected_bg);
                } else {
                    btn_cancel.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_unselected_bg);
                }
            }
        });
    }

    private void hideDialog(boolean isSuccess) {
        dismiss();
        if (onClickShowView != null) {
            onClickShowView.showView(isSuccess);
        }
    }

    private void login(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShort("密码不能为空");
            return;
        }
        HashMap<String, String> para = new HashMap<>();
        para.put("password", pwd);
        para.put("mac", DeviceUtil.getMac(BaseApp.getApp()));
        para.put("type", "0");
        RetrofitHelper.INSTANCE.getService().devcieLogout(para)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Boolean>>() {
                    @Override
                    public void onSuccess(BaseResponse<Boolean> baseResponse) {
                        LogUtil.e("baseResponse=" + baseResponse.getMsg());

                        if (baseResponse.getData()) {
                            hideDialog(true);
                            ToastUtils.showShort("注销成功");
                        } else {
                            ToastUtils.showShort("注销失败，请检查后重试");
                        }
                    }

                    @Override
                    public void dealError(String msg) {
                        ToastUtils.showLong(msg);
                    }
                });
    }


    public interface OnClickShowView {
        public void showView(boolean isSuccess);
    }
}
