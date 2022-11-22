package com.jkcq.hrwtv.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jkcq.hrwtv.util.DisplayUtils;
import com.jkcq.hrwtv.util.IniColorUtil;

import java.util.ArrayList;
import java.util.HashMap;

/*
 *
 *
 * @author mhj
 * Create at 2018/4/20 12:09
 */
public class HeartRateResultView extends View {

//    private LinkedList heartRateStrength = new LinkedList();

    private ArrayList heartRateStrength = null;

    //View总宽度
    private int VIEW_WIDTH = 0;
    //View总高度
    private int VIEW_HEIGTH = 0;
    //单元格宽度
    private int WIDTH = 0;
    //单元格高度
    private int HEIGTH = 0;

    //上下边距
    private int PADDING = 2;

    //色值
    HashMap<String, Integer> map;
    //左右边距
    private int MARGIN = 0;
    //当前需显示的数量
    private int LINKEDSIZE = 1;


    private int RADIO = 0;


//    private Bitmap background;


    public HeartRateResultView(Context context) {
        super(context, null);
        heartRateStrength = new ArrayList();
    }

    public HeartRateResultView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        map = IniColorUtil.initParams(context);
        PADDING = DisplayUtils.dip2px(context, 1);
        RADIO = DisplayUtils.dip2px(context, 10);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        VIEW_WIDTH = getWidth();
        VIEW_HEIGTH = getHeight();

        //初始化单元格宽高
        WIDTH = (VIEW_WIDTH - MARGIN * 2) / LINKEDSIZE;

        HEIGTH = VIEW_HEIGTH;
//        background = BitmapUtils.zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.seekbar_bg), VIEW_WIDTH, VIEW_HEIGTH);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // 基本的而绘制功能，比如绘制背景颜色、背景图片等
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //开始绘制

//        Rect scrR = new Rect(0, 0, background.getWidth(), background.getHeight());
//        Rect dstR = new Rect(MARGIN, 0, VIEW_WIDTH - MARGIN, HEIGTH);
//        canvas.drawBitmap(background, scrR, dstR, paint);

        if (heartRateStrength == null||heartRateStrength.size() == 0) {
            return;
        }

        for (int i = 0; i < LINKEDSIZE; i++) {
            int mapcolor = map.get(heartRateStrength.get(i));
            paint.setColor(mapcolor);

//            if (i == 0) {
//
//                int mapcolor = map.get(heartRateStrength.peekFirst());
//                paint.setColor(mapcolor);
//                Path path = new Path();
//
////                path.moveTo(MARGIN + WIDTH/2,0);
////                path.quadTo(MARGIN-WIDTH/2,HEIGTH/2,MARGIN + WIDTH/2,HEIGTH);
//
//                RectF oval = new RectF();
//                oval.left = 0;
//                oval.top = PADDING;
//                oval.right = WIDTH;
//                oval.bottom = HEIGTH - PADDING;
//                //path.addArc(oval,90,180);
//                //画圆角的正方形
//                path.addRoundRect(oval, RADIO, RADIO, Path.Direction.CCW);
//
//                RectF ovalR = new RectF();
//                ovalR.left = MARGIN + WIDTH / 2;
//                ovalR.top = PADDING;
//                ovalR.right = MARGIN + WIDTH;
//                ovalR.bottom = HEIGTH - PADDING;
//                path.addRect(ovalR, Path.Direction.CCW);
//                canvas.drawPath(path, paint);
//
//            } else {
//
//                int color = map.get(heartRateStrength.get(i));
//                paint.setColor(color);
//
//                //到最右边了
//                if ((WIDTH * i + WIDTH) >= (VIEW_WIDTH - MARGIN * 2)) {
////                if(i == (LINKEDSIZE-1)){
//                    //后面箭头
//                    Path lastPath = new Path();
////                    lastPath.moveTo(MARGIN+WIDTH*i+WIDTH/2,0);
////                    lastPath.quadTo(MARGIN+WIDTH*i+WIDTH,HEIGTH/2,MARGIN+WIDTH*i+WIDTH-WIDTH/2,HEIGTH);
//
//                    RectF lastOval = new RectF();
//                    lastOval.left = MARGIN + WIDTH * i;
//                    lastOval.top = PADDING;
//                    lastOval.right = MARGIN + WIDTH * i + WIDTH - PADDING;
//                    lastOval.bottom = HEIGTH - PADDING;
//                    lastPath.addRoundRect(lastOval, RADIO, RADIO, Path.Direction.CCW);
//                    //lastPath.addArc(lastOval,270,180);
//
//                    RectF oval = new RectF();                     //RectF对象
//                    oval.left = MARGIN + WIDTH * i;                         //左边
//                    oval.top = PADDING;                                   //上边
//                    oval.right = MARGIN + WIDTH * i + WIDTH / 2 - PADDING;         //右边
//                    oval.bottom = HEIGTH - PADDING;
//                    lastPath.addRect(oval, Path.Direction.CCW);
//                    canvas.drawPath(lastPath, paint);
//                } else {
            RectF rect = new RectF();
            rect.left = MARGIN + WIDTH * i;
            rect.top = PADDING;
            rect.right = MARGIN + WIDTH + WIDTH * i;
            rect.bottom = HEIGTH - PADDING;
            canvas.drawRect(rect, paint);
//                }
//
//            }
        }
    }

    public void updateHeartRateStrength(ArrayList<Integer> heartLists) {

        if (heartRateStrength != null) {
            heartRateStrength.clear();
        } else {
            heartRateStrength = new ArrayList();
        }

        for (int i = 0; i < heartLists.size(); i++) {

            int hearStrength = heartLists.get(i);
//            int hearStrength = (int) (HeartRateConvertUtils.hearRate2Percent(rate, maxHr));
            //新添加的放最后
            if (hearStrength < 60) {
                heartRateStrength.add("GRAY");
            }
//            else if (hearStrength >= 50 && hearStrength <= 59) {
//                heartRateStrength.add("GRAY");
//            }
            else if (hearStrength >= 60 && hearStrength <70) {
                heartRateStrength.add("BLUE");
            } else if (hearStrength >= 70 && hearStrength <80) {
                heartRateStrength.add("GREEN");
            } else if (hearStrength >= 80 && hearStrength <90) {
                heartRateStrength.add("YELLOW");
            } else if (hearStrength >= 90) {
                heartRateStrength.add("RED");
            }

        }

        LINKEDSIZE = heartRateStrength.size();
        if(LINKEDSIZE == 0){
            LINKEDSIZE = 1;
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
