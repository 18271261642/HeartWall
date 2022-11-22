package com.jkcq.hrwtv.heartrate.bean;

/*
 * 
 * 
 * @author mhj
 * Create at 2018/4/27 10:30 
 */
public class UpdateTaskBean {


    //当前区间的时长
    private int duration;
    //剩余总时长
    private int totalDuration;
    //当前区间在课程中的位置
    private int index;
    //当前区间的颜色
    private int color;
    //当前区间的起始时间
    private int intervalStartTime;
    //当前区间的结束时间
    private int intervalEndTime;
    //当前区间的心率等级
    private int rang;
    //下一区间颜色
    private int nextColor;
    //当前时间进度
    private int currentTime;
    //是否暂停了
    private boolean isPause;


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getIntervalStartTime() {
        return intervalStartTime;
    }

    public void setIntervalStartTime(int intervalStartTime) {
        this.intervalStartTime = intervalStartTime;
    }

    public int getIntervalEndTime() {
        return intervalEndTime;
    }

    public void setIntervalEndTime(int intervalEndTime) {
        this.intervalEndTime = intervalEndTime;
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public int getNextColor() {
        return nextColor;
    }

    public void setNextColor(int nextColor) {
        this.nextColor = nextColor;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }
}
