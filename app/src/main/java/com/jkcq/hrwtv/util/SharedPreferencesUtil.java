package com.jkcq.hrwtv.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.jkcq.hrwtv.app.BaseApp;


public class SharedPreferencesUtil {
    /**
     * 保存到sp中即为xml
     *
     * @param keyName
     * @param keyValue
     */
    public static SharedPreferences sharedPreferences;

    public static void saveToSharedPreferences(String keyName,
                                               String keyValue) {
        // if (sharedPreferences == null) {
        sharedPreferences = BaseApp.getApp().getSharedPreferences(keyName,
                Context.MODE_PRIVATE);
        // }
        Editor editor = sharedPreferences.edit();
        editor.putString(keyName, keyValue);
        editor.commit();
    }

    /**
     * 取出记录的数值
     *
     * @param keyName
     * @return
     */
    public static String getSharedPreferences(String keyName) {
        String dataStr = "";
        // if (sharedPreferences == null) {
        sharedPreferences = BaseApp.getApp().getSharedPreferences(keyName,
                Context.MODE_PRIVATE);
        // }
        dataStr = sharedPreferences.getString(keyName, dataStr);
        return dataStr;
    }

    /**
     * 刪除数据
     */
    public static void deleteSharedPreferences(String keyName) {
        sharedPreferences = BaseApp.getApp().getSharedPreferences(keyName, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
}
