package com.jkcq.hrwtv.wu.newversion.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.http.bean.CourseDetail;
import com.jkcq.hrwtv.service.UserContans;

import java.lang.reflect.GenericSignatureFormatError;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import timber.log.Timber;

public class RealMatchView extends View {
    private Context mContext;
    private Paint mPaint;
    private Paint mLinePaint;
    private int mHeight;
    private int mWidth;
    private int mRealViewHeight;
    private ArrayList<Integer> colors = new ArrayList<Integer>();

    public RealMatchView(Context context) {
        this(context, null);
    }

    public RealMatchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RealMatchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRangeView(canvas);
    }

    /**
     * 画课程区间视图
     *
     * @param canvas
     */
    RectF rect = new RectF();
    private Path mPath = new Path();
    RectF rectF = new RectF();
    float left = 0;
    float top = 0;
    float right = mWidth;
    float bottom = mRealViewHeight;
    int type;

    private void drawRangeView(Canvas canvas) {


        if (mDatas == null || mDatas.size() == 0) {
            Log.e("drawRangeView", "mDatas null");
            return;
        }


        left = 0;
        top = 0;
        right = mWidth;
        bottom = mRealViewHeight;
        rectF.left = left;
        rectF.right = mWidth;
        rectF.top = 0;
        rectF.bottom = mRealViewHeight;
        mPath.addRoundRect(rectF, (rectF.right - rectF.left) / 2, (rectF.right - rectF.left) / 2, Path.Direction.CCW);
        if (Build.VERSION.SDK_INT >= 28) {
            canvas.clipPath(mPath);
        } else {
            canvas.clipPath(mPath);
            // canvas.clipPath(mPath, Region.Op.XOR);
        }
        for (int i = 0; i < mDatas.size(); i++) {
            courseDetail = mDatas.get(i);
            float topV = courseDetail.getEnd() * mHeight;
//            bottom = mRealViewHeight - courseDetail.getBegin() * mHeight / mTotalSecond;
//            top = mRealViewHeight - topV  >=mTotalSecond ? mTotalSecond+=mTotalSecond : mTotalSecond;


            bottom = mRealViewHeight - courseDetail.getBegin() * mHeight / mTotalSecond;
            top = mRealViewHeight - courseDetail.getEnd() * mHeight / mTotalSecond;

            rect.left = left;
            rect.top = top;
            rect.right = right;
            rect.bottom = bottom;
            type = courseDetail.getTargetRange();
            mPaint.setColor(colors.get(1));
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
            // bottom=top ;
           // Log.e("drawRangeView", "type=" + mDatas.get(i) + "rec=" + rect);
            canvas.drawRect(rect, mPaint);


        }
    }


    /**
     * 画不断移动的指针
     *
     * @param canvas
     */

    private int mTotalSecond =  60;
    private List<CourseDetail> mDatas = new ArrayList<>();
    private CourseDetail courseDetail;

    public void setToalSecend(int secend) {

    }

    public void setValue(List<CourseDetail> mDatas, boolean isCourse) {

        if (isCourse) {
            this.mTotalSecond = UserContans.couserTime / Constant.REFRESH_RATE;
        } else {

            if (mDatas.size()>0 && mDatas.get(mDatas.size() - 1).getEnd() > mTotalSecond) {
               // mTotalSecond += mTotalSecond;
                int end = mDatas.get(mDatas.size()-1).getEnd();
                int total = end / 60;
                int decimal = end % 60;
//
//                if(decimal>0){
//                    total = total+1;
//                }
                mTotalSecond =total * 60 *2;
                Timber.e("---------setValue="+mTotalSecond+" "+end);
            } else {
               // this.mTotalSecond = 30 * 60;
            }
        }

        //this.mTotalSecond=10;
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);


        Collections.sort(mDatas, new Comparator<CourseDetail>() {
            @Override
            public int compare(CourseDetail o1, CourseDetail o2) {
                return o1.getBegin();
            }
        });

        //Log.e("list", "setValue mDatas=" + mDatas.size() + "this.mTotalSecond=" + this.mTotalSecond + ",mDatas.get(mDatas.size() - 1).getEnd()=" + mDatas.get(mDatas.size() - 1).getEnd());
        invalidate();
    }

}
