package com.jkcq.hrwtv.util;

import android.content.Context;
import android.text.TextUtils;


/**
 * Created by peng on 2018/5/23.
 */

public class JPushUtils {

    private static JPushUtils instance;

    public static JPushUtils getInstance() {
        if (null == instance) {
            instance = new JPushUtils();
        }
        return instance;
    }

    public void init(Context context) {
//        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
//        JPushInterface.init(context);            // 初始化 JPush
    }

    /**
     * 设置别名
     *
     * @param context
     * @param alias
     */
    public void setAlias(Context context, String alias) {
        if (TextUtils.isEmpty(alias)) {
            //退出之后设置默认推送信息
            alias = "1";
        }

//        JPushInterface.setAlias(context, alias, new TagAliasCallback() {
//            @Override
//            public void gotResult(int i, String s, Set<String> set) {
//                Logger.e("setAlias i = " + i + " ,s = " + s);
//            }
//        });

//        JPushInterface.setAlias(context, 1, alias);

    }

//    public void setTag(Context context,String userId){
//        if(TextUtils.isEmpty(userId)){
//            userId = "1";
//        }
//        Set<String> set = new HashSet<String>();
//        JPushInterface.setTags(context,1,set);
//    }


}