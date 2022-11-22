package com.jkcq.hrwtv.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.util.DisplayUtils;
import com.jkcq.hrwtv.util.IniColorUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/*
 *
 *
 * @author mhj
 * Create at 2018/4/20 12:09
 */
public class HeartRateStagehorizontalView extends View {

    private LinkedList<String> heartRateStrength = new LinkedList();

    //View总宽度
    private int VIEW_WIDTH = 0;
    //View总高度
    private int VIEW_HEIGTH = 0;
    //单元格宽度
    private int WIDTH = 0;
    //单元格高度
    private int HEIGTH = 0;

    //上下边距
    private int PADDING = 4;

    //色值
    HashMap<String, Integer> map;
    //左右边距
    private int MARGIN = 0;
    //最大显示数量
    private int MAXNUMBER = 10;
    //除以max的时候取整会损失一部分值
    private int mOffset = 0;
    //当前需显示的数量
    private int LINKEDSIZE = 0;


    private int RADIO = 0;
    private Bitmap background;

    private Paint paint;
    //整形
    private Rect scrR, dstR;
    //浮点型
    private RectF lastOval, ovalR, oval, rect;
    private Path path, lastPath;

    private int bgColor = 0;


    public HeartRateStagehorizontalView(Context context) {
        super(context, null);
    }

    public HeartRateStagehorizontalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        map = IniColorUtil.initParams(context);
        PADDING = DisplayUtils.dip2px(context, 2);
        bgColor = context.getResources().getColor(com.jkcq.hrwtv.base.R.color.white_10);
        paint = new Paint();
        path = new Path();
        lastOval = new RectF();
        ovalR = new RectF();
        oval = new RectF();
        rect = new RectF();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
       /* VIEW_WIDTH = getWidth();
        VIEW_HEIGTH = getHeight();*/
        VIEW_HEIGTH = getWidth();
        VIEW_WIDTH = getHeight();
        //初始化单元格宽高
        WIDTH = (VIEW_WIDTH - MARGIN * 2) / MAXNUMBER;
        RADIO = VIEW_HEIGTH / 2;
        mOffset = (VIEW_WIDTH - MARGIN * 2) % MAXNUMBER;
//        VIEW_WIDTH = WIDTH * MAXNUMBER + (MARGIN * 2);
        HEIGTH = VIEW_HEIGTH;
        //background = BitmapUtils.zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.seekbar_bg), VIEW_WIDTH, VIEW_HEIGTH);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int mDrawTimes;
    int mapcolor;

    @Override
    protected void onDraw(Canvas canvas) {
        // 基本的而绘制功能，比如绘制背景颜色、背景图片等
        //super.onDraw(canvas);
        paint.setAntiAlias(true);


        //#1affffff
       /* oval.left = 0;
        oval.top = 0;
        oval.right = VIEW_HEIGTH;
        oval.bottom = VIEW_WIDTH;
        paint.setColor(bgColor);
        canvas.drawRoundRect(oval, RADIO, RADIO, paint);*/

        //开始绘制
//        if (background == null) {
//            background = BitmapUtils.zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.seekbar_bg), VIEW_WIDTH, VIEW_HEIGTH);
//        }
//        scrR = new Rect(0, 0, background.getWidth(), background.getHeight());
//        dstR = new Rect(MARGIN, 0, VIEW_WIDTH - MARGIN, HEIGTH);
//        canvas.drawBitmap(background, scrR, dstR, paint);

        if (heartRateStrength.size() == 0) {
            return;
        }
        Log.e("rect---------", "LINKEDSIZE==" + LINKEDSIZE);

        for (int i = 0; i < LINKEDSIZE; i++) {

            if (i == 0) {
                mapcolor = map.get(heartRateStrength.peekFirst());
                paint.setColor(mapcolor);
                oval.left = 0;
                oval.top = VIEW_WIDTH - WIDTH;
                oval.right = VIEW_HEIGTH;
                oval.bottom = VIEW_WIDTH;
                //画圆角的正方形
                canvas.drawRoundRect(oval, RADIO, RADIO, paint);
                ovalR.left = 0;
                ovalR.top = VIEW_WIDTH - WIDTH;
                ovalR.right = VIEW_HEIGTH;
                ovalR.bottom = VIEW_WIDTH - WIDTH / 2;
                canvas.drawRect(ovalR, paint);
                Log.e("rect---------", "i==" + i + "left=" + oval.left + ",top=" + oval.top + ",right=" + oval.right + ",bottom=" + oval.bottom);


            } else {
                mapcolor = map.get(heartRateStrength.get(i));
                paint.setColor(mapcolor);
                if (i == MAXNUMBER - 1) {
                    //最上面的的数据
                    lastOval.left = 0;
                    lastOval.top = VIEW_WIDTH - (WIDTH + WIDTH * i);
                    lastOval.right = VIEW_HEIGTH;
                    lastOval.bottom = VIEW_WIDTH - WIDTH * i;
                    canvas.drawRoundRect(lastOval, lastOval.right / 2, lastOval.right / 2, paint);
                    oval = new RectF();
                    oval.left = 0;
                    oval.top = VIEW_WIDTH - (WIDTH / 2 + WIDTH * i);
                    oval.right = VIEW_HEIGTH;
                    oval.bottom = VIEW_WIDTH - WIDTH * i;
                    canvas.drawRect(oval, paint);

                } else {
                    //中间的数据
                    rect.left = 0;
                    rect.top = VIEW_WIDTH - (WIDTH + WIDTH * i);
                    rect.right = VIEW_WIDTH;
                    rect.bottom = VIEW_WIDTH - WIDTH * i;
                    canvas.drawRect(rect, paint);
                }

            }
        }

    }


    public void addAllHeartRateStrength(ArrayList<Integer> list, int maxNumber) {

        MAXNUMBER = maxNumber;
        heartRateStrength.clear();
        for (int i = 0; i < list.size(); i++) {

            int hearStrength = list.get(i);
            //维护一个最大的linkedlist,超过移除第一个

            int size = heartRateStrength.size();
            if (heartRateStrength.size() >= MAXNUMBER) {
                //移除第一个
                heartRateStrength.remove(0);
            }

            //新添加的放最后
            if (hearStrength < 50) {
                heartRateStrength.addLast("LTGRAY");
            } else if (hearStrength >= 50 && hearStrength <= 59) {
                heartRateStrength.addLast("GRAY");
            } else if (hearStrength >= 60 && hearStrength <= 69) {
                heartRateStrength.addLast("BLUE");
            } else if (hearStrength >= 70 && hearStrength <= 79) {
                heartRateStrength.addLast("GREEN");
            } else if (hearStrength >= 80 && hearStrength <= 89) {
                heartRateStrength.addLast("YELLOW");
            } else if (hearStrength >= 90) {
                heartRateStrength.addLast("RED");
            }
        }
        LINKEDSIZE = heartRateStrength.size();

        //判断当前区间的颜色是否没有变化，没有变化就不需要绘制
        boolean isAllSame = true;
        if (LINKEDSIZE != MAXNUMBER) {
            isAllSame = false;
        } else {
            for (int i = 0; i < LINKEDSIZE - 1; i++) {
                if (!heartRateStrength.get(i).equals(heartRateStrength.get(i + 1))) {
                    isAllSame = false;
                    break;
                }
            }
        }

        //如果不是所有都一样就刷新，即图形有变化
        if (!isAllSame) {
//            postInvalidate();//只是重新画了view 的没有重新画recybel
        }

    }


    //不断移动的View
    public void updateHeartRateStrength(int hearStrength) {
        //维护一个最大的linkedlist,超过移除第一个
        if (heartRateStrength.size() >= MAXNUMBER) {
            //移除第一个
            heartRateStrength.remove(0);
        }

        //新添加的放最后
        if (hearStrength < 50) {
            heartRateStrength.addLast("LTGRAY");
        } else if (hearStrength >= 50 && hearStrength <= 59) {
            heartRateStrength.addLast("GRAY");
        } else if (hearStrength >= 60 && hearStrength <= 69) {
            heartRateStrength.addLast("BLUE");
        } else if (hearStrength >= 70 && hearStrength <= 79) {
            heartRateStrength.addLast("GREEN");
        } else if (hearStrength >= 80 && hearStrength <= 89) {
            heartRateStrength.addLast("YELLOW");
        } else if (hearStrength >= 90) {
            heartRateStrength.addLast("RED");
        }
        LINKEDSIZE = heartRateStrength.size();
        postInvalidate();//只是重新画了view 的没有重新画recybel
    }

    public void addHeartRateStrength(int hearStrength) {

        //新添加的放最后
        if (hearStrength < 50) {
            heartRateStrength.addLast("LTGRAY");
        } else if (hearStrength >= 50 && hearStrength <= 59) {
            heartRateStrength.addLast("GRAY");
        } else if (hearStrength >= 60 && hearStrength <= 69) {
            heartRateStrength.addLast("BLUE");
        } else if (hearStrength >= 70 && hearStrength <= 79) {
            heartRateStrength.addLast("GREEN");
        } else if (hearStrength >= 80 && hearStrength <= 89) {
            heartRateStrength.addLast("YELLOW");
        } else if (hearStrength >= 90) {
            heartRateStrength.addLast("RED");
        }

        LINKEDSIZE = heartRateStrength.size();
        MAXNUMBER = LINKEDSIZE;
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
