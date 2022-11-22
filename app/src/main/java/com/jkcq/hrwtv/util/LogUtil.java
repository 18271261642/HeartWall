package com.jkcq.hrwtv.util;

import android.util.Log;

public class LogUtil {
    private static final String TAG="heartratewall_log";
    private static final boolean IS_DEBUG=true;
    public static void e(String msg){
        if(IS_DEBUG){
            Log.e(TAG,msg);
        }
    }
    public static void e(String tag,String msg){
        if(IS_DEBUG){
            Log.e(tag,msg);
        }
    }

    public static void w(String msg){
        if(IS_DEBUG){
            Log.e(TAG,msg);
        }
    }
    public static void w(String tag,String msg){
        if(IS_DEBUG){
            Log.e(tag,msg);
        }
    }
    public static void d(String msg){
        if(IS_DEBUG){
            Log.e(TAG,msg);
        }
    }
    public static void d(String tag,String msg){
        if(IS_DEBUG){
            Log.e(tag,msg);
        }
    }
    public static void i(String msg){
        if(IS_DEBUG){
            Log.e(TAG,msg);
        }
    }
    public static void i(String tag,String msg){
        if(IS_DEBUG){
            Log.e(tag,msg);
        }
    }
    public static void v(String msg){
        if(IS_DEBUG){
            Log.e(TAG,msg);
        }
    }
    public static void v(String tag,String msg){
        if(IS_DEBUG){
            Log.e(tag,msg);
        }
    }
}
