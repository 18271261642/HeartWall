package com.jkcq.hrwtv.heartrate.bean;

import com.jkcq.hrwtv.http.bean.CourseUserInfo;

import java.util.ArrayList;

/*
 *
 *
 * @author mhj
 * Create at 2018/7/13 9:59
 */public class SecondHeartRateBean {

    private CourseUserInfo courseUserInfo;
    private UserBean userInfo;
    private String devicesSN;
    private boolean isTask;
    private long time;
    private int heart;
    private int energy;
    private boolean isOnline;

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public CourseUserInfo getCourseUserInfo() {
        return courseUserInfo;
    }

    public void setCourseUserInfo(CourseUserInfo courseUserInfo) {
        this.courseUserInfo = courseUserInfo;
    }

    public void setHeartList(ArrayList<Integer> heartList) {
        this.heartList = heartList;
    }

    private ArrayList<Integer> heartList = new ArrayList<>();

    public String getDevicesSN() {
        return devicesSN;
    }

    public void setDevicesSN(String devicesSN) {
        this.devicesSN = devicesSN;
    }

    public ArrayList<Integer> getHeartList() {
        return heartList;
    }


    public boolean isTask() {
        return isTask;
    }

    public void setTask(boolean task) {
        isTask = task;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public UserBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserBean userInfo) {
        this.userInfo = userInfo;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }


    @Override
    public String toString() {
        return "SecondHeartRateBean{" +
                "courseUserInfo=" + courseUserInfo +
                ", userInfo=" + userInfo +
                ", devicesSN='" + devicesSN + '\'' +
                ", isTask=" + isTask +
                ", time=" + time +
                ", heart=" + heart +
                ", energy=" + energy +
                ", isOnline=" + isOnline +
                ", heartList=" + heartList +
                '}';
    }
}
