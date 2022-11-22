package com.jkcq.hrwtv.service.observe;

import java.util.Observable;

/**
 * 网络变化监听回调
 * <p>
 * Created by huashao on 2017/8/4.
 */
public class NetWorkObservable extends Observable {

    private static NetWorkObservable instance;

    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

    private NetWorkObservable() {
        super();
    }

    public static NetWorkObservable getInstance() {
        if (instance == null) {
            synchronized (NetWorkObservable.class) {
                if (instance == null) {
                    instance = new NetWorkObservable();
                }
            }
        }
        return instance;
    }
}