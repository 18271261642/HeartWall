package com.jkcq.hrwtv.util;

import com.jkcq.hrwtv.heartrate.bean.DeviceBean;
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean;

import java.util.ArrayList;

/**
 * Created by peng on 2018/5/17.
 */

public class DeviceBeanUtil {

    public static ArrayList<DevicesDataShowBean> updateSortType(ArrayList<DevicesDataShowBean> list, String sortType, String type) {
        for (int i = 0; i < list.size(); i++) {
            if (type.equals("sort")) {
                list.get(i).setSortType(sortType);
            } else {
                list.get(i).setCourseSortType(sortType);
            }
        }
        return list;
    }
}
