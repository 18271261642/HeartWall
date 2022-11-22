package com.jkcq.hrwtv.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.util.DisplayUtils;

/*
 * @author mhj
 * Create at 2018/4/18 12:04 
 */
public class CustomDialog extends Dialog {

    private int mLayout;
    private Context context;
    private View contenntView;
    private int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
    private int mHeight = ViewGroup.LayoutParams.MATCH_PARENT;
    private boolean isCancelOutSide = true;

    public CustomDialog(Context context, int layout) {
        super(context, R.style.MyDialog);
        this.mLayout = layout;
        this.context = context;
        contenntView = LayoutInflater.from(context).inflate(layout, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(mWidth, mHeight);
        //ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(DisplayUtils.getScreenWidth(context), DisplayUtils.getScreenHeight(context));
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(DisplayUtils.getScreenWidth(context), DisplayUtils.getScreenHeight(context) / 4);
        setContentView(contenntView, lp);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(isCancelOutSide);

    }

    public void setCanceledOnTouchOutSide(boolean flag) {
        isCancelOutSide = flag;
    }

    public void setSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public View findViewById(int id) {
        if (contenntView != null) {
            return contenntView.findViewById(id);
        }
        return null;
    }

}
