package com.jkcq.hrwtv.service;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.jkcq.hrwtv.app.BaseApp;
import com.jkcq.hrwtv.http.bean.CourseUserInfo;
import com.jkcq.hrwtv.util.DeviceUtil;
import com.jkcq.hrwtv.util.LogUtil;
import com.jkcq.hrwtv.util.NetUtils;
import com.jkcq.hrwtv.wu.CourseUpdateMqttService;

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


/**
 * Desc         ${MQTT服务}
 */

public class UserInfoMqttService extends BaseMqttservice {

    public final String TAG = UserInfoMqttService.class.getSimpleName();

    private MqttConnectOptions mMqttConnectOptions;
    //public        String HOST           = "tcp://192.168.10.102:61613";//服务器地址（协议+地址+端口号）
    public String HOST = "tcp://mqtt.fitalent.com.cn:61613";//服务器地址（协议+地址+端口号）
    public String USERNAME = "admin";//用户名
    public String PASSWORD = "fitalent@1.";//密码


    //客户端ID，一般以客户端唯一标识符表示，这里用设备MAC表示
    public String CLIENTID = DeviceUtil.getMac(BaseApp.getApp()) + "userinfo";


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        String topic = intent.getStringExtra(PUBLISH_TOPIC_KEY);
        if (!TextUtils.isEmpty(topic)) {
            mPublisTopic = topic;
        }
        init();
        return new MyMqttServiceBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
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
        mCourseCallback=null;
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
    }

    /**
     * 连接MQTT服务器
     */
    private void doClientConnection() {
        if(!isEnd){
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

            Log.i(TAG, "topic=" + topic +" mPublisTopic="+mPublisTopic+ " 收到消息： " + new String(message.getPayload()));
            if(mPublisTopic.contains(topic)){
                parse(new String(message.getPayload()));
            }
            else if(topic.equals(CourseUpdateMqttService.MQTT_PULL_COURSE) ){
                mCourseCallback.update();
            }

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


    public void parse(String data) {
        try {
            Gson gson = new Gson();
            CourseUserInfo user = gson.fromJson(data, CourseUserInfo.class);
            if(mCourseCallback!=null){
                mCourseCallback.notify(user.getCourseBean().getCourseName(),user.getCourseBean().getCourseId());
            }
            Log.i(TAG, "\nuser=" + user.toString());
            if(user.getSn()==null){
                ToastUtils.showShort(user.getNickName()+" 的sn为空，请检查！");
            }
            else{
                LogUtil.e(TAG, "data= "+data);
                BaseApp.sUserInfoMap.put(user.getSn(), user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyMqttServiceBinder extends Binder {
        public UserInfoMqttService getCollectionServices() {
            return UserInfoMqttService.this;
        }
    }
    private CourseCallback mCourseCallback;
    public void setCourseCallback(CourseCallback callback){
        this.mCourseCallback=callback;
    }
    public interface CourseCallback{
        void notify(String courseName,String courseId);
        void update();
    }

}