package com.jkcq.hrwtv.service;

public class ReceiveSnHrTimeBean {
    int hr;
    long time;

    public ReceiveSnHrTimeBean() {

    }

    public ReceiveSnHrTimeBean(int hr, long time) {
        this.hr = hr;
        this.time = time;
    }

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
