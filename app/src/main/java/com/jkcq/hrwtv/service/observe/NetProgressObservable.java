
package com.jkcq.hrwtv.service.observe;

import java.util.Observable;

/**
 * ClassName:NetProgressBar <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年4月19日 上午11:31:46 <br/>
 * 
 * @author Administrator
 * @version
 */
public class NetProgressObservable extends Observable {

    private static NetProgressObservable obser;

    public static final int SHOW_PROGRESS_BAR = 0;
    public static final int DISMISS_PORGRESS_BAR = 1;

    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

    private NetProgressObservable() {
        super();
    }

    public static NetProgressObservable getInstance() {
        if (obser == null) {
            synchronized (NetProgressObservable.class) {
                if (obser == null) {
                    obser = new NetProgressObservable();
                }
            }
        }
        return obser;
    }
}