package com.jkcq.hrwtv.heartrate.bean;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.util.Arith;
import com.jkcq.hrwtv.util.CacheDataUtil;

import java.util.ArrayList;

/**
 * Created by peng on 2018/4/23.
 */

public class DeviceBean implements Comparable {

    private User user;
    private String certerShowType;//item中间显示的数据
    private String courseCerterShowType;//课程item中间显示的数据
    private String sortType;//主页面排序的类型
    private String courseSortType;//课程排序的类型
    private String rage;
    private int maxHr; //最大心率值
    private double sumCal;//连接上计算的总卡路里
    private ArrayList<Integer> hrList;//十秒钟画一次
    private ArrayList<Double> calDetailByMs;//记录每秒钟产生的卡路里
    private double sumPoint;//自由运动不记录具体的数据
    private ArrayList<Double> pointDetailByMs;//一共的点数课程记录的点数
    private ArrayList<Integer> hrDetailByMs;//一共的点数课程记录的点数
    private long joinTime;//设备连接的时间

    private int maximalExercise;
    private int anaerobicExercise;
    private int aerobicExercise;
    private int fatMovement;
    private int warmup;
    private int taskPoint;
    private int taskCal;
    private int averageHeartRate;
    private int averageStrength;


    //总的心率个数
    public int totalHeartCount = 0;
    //匹配的个数
    public int matchheartCount = 0;
    public double matchHeartPrecent = 0;


    private double cal;//课程显示的卡路里值
    private String hr;//心率值
    private double point;//点数
    private String precent;//显示的百分比
    private String light;//发电量


    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public int getMaxHr() {
        return maxHr;
    }

    public void setMaxHr(int maxHr) {
        this.maxHr = maxHr;
    }

    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    private ArrayList<Integer> heartRates = null;


    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }


    public double getCal() {
        return cal;
    }

    public void setCal(double cal) {
        this.cal = cal;
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }


    public String getPrecent() {
        return precent;
    }

    public void setPrecent(String precent) {
        this.precent = precent;
    }

    private boolean isOver;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getCerterShowType() {
        return certerShowType;
    }

    public void setCerterShowType(String certerShowType) {
        this.certerShowType = certerShowType;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getRage() {
        return rage;
    }

    public void setRage(String rage) {
        this.rage = rage;
    }


    public double getSumCal() {
        return sumCal;
    }

    public void setSumCal(double sumCal) {
        this.sumCal = sumCal;
    }

    public ArrayList<Double> getCalDetailByMs() {
        return calDetailByMs;
    }

    public void setCalDetailByMs(ArrayList<Double> calDetailByMs) {
        this.calDetailByMs = calDetailByMs;
    }

    public double getSumPoint() {
        return sumPoint;
    }

    public void setSumPoint(double sumPoint) {
        this.sumPoint = sumPoint;
    }

    public ArrayList<Double> getPointDetailByMs() {
        return pointDetailByMs;
    }

    public void setPointDetailByMs(ArrayList<Double> pointDetailByMs) {
        this.pointDetailByMs = pointDetailByMs;
    }

    public ArrayList<Integer> getHrDetailByMs() {
        return hrDetailByMs;
    }

    public void setHrDetailByMs(ArrayList<Integer> hrDetailByMs) {
        this.hrDetailByMs = hrDetailByMs;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    @Override
    public boolean equals(Object obj) {
        //两个的personID相等，但是在列表中没有这个item
        DeviceBean bean = (DeviceBean) obj;
        if (bean == null) {
            return false;
        }
        User user = bean.getUser();
        if (user == null) {
            return false;
        }
        if (TextUtils.isEmpty(user.getUserId()) || TextUtils.isEmpty(this.getUser().getUserId())) {
            if (TextUtils.isEmpty(user.getSn()) || TextUtils.isEmpty(this.getUser().getSn())) {
                return false;
            }
            if (user.getSn().equals(this.getUser().getSn())) {
                return true;
            } else {
                return false;
            }
        }
        if (user.getUserId().equals(this.getUser().getUserId())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(@NonNull Object o) {
        DeviceBean deviceBean = (DeviceBean) o;
        String type;
        /**
         * 对主界面的数据还是对课程的数据进行排序
         */
        if (CacheDataUtil.getShowFragment().equals(Constant.TASK_FRAGMENT)) {
            type = deviceBean.getCourseSortType();
        } else {
            type = deviceBean.getSortType();
        }
        int count = 0;
        switch (type) {
            /**
             * 默认排序方式
             * 按照加入的时间排序
             */
            case Constant.TYPE_DEF:
                if (this.getJoinTime() > deviceBean.getJoinTime()) {
                    count = -1;
                } else {
                    count = 1;
                }
                break;
            /**
             * 根据cal路里进行排序
             */
            case Constant.TYPE_CAL:
                if (this.getCal() > deviceBean.getCal()) {
                    count = -1;
                } else {
                    count = 1;
                }
                break;
            /**
             * 根据心率值从高到底进行排序
             */
            case Constant.TYPE_HR:
                Integer hr1 = Integer.parseInt(this.getHr());
                Integer hr2 = Integer.parseInt(deviceBean.getHr());
                if (hr1 > hr2) {
                    count = -1;
                } else {
                    count = 1;
                }
                break;
            /**
             * 根据百分比从高到底进行排序
             */
            case Constant.TYPE_PERCENT:
                Integer precent1 = Integer.parseInt(this.getPrecent());
                Integer precent2 = Integer.parseInt(deviceBean.getPrecent());
                if (precent1 > precent2) {
                    count = -1;
                } else {
                    count = 1;
                }
                break;
            /**
             * 根据点数从高到底进行排序
             */
            case Constant.TYPE_POINT:
                if (this.getPoint() > deviceBean.getPoint()) {
                    count = -1;
                } else {
                    count = 1;
                }
                break;
        }
        return count;
    }


    public ArrayList<Integer> getHrList() {
        return hrList;
    }

    public void setHrList(ArrayList<Integer> hrList) {
        this.hrList = hrList;
    }

    public void setHeartRates(ArrayList<Integer> heartRates) {
        this.heartRates = heartRates;
    }

    public ArrayList<Integer> getHeartRates() {
        return heartRates;
    }

    public String getCourseCerterShowType() {
        return courseCerterShowType;
    }

    public void setCourseCerterShowType(String courseCerterShowType) {
        this.courseCerterShowType = courseCerterShowType;
    }

    public String getCourseSortType() {
        return courseSortType;
    }

    public void setCourseSortType(String courseSortType) {
        this.courseSortType = courseSortType;
    }


    public int getTotalHeartCount() {
        return totalHeartCount;
    }

    public void setTotalHeartCount() {
        this.totalHeartCount++;
    }

    public int getMatchheartCount() {
        return matchheartCount;
    }

    public void setMatchheartCount() {
        this.matchheartCount++;
    }

    public double getMatchHeartPrecent() {
        matchHeartPrecent = Arith.div(matchheartCount, totalHeartCount, 1) * 100;
        return matchHeartPrecent;
    }

    public void initHeartRateCount() {
        matchheartCount = 0;
        totalHeartCount = 0;
        matchHeartPrecent = 0;
    }


    public int getMaximalExercise() {
        return maximalExercise;
    }

    public void setMaximalExercise(int maximalExercise) {
        this.maximalExercise = maximalExercise;
    }

    public int getAnaerobicExercise() {
        return anaerobicExercise;
    }

    public void setAnaerobicExercise(int anaerobicExercise) {
        this.anaerobicExercise = anaerobicExercise;
    }

    public int getAerobicExercise() {
        return aerobicExercise;
    }

    public void setAerobicExercise(int aerobicExercise) {
        this.aerobicExercise = aerobicExercise;
    }

    public int getFatMovement() {
        return fatMovement;
    }

    public void setFatMovement(int fatMovement) {
        this.fatMovement = fatMovement;
    }

    public int getWarmup() {
        return warmup;
    }

    public void setWarmup(int warmup) {
        this.warmup = warmup;
    }

    public int getTaskPoint() {
        return taskPoint;
    }

    public void setTaskPoint(int taskPoint) {
        this.taskPoint = taskPoint;
    }

    public int getTaskCal() {
        return taskCal;
    }

    public void setTaskCal(int taskCal) {
        this.taskCal = taskCal;
    }

    public int getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(int averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    public int getAverageStrength() {
        return averageStrength;
    }

    public void setAverageStrength(int averageStrength) {
        this.averageStrength = averageStrength;
    }
}
