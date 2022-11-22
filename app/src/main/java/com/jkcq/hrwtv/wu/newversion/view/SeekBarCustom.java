package com.jkcq.hrwtv.wu.newversion.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class SeekBarCustom extends androidx.appcompat.widget.AppCompatSeekBar {
    public SeekBarCustom(Context context) {
        super(context);
    }

    public SeekBarCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeekBarCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
