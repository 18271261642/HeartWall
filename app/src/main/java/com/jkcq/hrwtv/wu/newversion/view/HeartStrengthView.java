package com.jkcq.hrwtv.wu.newversion.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean;
import com.jkcq.hrwtv.util.AdpterUtil;
import com.jkcq.hrwtv.util.HeartRateConvertUtils;
import com.jkcq.hrwtv.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class HeartStrengthView extends View {

    private float mWidth;
    private float mHeight;
    private float mItemWidth;
    private Paint mPaint;

    private int mAge;

    public HeartStrengthView(Context context) {
        super(context);
    }

    public HeartStrengthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartStrengthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = (float) getWidth();
        mHeight = (float) getHeight();
    }

    private void init() {
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mItemWidth = mWidth / tempList.size();
        for (int i = 0; i < tempList.size(); i++) {
            int heart = tempList.get(i);
            float left = mItemWidth * i;
            float top = 0;
            float right = mItemWidth * (i + 1);
            float bottom = mHeight;
            RectF rectF = new RectF(left, top, right, bottom);
            drawHeart(canvas, heart, rectF);
        }
    }

    /**
     * 画心率
     *
     * @param canvas
     * @param heart
     * @param rectF
     */
    private void drawHeart(Canvas canvas, int heart, RectF rectF) {
        int type = AdpterUtil.convertRang(HeartRateConvertUtils.hearRate2Percent(heart, mAge));
        switch (type) {
            case 0:
                mPaint.setColor(Color.parseColor("#BDC1C7"));
                break;
            case 1:
                mPaint.setColor(Color.parseColor("#9399A5"));
                break;
            case 2:
                mPaint.setColor(Color.parseColor("#3FA6F2"));
                break;
            case 3:
                mPaint.setColor(Color.parseColor("#14D36B"));
                break;
            case 4:
                mPaint.setColor(Color.parseColor("#FFCB14"));
                break;
            case 5:
                mPaint.setColor(Color.parseColor("#F85842"));
                break;
        }
        canvas.drawRect(rectF, mPaint);
        LogUtil.e("Heart", "onDraw");
    }


    private ArrayList<Integer> tempList = new ArrayList<>();

    public void setData(DevicesDataShowBean deviceBean) {
        tempList = deviceBean.getTempList();
        mAge = deviceBean.getAge();
    }

    private void setTargetData(DevicesDataShowBean deviceBean) {
        mAge = deviceBean.getAge();
        int size = deviceBean.getHrTaskList().size();
        int pagesize = 60 / Constant.REFRESH_RATE;
        int pagecount;
        int m = size % pagesize;
        if (m > 0) {
            pagecount = size / pagesize + 1;
        } else {
            pagecount = size / pagesize;
        }
        for (int i = 1; i <= pagecount; i++) {
            if (m == 0) {
                List<Integer> subList = deviceBean.getHrTaskList().subList((i - 1) * pagesize, pagesize * i);

                int heart = 0;
                for (int j = 0; j < subList.size(); j++) {
                    heart = heart + subList.get(j);
                }
                heart = heart / subList.size();
                tempList.add(heart);
            } else {
                if (i == pagecount) {
                    List<Integer> subList = deviceBean.getHrTaskList().subList((i - 1) * pagesize, size);
                    int heart = 0;
                    for (int j = 0; j < subList.size(); j++) {
                        heart = heart + subList.get(j);
                    }
                    heart = heart / subList.size();
                    tempList.add(heart);
                } else {
                    List<Integer> subList = deviceBean.getHrTaskList().subList((i - 1) * pagesize, pagesize * (i));
                    int heart = 0;
                    for (int j = 0; j < subList.size(); j++) {
                        heart = heart + subList.get(j);
                    }
                    heart = heart / subList.size();
                    tempList.add(heart);
                }
            }
        }
    }


}
