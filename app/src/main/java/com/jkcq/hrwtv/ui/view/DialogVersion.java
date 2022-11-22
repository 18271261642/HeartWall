package com.jkcq.hrwtv.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jkcq.hrwtv.R;

public class DialogVersion extends Dialog {
    private Context mContext;
    public OnButtonClick onClickShowView;
    public String titleMsg;
    public String strSure;

    public DialogVersion(Context context, OnButtonClick onClickShowView, String titelMsg) {

        this(context, R.style.MyDialogStyle, onClickShowView, titelMsg, "");

    }

    public DialogVersion(Context context, OnButtonClick onClickShowView, String titelMsg, String tvSure) {

        this(context, R.style.MyDialogStyle, onClickShowView, titelMsg, tvSure);

    }

    public DialogVersion(Context context, int themeResId, OnButtonClick onClickShowView, String titleMsg, String tvSure) {
        super(context, themeResId);
        this.mContext = context;
        this.onClickShowView = onClickShowView;
        this.titleMsg = titleMsg;
        this.strSure = tvSure;
        initView();
    }

    protected DialogVersion(Context context, boolean cancelable, OnCancelListener cancelListener, OnButtonClick onClickShowView) {
        super(context, cancelable, cancelListener);
        this.onClickShowView = onClickShowView;
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(com.jkcq.managersetting.R.layout.dialog_cheack_version, null);
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
        final TextView tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText(titleMsg);
        if (!TextUtils.isEmpty(strSure)) {
            btn_sure.setText(strSure);
        }

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickShowView != null) {
                    onClickShowView.onButtonClickSure();
                    dismiss();
                }
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
                if (onClickShowView != null) {
                    onClickShowView.onButtonClickCancel();
                    dismiss();
                }
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


    public interface OnButtonClick {
        public void onButtonClickSure();

        public void onButtonClickCancel();
    }
}
