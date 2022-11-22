package com.jkcq.hrwtv.heartrate.bean;


/*
 *
 *
 * @author mhj
 * Create at 2018/7/10 9:19
 *
 */
public class AcceptorDataBean {

     private String devicesSN;
     private int devicesHeartRate;
    private int devicesEnergy;
     private long createTime;
     private boolean isTask;
     private boolean isDrop;



    public String getDevicesSN() {
        return devicesSN;
    }

    public void setDevicesSN(String devicesSN) {
        this.devicesSN = devicesSN;
    }

    public int getDevicesHeartRate() {
        return devicesHeartRate;
    }

    public void setDevicesHeartRate(int devicesHeartRate) {
        this.devicesHeartRate = devicesHeartRate;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isTask() {
        return isTask;
    }

    public void setTask(boolean task) {
        isTask = task;
    }

    public boolean isDrop() {
        return isDrop;
    }

    public void setDrop(boolean drop) {
        isDrop = drop;
    }


    public int getDevicesEnergy() {
        return devicesEnergy;
    }

    public void setDevicesEnergy(int devicesEnergy) {
        this.devicesEnergy = devicesEnergy;
    }
}
