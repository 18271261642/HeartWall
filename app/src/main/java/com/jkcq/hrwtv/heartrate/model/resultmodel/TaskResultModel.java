package com.jkcq.hrwtv.heartrate.model.resultmodel;

import com.jkcq.hrwtv.heartrate.bean.DeviceBean;
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean;

import java.util.ArrayList;

/*
 *
 *
 * @author mhj
 * Create at 2018/5/7 21:31
 */
public interface TaskResultModel {
    void postPk(ArrayList<DevicesDataShowBean> redlist, ArrayList<DevicesDataShowBean> bluelist, String heartRateCourseId, String winGroup, String modeType, long endTime, double redSum, double blueSum);

    void postHwall(ArrayList<DevicesDataShowBean> list, String modeType);

    void postCourse(ArrayList<DevicesDataShowBean> list, String heartRateCourseId, String modeType, long endTime);
}
