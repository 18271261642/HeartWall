package com.jkcq.managersetting.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.blankj.utilcode.util.ScreenUtils;
import com.jkcq.hrwtv.base.R;

/**
 * created by wq on 2019/6/13
 */
public class DialogFactory {
    private volatile static DialogFactory sDialogFactory;
    private int mDialogWidth;
    private int mDialogHeight;
    private static final float WIDTH_PERCENT = 1.0f;

    private DialogFactory() {
        mDialogWidth = (int) (ScreenUtils.getScreenWidth() * WIDTH_PERCENT);
        mDialogHeight = (int) (ScreenUtils.getScreenDensity()) * 130;
    }

    public static DialogFactory getInstance() {
        if (sDialogFactory == null) {
            synchronized (DialogFactory.class) {
                if (sDialogFactory == null) {
                    sDialogFactory = new DialogFactory();
                }
            }
        }
        return sDialogFactory;
    }

    private View setDefaultDialogStyle(Activity activity, Dialog dialog, int resId) {
        return setDefaultDialogStyle(activity, dialog, resId, false, mDialogWidth, mDialogHeight);
    }

    private View setDefaultDialogStyle(Activity activity, Dialog dialog, int resId, boolean cancelable) {
        return setDefaultDialogStyle(activity, dialog, resId, cancelable, mDialogWidth, mDialogHeight);
    }

    private View setDefaultDialogStyle(Activity activity, Dialog dialog, int resId, boolean cancelable, int width, int height) {
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.from(activity).inflate(resId, null);
        //添加需要加布局参数，设置不用
//        dialog.addContentView(view, layoutParams);
        dialog.setContentView(view);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancelable);
        //设置Dialog大小位置
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        int desity = (int) ScreenUtils.getScreenDensity();
//        lp.width= LinearLayout.LayoutParams.MATCH_PARENT;
//        lp.height=LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.width = width; // 宽度
        lp.height = height; // 高度
        dialogWindow.setAttributes(lp);
        return view;
    }


    public Dialog createDialog(Activity activity, int resId, String content) {
        final Dialog dialog = new Dialog(activity, R.style.MyDialogStyle);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_network_layout, null);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        iv.setImageResource(resId);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_content.setText(content);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        //设置Dialog大小位置
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = 510;
        dialogWindow.setAttributes(lp);
        return dialog;
    }

    public void showFinishDialog() {

    }

    public void showLoginDialog(Activity activity) {
        int width = (int) (ScreenUtils.getScreenWidth() * 0.4);
        int height = (int) (width * 0.6);
        final Dialog dialog = new Dialog(activity, R.style.MyDialogStyle);
        View view = setDefaultDialogStyle(activity, dialog, R.layout.dialog_login, false, width, height);
        final Button btn_cancel = view.findViewById(R.id.btn_cancel);
        final Button btn_sure = view.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                dialog.dismiss();
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
        dialog.show();

    }

    public void showCourseDialog(Activity activity) {
        int width = (int) (ScreenUtils.getScreenWidth() * 0.3);
        int height = (int) (width * 0.6);
        final Dialog dialog = new Dialog(activity, R.style.MyDialogStyle);
        View view = setDefaultDialogStyle(activity, dialog, R.layout.dialog_login, false, width, height);
        final Button btn_cancel = view.findViewById(R.id.btn_cancel);
        final Button btn_sure = view.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                dialog.dismiss();
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
        dialog.show();

    }
}
