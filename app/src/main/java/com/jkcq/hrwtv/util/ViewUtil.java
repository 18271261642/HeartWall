package com.jkcq.hrwtv.util;

/*
 * 防止短时间内多次重复点击
 *
 */
public class ViewUtil {

    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long LAST_CLICK_TIME = 0;

    /**
     * 避免重复点击
     *
     * @return
     */
    public static boolean isDoubleClick() {
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - LAST_CLICK_TIME) < MIN_CLICK_DELAY_TIME) {
            LAST_CLICK_TIME = currentClickTime;
            return true;
        } else {
            LAST_CLICK_TIME = currentClickTime;
            return false;
        }
    }

    public static boolean isDoubleClick(int delayTime) {
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - LAST_CLICK_TIME) < delayTime) {
            LAST_CLICK_TIME = currentClickTime;
            return true;
        } else {
            LAST_CLICK_TIME = currentClickTime;
            return false;
        }
    }
}
