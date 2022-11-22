package com.jkcq.hrwtv.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.jkcq.hrwtv.AllocationApi;
import com.jkcq.hrwtv.app.BaseApp;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.util.CacheDataUtil;
import com.jkcq.hrwtv.util.DeviceUtil;
import com.jkcq.hrwtv.util.LogUtil;
import com.jkcq.hrwtv.util.NetUtils;
import com.jkcq.hrwtv.wu.obsever.HrDataObservable;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Desc         ${MQTT服务}
 * @author Admin
 */

public class MyMqttService extends BaseMqttservice {

    public final String TAG = MyMqttService.class.getSimpleName();
    private static MqttAndroidClient mqttAndroidClient;
    private MqttConnectOptions mMqttConnectOptions;
    //public        String HOST           = "tcp://192.168.10.102:61613";//服务器地址（协议+地址+端口号）
    // public String HOST = "tcp://120.77.62.190:61613";//服务器地址（协议+地址+端口号）
    //服务器地址（协议+地址+端口号）
    public String HOST = "tcp://mqtt.fitalent.com.cn:61613";
    //用户名
    public String USERNAME = "admin";
    //密码
    public String PASSWORD = "fitalent@1.";
    public static String PUBLISH_TOPIC = "";//发布主题
    public static String RESPONSE_TOPIC = "message_arrived";//响应主题
    public String CLIENTID = DeviceUtil.getMac(BaseApp.getApp()) + "hall";
    ;//客户端ID，一般以客户端唯一标识符表示，这里用设备序列号表示


    private TaskChangeBroadcast broadcast;
    //每一分钟
    private String path = "";
    private int mTotalBytes = 0;//接收到的总字节数
    private String current_time = "";


    HandlerThread mHandlerThread = new HandlerThread("mqttService");
    private static int PARSE_DATA = 1;
    Handler mParseHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        //开始解析线程
        mHandlerThread.start();
        mParseHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == PARSE_DATA) {
                    parseMqttData(msg.obj.toString());
                }
            }
        };

        String clubId = CacheDataUtil.getClubId();
        if (!TextUtils.isEmpty(clubId)) {
            PUBLISH_TOPIC = clubId + "/HEART_RATE_SYSTEM";
//            PUBLISH_TOPIC = 10000 + "/HEART_RATE_SYSTEM";
        } else {
            PUBLISH_TOPIC = "10000" + "/HEART_RATE_SYSTEM";
        }
        // PUBLISH_TOPIC = "10000" + "/HEART_RATE_SYSTEM";
        init();
        //初始化Log写入工具
        //FileUtil.initFile(BaseApp.getApp());
        //path = FileUtil.getFile();
        // current_time = DateUtils.getStrTimes(System.currentTimeMillis());
        // }
        // LogUtil.i(TAG, "onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        String topic = intent.getStringExtra(PUBLISH_TOPIC_KEY);
//        if (!TextUtils.isEmpty(topic)) {
//            mPublisTopic = topic;
//        }
//        init();
        return new MyMqttServiceBinder();
    }


    /**
     * 发布 （模拟其他客户端发布消息）
     *
     * @param message 消息
     */
    public static void publish(String message) {
        String topic = PUBLISH_TOPIC;
        Integer qos = 2;
        Boolean retained = false;
        try {
            //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
            mqttAndroidClient.publish(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 响应 （收到其他客户端的消息后，响应给对方告知消息已到达或者消息有问题等）
     *
     * @param message 消息
     */
    public void response(String message) {
        String topic = RESPONSE_TOPIC;
        Integer qos = 2;
        Boolean retained = false;
        try {
            //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
            mqttAndroidClient.publish(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
        } catch (MqttException e) {
            e.printStackTrace();
        }
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
        String topic = PUBLISH_TOPIC;
        Log.e(TAG, "topic=" + mPublisTopic + "message=" + message);
        Integer qos = 2;
        Boolean retained = false;
        if ((!message.equals("")) || (!topic.equals(""))) {
            // 最后的遗嘱
            try {
                mMqttConnectOptions.setWill(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, "Exception Occured" + e.toString());
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
            LogUtil.i(TAG, "当前网络名称：" + name);
            return true;
        } else {
            LogUtil.i(TAG, "没有可用网络");
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
            LogUtil.i(TAG, "连接成功 订阅主题为 :" + PUBLISH_TOPIC + "#");
            try {
                mqttAndroidClient.subscribe(PUBLISH_TOPIC, 2);//订阅主题，参数：主题、服务质量
            } catch (Exception e) {
                //todo mqttAndroidClient will be null
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            LogUtil.i(TAG, "连接失败 ");
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
                    LogUtil.i(TAG, "重连中.... ");
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
            Timber.e(TAG+"topic= " + topic + " 收到消息： " + new String(message.getPayload()));
            Message msg = mParseHandler.obtainMessage();
            msg.what = PARSE_DATA;
            msg.obj = new String(message.getPayload());
            mParseHandler.sendMessage(msg);
//            parseMqttData(new String(message.getPayload()));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
            LogUtil.i(TAG, "连接断开 ");
            doClientConnection();//连接断开，重连
        }
    };


    //解析MQtt数据
    public void parseMqttData(String content) {
        String[] arry = content.split("/");
        int size = arry.length;
        for (int i = 0; i < size; i++) {
            parse(arry[i]);
        }
    }

    //解析单个心率数据
    public void parse(String data) {
        mTotalBytes++;
        //1023_60_71  sn_heartrate_power
        String[] datas = data.split("_");
        //int sn = Integer.parseInt(datas[0]);
        int size = datas.length;
        if (size == 3) {
            String sn = datas[0];
            int heartRate = Integer.parseInt(datas[1]);
            int energy = Integer.parseInt(datas[2]);
            UserContans.mSnHrMap.put(sn, heartRate);
            UserContans.mSnHrTime.put(sn, System.currentTimeMillis());
            Timber.e("-----mqtt接收数据="+sn);
         //   Log.e(TAG,"------mqtt接收数据="+sn+" "+"\n"+DateUtils.getFormatData(UserContans.mSnHrTime.get(sn)));
        }
    }


    public void sendHeartRateBySecond() {


        mParseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doEverySenond();
                sendHeartRateBySecond();
            }
        }, 1000 * Constant.REFRESH_RATE);

    }

    private void doEverySenond() {
//        Log.e("MyMqtt", " ThreadName=" + Thread.currentThread().getName());
        /*long curTime = System.currentTimeMillis();
        BaseApp.receiveTime = curTime;
        UserContans.sendSecondMap.clear();
        Iterator<String> dataIter = UserContans.secondHeartRateBeanHashMap.keySet().iterator();
        while (dataIter.hasNext()) {
            String key = dataIter.next();
            //已获取到用户信息
            if (UserContans.userInfoHashMap.containsKey(key)) {
                SecondHeartRateBean secondBean = UserContans.secondHeartRateBeanHashMap.get(key);
                long addTime = secondBean.getTime();
                secondBean.setUserInfo(UserContans.userInfoHashMap.get(key));
                UserContans.sendSecondMap.put(key, secondBean);
                //掉线，移除数据
                if ((curTime - addTime) > Constant.DROP_TIME) {
//                                    mTotalMap.remove(key);
                    UserContans.secondHeartRateBeanHashMap.remove(key);
                    UserContans.mCacheMap.remove(key);
                    removeOffLine(key);
                }

            } else {
                if (!UserContans.mCacheMap.containsKey(key)) {
                    uploadDevciesSN(key);
                } else {
                    //这里不需要移除，因为移除了会导致不是该俱乐部的会员每次都去请求
                    //mCacheMap.remove(key);
                    UserContans.secondHeartRateBeanHashMap.remove(key);
                }

            }
        }*/

        HrDataObservable.getInstance().sendAllHrData(UserContans.mSnHrMap);

    }

    //掉线移除用户信息
    @Override
    public void removeOffLine(String sn) {
        if (UserContans.userInfoHashMap.containsKey(sn)) {
            UserContans.userInfoHashMap.remove(sn);
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


    public class MyMqttServiceBinder extends Binder {
        public MyMqttService getCollectionServices() {
            return MyMqttService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.i(TAG, "onUnbind");
        if (broadcast != null) {
            unregisterReceiver(broadcast);
        }
        try {
            if (mqttAndroidClient != null) {
                mqttAndroidClient.unregisterResources();
                mqttAndroidClient.disconnect(); //断开连接
                mqttAndroidClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        isEnd = true;
        mParseHandler.removeMessages(0);
        mHandlerThread.quit();
        release();
        mqttCallback = null;
        UserContans.userInfoHashMap.clear();
        return super.onUnbind(intent);
    }
}