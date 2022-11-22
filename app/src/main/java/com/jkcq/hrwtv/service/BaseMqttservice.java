package com.jkcq.hrwtv.service;

import android.app.Service;
import android.content.Intent;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * created by wq on 2019/5/31
 */
public abstract class BaseMqttservice extends Service {


    public  static final String PUBLISH_TOPIC_KEY = "publish_topic";
    protected boolean isEnd = false;
    protected  MqttAndroidClient mqttAndroidClient;
    protected   String mPublisTopic = "";//发布主题
    public  String RESPONSE_TOPIC = "";//响应主题



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
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
     * other
     */
    protected boolean isTask = false;

    //protected OnHeartRateListener heartRateListener;
/*
    public void setOnHeartRateListener(OnHeartRateListener heartRateListeners) {
        heartRateListener = heartRateListeners;
    }*/

    public void setTask(boolean isTask) {
        this.isTask = isTask;
    }

    /**
     * 移除掉线的
     * @param sn
     */
    public  void removeOffLine(String sn){

    }

    public void release(){
      //  heartRateListener=null;
    }
}
