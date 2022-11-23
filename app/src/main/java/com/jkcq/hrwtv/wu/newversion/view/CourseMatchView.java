package com.jkcq.hrwtv.wu.newversion.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.http.bean.CourseDetail;
import com.jkcq.hrwtv.http.bean.CourseInfo;
import com.jkcq.hrwtv.http.bean.CourseUserInfo;
import com.jkcq.hrwtv.http.bean.HeartRateClassInfo;
import com.jkcq.hrwtv.service.UserContans;
import com.jkcq.hrwtv.util.DisplayUtils;
import com.jkcq.hrwtv.util.HeartRateConvertUtils;
import com.jkcq.hrwtv.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class CourseMatchView extends View {
    private Context mContext;
    private Paint mPaint;
    private Paint mLinePaint;
    private int mHeight;
    private int mWidth;
    private int mRealViewHeight;

    private ArrayList<Integer> colors = new ArrayList<Integer>();

    public CourseMatchView(Context context) {
        this(context, null);
    }

    public CourseMatchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CourseMatchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.WHITE);
        colors.add(mContext.getResources().getColor(com.jkcq.hrwtv.base.R.color.white));
        colors.add(mContext.getResources().getColor(com.jkcq.hrwtv.base.R.color.sport_state_one));
        colors.add(mContext.getResources().getColor(com.jkcq.hrwtv.base.R.color.sport_state_two));
        colors.add(mContext.getResources().getColor(com.jkcq.hrwtv.base.R.color.sport_state_three));
        colors.add(mContext.getResources().getColor(com.jkcq.hrwtv.base.R.color.sport_state_four));
        colors.add(mContext.getResources().getColor(com.jkcq.hrwtv.base.R.color.sport_state_five));
        colors.add(mContext.getResources().getColor(com.jkcq.hrwtv.base.R.color.sport_state_six));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = getHeight();
        mWidth = getWidth();
        mRealViewHeight = mHeight;
    }

    private Bitmap mBitmap;
    private int mBitmapWidth;
    private int mBitmapHeight;
    private Path mPath = new Path();
    RectF rectF = new RectF();
    float left = 0;
    float top = mHeight - mRealViewHeight;
    float right = mWidth;
    float bottom = mHeight;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("drawPointerView", "CourseMatchView" + mWidth);
        left = 0;
        top = mHeight - mRealViewHeight;
        right = mWidth;
        bottom = mHeight;
        rectF.left = left;
        rectF.right = mWidth;
        rectF.top = 0;
        rectF.bottom = mRealViewHeight;
        mPath.addRoundRect(rectF, (rectF.bottom - rectF.top) / 2, (rectF.bottom - rectF.top) / 2, Path.Direction.CCW);
        if (Build.VERSION.SDK_INT >= 28) {
            canvas.clipPath(mPath);
        } else {
            canvas.clipPath(mPath);
            // canvas.clipPath(mPath, Region.Op.XOR);
        }
        if (isMoveArrow) {
            drawPointerView(canvas);
        } else {
            drawPointerView(canvas);
            drawRangeView(canvas);
        }
    }

    /**
     * 画课程区间视图
     *
     * @param canvas
     */
    int type;
    RectF rect = new RectF();

    private void drawRangeView(Canvas canvas) {
        if (mDatas == null || mDatas.size() == 0) {
            return;
        }
        if (mTotalSecond == 0) {
            return;
        }
        for (int i = 0; i < mDatas.size(); i++) {
            courseDetail = mDatas.get(i);
            left = courseDetail.getBegin() * 1.0f * mWidth / mTotalSecond;
            //float right = left + mDatas.get(i).getEnd() * mWidth / mTotalSecond;
            if (courseDetail.getEnd() - courseDetail.getBegin() > 1) {
                right = (courseDetail.getEnd() + 1) * 1.0f * mWidth / mTotalSecond;
            } else {
                right = courseDetail.getEnd() * 1.0f * mWidth / mTotalSecond;
            }
//            Log.e("test", "mTotalSecond= " + mTotalSecond + " currentTime=" + mDatas.get(i).getDuration() * 60 + " left=" + left + " right=" + right + " width=" + mWidth);

            if (i != mDatas.size() - 1) {
                rect.left = left;
                rect.top = mHeight - mRealViewHeight;
                rect.right = right;
                rect.bottom = mHeight;
            } else {
                rect = new RectF(left, mHeight - mRealViewHeight, mWidth, mHeight);
                rect.left = left;
                rect.top = mHeight - mRealViewHeight;
                rect.right = mWidth;
                rect.bottom = mHeight;
            }
            type = mDatas.get(i).getTargetRange();

            if (type == -1) {
                mPaint.setColor(colors.get(0));
            } else if (type == 0) {
                mPaint.setColor(colors.get(1));
            } else if (type == 1) {
                mPaint.setColor(colors.get(2));
            } else if (type == 2) {
                mPaint.setColor(colors.get(3));
            } else if (type == 3) {
                mPaint.setColor(colors.get(4));
            } else if (type == 4) {
                mPaint.setColor(colors.get(5));
            } else if (type == 5) {
                mPaint.setColor(colors.get(6));
            }
            left = right;
        //    Log.e("drawPointerView", "cousreMatchView--------------" + mWidth + "left=" + rect.left + ",right=" + rect.right + "mWidth=" + mWidth + "start" + mDatas.get(i).getBegin() + ",end=" + mDatas.get(i).getEnd() + ",type=" + type + ",mTotalSecond=" + mTotalSecond);
            canvas.drawRect(rect, mPaint);

        }
    }


    /**
     * 画不断移动的指针
     *
     * @param canvas
     */
    private void drawPointerView(Canvas canvas) {
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_route_down);
            mBitmapWidth = mBitmap.getWidth();
            mBitmapHeight = mBitmap.getHeight();
        }
        float left = mTarget * mWidth / mTotalSecond;
        canvas.drawBitmap(mBitmap, left - mBitmapWidth , mHeight - mRealViewHeight - mBitmapHeight, mLinePaint);
        canvas.drawLine(left, mHeight - mRealViewHeight, left, mHeight, mLinePaint);
    }

    private int mTotalSecond;
    private List<CourseDetail> mDatas = new ArrayList<>();
    private boolean isMoveArrow = false;
    private CourseDetail courseDetail;
    private float mTarget;

    public void setValue(int mTotalSecond, List<CourseDetail> list) {
        this.mTotalSecond = mTotalSecond;
        mDatas.clear();
        mDatas.addAll(list);
        isMoveArrow = false;
        invalidate();
    }

    public void setValue(List<CourseDetail> list) {
        if (list.size() > 0) {
            this.mTotalSecond = list.get(list.size() - 1).getEnd();
            if (this.mTotalSecond >= UserContans.couserTime && UserContans.couserTime != 0) {
                this.mTotalSecond = UserContans.couserTime;
            }
            mDatas.clear();
            mDatas.addAll(list);


            Log.e("setValue", "setValue=" + mDatas.size() + "this.mTotalSecond=" + this.mTotalSecond);


            invalidate();
        }
    }

}
