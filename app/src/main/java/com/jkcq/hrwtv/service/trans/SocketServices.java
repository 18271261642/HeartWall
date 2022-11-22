package com.jkcq.hrwtv.service.trans;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 *
 *
 * @author mhj
 * Create at 2018/1/15 17:25
 */
public class SocketServices extends Service{


    private static final String Address = "192.168.10.15";
    private static final int port = 7397;
    private Queue<SocketPackets>  queue = null;
    private ExecutorService mThreadPool;
    // 单个CPU线程池大小
    public static final int POOL_SIZE = 5;


    private Socket socket = null;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;
    private DataInputStream dataInputStream = null;


    private static final long RECONNECT_DELAY_TIME = 1000*5;
    private Timer reConTimer = null;
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            try {
                if(socket==null||!socket.isConnected()){
                    socket = new Socket(Address,port);
                    socket.setKeepAlive(true);
                    socket.setTcpNoDelay(true);
                    socket.setReuseAddress(true);
                    Log.e("shao","--------reConnect-------TimerTask-----isConnect:"+socket.isConnected());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("shao","-----SimplyServices---------------onCreate--");
        init();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("shao","-------SimplyServices-------------onStartCommand--");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        disConnectSocket();
        if(reConTimer!=null){
            reConTimer.cancel();
        }
        Log.e("shao","-----SimplyServices---------------onDestroy--");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("shao","----SimplyServices----------------onBind--");
        return new SocketBinder();
    }

    public void init(){
        queue = new LinkedList<SocketPackets>();
        int cpuNumbers = Runtime.getRuntime().availableProcessors();
        // 根据CPU数目初始化线程池
        mThreadPool = Executors.newFixedThreadPool(cpuNumbers * 5);
        reConTimer = new Timer();
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(Address,port);
                    socket.setKeepAlive(true);
                    socket.setTcpNoDelay(true);
                    socket.setReuseAddress(true);
                    Log.e("shao","-----socket connect: "+socket.isConnected());
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    reConTimer.schedule(task,RECONNECT_DELAY_TIME,RECONNECT_DELAY_TIME);
                }
            }
        });

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (socket != null && socket.isConnected() && queue != null && !queue.isEmpty()) {
                            outputStream = socket.getOutputStream();
                            if (outputStream != null) {
                                SocketPackets packet = queue.poll();
                                if (packet != null) {
                                    // 步骤2：写入需要发送的数据到输出流对象中
                                    //outputStream.write(("PBJ0000000100010000END22".toString()).getBytes("utf-8"));
                                    Log.e("shao", "-----packet: " + packet.toString());
                                    outputStream.write((packet.toString()).getBytes());

                                    // 步骤3：发送数据到服务端
                                    outputStream.flush();
                                }
                            }
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        disConnectSocket();
                    }
                }
            }
        });

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (socket != null) {
                            inputStream = socket.getInputStream();
                            dataInputStream = new DataInputStream(inputStream);

                            // 步骤3：接收服务器发送过来的数据
//                                        byte[] b = new byte[10000];
//                                        int length = dataInputStream.read(b);
//                                        String Msg = new String(b, 0, length, "utf-8");

                            int length = getDataCount(dataInputStream);
                            byte[] b = new byte[length];
                            int l = dataInputStream.read(b);
                            if (l != -1) {
                                String Msg = new String(b, 0, length, "utf-8");
                                Log.i("TAGG", "收到数据" + Msg);
                            }

//                                        // 步骤4:通知主线程,将接收的消息显示到界面
//                                        Message message = new Message();
//                                        message.what = 0;
//                                        message.obj = Msg;
//                                        mMainHandler.sendMessage(message);
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                        disConnectSocket();
                    }
                }
            }
        });

    }

    //有多少读多少
//    public  byte[] readStream(DataInputStream inStream) throws IOException {
//        int count = 0;
//        while (count == 0) {
//            count = inStream.available();
//        }
//        byte[] b = new byte[count];
//        inStream.read(b);
//        return b;
//    }

    public int getDataCount(DataInputStream inStream) throws IOException{
        int count = 0;
        while (count == 0) {
            count = inStream.available();
        }
        return  count;
    }



    public class SocketBinder extends Binder{
        public SocketServices getSocketServices(){
            return SocketServices.this;
        }
    }


    public void disConnectSocket(){
        try {
            if(dataInputStream!=null){
                dataInputStream.close();
                dataInputStream = null;
            }
            if(inputStream!=null){
                inputStream.close();
                inputStream = null;
            }
            if(outputStream!=null){
                outputStream.close();
                outputStream = null;
            }
            if(socket!=null){
                socket.close();
                socket = null;
            }
            Log.e("shao","-----------close");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSocketPacket(SocketPackets packet){
        if(queue!=null){
            queue.add(packet);
        }
    }
}
