package com.jkcq.hrwtv.service;

import com.jkcq.hrwtv.heartrate.bean.SecondHeartRateBean;

import java.util.concurrent.ConcurrentHashMap;

/*
 *
 *
 * @author mhj
 * Create at 2018/7/11 16:56
 */
public interface OnHeartRateListener {

     void onHeartRate(ConcurrentHashMap<String,SecondHeartRateBean> secondMap ,Boolean isCourse);

}
