
package com.jkcq.hrwtv.wu.obsever;


import com.jkcq.hrwtv.heartrate.bean.SecondHeartRateBean;

import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName:NetProgressBar <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年4月19日 上午11:31:46 <br/>
 *
 * @author Administrator
 */
public class HrCourseDataObservable extends Observable {

    private static HrCourseDataObservable obser;


    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

    private HrCourseDataObservable() {
        super();
    }

    public static HrCourseDataObservable getInstance() {
        if (obser == null) {
            synchronized (HrCourseDataObservable.class) {
                if (obser == null) {
                    obser = new HrCourseDataObservable();
                }
            }
        }
        return obser;
    }


    public void sendCourseHrData(ConcurrentHashMap<String, Integer> data) {
        HrCourseDataObservable.getInstance().setChanged();
        HrCourseDataObservable.getInstance().notifyObservers(data);

    }


}