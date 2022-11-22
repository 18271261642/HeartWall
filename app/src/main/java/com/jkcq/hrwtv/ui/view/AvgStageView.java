package com.jkcq.hrwtv.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jkcq.hrwtv.util.DisplayUtils;
import com.jkcq.hrwtv.util.IniColorUtil;

import java.util.HashMap;
import java.util.LinkedList;

/*
 *
 *
 * @author mhj
 * Create at 2018/4/20 12:09
 */
public class AvgStageView extends View {

    private LinkedList heartRateStrength = new LinkedList();


    //色值
    HashMap<String, Integer> map;


    private int RADIO = 0;


    private Bitmap background;


    int color;

    private Paint paint;


    public AvgStageView(Context context) {
        super(context, null);
    }

    public AvgStageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        map = IniColorUtil.initParams(context);
        RADIO = DisplayUtils.dip2px(context, 10);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(color);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, RADIO, paint);

    }


    public void updateColor(int hearStrength) {
        if (hearStrength < 50) {
            color = map.get("LTGRAY");
        } else if (hearStrength >= 50 && hearStrength <= 59) {
            color = map.get("GRAY");
        } else if (hearStrength >= 60 && hearStrength <= 69) {
            color = map.get("BLUE");
        } else if (hearStrength >= 70 && hearStrength <= 79) {
            color = map.get("GREEN");
        } else if (hearStrength >= 80 && hearStrength <= 89) {
            color = map.get("YELLOW");
        } else if (hearStrength >= 90) {
            color = map.get("RED");
        }
        postInvalidate();//只是重新画了view 的没有重新画recybel

    }
    /**
     * 完全自己自定义的方法哦，你甚至可以让不管设置什么都返回固定大小!
     *
     * @param measureSpec 传入的measureSpec
     * @return 处理后的大小
     */
    private int measure(int measureSpec) {
        int result = 0;
        //分别获取测量模式 和 测量大小
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        //如果是精确度模式，呢就按xml中定义的来
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        //如果是最大值模式，就按我们定义的来
        else if (specMode == MeasureSpec.AT_MOST) {
            result = 100;
        }

        return result;
    }


}
