package com.jkcq.hrwtv.wu.newversion.observa;

import java.util.Observable;

/**
 * 网络变化监听回调
 * <p>
 * Created by huashao on 2017/8/4.
 */
public class UnRegObservable extends Observable {

    private static UnRegObservable instance;

    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }

    public UnRegObservable() {
        super();
    }

    public static UnRegObservable getInstance() {
        if (instance == null) {
            synchronized (UnRegObservable.class) {
                if (instance == null) {
                    instance = new UnRegObservable();
                }
            }
        }
        return instance;
    }

    public void sendUnRe() {
        UnRegObservable.getInstance().setChanged();
        UnRegObservable.getInstance().notifyObservers();
    }
}