package com.jkcq.hrwtv.heartrate.model.resultmodel;

import com.jkcq.hrwtv.base.mvp.BaseView;
import com.jkcq.hrwtv.heartrate.bean.DeviceBean;
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean;

import java.util.ArrayList;

/*
 * 
 * 
 * @author mhj
 * Create at 2018/5/7 21:33 
 */
public interface TaskResultView extends BaseView {

    void getHeartRateSuccess(ArrayList<DevicesDataShowBean> rates);
    void postHeartRateDataSuccess();
}
