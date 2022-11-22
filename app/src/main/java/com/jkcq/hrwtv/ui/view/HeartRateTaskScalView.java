package com.jkcq.hrwtv.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jkcq.hrwtv.heartrate.bean.TaskUnits;
import com.jkcq.hrwtv.heartrate.bean.UpdateTaskBean;
import com.jkcq.hrwtv.util.DisplayUtils;
import com.jkcq.hrwtv.util.IniColorUtil;

import java.util.ArrayList;

/*
 * 
 * 
 * @author mhj
 * Create at 2018/4/27 14:27 
 */
public class HeartRateTaskScalView extends View implements onTaskChangeListener {

    private HeartRateTaskView taskView;

    private ArrayList<TaskUnits> taskUnits;
    private int taskLength = 0;

    private int VIEW_WIDTH = 0;
    private int VIEW_HEIGHT = 0;

    private int UNITS_WIDTH = 50;
    private int UNITS_HEIGHT = 0;

    private int MARGIN = 2;

    private int SECOND = 5;
    private int raido = 0;

    private Paint paint;
    private int CURRENT_WIDTH = 0;

    private Handler handler = new Handler();

    private int SPEED = UNITS_WIDTH / SECOND;


    private Context mContext;

    public HeartRateTaskScalView(Context context) {
        super(context);
        this.mContext = context;
        raido = DisplayUtils.dip2px(context, 1);
//        taskView.setTaskChangeListener(this);
//        taskUnits = (ArrayList<TaskUnits>) taskView.getTask().getUnits();
//        taskLength = taskUnits.size();

    }

    public HeartRateTaskScalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        raido = DisplayUtils.dip2px(context, 1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        VIEW_WIDTH = getWidth();
        VIEW_HEIGHT = getHeight();

        UNITS_HEIGHT = VIEW_HEIGHT / 6;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }


    @Override
    protected void onDraw(Canvas canvas) {

        paint = new Paint();
        for (int i = 0; i < taskLength; i++) {

            TaskUnits units = taskUnits.get(i);

            int duration = units.getDuration();
            int rangNumber = duration * 60 / SECOND;
            int range = units.getSequence();

            paint.setColor(IniColorUtil.getConvert2Color(units.getSequence(), mContext));

            for (int j = 0; j < rangNumber; j++) {

                RectF rectf = new RectF();
                rectf.top = VIEW_HEIGHT - range * UNITS_HEIGHT;
                rectf.left = CURRENT_WIDTH + MARGIN + UNITS_WIDTH * j;
                rectf.right = CURRENT_WIDTH + UNITS_WIDTH * j + UNITS_WIDTH;
                rectf.bottom = VIEW_HEIGHT;
                // canvas.drawRect(rectf, paint);
                canvas.drawRoundRect(rectf, raido, raido, paint);
                canvas.drawRect(rectf.left, rectf.bottom - raido, rectf.left + raido, rectf.bottom, paint);
                canvas.drawRect(rectf.right - raido, rectf.bottom - raido, rectf.right, rectf.bottom, paint);

              /*  int notRoundedCorners = corners ^ CORNER_ALL;
                //哪个角不是圆角我再把你用矩形画出来
                if ((notRoundedCorners & CORNER_TOP_LEFT) != 0) {
                    canvas.drawRect(0, 0, cornerRadius, cornerRadius, paint);
                }
                if ((notRoundedCorners & CORNER_TOP_RIGHT) != 0) {
                    canvas.drawRect(mRect.right - cornerRadius, 0, mRect.right, cornerRadius, paint);
                }
                if ((notRoundedCorners & CORNER_BOTTOM_LEFT) != 0) {
                    canvas.drawRect(0, mRect.bottom - cornerRadius, cornerRadius, mRect.bottom, paint);
                }
                if ((notRoundedCorners & CORNER_BOTTOM_RIGHT) != 0) {
                    canvas.drawRect(mRect.right - cornerRadius, mRect.bottom - cornerRadius, mRect.right, mRect.bottom, paint);
                }*/

            }
            CURRENT_WIDTH = CURRENT_WIDTH + MARGIN + UNITS_WIDTH * (duration * 60 / SECOND);
        }

        CURRENT_WIDTH = 0;
    }


    @Override
    public void onTaskChange(UpdateTaskBean bean) {

        if (!bean.isPause()) {
//            SPEED = SPEED+ UNITS_WIDTH/SECOND;
            final int currentTime = bean.getCurrentTime();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    scrollTo(SPEED * currentTime, 0);
                    postInvalidate();
                }
            });
        }
    }


    public void setTaskView(HeartRateTaskView taskView) {
        this.taskView = taskView;
        this.taskView.setTaskChangeListener(this);
        taskUnits = (ArrayList<TaskUnits>) this.taskView.getTask().getUnits();
        taskLength = taskUnits.size();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                SPEED = SPEED+ UNITS_WIDTH/SECOND;
//                scrollTo(SPEED,0);
//                postInvalidate();
//                handler.postDelayed(this,1000);
//            }
//        });
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
//            result = DisplayUtils.getScreenHeight(mContext)*(300/1080);
            result = 260;
        }

        return result;
    }
}
