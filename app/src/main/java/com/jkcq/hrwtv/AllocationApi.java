package com.jkcq.hrwtv;


import com.jkcq.hrwtv.heartrate.bean.TaskInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ck
 * @version 创建时间：2016年10月11日 上午8:58:18 说明: 公共配置
 */
public class AllocationApi {

    public static final String BaseUrl = com.jkcq.hrwtv.base.AllocationApi.BaseUrl;

    public static final String TASK_CHANGE_BROADCAST = "task_change_broadcast";
    public static final String EXTRA_TASK_CHANGE = "extra_task_change";

    public static boolean isTaskResult = false;

    public static boolean isTaskResult() {
        return isTaskResult;
    }

    public static void setTaskResult(boolean taskResult) {
        isTaskResult = taskResult;
    }

    public static final String USER_ID = "";


    //课程列表
    public static String queryTask() {
        return BaseUrl + "/gymManager/manager/heartRateCourse";
    }

    //课程详细
    public static String queryTaskDetails(String taskId) {
        return BaseUrl + "/gymManager/manager/heartRateCourse/" + taskId;
    }


    //数据
    public static String postHeartRateWallData() {
        return BaseUrl + "/gymApi/gymApi/heartRate/heartRateWallSubmitMotionData";
    }


    /**
     * 根据设备sn查询个人信息
     *
     * @return
     */
    //查询个人信息bySn
    public static String queryCustomerInfoBySN() {
        return BaseUrl + "/gymApi/gymApi/myPersonalDevice/findCustomerInfoBySN/";
    }


    // 判断是否有网络
    public static boolean isNetwork = true;
    public static boolean isShowHint = false;

    // SharedPerferences的String类。
    public interface SpStringTag {
        // 是否提示需要开启高耗电模式的提示！
        String IS_SHOULD_SHOW_HIGH_POWER_TIPS = "is_should_show_high_power_tips";
    }


    private static Set<String> hashSet = new HashSet<>();

    public static Set<String> getAllSNSet() {
        return hashSet;
    }


    public static TaskInfo mCurrentTask = null;

    public static void setCurrentTask(TaskInfo info) {
        mCurrentTask = info;
    }

    public static TaskInfo getCurrentTask() {
        return mCurrentTask;
    }

}
