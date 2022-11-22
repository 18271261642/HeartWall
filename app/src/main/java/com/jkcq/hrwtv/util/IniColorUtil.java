package com.jkcq.hrwtv.util;

import android.content.Context;

import com.jkcq.hrwtv.R;

import java.util.HashMap;

/**
 * Created by peng on 2018/5/17.
 */

public class IniColorUtil {

    public static HashMap initParams(Context context) {
        //色值
        HashMap<String, Integer> map;
        map = new HashMap<>();
        map.put("LTGRAY", context.getResources().getColor(com.jkcq.hrwtv.base.R.color.sport_state_one));
        map.put("GRAY", context.getResources().getColor(com.jkcq.hrwtv.base.R.color.sport_state_two));
        map.put("BLUE", context.getResources().getColor(com.jkcq.hrwtv.base.R.color.sport_state_three));
        map.put("GREEN", context.getResources().getColor(com.jkcq.hrwtv.base.R.color.sport_state_four));
        map.put("YELLOW", context.getResources().getColor(com.jkcq.hrwtv.base.R.color.sport_state_five));
        map.put("RED", context.getResources().getColor(com.jkcq.hrwtv.base.R.color.sport_state_six));
        return map;
    }

    public static int getConvert2Color(int range,Context context) {
        HashMap<String, Integer> map;
        map=initParams(context);
        int color = -1;
        switch (range) {
            case 0:
                color = map.get("LTGRAY");
                break;
            case 1:
                color = map.get("GRAY");
                break;
            case 2:
                color = map.get("BLUE");
                break;
            case 3:
                color = map.get("GREEN");
                break;
            case 4:
                color = map.get("YELLOW");
                break;
            case 5:
                color = map.get("RED");
                break;
        }

        return color;
    }

}
