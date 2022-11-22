package com.jkcq.hrwtv.util.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.jkcq.hrwtv.util.ThreadPoolUtils;

/*
 * Glide工具类
 * classes : com.xingye.project.utils.glide
 * @author 苗恒聚
 * V 2.1.3
 * Create at 2016-3-14 15:38
 */
public class GlideUtils {

    private static GlideUtils instance;

    public static GlideUtils getInstance() {
        if (null == instance) {
            instance = new GlideUtils();
        }
        return instance;
    }

    /**
     * 清理Glide缓存
     *
     * @param context
     */
    public void clear(Context context) {
        clearMemory(context);
        clearDiskCache(context);
    }

    /**
     * 清理Glide内存缓存
     *
     * @param context
     */
    public void clearMemory(Context context) {
        try {
            Glide.get(context).clearMemory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清理Glide磁盘缓存
     *
     * @param context
     */
    public void clearDiskCache(final Context context) {
        try {
            ThreadPoolUtils.getInstance().addTask(new Runnable() {
                @Override
                public void run() {
                    Glide.get(context).clearDiskCache();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}