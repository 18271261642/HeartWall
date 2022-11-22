package com.jkcq.hrwtv.app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.multidex.MultiDex;
import timber.log.Timber;

import com.jkcq.hrwtv.eventBean.EventConstant;
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean;
import com.jkcq.hrwtv.heartrate.bean.ShowBean;
import com.jkcq.hrwtv.http.bean.ClubInfo;
import com.jkcq.hrwtv.http.bean.CourseUserInfo;
import com.jkcq.hrwtv.service.OperateUserSnService;
import com.jkcq.hrwtv.util.ACache;
import com.jkcq.hrwtv.util.CacheDataUtil;
import com.jkcq.hrwtv.util.DeviceUtil;
import com.jkcq.hrwtv.util.JPushUtils;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.concurrent.ConcurrentHashMap;

// 该程序退出整个系统
public class BaseApp extends Application {

    private String TAG = BaseApp.class.getSimpleName();
    public static BaseApp instance;
    public static ACache aCache;
    private static final String BUGLY_KEY = "e1c67f6172";
    public static long receiveTime = 0L;
    public static ConcurrentHashMap<String, CourseUserInfo> sUserInfoMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Long> sUserStartTime = new ConcurrentHashMap<>();
    public static String DEVICE_NAME = "心率墙";
    public static String DEVICE_TYPE_ID = "";
    public static String DEVICE_TOKEN = "";
    public static ClubInfo sClubInfo;

    public static volatile int sSortType = EventConstant.SORT_DATA_CAL;
    public static volatile int sMainType = EventConstant.MAIN_DATA_HR_STRENGTH;

    //用于记录课程或PK模式已经超过2分钟的用户信息
    public static ConcurrentHashMap<String, DevicesDataShowBean> recordHashData = new ConcurrentHashMap<>();


    // 初始化Instance；
    public synchronized void setInstance() {
        if (instance == null) {
            instance = this;
            aCache = ACache.get(BaseApp.getApp());
        }
    }

    public static ACache getaCache() {
        return aCache;
    }

    public static BaseApp getApp() {
        return instance;
    }

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        BaseApp application = (BaseApp) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Timber.plant(new DebugLoggerTree());
//        if (BuildConfig.DEBUG) {
//            if (LeakCanary.isInAnalyzerProcess(this)) {
//                return;
//            } else {
//                LeakCanary.install(this);
//            }
//        }

        initApp();
        /**
         * 本地保存显示规则数据
         */
        // CacheDataUtil.clearAll();
        ShowBean showBean = CacheDataUtil.getDisplayRule();
//        initBaidu(); // 百度地图
//        // 加载友盟。
//        initUMeng();
//
        JPushUtils.getInstance().init(this);
        JPushUtils.getInstance().setAlias(this, DeviceUtil.getMac(this));
//        FileUtil.initFile(this);
//
        CrashReport.initCrashReport(getApplicationContext(), BUGLY_KEY, true);
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
    }

//    private void initLeakCanary() {
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
//    }

    /*
     * 初始化友盟。
     */
    private void initUMeng() {
        // 友盟分享
//        Config.DEBUG = false;// 开启debug模式，方便定位错误
////        Config.REDIRECT_URL = "http://sns.whalecloud.com";
//        QueuedWork.isUseThreadPool = false;
//        UMShareAPI.get(this);
    }

    private void initApp() {
        setInstance();

        Intent intent = new Intent(this, OperateUserSnService.class);
        bindService(intent,serviceConnection,Context.BIND_AUTO_CREATE);
    }

    private void initBaidu() {
//        SDKInitializer.initialize(getApplicationContext());
    }


    // UMENG 各个平台的配置，建议放在全局Application或者程序入口
    static {
        // 微信
//        PlatformConfig.setWeixin("wxe5eee7504a45f517", "d3d8841900a0002a919cb255d3772653");
//        // 新浪微博
//        PlatformConfig.setSinaWeibo("891334947", "196c1e1fabc54410f3a971e16c67d99b", "http://sns.whalecloud.com");
//        // QQ
//        PlatformConfig.setQQZone("1105670437", "HBlBoy5XSs7XZIR4");
    }


    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

}