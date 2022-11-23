package com.jkcq.hrwtv.wu.newversion.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.http.bean.CourseInfo;
import com.jkcq.hrwtv.http.bean.HeartRateClassInfo;
import com.jkcq.hrwtv.util.DisplayUtils;
import com.jkcq.hrwtv.util.LogUtil;

public class CourseMatchPointView extends View {
    private Context mContext;
    private Paint mPaint;
    private Paint mLinePaint;
    private int mHeight;
    private int mWidth;
    private int mRealViewHeight;

    public CourseMatchPointView(Context context) {
        this(context, null);
    }

    public CourseMatchPointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CourseMatchPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setTextSize(21f);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(3f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = getHeight();
        mWidth = getWidth() - DisplayUtils.dip2px(mContext, 15) * 2;
        mRealViewHeight = mHeight;
    }

    private Bitmap mBitmap;
    private int mBitmapWidth;
    private int mBitmapHeight;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPointerView(canvas);
    }

    public void setStrTime(String time) {
        this.strTime = time;
    }

    String strTime = "00:00";

    /**
     * 画不断移动的指针
     *
     * @param canvas
     */
    private Rect rect = new Rect();
    private Path mpath = new Path();

    float left;

    private void drawPointerView(Canvas canvas) {

        try {
            if (mTarget >= mTotalSecond) {
                mTarget = mTotalSecond;
            }
            mpath.reset();
            left = mTarget * 1.0f * mWidth / mTotalSecond + DisplayUtils.dip2px(mContext, 15);
            /*if (left >= mWidth) {
                left = mWidth-DisplayUtils.dip2px(mContext, 15);
            }*/
            Log.e("drawPointerView", "drawPointerView---------------- left=" + left + "mWidth=" + mWidth);
            //  Log.e("drawPointerView", "courseMatchPoint--------------" + mWidth + "left=" + left + "mTarget=" + mTarget);
            mLinePaint.getTextBounds(mTotalSecond>=3600 ? "00:00:00" : "00:00", 0, strTime.length(), rect);
            canvas.drawText(strTime, left - (rect.right - rect.left) / 2, mHeight - DisplayUtils.dip2px(mContext, 5), mLinePaint);
            //mpathd的起始位置
            mpath.moveTo(left - (rect.right - rect.left) / 2 + (rect.right - rect.left) / 2 - DisplayUtils.dip2px(mContext, 2), mHeight - DisplayUtils.dip2px(mContext, 2));
            //从起始位置划线到(200, 200)坐标
            mpath.lineTo(left - (rect.right - rect.left) / 2 + (rect.right - rect.left) / 2 + DisplayUtils.dip2px(mContext, 2), mHeight - DisplayUtils.dip2px(mContext, 2));
            mpath.lineTo(left - (rect.right - rect.left) / 2 + (rect.right - rect.left) / 2, mHeight);
            //将mpath封闭，也可以写 mpath.lineTo(100, 100);代替
            mpath.close();
            //绘制path路径
            canvas.drawPath(mpath, mLinePaint);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("drawPointerView", "drawPointerView---------------- left=" + e.toString());
           // Log.e("Exception", e.toString());
        }

    }

    private int mTotalSecond;
    private float mTarget;

    public void setValue(CourseInfo data) {
        mTotalSecond = data.getDuration();
    }

    public void moveArrow(long remainTime) {
        this.mTarget = mTotalSecond - remainTime;
        //  LogUtil.e("mTarget=" + mTarget + " mTotalSecond=" + mTotalSecond);
        invalidate();
    }
}
