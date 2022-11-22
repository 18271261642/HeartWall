package com.jkcq.hrwtv.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jkcq.hrwtv.AllocationApi;
import com.jkcq.hrwtv.heartrate.bean.TaskInfo;
import com.jkcq.hrwtv.heartrate.bean.TaskUnits;
import com.jkcq.hrwtv.heartrate.bean.UpdateTaskBean;
import com.jkcq.hrwtv.util.IniColorUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * 
 * 
 * @author mhj
 * Create at 2018/4/21 11:55 
 */
public class HeartRateTaskView extends View implements Runnable {


    private TaskInfo taskInfo = null;

    private int TOTAL_WIDTH = 0;
    private int TOTAL_HEIGHT = 0;

    //单元格宽度，一分钟时间显示的宽度
    private int UNITS_WIDTH = 0;
    private int UNITS_HEIGTH = 0;

    //一秒钟遮罩走的距离
    private float UNITS_WIDTH_SPEED = 0;
    //当前时间
    private int CURRENT_TIME = 0;
    private int CURRENT_WIDTH = 0;

    //单元格，5秒钟的显示宽高
    private int SECOND_UNITS_WIDTH = 0;
    private int SECOND_UNITS_HEIGHT = 0;
    private int SECOND_MARGIN = 0;
    private int SECOND = 5;


    private ArrayList<TaskUnits> taskUnits;
    private int totalTime = 0;

    private Paint paint = null;
    private Handler handler = new Handler();
    private static boolean isPause = false;
    private HashMap<Integer, IntervalBean> interValMap;

    //    private onTaskCompleteListener completeListener;
//    private onTaskChangeListener listener;
    private UpdateTaskBean updateTaskBean;

    private ArrayList<onTaskChangeListener> listenersList = new ArrayList<>();
    private ArrayList<onTaskCompleteListener> listenerCompletes = new ArrayList<>();

    private Context mContext;

    public HeartRateTaskView(Context context) {
        super(context);
        mContext = context;
    }

    public HeartRateTaskView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        TOTAL_WIDTH = getWidth();
        TOTAL_HEIGHT = getHeight();

        UNITS_WIDTH = TOTAL_WIDTH / totalTime;
        UNITS_HEIGTH = TOTAL_HEIGHT;

        UNITS_WIDTH_SPEED = (UNITS_WIDTH / 60.0f) * 1.0f;

        SECOND_UNITS_WIDTH = 20;
        SECOND_UNITS_HEIGHT = UNITS_HEIGTH;
        SECOND_MARGIN = 2;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (taskInfo == null) {
            return;
        }

        paint = new Paint();

        for (int i = 0; i < taskUnits.size(); i++) {

            TaskUnits units = taskUnits.get(i);
            int duration = units.getDuration();

            paint.setColor(IniColorUtil.getConvert2Color(units.getSequence(), mContext));

            RectF rect = new RectF();
            rect.left = CURRENT_WIDTH;
            rect.top = TOTAL_HEIGHT - UNITS_HEIGTH;
            rect.right = CURRENT_WIDTH + (duration * UNITS_WIDTH);
            rect.bottom = TOTAL_HEIGHT;
            canvas.drawRect(rect, paint);
            CURRENT_WIDTH = CURRENT_WIDTH + (duration * UNITS_WIDTH);
        }
        //保存图层
        canvas.saveLayerAlpha(0, TOTAL_HEIGHT - UNITS_HEIGTH, CURRENT_WIDTH, 200, 0x88, LAYER_FLAGS);

        RectF shadeF = new RectF();
        shadeF.left = 0;
        shadeF.top = TOTAL_HEIGHT - UNITS_HEIGTH;
        shadeF.right = CURRENT_TIME * UNITS_WIDTH_SPEED;
        shadeF.bottom = TOTAL_HEIGHT;
        paint.setColor(Color.BLACK);

        canvas.drawRect(shadeF, paint);

        //保存图层
//        canvas.saveLayerAlpha(0, TOTAL_HEIGHT-UNITS_HEIGTH, TOTAL_WIDTH, 200, 0x88, LAYER_FLAGS);

//        for (int i = 0; i < taskUnits.size(); i++) {
//
//            TaskUnits units = taskUnits.get(i);
//            int duration = units.getDuration();
//            paint.setColor(getConvert2Color(units.getRange()));
//
//            int number = duration*60/SECOND;
//
//            for (int j = 0; j < number; j++) {
//
//                RectF secondF = new RectF();
//                secondF.left = SECOND_UNITS_WIDTH*j;
//                secondF.top = TOTAL_HEIGHT -UNITS_HEIGTH -SECOND_UNITS_HEIGHT * units.getRange();
//                secondF.right = SECOND_UNITS_WIDTH*j +SECOND_UNITS_WIDTH;
//                //去掉底部高度
//                secondF.bottom = TOTAL_HEIGHT -SECOND_UNITS_HEIGHT * units.getRange();
//
//                canvas.drawRect(secondF,paint);
//            }
//
//        }


        canvas.restore();
        //课程绘制完之后设置成初始化状态
        CURRENT_WIDTH = 0;


    }

//    private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG |
//            Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
//            Canvas.CLIP_TO_LAYER_SAVE_FLAG|Canvas.ALL_SAVE_FLAG;

    private static final int LAYER_FLAGS = Canvas.ALL_SAVE_FLAG;

    //设置数据源
    public void setTaskInfo(TaskInfo info) {
        this.taskInfo = info;
        //用于图形数据更新的
        updateTaskBean = new UpdateTaskBean();

        taskUnits = (ArrayList) taskInfo.getUnits();
        convert2Interval();
        //单个宽度
        totalTime = taskInfo.getTotalTime();
        postInvalidate();
        updateTaskBean.setTotalDuration(totalTime);

        handler.post(this);

    }


    public TaskInfo getTask() {
        return taskInfo;
    }


    //获取区间颜色
    public int getIntervalColor(int interval) {
        if (interval < 0) {
            return -1;
        }
        if (interval <= (taskUnits.size() - 1)) {
            TaskUnits units = taskUnits.get(interval);
            return IniColorUtil.getConvert2Color(units.getSequence(), mContext);
        } else {
            return -1;
        }
    }


    //下一区间
    public void setNext() {

        int currentInterVal = getCurrentInterval();
        //已经是最后一个区间 不能快进
        if (currentInterVal == (taskUnits.size() - 1)) {
            return;
        } else {
            //下一个区间
            currentInterVal++;
            IntervalBean interVal = interValMap.get(currentInterVal);
            if (interVal == null) {
                return;
            }
            CURRENT_TIME = interVal.getStartTime() * 60;
        }
        if (currentInterVal != -1) {
            updateTaskBean.setIndex(currentInterVal);
            updateTaskBean.setDuration(taskUnits.get(currentInterVal).getDuration());
        }
        for (int i = 0; i < listenersList.size(); i++) {

            listenersList.get(i).onTaskChange(updateTaskBean);
        }
        postInvalidate();

    }


    //上一区间
    public void setForward() {

        int currentInterVal = getCurrentInterval();
        //已经是第一个区间 不能快进
        if (currentInterVal == 0) {
            return;
        } else {
            //上一个区间
            currentInterVal--;
            IntervalBean interVal = interValMap.get(currentInterVal);
            if (interVal == null) {
                return;
            }
            CURRENT_TIME = interVal.getStartTime() * 60;
        }
        if (currentInterVal != -1) {
            updateTaskBean.setIndex(currentInterVal);
            updateTaskBean.setDuration(taskUnits.get(currentInterVal).getDuration());
        }
        for (int i = 0; i < listenersList.size(); i++) {
            listenersList.get(i).onTaskChange(updateTaskBean);
        }
        postInvalidate();
    }


    public void setPauseOrStart() {
        if (isPause) {
            isPause = false;
        } else {
            isPause = true;
        }
        updateTaskBean.setPause(isPause);
        //发送暂停开始广播
        Intent intent = new Intent();
        intent.setAction(AllocationApi.TASK_CHANGE_BROADCAST);
        intent.putExtra(AllocationApi.EXTRA_TASK_CHANGE,isPause);
        mContext.sendBroadcast(intent);

        for (int i = 0; i < listenersList.size(); i++) {
            listenersList.get(i).onTaskChange(updateTaskBean);
        }
    }

    public void setStop() {
        CURRENT_TIME = totalTime * 60;
        isPause = true;
        handler.removeCallbacks(this);
        updateTaskBean.setPause(isPause);
        for (int i = 0; i < listenersList.size(); i++) {
            listenersList.get(i).onTaskChange(updateTaskBean);
        }
    }

    public void initParam() {
        CURRENT_TIME = 0;
        totalTime = 0;
        isPause = false;

    }


    //查找当前区间的下标
    public int getCurrentInterval() {
        Iterator iter = interValMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Integer key = (Integer) entry.getKey();
            IntervalBean val = (IntervalBean) entry.getValue();

            //分钟换算成秒，current_time是秒，课程区间是分钟
            if (CURRENT_TIME > (val.getStartTime() * 60) && CURRENT_TIME <= (val.getEndTime() * 60)) {
                //找到对应节点，根据该值获取Task信息
                return key;
            }
        }
        return -1;
    }


    //把课程分解成区间, Key对应 taskUnits下标；
    public void convert2Interval() {
        int endTime = 0;
        interValMap = new HashMap<>();
        for (int i = 0; i < taskUnits.size(); i++) {

            IntervalBean interval = new IntervalBean();

            TaskUnits task = taskUnits.get(i);
            endTime = task.getDuration();

            for (int j = 0; j < i; j++) {
                TaskUnits units = taskUnits.get(j);
                int duration = units.getDuration();
                endTime = endTime + duration;
            }

            interval.setStartTime(endTime - task.getDuration());
            interval.setEndTime(endTime);
            interValMap.put(i, interval);
        }
    }


    //设置数据改变监听事件
    public void setTaskChangeListener(onTaskChangeListener listener) {
//        this.listener =  listener;
        listenersList.add(listener);
    }

    public void setTaskCompleteListener(onTaskCompleteListener listener) {
//        this.completeListener = listener;
        listenerCompletes.add(listener);
    }

    public void clearTaskListener() {
        listenersList.clear();
        listenerCompletes.clear();
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
            result = 200;
        }

        return result;
    }

    @Override
    public void run() {

        //当前时间小于总时间
        if (!isPause) {
            if (CURRENT_TIME < totalTime * 60) {
                CURRENT_TIME++;

                updateTaskBean.setCurrentTime(CURRENT_TIME);
                updateTaskBean.setPause(isPause);
                int index = getCurrentInterval();
                if (index != -1) {
                    updateTaskBean.setIndex(index);
                    updateTaskBean.setDuration(taskUnits.get(index).getDuration());
                    updateTaskBean.setIntervalEndTime(interValMap.get(index).getEndTime());
                    updateTaskBean.setIntervalStartTime(interValMap.get(index).getStartTime());
                    updateTaskBean.setColor(getIntervalColor(index));
                    updateTaskBean.setNextColor(getIntervalColor(index + 1));
                    updateTaskBean.setRang(taskUnits.get(index).getSequence());

                }
                for (int i = 0; i < listenersList.size(); i++) {
                    listenersList.get(i).onTaskChange(updateTaskBean);
                }
                postInvalidate();
            } else if (CURRENT_TIME == totalTime * 60) {
                for (int i = 0; i < listenerCompletes.size(); i++) {
                    listenerCompletes.get(i).onTaskComplete();
                }
            }
        }
        handler.postDelayed(this, 1000);
    }
}
