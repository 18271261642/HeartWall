package com.jkcq.hrwtv.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.TimeUtils;
import com.jkcq.hrwtv.AllocationApi;
import com.jkcq.hrwtv.app.BaseApp;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.heartrate.bean.SecondHeartRateBean;
import com.jkcq.hrwtv.heartrate.bean.UserBean;
import com.jkcq.hrwtv.http.bean.CourseUserInfo;
import com.jkcq.hrwtv.util.DeviceUtil;
import com.jkcq.hrwtv.util.LogUtil;
import com.jkcq.hrwtv.util.NetUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Desc         ${MQTT服务}
 */

public class CourseMqttService extends BaseMqttservice {

    public final String TAG = CourseMqttService.class.getSimpleName();
    private MqttConnectOptions mMqttConnectOptions;
    //public        String HOST           = "tcp://192.168.10.102:61613";//服务器地址（协议+地址+端口号）
    public String HOST = "tcp://mqtt.fitalent.com.cn:61613";//服务器地址（协议+地址+端口号）
    public String USERNAME = "admin";//用户名
    public String PASSWORD = "fitalent@1.";//密码

    public String CLIENTID = DeviceUtil.getMac(BaseApp.getApp()) + "course";

    private Disposable mSecondDisposable;
    private ConcurrentHashMap<String, SecondHeartRateBean> secondHeartRateBeanHashMap = new ConcurrentHashMap<>();

    private long mBeforeCourseTime = 10 * 60 * 1000;
//    private long mAfterCoursetime = 15 * 60 * 1000;

    private TaskChangeBroadcast broadcast;


    @Override
    public void onCreate() {
        super.onCreate();
        mPublisTopic = 10000 + "/HEART_RATE_SYSTEM";
        init();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyMqttServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (broadcast != null) {
            unregisterReceiver(broadcast);
        }
        //取消定时器
        if (mSecondDisposable != null && !mSecondDisposable.isDisposed()) {
            mSecondDisposable.dispose();
        }
        try {
            if (mqttAndroidClient != null) {
                mqttAndroidClient.unregisterResources();
//                mqttAndroidClient.disconnect(); //断开连接
                mqttAndroidClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        isEnd = true;
        release();
        return super.onUnbind(intent);
    }

    /**
     * 初始化
     */
    private void init() {
        String serverURI = HOST; //服务器地址（协议+地址+端口号）
        mqttAndroidClient = new MqttAndroidClient(this, serverURI, CLIENTID);
        mqttAndroidClient.setCallback(mqttCallback); //设置监听订阅消息的回调
        mMqttConnectOptions = new MqttConnectOptions();
        mMqttConnectOptions.setCleanSession(true); //设置是否清除缓存
        mMqttConnectOptions.setConnectionTimeout(10); //设置超时时间，单位：秒
        mMqttConnectOptions.setKeepAliveInterval(60); //设置心跳包发送间隔，单位：秒
        mMqttConnectOptions.setUserName(USERNAME); //设置用户名
        mMqttConnectOptions.setPassword(PASSWORD.toCharArray()); //设置密码

        // last will message
        boolean doConnect = true;
        String message = "{\"terminal_uid\":\"" + CLIENTID + "\"}";
        String topic = mPublisTopic;


        Log.e(TAG, "topic=" + mPublisTopic + "message=" + message);
        Integer qos = 2;
        Boolean retained = false;
        if ((!message.equals("")) || (!topic.equals(""))) {
            // 最后的遗嘱
            try {
                mMqttConnectOptions.setWill(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
            } catch (Exception e) {
                Log.i(TAG, "Exception Occured", e);
                doConnect = false;
                iMqttActionListener.onFailure(null, e);
            }
        }
        if (doConnect) {
            doClientConnection();
        }

        broadcast = new TaskChangeBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AllocationApi.TASK_CHANGE_BROADCAST);
        registerReceiver(broadcast, filter);
        sendHeartRateBySecond();
    }

    /**
     * 连接MQTT服务器
     */
    private void doClientConnection() {
        if (!isEnd) {
            try {
                if (!mqttAndroidClient.isConnected() && isConnectIsNomarl()) {
                    try {
                        mqttAndroidClient.connect(mMqttConnectOptions, null, iMqttActionListener);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断网络是否连接
     */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "没有可用网络");
            /*没有可用网络的时候，延迟3秒再尝试重连*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doClientConnection();
                }
            }, 3000);
            return false;
        }
    }

    //MQTT是否连接成功的监听
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 订阅主题为 :" + mPublisTopic);
            try {
                mqttAndroidClient.subscribe(mPublisTopic, 2);//订阅主题，参数：主题、服务质量
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            Log.i(TAG, "连接失败 ");
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> e) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    if (NetUtils.hasNetwork(BaseApp.getApp())) {
                        e.onNext("");
                    }
                }
            }).subscribeOn(Schedulers.io()).subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(String s) {
                    Log.i(TAG, "重连中.... ");
                    doClientConnection();//连接失败，重连（可关闭服务器进行模拟）
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }
    };


    //订阅主题的回调
    private MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            Log.i(TAG, "收到消息： " + new String(message.getPayload()));
            parseMqttData(new String(message.getPayload()));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
            Log.i(TAG, "连接断开 ");
            doClientConnection();//连接断开，重连
        }
    };

    public void parseMqttData(String content) {
        String[] arry = content.split("/");
        int size = arry.length;
        for (int i = 0; i < size; i++) {
            parse(arry[i]);
        }
    }

    public void parse(String data) {

        //1023_60_71  sn_heartrate_power
        String[] datas = data.split("_");
        //int sn = Integer.parseInt(datas[0]);
        int size = datas.length;
        if (size == 3) {
            String sn = datas[0];
            int heartRate = Integer.parseInt(datas[1]);
            int energy = Integer.parseInt(datas[2]);
            SecondHeartRateBean secondHeartRateBean;
            if (secondHeartRateBeanHashMap.containsKey(sn)) {
                secondHeartRateBean = secondHeartRateBeanHashMap.get(sn);
            } else {
                secondHeartRateBean = new SecondHeartRateBean();
            }
            secondHeartRateBean.setDevicesSN(sn);
            secondHeartRateBean.getHeartList().add(heartRate);
            secondHeartRateBean.setHeart(heartRate);
            secondHeartRateBean.setEnergy(energy);
            secondHeartRateBean.setTask(true);
            secondHeartRateBean.setTime(System.currentTimeMillis());

            secondHeartRateBeanHashMap.put(sn, secondHeartRateBean);
        }

    }

    ConcurrentHashMap<String, SecondHeartRateBean> sendSecondMap = new ConcurrentHashMap<>();

    public void sendHeartRateBySecond() {
        //一秒发送一次数据刷新
        mSecondDisposable = Observable.interval(1, Constant.REFRESH_RATE, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread()) // 由于interval默认在新线程，所以我们应该切回主线程
                .subscribeOn(Schedulers.computation())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        long curTime = System.currentTimeMillis();
                        //清空
                        sendSecondMap.clear();
                        Iterator<String> dataIter = secondHeartRateBeanHashMap.keySet().iterator();

                        while (dataIter.hasNext()) {
                            String key = dataIter.next();
                            //已获取到用户信息
                            if (BaseApp.sUserInfoMap.containsKey(key)) {
                                SecondHeartRateBean secondBean = secondHeartRateBeanHashMap.get(key);
                                long addTime = secondBean.getTime();
                                if ((curTime + mBeforeCourseTime) < BaseApp.sUserInfoMap.get(key).getCourseBean().getCourseStartTime()
                                        || curTime > BaseApp.sUserInfoMap.get(key).getCourseBean().getCourseEndTime()) {
                                    LogUtil.e(TAG, "curTime=" + (curTime + mBeforeCourseTime) + " curTimeString" + TimeUtils.millis2String(curTime) +
                                            "startTime=" + TimeUtils.millis2String(BaseApp.sUserInfoMap.get(key).getCourseBean().getCourseStartTime()) +
                                            "" + "endTime=" + BaseApp.sUserInfoMap.get(key).getCourseBean().getCourseEndTime() + " removeKey =" + key);

                                    BaseApp.sUserInfoMap.remove(key);
                                    secondHeartRateBeanHashMap.remove(key);
//                                    removeOffLine(key);
                                } else {
                                    if (secondBean.getUserInfo() == null) {
                                        secondBean.setUserInfo(setCommonUser(BaseApp.sUserInfoMap.get(key)));
                                    }
                                    //只传不掉线的数据
                                    if (curTime - addTime < Constant.DROP_TIME) {
                                        secondBean.setOnline(true);
                                    } else {
                                        secondBean.setOnline(false);
                                    }
                                    sendSecondMap.put(key, secondBean);
                                }

                            }
                        }
                        //  HrCourseDataObservable.getInstance().sendCourseHrData(sendSecondMap);
                        //heartRateListener.onHeartRate(sendSecondMap, true);
                    }
                });
    }

    private UserBean setCommonUser(CourseUserInfo courseUserInfo) {
        UserBean userBean = new UserBean();
        userBean.setId(courseUserInfo.getUserId());
        userBean.setAge(courseUserInfo.getAge());
        userBean.setWeight(courseUserInfo.getWeight());
        userBean.setSex(courseUserInfo.getGender());
        userBean.setSn(courseUserInfo.getSn());
        userBean.setHeight(courseUserInfo.getHeight());
        userBean.setAvatar(courseUserInfo.getHeadShotUrl());
        userBean.setNickname(courseUserInfo.getNickName());
        userBean.setNickname(courseUserInfo.getUserName());
        userBean.setJoinTime(System.currentTimeMillis());
        return userBean;
    }

    //掉线移除用户信息
    @Override
    public void removeOffLine(String sn) {
//        BaseApp.sUserInfoMap.remove(sn);
        secondHeartRateBeanHashMap.remove(sn);
    }

    public void clearData() {
        if (secondHeartRateBeanHashMap != null) {
            secondHeartRateBeanHashMap.clear();
        }
    }

    private class TaskChangeBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null && intent.hasExtra(AllocationApi.EXTRA_TASK_CHANGE)) {
                //课程暂停与开始
                isTask = intent.getBooleanExtra(AllocationApi.EXTRA_TASK_CHANGE, false);
            }
        }
    }


    public void setTask(boolean isTask) {
        this.isTask = isTask;
    }


    public class MyMqttServiceBinder extends Binder {
        public CourseMqttService getCollectionServices() {
            return CourseMqttService.this;
        }
    }
}