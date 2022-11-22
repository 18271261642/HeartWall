
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
public class AddUseObservable extends Observable {

    private static AddUseObservable obser;


    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

    private AddUseObservable() {
        super();
    }

    public static AddUseObservable getInstance() {
        if (obser == null) {
            synchronized (AddUseObservable.class) {
                if (obser == null) {
                    obser = new AddUseObservable();
                }
            }
        }
        return obser;
    }


    public void addUserNotify() {
        AddUseObservable.getInstance().setChanged();
        AddUseObservable.getInstance().notifyObservers();

    }

}