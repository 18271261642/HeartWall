package com.jkcq.hrwtv.heartrate.bean;

/*
 *
 *
 * @author mhj
 * Create at 2018/7/10 12:03
 */
public class AcceptorDropBean {

    private int devicesSN;
    private int mDropCounter;//次数


    public int getDevicesSN() {
        return devicesSN;
    }

    public void setDevicesSN(int devicesSN) {
        this.devicesSN = devicesSN;
    }

    public int getmDropCounter() {
        return mDropCounter;
    }

    public void setmDropCounter() {
        this.mDropCounter ++;
    }
    public void setInitDropCounter(){
        this.mDropCounter = 0;
    }
}
