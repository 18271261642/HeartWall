package com.jkcq.hrwtv.service.daemon.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jkcq.hrwtv.service.observe.NetWorkObservable;
import com.jkcq.hrwtv.util.Logger;


/**
 * 注册网络监听广播
 * <p>
 * Created by Administrator on 2017/9/12.
 */
public class NetWorkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Logger.e("NetWorkReceiver", "action = " + action);

        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            NetWorkObservable.getInstance().setChanged();
            if (info != null && info.isAvailable()) {
                //当前网络状态可用
                NetWorkObservable.getInstance().notifyObservers(new NetWorResult(true));
            } else {
                NetWorkObservable.getInstance().notifyObservers(new NetWorResult(false));
            }
        }
    }

    public static class NetWorResult {

        private boolean isConnect;

        public NetWorResult(boolean isConnect) {
            this.isConnect = isConnect;
        }

        public boolean isConnect() {
            return isConnect;
        }

        public void setConnect(boolean connect) {
            isConnect = connect;
        }
    }
}
