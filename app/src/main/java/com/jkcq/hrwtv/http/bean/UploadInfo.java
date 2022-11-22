package com.jkcq.hrwtv.http.bean;

import java.util.List;

public class UploadInfo {
    // 预约时间段id
    private String inAndOutId;
    // 运动总时间
    private Long totalExerciseTime;
    // 设备类型
    private String deviceTypeId;
    private String deviceTypeName;
    // 设备token
    private String deviceToken;
    // 消耗(卡路里)
    private int consume;

    // 距离(公里)
    private int distance;
    // 运动状态 0未完成 1进行中 2已完成
    private int motionState;
    // 运动点数
    private int motionPoint;
    // 极限运动时间
    private Integer maximalExercise;
    // 无氧运动时间
    private Long anaerobicExercise;
    // 有氧运动时间
    private Long aerobicExercise;
    // 燃脂运动时间
    private Long fatMovement;
    // 热身运动时间
    private Long warmUp;
    private Long startTime;// 开始时间
    private List<Integer> heartRate;


    private String motionId;
    // 发电量
    private int powerGeneration;
}
