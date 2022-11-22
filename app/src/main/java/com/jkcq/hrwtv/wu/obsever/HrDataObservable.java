
package com.jkcq.hrwtv.wu.obsever;


import com.jkcq.hrwtv.heartrate.bean.SecondHeartRateBean;
import com.jkcq.hrwtv.service.ReceiveSnHrTimeBean;

import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName:NetProgressBar <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年4月19日 上午11:31:46 <br/>
 *
 * @author Administrator
 */
public class HrDataObservable extends Observable {

    private static HrDataObservable obser;


    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

    private HrDataObservable() {
        super();
    }

    public static HrDataObservable getInstance() {
        if (obser == null) {
            synchronized (HrDataObservable.class) {
                if (obser == null) {
                    obser = new HrDataObservable();
                }
            }
        }
        return obser;
    }


    public void sendCourseHrData(ConcurrentHashMap<String, SecondHeartRateBean> data) {
        HrDataObservable.getInstance().setChanged();
        HrDataObservable.getInstance().notifyObservers(data);

    }

    public void sendHallHrData(ConcurrentHashMap<String, SecondHeartRateBean> data) {
        HrDataObservable.getInstance().setChanged();
        HrDataObservable.getInstance().notifyObservers(data);

    }

    public void sendAllHrData(ConcurrentHashMap<String, Integer> data) {
        HrDataObservable.getInstance().setChanged();
        HrDataObservable.getInstance().notifyObservers(data);

    }

}