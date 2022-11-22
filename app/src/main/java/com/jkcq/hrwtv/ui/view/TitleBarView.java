package com.jkcq.hrwtv.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkcq.hrwtv.R;


/*
 * 通用标题控件封装
 * @author 苗恒聚
 * V 1.0.0
 * Create at 2017/10/25
 */
public class TitleBarView extends LinearLayout {

    /**
     * 标题
     */
    private String titleText;

    /**
     * 左边按钮显示内容
     */
    private String leftText;

    /**
     * 右边按钮显示内容
     */
    private String rightText;

    /**
     * 左边按钮显示图标
     */
    private Drawable leftIcon;

    /**
     * 右边按钮显示图标
     */
    private Drawable rightIcon;

    private TextView titleTextView;

    private TextView leftTextView;

    private TextView rightTextView;

    private OnTitleBarClickListener onTitleBarClickListener;

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initBase(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initBase(context, attrs, defStyleAttr);
    }

    private void initBase(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBar,
                defStyleAttr, 0);
        titleText = a.getString(R.styleable.TitleBar_titleText);
        leftText = a.getString(R.styleable.TitleBar_leftText);
        rightText = a.getString(R.styleable.TitleBar_rightText);

        leftIcon = a.getDrawable(R.styleable.TitleBar_leftIcon);
        rightIcon = a.getDrawable(R.styleable.TitleBar_rightIcon);
        a.recycle();

        initView();
        setListener();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initData();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_titlebar, this, true);

        setGravity(Gravity.CENTER);

        titleTextView = (TextView) findViewById(R.id.view_title_text);
        leftTextView = (TextView) findViewById(R.id.view_title_left);
        rightTextView = (TextView) findViewById(R.id.view_title_right);
    }


    private void initData() {
        if (!TextUtils.isEmpty(titleText)) {
            titleTextView.setText(titleText);
        }

        leftTextView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(leftText)) {
            leftTextView.setText(leftText);
            leftTextView.setCompoundDrawables(null, null, null, null);
        } else if (leftIcon != null) {
            leftIcon.setBounds(0, 0, leftIcon.getMinimumWidth(), leftIcon.getMinimumHeight());
            leftTextView.setCompoundDrawables(leftIcon, null, null, null);
            leftTextView.setText("");
        } else {
            leftTextView.setVisibility(View.INVISIBLE);
        }

        rightTextView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(rightText)) {
            rightTextView.setText(rightText);
            rightTextView.setCompoundDrawables(null, null, null, null);
        } else if (rightIcon != null) {
            rightIcon.setBounds(0, 0, rightIcon.getMinimumWidth(), rightIcon.getMinimumHeight());
            rightTextView.setCompoundDrawables(null, null, rightIcon, null);
            rightTextView.setText("");
        } else {
            rightTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void setListener() {
        titleTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTitleBarClickListener != null) {
                    onTitleBarClickListener.onTitleClicked(view);
                }
            }
        });

        leftTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTitleBarClickListener != null) {
                    onTitleBarClickListener.onLeftClicked(view);
                }
            }
        });

        rightTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTitleBarClickListener != null) {
                    onTitleBarClickListener.onRightClicked(view);
                }
            }
        });
    }

    public void setRightTextViewStateIsShow(boolean rightTextViewState) {
        if (rightTextViewState) {
            rightTextView.setVisibility(View.VISIBLE);

        } else {
            rightTextView.setVisibility(View.INVISIBLE);
        }
    }

    public void setTitle(int resId) {
        if ((resId >>> 24) < 2) {
            return;
        }
        setTitle(getContext().getString(resId));
    }

    public void setTitle(String text) {
        if (null == titleTextView) {
            return;
        }
        titleTextView.setText(TextUtils.isEmpty(text) ? "" : text);
    }

    public void setLeftText(int resId) {
        if ((resId >>> 24) < 2) {
            return;
        }
        setLeftText(getContext().getString(resId));
    }

    public void setLeftText(String text) {
        if (null == leftTextView) {
            return;
        }
        leftTextView.setVisibility(View.VISIBLE);
        leftTextView.setText(TextUtils.isEmpty(text) ? "" : text);
    }

    public void setRightText(int resId) {
        if ((resId >>> 24) < 2) {
            return;
        }
        setRightText(getContext().getString(resId));
    }

    public void setRightText(String text) {
        if (null == rightTextView) {
            return;
        }
        rightTextView.setVisibility(View.VISIBLE);
        rightTextView.setText(TextUtils.isEmpty(text) ? "" : text);
    }

    public void setLeftIcon(int resId) {
        if ((resId >>> 24) < 2) {
            return;
        }
        leftTextView.setVisibility(View.VISIBLE);
        rightTextView.setText("");
        leftTextView.setCompoundDrawables(getDrawable(resId), null, null, null);
    }

    public void setLeftIconEnable(boolean enable) {
        leftTextView.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
    }

    public void setRightIcon(int resId) {
        if ((resId >>> 24) < 2) {
            return;
        }
        rightTextView.setVisibility(View.VISIBLE);
        rightTextView.setText("");
        rightTextView.setCompoundDrawables(null, null, getDrawable(resId), null);
    }

    public TextView getRightTextView() {
        return rightTextView;
    }

    /**
     * 根据资源ID获取Drawable/设置边框
     *
     * @param resId
     * @return
     */
    private Drawable getDrawable(int resId) {
        if ((resId >>> 24) < 2) {
            return null;
        }
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getContext().getDrawable(resId);
        } else {
            drawable = getContext().getResources().getDrawable(resId);
        }

        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        return drawable;
    }

    public void setOnTitleBarClickListener(OnTitleBarClickListener onTitleBarClickListener) {
        this.onTitleBarClickListener = onTitleBarClickListener;
    }

    public abstract static class OnTitleBarClickListener {
        /**
         * 标题点击回调
         *
         * @param view
         */
        public void onTitleClicked(View view) {

        }

        /**
         * 左边按钮点击回调
         *
         * @param view
         */
        public abstract void onLeftClicked(View view);

        /**
         * 右边按钮点击回调
         *
         * @param view
         */
        public abstract void onRightClicked(View view);
    }
}