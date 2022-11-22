package com.jkcq.hrwtv.service;

/*
 *
 *
 * @author mhj
 * Create at 2018/7/11 12:16
 */

import com.jkcq.hrwtv.heartrate.bean.AccepterCacheBean;
import com.jkcq.hrwtv.heartrate.bean.AcceptorDropBean;

import java.util.ArrayList;
import java.util.HashMap;

public interface OnHeartRateUpdateListener {

     void updateHeartRateData(ArrayList<Integer> devicesSN, HashMap<Integer,AccepterCacheBean> snHeartRates);

}
