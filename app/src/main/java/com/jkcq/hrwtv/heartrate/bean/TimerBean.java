package com.jkcq.hrwtv.heartrate.bean;

/**
 * Created by peng on 2018/5/7.
 */

public class TimerBean {


    private String time;
    private int min;
    private int hour;
    private int mills;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMills() {
        return mills;
    }

    public void setMills(int mills) {
        this.mills = mills;
    }
}
