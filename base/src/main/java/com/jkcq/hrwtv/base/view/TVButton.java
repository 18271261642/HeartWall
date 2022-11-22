package com.jkcq.hrwtv.base.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jkcq.hrwtv.base.R;

public class TVButton extends Button {
    private Context mContext;
    public TVButton(Context context) {
        super(context);
        init(context);
    }

    public TVButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TVButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext=context;
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e("test", "TVButton= "+hasFocus);
                if (hasFocus) {
                    TVButton.this.setTextColor(Color.WHITE);
                    TVButton.this.setBackground(mContext.getResources().getDrawable(R.drawable.shape_btn_selected_bg));
                } else {
                    TVButton.this.setTextColor(getResources().getColor(R.color.common_green));
                    TVButton.this.setBackground(mContext.getResources().getDrawable(R.drawable.shape_btn_unselected_bg));
                }
            }
        });
    }
}
