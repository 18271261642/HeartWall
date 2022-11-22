package com.jkcq.hrwtv.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jkcq.hrwtv.app.BaseApp;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean;
import com.jkcq.hrwtv.heartrate.bean.DevicesTypeBean;
import com.jkcq.hrwtv.heartrate.bean.ShowBean;
import com.jkcq.hrwtv.heartrate.bean.TimerTagBean;
import com.jkcq.hrwtv.heartrate.bean.User;
import com.jkcq.hrwtv.heartrate.bean.UserBean;
import com.jkcq.hrwtv.http.bean.ClubInfo;
import com.jkcq.hrwtv.http.bean.CourseInfo;
import com.jkcq.hrwtv.service.UserContans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by peng on 2018/5/5.
 */

public class CacheDataUtil {

    /**
     * 临时保存需要上传的心率数据
     */
    public static CopyOnWriteArrayList<DevicesDataShowBean> sUploadHeartData = new CopyOnWriteArrayList<>();
    //大于5表示课程模式，0-5分别表示当前心率课程区间
    public static int mCurrentRange = 10;
    public static String sHeartModel = Constant.HALL_MODEL;


    public static ShowBean getDisplayRule() {
        ACache aCache = ACache.get(BaseApp.getApp());
        Gson mGson = new Gson();
        ShowBean showBean = mGson.fromJson(aCache.getAsString(Constant.DISPLAY_CACHE_NAME), ShowBean.class);
        if (showBean == null) {
            showBean = new ShowBean(Constant.TYPE_PERCENT, Constant.TYPE_HR, Constant.TYPE_CAL, Constant.TYPE_Light, Constant.TYPE_POINT);
            aCache.put(Constant.DISPLAY_CACHE_NAME, mGson.toJson(showBean));
        }
        return showBean;
    }

    public static ShowBean getTaskDisplayRule() {
        ACache aCache = BaseApp.getaCache();
        Gson mGson = new Gson();
        ShowBean showBean = mGson.fromJson(BaseApp.getaCache().getAsString(Constant.DISPLAY_COURSE_CACHE_NAME), ShowBean.class);
        if (showBean == null) {
            showBean = new ShowBean(Constant.TYPE_PERCENT, Constant.TYPE_HR, Constant.TYPE_CAL, Constant.TYPE_Light, Constant.TYPE_POINT);
            aCache.put(Constant.DISPLAY_CACHE_NAME, mGson.toJson(showBean));
        }
        return showBean;
    }

    public static TimerTagBean getTimerBean() {
        Gson mGson = new Gson();
        TimerTagBean timebean = mGson.fromJson(BaseApp.getaCache().getAsString(Constant.SHOW_TIME), TimerTagBean.class);
        return timebean;
    }


    public static void saveShowFragment(String value) {
        BaseApp.getaCache().put(Constant.SHOW_FRAGMENT, value);
    }

    public static String getShowFragment() {
        String str = BaseApp.getaCache().getAsString(Constant.SHOW_FRAGMENT);
        if (TextUtils.isEmpty(str)) {
            str = Constant.MAIN_FRAGMENT;
        }
        return str;
    }

    public static void saveClubName(String name, String id) {
        SharedPreferencesUtil.saveToSharedPreferences(Constant.CLUB_NAME, name);
        SharedPreferencesUtil.saveToSharedPreferences(Constant.CLUB_Id, id);
    }

    public static String getClubId() {
        return SharedPreferencesUtil.getSharedPreferences(Constant.CLUB_Id);
    }

    public static ClubInfo getClubInfo() {
        ClubInfo bean = new ClubInfo();
        bean.setName(SharedPreferencesUtil.getSharedPreferences(Constant.CLUB_NAME));
        bean.setUid(SharedPreferencesUtil.getSharedPreferences(Constant.CLUB_Id));
        return bean;
    }


    public static DevicesTypeBean getDevicesType() {
        DevicesTypeBean bean = new DevicesTypeBean();
        bean.setName(SharedPreferencesUtil.getSharedPreferences(Constant.DEVICES_TYPE_NAME));
        bean.setId(SharedPreferencesUtil.getSharedPreferences(Constant.DEVICES_TYPE_ID));
        return bean;
    }

    public static void clearTask() {
        ACache aCache = ACache.get(BaseApp.getApp());
        aCache.remove(Constant.SHOW_TASk);
    }

    public static void saveCourseUserBean(CopyOnWriteArrayList<DevicesDataShowBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }

       /* ACache aCache = ACache.get(BaseApp.getApp());
        Gson mGson = new Gson();
        String str = mGson.toJson(list);
        aCache.put(Constant.saveCoureList, str, 10 * 60);*/


       // Log.e("Course", "saveCourseUserBean" + aCache.getAsString(Constant.saveCoureList));
    }


    public static void saveCouseInfo(CourseInfo info) {
        /*ACache aCache = ACache.get(BaseApp.getApp());
        Gson mGson = new Gson();
        String str = mGson.toJson(info);
        aCache.put(Constant.saveCourseInfo, str);*/

    }


    public static void savemRemainTime(int time) {
        /*ACache aCache = ACache.get(BaseApp.getApp());
        aCache.put(Constant.saveRemainTime, time + "");*/
    }

    public static int getRemainTime() {
        try {
            ACache aCache = ACache.get(BaseApp.getApp());
            String time = aCache.getAsString(Constant.saveRemainTime);
            if (TextUtils.isEmpty(time)) {
                return 0;
            } else {
                return Integer.parseInt(time);
            }
        } catch (Exception e) {
            return 0;
        }

    }

    public static CourseInfo getCourseInfo() {
        ACache aCache = ACache.get(BaseApp.getApp());
        Gson mGson = new Gson();

        Log.e("getCourseInfo", aCache.getAsString(Constant.saveCourseInfo));
        CourseInfo timebean = mGson.fromJson(aCache.getAsString(Constant.saveCourseInfo), CourseInfo.class);
        if (timebean == null) {
            return null;
        } else {
            return timebean;
        }
    }

    public static void clearCourseList() {
        ACache aCache = ACache.get(BaseApp.getApp());
        aCache.remove(Constant.saveCoureList);
        aCache.remove(Constant.saveUserBean);
        aCache.remove(Constant.saveCourseInfo);
        aCache.remove(Constant.saveRemainTime);
    }

    public static void saveUserMap() {
        /*if (UserContans.userInfoHashMap == null || UserContans.userInfoHashMap.size() == 0) {
            return;
        }
        ACache aCache = ACache.get(BaseApp.getApp());
        Gson mGson = new Gson();
        String str = mGson.toJson(UserContans.userInfoHashMap);
        Log.e("getUserMap", "saveUserMap" + aCache.getAsString(Constant.saveUserBean));
        aCache.put(Constant.saveUserBean, str);*/
    }


    public static void getUserMap() {
        ACache aCache = ACache.get(BaseApp.getApp());
        Gson mGson = new Gson();

        Log.e("getUserMap", "get" + aCache.getAsString(Constant.saveUserBean));

        ConcurrentHashMap<String, UserBean> list = mGson.fromJson(aCache.getAsString(Constant.saveUserBean), new TypeToken<ConcurrentHashMap<String, UserBean>>() {
        }.getType());
        if (list == null) {
            list = new ConcurrentHashMap<>();
        }
        UserContans.userInfoHashMap.putAll(list);


    }

    public static List<DevicesDataShowBean> getCourseUserList() {
        try {
            ACache aCache = ACache.get(BaseApp.getApp());
            Gson mGson = new Gson();
            Log.e("Course", "getCourseUserList" + aCache.getAsString(Constant.saveCoureList));

            List<DevicesDataShowBean> list = mGson.fromJson(aCache.getAsString(Constant.saveCoureList), new TypeToken<List<DevicesDataShowBean>>() {
            }.getType());
            if (list != null && list.size() > 0) {
                return list;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }

    public static TimerTagBean saveTimer(boolean isShow, boolean isPasue) {
        ACache aCache = ACache.get(BaseApp.getApp());
        Gson mGson = new Gson();
        TimerTagBean timebean = mGson.fromJson(aCache.getAsString(Constant.SHOW_TIME), TimerTagBean.class);
        if (timebean == null) {
            timebean = new TimerTagBean();
        }
        timebean.setPause(isPasue);
        timebean.setShow(isShow);
        aCache.put(Constant.SHOW_TIME, mGson.toJson(timebean));
        return timebean;
    }

    public static TimerTagBean saveTimer(boolean isPasue) {
        ACache aCache = ACache.get(BaseApp.getApp());
        Gson mGson = new Gson();
        TimerTagBean timebean = mGson.fromJson(aCache.getAsString(Constant.SHOW_TIME), TimerTagBean.class);
        if (timebean == null) {
            timebean = new TimerTagBean();
        }
        timebean.setPause(isPasue);
        aCache.put(Constant.SHOW_TIME, mGson.toJson(timebean));
        return timebean;
    }

    public static void clearTimerTag() {
        ACache aCache = ACache.get(BaseApp.getApp());
        aCache.remove(Constant.SHOW_TIME);
    }


    public static void clearAll() {
        ACache aCache = ACache.get(BaseApp.getApp());
        aCache.clear();
    }


}
