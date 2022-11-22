package com.jkcq.hrwtv.wu.newversion.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jkcq.hrwtv.R;

public class ChangeBtnView extends RelativeLayout implements View.OnFocusChangeListener, View.OnClickListener {

    //显示类型 0：文字加图片，1只有图片，2只有文字
    private int mShowType = 0;
    //属性
    private String mDefaultText;
    private float mDefaultTextSize;
    private int mDefaultTextColor;
    private int mSelectTextColor;
    private Drawable mDefaultImgRes;
    private Drawable mSelectImgRes;
    private Drawable mDefaultBg;
    private Drawable mSelectBg;
    //view
    private LinearLayout ll_change_view;
    private TextView tv_content;
    private ImageView iv_res;

    public ChangeBtnView(Context context) {
        this(context, null);
    }

    public ChangeBtnView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangeBtnView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChangeBtnView,
                defStyleAttr, 0);
        mDefaultText = a.getString(R.styleable.ChangeBtnView_defaultText);
        mDefaultTextSize = a.getDimension(R.styleable.ChangeBtnView_defaultTextSize, 26);
        mDefaultTextColor = a.getColor(R.styleable.ChangeBtnView_defaultTextColor, context.getResources().getColor(com.jkcq.managersetting.R.color.common_text_color));
        mSelectTextColor = a.getColor(R.styleable.ChangeBtnView_selectTextColor, context.getResources().getColor(com.jkcq.hrwtv.base.R.color.white));
        mDefaultImgRes = a.getDrawable(R.styleable.ChangeBtnView_defaultImgRes);
        mSelectImgRes = a.getDrawable(R.styleable.ChangeBtnView_selectImgRes);
        mDefaultBg = a.getDrawable(R.styleable.ChangeBtnView_defaultBackground);
        mSelectBg = a.getDrawable(R.styleable.ChangeBtnView_selectBackground);
        mShowType = a.getInt(R.styleable.ChangeBtnView_showType, 0);
        a.recycle();
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_changebtnview, this, true);
        ll_change_view = findViewById(R.id.ll_change_view);

        tv_content = findViewById(R.id.tv_content);
        iv_res = findViewById(R.id.iv_res);
        if (mShowType == 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tv_content.getLayoutParams());
            layoutParams.setMargins(5, 0, 0, 0);
            tv_content.setLayoutParams(layoutParams);
        } else if (mShowType == 1) {
            tv_content.setVisibility(View.GONE);
        } else if (mShowType == 2) {
            iv_res.setVisibility(View.GONE);
        } else {
            throw new IllegalArgumentException("showType value is only 0,1,2");
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initData();
        setOnFocusChangeListener(this);
        setOnClickListener(this);
    }

    private void initData() {
        ll_change_view.setBackground(mDefaultBg);
        tv_content.setText(mDefaultText);
        tv_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDefaultTextSize);
        tv_content.setTextColor(mDefaultTextColor);
        iv_res.setImageDrawable(mDefaultImgRes);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (mShowType == 0) {
            if (hasFocus) {
                ll_change_view.setBackground(mSelectBg);
                tv_content.setTextColor(mSelectTextColor);
                iv_res.setImageDrawable(mSelectImgRes);
            } else {
                ll_change_view.setBackground(mDefaultBg);
                tv_content.setTextColor(mDefaultTextColor);
                iv_res.setImageDrawable(mDefaultImgRes);
            }
        } else if (mShowType == 2) {
            if (hasFocus) {
                ll_change_view.setBackground(mSelectBg);
                tv_content.setTextColor(mSelectTextColor);
            } else {
                ll_change_view.setBackground(mDefaultBg);
                tv_content.setTextColor(mDefaultTextColor);
            }

        } else if (mShowType == 1) {
            if (hasFocus) {
                ll_change_view.setBackground(mSelectBg);
                iv_res.setImageDrawable(mSelectImgRes);
            } else {
                ll_change_view.setBackground(mDefaultBg);
                iv_res.setImageDrawable(mDefaultImgRes);
            }
        }
        Log.e("ChangeBtnView", "focus=" + hasFocus);
    }

    @Override
    public void onClick(View v) {
        Log.e("ChangeBtnView", "onClick");
    }
}
