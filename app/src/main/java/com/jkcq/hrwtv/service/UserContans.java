package com.jkcq.hrwtv.service;

import com.jkcq.hrwtv.heartrate.bean.SecondHeartRateBean;
import com.jkcq.hrwtv.heartrate.bean.UserBean;
import com.jkcq.hrwtv.http.bean.CourseInfo;

import java.util.concurrent.ConcurrentHashMap;

public class UserContans {


    volatile public static long classTime = 0;//开始上课时间
    volatile public static Boolean isPause = false;
    volatile public static int couserTime = 0;
    volatile public static CourseInfo info;

    //SN包含的所有用户数据
    volatile public static ConcurrentHashMap<String, UserBean> userInfoHashMap = new ConcurrentHashMap<>();
    //收到的所有SN码集合
    volatile public static ConcurrentHashMap<String, String> mCacheMap = new ConcurrentHashMap<>();
    //收到的SN对应的hr值
    volatile public static ConcurrentHashMap<String, Integer> mSnHrMap = new ConcurrentHashMap<>();
    //收到的SN对应的时间
    volatile public static ConcurrentHashMap<String, Long> mSnHrTime = new ConcurrentHashMap<>();
    //收到SN对应的电池
    volatile public static ConcurrentHashMap<String, Integer> mSnEnergyMap = new ConcurrentHashMap<>();




    //已经打标签的sn码集合，已经打标签的不能在大厅模式显示，课程和PK模式下打标签
    public volatile static ConcurrentHashMap<String,Integer> markTagsMap = new ConcurrentHashMap<>();
    //单个的盒子打标签sn码集合，仅用于当前设备取消标签
    public volatile static ConcurrentHashMap<String,Integer> privateMarkTagsMap = new ConcurrentHashMap<>();

    volatile public static ConcurrentHashMap<String, SecondHeartRateBean> secondHeartRateBeanHashMap = new ConcurrentHashMap<>();

    volatile public static ConcurrentHashMap<String, SecondHeartRateBean> sendSecondMap = new ConcurrentHashMap<>();


    public static void clearMap() {
        secondHeartRateBeanHashMap.clear();
        mCacheMap.clear();
        mSnHrTime.clear();
        mSnHrMap.clear();
        userInfoHashMap.clear();
    }

    public static void courseClearMap() {
        secondHeartRateBeanHashMap.clear();
        mCacheMap.clear();
    }

    public static String getDifficultyLevel(int strength) {
        String level = "新手";
        if (strength == 0) {
            level = "新手";
        } else if (strength == 1) {
            level = "入门";
        } else if (strength == 2) {
            level = "中级";
        } else if (strength == 3) {
            level = "高阶";
        } else if (strength == 4) {
            level = "挑战";
        } else if (strength == 5) {
            level = "地狱";
        }
        return level;
    }


}
