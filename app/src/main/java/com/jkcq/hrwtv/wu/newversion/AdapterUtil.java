package com.jkcq.hrwtv.wu.newversion;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.app.BaseApp;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.eventBean.EventConstant;
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean;
import com.jkcq.hrwtv.util.HeartRateConvertUtils;
import com.jkcq.hrwtv.wu.util.ShapeUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class AdapterUtil {

    private static Map<String, String> sMainDataStateMap;

    public static Map getMainDataStateMap() {
        if (sMainDataStateMap == null) {
            sMainDataStateMap = new HashMap();
            sMainDataStateMap.put(Constant.LOCATION_ONE, Constant.TYPE_CAL);
            sMainDataStateMap.put(Constant.LOCATION_TWO, Constant.TYPE_HR);
            sMainDataStateMap.put(Constant.LOCATION_THREE, Constant.TYPE_POINT);
            sMainDataStateMap.put(Constant.LOCATION_CENTER, Constant.TYPE_PERCENT);
        }
        return sMainDataStateMap;
    }

    public static void setMainDataStateMap(String typeName) {
        if (sMainDataStateMap == null) {
            sMainDataStateMap = new HashMap();
            sMainDataStateMap.put(Constant.LOCATION_ONE, Constant.TYPE_CAL);
            sMainDataStateMap.put(Constant.LOCATION_TWO, Constant.TYPE_HR);
            sMainDataStateMap.put(Constant.LOCATION_THREE, Constant.TYPE_POINT);
            sMainDataStateMap.put(Constant.LOCATION_CENTER, Constant.TYPE_PERCENT);
        }
        Iterator<String> dataIter = sMainDataStateMap.keySet().iterator();
        while (dataIter.hasNext()) {
            String key = dataIter.next();
            if (sMainDataStateMap.get(key).equals(typeName)) {
                sMainDataStateMap.put(key, sMainDataStateMap.get(Constant.LOCATION_CENTER));
                sMainDataStateMap.put(Constant.LOCATION_CENTER, typeName);
                break;
            }
        }
    }


    public static void setVaule(TextView textview, DevicesDataShowBean data, String type) {
        if (sMainDataStateMap == null) {
            getMainDataStateMap();
        }

        if (data == null) {
            return;
        }
        switch (type) {
            case Constant.TYPE_CAL:
//                textview.setText("920");
                textview.setText(HeartRateConvertUtils.doubleParseStr(data.getCal()));
                break;
            case Constant.TYPE_HR:
//                textview.setText("146");
                if (data.getLiveHeartRate() == 0) {
                    textview.setText(String.valueOf("--"));
                } else {
                    textview.setText(String.valueOf(data.getLiveHeartRate()));
                }

                break;
            case Constant.TYPE_PERCENT:
//                textview.setText("85");
                if (data.getLiveHeartRate() == 0) {
                    textview.setText(String.valueOf("--"));
                } else {
                    textview.setText(data.getPrecent());
                }
               /* if (key.equals(Constant.LOCATION_CENTER)) {
                    imageView.setVisibility(View.GONE);
                }*/

                break;
            case Constant.TYPE_POINT:
//                textview.setText("37");
                textview.setText(HeartRateConvertUtils.doubleParseStr(data.getPoint()));
                break;
            default:
                break;
        }

    }


    public static void setLeftOvalColor4(int hearStrength, ImageView iv) {
        if (hearStrength < 50) {
            iv.setImageResource(R.mipmap.icon_card4_one);
        } else if (hearStrength >= 50 && hearStrength < 60) {
            iv.setImageResource(R.mipmap.icon_card4_two);
        } else if (hearStrength >= 60 && hearStrength < 70) {
            iv.setImageResource(R.mipmap.icon_card4_three);
        } else if (hearStrength >= 70 && hearStrength < 80) {
            iv.setImageResource(R.mipmap.icon_card4_four);

        } else if (hearStrength >= 80 && hearStrength < 90) {
            iv.setImageResource(R.mipmap.icon_card4_five);
        } else if (hearStrength >= 90) {
            iv.setImageResource(R.mipmap.icon_card4_six);
        }


    }


    public static void setItemBg(View linearLayout, int hearStrength, Context context, float radius, ImageView iv, boolean isCard1) {

        if (hearStrength < 50) {
            linearLayout.setBackgroundResource(R.drawable.shape_one_8);

            //  ShapeUtil.setColorRecLeftBottom(linearLayout, context.getResources().getColor(R.color.sport_state_one), radius);
            if (isCard1) {
                iv.setImageResource(R.mipmap.icon_card1_one);
            } else {
                iv.setImageResource(R.mipmap.icon_card4_one);
            }

        } else if (hearStrength >= 50 && hearStrength < 60) {
            linearLayout.setBackgroundResource(R.drawable.shape_two_8);
            //ShapeUtil.setColorRec(linearLayout, context.getResources().getColor(R.color.sport_state_two), radius);
            if (isCard1) {
                iv.setImageResource(R.mipmap.icon_card1_two);
            } else {
                iv.setImageResource(R.mipmap.icon_card4_two);
            }
        } else if (hearStrength >= 60 && hearStrength < 70) {
            linearLayout.setBackgroundResource(R.drawable.shape_three_8);
            // ShapeUtil.setColorRec(linearLayout, context.getResources().getColor(R.color.sport_state_three), radius);
            if (isCard1) {
                iv.setImageResource(R.mipmap.icon_card1_three);
            } else {
                iv.setImageResource(R.mipmap.icon_card4_three);
            }
        } else if (hearStrength >= 70 && hearStrength < 80) {
            linearLayout.setBackgroundResource(R.drawable.shape_four_8);
            //  ShapeUtil.setColorRec(linearLayout, context.getResources().getColor(R.color.sport_state_four), radius);
            if (isCard1) {
                iv.setImageResource(R.mipmap.icon_card1_four);
            } else {
                iv.setImageResource(R.mipmap.icon_card4_four);
            }
        } else if (hearStrength >= 80 && hearStrength < 90) {
            linearLayout.setBackgroundResource(R.drawable.shape_five_8);
            if (isCard1) {
                iv.setImageResource(R.mipmap.icon_card1_five);
            } else {
                iv.setImageResource(R.mipmap.icon_card4_five);
            }
            //  ShapeUtil.setColorRec(linearLayout, context.getResources().getColor(R.color.sport_state_five), radius);
        } else if (hearStrength >= 90) {
            linearLayout.setBackgroundResource(R.drawable.shape_six_8);
            if (isCard1) {
                iv.setImageResource(R.mipmap.icon_card1_six);
            } else {
                iv.setImageResource(R.mipmap.icon_card4_six);
            }
            // ShapeUtil.setColorRecLeftTop(linearLayout, context.getResources().getColor(R.color.sport_state_six), radius);
        }
    }

    public static int convertRang(float hearStrength) {

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

    /**
     * 排序是否更换位置
     *
     * @param curData
     * @param previousData
     * @return
     */
    public static boolean isChange(DevicesDataShowBean curData, DevicesDataShowBean previousData) {

        //TODO 现在只有加入时间排序
        if ((curData.getJoinTime() / 1000) < (previousData.getJoinTime() / 1000)) {
            return true;
        }
       /* switch (BaseApp.sSortType) {
            case EventConstant.SORT_DATA_DEFUT:
                if (curData.getJoinTime() > previousData.getJoinTime()) {
                    return true;
                }
                break;
            case EventConstant.SORT_DATA_CAL:
                if ((int) curData.getCal() > (int) previousData.getCal()) {
                    return true;
                }
                break;
            case EventConstant.SORT_DATA_HR:
                if (curData.getLiveHeartRate() > previousData.getLiveHeartRate()) {
                    return true;
                }
                break;
            case EventConstant.SORT_DATA_HR_STRENGTH:
                if (Integer.valueOf(curData.getPrecent()) > Integer.valueOf(previousData.getPrecent())) {
                    return true;
                }
                break;
            case EventConstant.SORT_DATA_POINT:
                if ((int) curData.getPoint() > (int) previousData.getPoint()) {
                    return true;
                }
                break;
            case EventConstant.SORT_DATA_MATCH:
                if (curData.getMatchRate() > previousData.getMatchRate()) {
                    return true;
                }
                break;
            default:
                return false;
        }*/
        return false;
    }

}
