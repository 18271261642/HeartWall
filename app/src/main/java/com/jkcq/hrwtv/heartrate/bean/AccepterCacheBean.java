package com.jkcq.hrwtv.heartrate.bean;

import java.util.ArrayList;

/*
 *
 *
 * @author mhj
 * Create at 2018/7/10 10:00
 */public class AccepterCacheBean {

     private boolean isTask;
     private int devicesSN;
     private ArrayList<AcceptorDataBean> dataList = new ArrayList<>();
     private ArrayList<Integer>  heartForStrengthList = new ArrayList<>();

    public int getDevicesSN() {
        return devicesSN;
    }

    public void setDevicesSN(int devicesSN) {
        this.devicesSN = devicesSN;
    }

    public ArrayList<AcceptorDataBean> getDataList() {
        return dataList;
    }

    public ArrayList<Integer> getHeartForStrengthList() {
        return heartForStrengthList;
    }

    public void addHeartForStrength(int heartRate){
        heartForStrengthList.add(heartRate);
    }

    public boolean isTask() {
        return isTask;
    }

    public void setTask(boolean task) {
        isTask = task;
    }
}
