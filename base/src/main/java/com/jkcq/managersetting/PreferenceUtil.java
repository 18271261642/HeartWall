package com.jkcq.managersetting;

import android.content.Context;
import android.content.SharedPreferences;

import com.blankj.utilcode.util.Utils;

/**
 * created by wq on 2019/4/9
 */
public class PreferenceUtil {

    private static final String PREFERENCE_NAME = "jkcq_shared";

    public static final String KEY_MAC = "key_mac";
    public static final String CLUB_ID="club_id";
    public static final String CLASSROOM_ID = "classroom_id";
    public static final String FUNC_ID = "func_id";
    public static final String DEVICETYPE_ID = "devicetype_id";

    public static final String REQUEST_AD_TIMES="request_ad_times";
    private volatile static PreferenceUtil sPreference;
    private SharedPreferences mSharePreference;

    private PreferenceUtil() {
        mSharePreference = Utils.getApp().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceUtil getInstance() {
        if (sPreference == null) {
            synchronized (PreferenceUtil.class) {
                if (sPreference == null) {
                    sPreference = new PreferenceUtil();
                }
            }
        }
        return sPreference;
    }

    public void putString(String key, String value) {
        mSharePreference.edit().putString(key, value).commit();
    }

    public String getString(String key) {
        return mSharePreference.getString(key, "");
    }

    public void putInt(String key, Integer value) {
        mSharePreference.edit().putInt(key, value).commit();
    }

    public Integer getInt(String key) {
        return mSharePreference.getInt(key, 0);
    }

    public void clear() {
        mSharePreference.edit().clear().apply();
    }
}
