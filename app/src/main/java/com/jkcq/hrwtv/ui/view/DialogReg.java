package com.jkcq.hrwtv.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
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

import com.jkcq.managersetting.OnNotRegisterListener;
import com.jkcq.managersetting.PreferenceUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.jkcq.hrwtv.app.BaseApp;
import com.jkcq.hrwtv.http.bean.TokenBean;
import com.jkcq.hrwtv.http.widget.BaseObserver;
import com.jkcq.hrwtv.util.DeviceUtil;
import com.jkcq.hrwtv.util.LogUtil;
import com.jkcq.hrwtv.http.RetrofitHelper;
import com.jkcq.hrwtv.http.bean.BaseResponse;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DialogReg extends Dialog {
    private Context mContext;
    public OnClickShowView onClickShowView;
    private LinearLayout layout_brand_id, layout_club_id, layout_accout, layout_pwd;
    private EditText et_accout, et_pwd;

    private OnNotRegisterListener onNotRegisterListener;

    public void setOnNotRegisterListener(OnNotRegisterListener onNotRegisterListener) {
        this.onNotRegisterListener = onNotRegisterListener;
    }

    public DialogReg(@NonNull Context context) {
        super(context);
    }

    public DialogReg(Context context, OnClickShowView onClickShowView) {
        this(context, com.jkcq.managersetting.R.style.BaseDialog, onClickShowView);

    }

    public DialogReg(Context context, int themeResId, OnClickShowView onClickShowView) {
        super(context, themeResId);
        this.mContext = context;
        this.onClickShowView = onClickShowView;
        initView();
    }

    protected DialogReg(Context context, boolean cancelable, OnCancelListener cancelListener, OnClickShowView onClickShowView) {
        super(context, cancelable, cancelListener);
        this.onClickShowView = onClickShowView;
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(com.jkcq.managersetting.R.layout.dialog_user_alone_reg_layout, null);
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
        int height = (int) (mContext.getResources().getDimension(com.jkcq.hrwtv.base.R.dimen.dp417));
        lp.width = width; // 宽度
        lp.height = height; // 高度
        dialogWindow.setAttributes(lp);


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
                    hideDialog(false, "");

                } else {
                }
                return false;
            }
        });
        final LinearLayout layout_accout = view.findViewById(com.jkcq.managersetting.R.id.layout_accout);
        final EditText et_accout = view.findViewById(com.jkcq.managersetting.R.id.et_accout);
        final LinearLayout layout_pwd = view.findViewById(com.jkcq.managersetting.R.id.layout_pwd);
        final EditText et_pwd = view.findViewById(com.jkcq.managersetting.R.id.et_pwd);

        final TextView btn_cancel = view.findViewById(com.jkcq.managersetting.R.id.btn_cancel);
        final TextView btn_sure = view.findViewById(com.jkcq.managersetting.R.id.btn_sure);
        // final LinearLayout layout_pwd = view.findViewById(R.id.layout_pwd);

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
        et_accout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    layout_accout.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_selected_bg);
                } else {
                    layout_accout.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_unselected_bg);
                }
            }
        });
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regDevice(et_accout.getText().toString(), et_pwd.getText().toString());
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
                hideDialog(false, "");
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

    private void hideDialog(boolean isSuccess, String token) {
        dismiss();
        if (onClickShowView != null) {
            onClickShowView.showView(isSuccess, token);
        }
    }


    /**
     * {
     * "mac": "buzhidao",
     * "password": "buzhidao",
     * "type": 1, 心率墙为：0  接收器：1
     * "username": "yudaoyuanma"
     * }
     *
     * @param pwd
     */

    public void regDevice(String userName, String pwd) {
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.showShort("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShort("密码不能为空");
            return;
        }

        HashMap<String, String> para = new HashMap<>();
        para.put("password", pwd);
        para.put("type", "0");
        para.put("username", userName);
        para.put("mac", DeviceUtil.getMac(BaseApp.getApp()));
        RetrofitHelper.INSTANCE.getNoAuthservice().deviceRegister(para)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<TokenBean>>() {
                    @Override
                    public void onSuccess(BaseResponse<TokenBean> baseResponse) {
                        LogUtil.e("baseResponse=" + baseResponse.getMsg());
                        ToastUtils.showLong("注册成功");
                        PreferenceUtil.getInstance().putString("user_name",userName);
                        PreferenceUtil.getInstance().putString("user_pwd",pwd);
                        hideDialog(true, baseResponse.getData().getToken());
                        //  EventBus.getDefault().post(new TypeEvent(EventConstant.EVENT_LOGIN, ""));
                    }

                    @Override
                    public void dealError(String msg) {
                        ToastUtils.showLong(msg);
                    }
                });

    }



    public void regDevice(String userName, String pwd,OnNotRegisterListener onNotRegisterListener) {
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.showShort("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShort("密码不能为空");
            return;
        }

        HashMap<String, String> para = new HashMap<>();
        para.put("password", pwd);
        para.put("type", "0");
        para.put("username", userName);
        para.put("mac", DeviceUtil.getMac(BaseApp.getApp()));
        RetrofitHelper.INSTANCE.getNoAuthservice().deviceRegister(para)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<TokenBean>>() {
                    @Override
                    public void onSuccess(BaseResponse<TokenBean> baseResponse) {
                        LogUtil.e("baseResponse=" + baseResponse.getMsg());
                       // ToastUtils.showLong("注册成功");
                        PreferenceUtil.getInstance().putString("user_name",userName);
                        PreferenceUtil.getInstance().putString("user_pwd",pwd);
                        hideDialog(true, baseResponse.getData().getToken());
                        //  EventBus.getDefault().post(new TypeEvent(EventConstant.EVENT_LOGIN, ""));
                    }

                    @Override
                    public void dealError(String msg) {
                        ToastUtils.showLong(msg);
                        if(onNotRegisterListener != null)
                            onNotRegisterListener.notRegister();
                    }
                });

    }

    public interface OnClickShowView {
        public void showView(boolean isSuccess, String token);
    }
}
