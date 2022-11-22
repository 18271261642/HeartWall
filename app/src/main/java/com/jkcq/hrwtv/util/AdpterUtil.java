package com.jkcq.hrwtv.util;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.configure.Constant;

/**
 * Created by peng on 2018/5/25.
 */

public class AdpterUtil {

    public static void setVaule(String typeValue, TextView textview, ImageView imageView) {
        switch (typeValue) {
            case Constant.TYPE_CAL:
                textview.setTag(Constant.TYPE_CAL);
                imageView.setImageResource(R.drawable.icon_main_cal);
                break;
            case Constant.TYPE_HR:
                textview.setTag(Constant.TYPE_HR);
                imageView.setImageResource(R.drawable.icon_main_hr);
                break;
            case Constant.TYPE_PERCENT:
                textview.setTag(Constant.TYPE_PERCENT);
                imageView.setImageResource(R.drawable.icon_main_precent);
                break;
            case Constant.TYPE_POINT:
                textview.setTag(Constant.TYPE_POINT);
                imageView.setImageResource(R.drawable.icon_main_point);
                break;
            case Constant.TYPE_Light:
                textview.setTag(Constant.TYPE_Light);
                imageView.setImageResource(R.drawable.icon_main_light);
                break;
        }
    }

    public static void setCardBg(ViewGroup linearLayout, int hearStrength) {
        if (hearStrength < 50) {
            linearLayout.setBackgroundResource(R.drawable.card_light_gray_bg);
        } else if (hearStrength >= 50 && hearStrength < 60) {
            linearLayout.setBackgroundResource(R.drawable.card_deep_gray_bg);
        } else if (hearStrength >= 60 && hearStrength < 70) {
            linearLayout.setBackgroundResource(R.drawable.card_blue_bg);
        } else if (hearStrength >= 70 && hearStrength < 80) {
            linearLayout.setBackgroundResource(R.drawable.card_green_bg);
        } else if (hearStrength >= 80 && hearStrength < 90) {
            linearLayout.setBackgroundResource(R.drawable.card_yello_bg);
        } else if (hearStrength >= 90) {
            linearLayout.setBackgroundResource(R.drawable.card_red_bg);
        }
    }

    public static int convertRang(int hearStrength) {

        if (hearStrength < 50) {
            return 0;
        } else if (hearStrength >= 50 && hearStrength < 60) {
            return 1;
        } else if (hearStrength >= 60 && hearStrength < 70) {
            return 2;
        } else if (hearStrength >= 70 && hearStrength < 80) {
            return 3;
        } else if (hearStrength >= 80 && hearStrength < 90) {
            return 4;
        } else if (hearStrength >= 90) {
            return 5;
        }
        return 0;
    }


}
