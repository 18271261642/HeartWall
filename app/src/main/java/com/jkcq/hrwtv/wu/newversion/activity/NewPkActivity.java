package com.jkcq.hrwtv.wu.newversion.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkcq.hrwtv.AllocationApi;
import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.app.BaseApp;
import com.jkcq.hrwtv.base.mvp.BaseMVPActivity;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean;
import com.jkcq.hrwtv.heartrate.bean.SecondHeartRateBean;
import com.jkcq.hrwtv.heartrate.bean.UserBean;
import com.jkcq.hrwtv.heartrate.model.MainActivityView;
import com.jkcq.hrwtv.heartrate.presenter.MainActivityPresenter;
import com.jkcq.hrwtv.http.bean.ClubInfo;
import com.jkcq.hrwtv.http.bean.CourseDetail;
import com.jkcq.hrwtv.http.bean.CourseInfo;
import com.jkcq.hrwtv.http.bean.CourseList;
import com.jkcq.hrwtv.http.bean.CourseUserInfo;
import com.jkcq.hrwtv.service.UserContans;
import com.jkcq.hrwtv.ui.view.BebasNeueTextView;
import com.jkcq.hrwtv.ui.view.ShowEmptyDialog;
import com.jkcq.hrwtv.util.CacheDataUtil;
import com.jkcq.hrwtv.util.DoubleClickUtil;
import com.jkcq.hrwtv.util.HeartRateConvertUtils;
import com.jkcq.hrwtv.util.Logger;
import com.jkcq.hrwtv.util.TimeUtil;
import com.jkcq.hrwtv.wu.newversion.view.CourseMatchPointView;
import com.jkcq.hrwtv.wu.newversion.view.CourseMatchView;
import com.jkcq.hrwtv.wu.newversion.view.HeartResultView;
import com.jkcq.hrwtv.wu.newversion.view.NewPkResultView;
import com.jkcq.hrwtv.wu.obsever.HrDataObservable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * PK页面
 * Created by Admin
 * Date 2022/1/4
 */
public class NewPkActivity extends BaseMVPActivity<MainActivityView, MainActivityPresenter>implements MainActivityView , View.OnClickListener {

    private final int UPDATE_HALL = 115;
    private final int REMOVE_DATA = 116;
    private final int ADD_DATA = 117;

    private ShowEmptyDialog showEmptyDialog;

    //展示当前时间
    private BebasNeueTextView tv_time;
    //会员名称
    private TextView tv_name;
    //在店人数
    private TextView tv_man_count;
    //添加人数
    private LinearLayout layout_select_user;
    //退出挑战
    private LinearLayout layout_setting;

    //进度条
    private ProgressBar new_pk_progress;
    //红队总的经验值
    private BebasNeueTextView newPkRedProgressTv;
    //蓝队总的经验值
    private BebasNeueTextView newPkBlueProgressTv;

    //红队列表
    private HeartResultView newPkRedView;
    //蓝队列表
    private HeartResultView newPkBlueView;

    //课程名称
    private TextView tv_course_name;
    //课程难度
    private TextView tv_course_leve;
    //剩余时间
    private BebasNeueTextView tv_remain_time;
    //进度条布局
    private LinearLayout rl_coure_bottom;
    private LinearLayout layout_coure_match;
    private CourseMatchPointView coursematchpoint_view;
    private CourseMatchView coursematch_view;


    //总页数
    private  int mTotalPage = 0;
    private int  currentPage = 1;
    private long  endTime = 0L;
    private  int mRemainTime = 0;
    private  int intervalTime = 0;

    private boolean isRestart;
    private List<DevicesDataShowBean> mDataShowBeans = new ArrayList<>();
    private final ArrayList<DevicesDataShowBean> mRedDataShowBeans = new ArrayList<DevicesDataShowBean>();
    private final ArrayList<DevicesDataShowBean> mBlueDataShowBeans = new  ArrayList<DevicesDataShowBean>();

    private final CopyOnWriteArrayList<DevicesDataShowBean> mRedCurrentShowBeans = new CopyOnWriteArrayList<DevicesDataShowBean>();
    private final CopyOnWriteArrayList<DevicesDataShowBean> mBlueCurrentShowBeans = new CopyOnWriteArrayList<DevicesDataShowBean>();


    //倒计时
    private CusPKCountDownTimer mCurrentDownTimer = null;

    private ConcurrentHashMap<String, SecondHeartRateBean> secondMap = new ConcurrentHashMap<>();
    private UserBean userInfo;

    private ConcurrentLinkedQueue<DevicesDataShowBean> mAddList = new ConcurrentLinkedQueue<>();
    private int mCurrentRemoveIndex = 0;
    private boolean isAddItem = false;

    private List<Integer> reMoveList = new ArrayList<>();

    private final Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int whatCode = msg.what;
            if(whatCode == UPDATE_HALL){
                Log.e(TAG,"-----UPDATE_ALL="+new Gson().toJson(mDataShowBeans));
                newPkBlueView.updateData(mBlueCurrentShowBeans,false,0);
            }


            if(whatCode == ADD_DATA){

                Log.e(TAG,"---111--ADD_DATA="+new Gson().toJson(mAddList));

                if (mAddList.size() > 0) {

                    DevicesDataShowBean addDevice = mAddList.poll();
                    boolean hasExsit = false;

                    if (mDataShowBeans.size() == 0) {
                        UserContans.classTime = addDevice.getJoinTime();
                    }

                    for(DevicesDataShowBean db : mDataShowBeans){
                        if(db.getDevicesSN().equals(addDevice.getDevicesSN())){
                            hasExsit = true;
                        }
                    }

                    if (!hasExsit) {
                        mDataShowBeans.add(addDevice);
                    }

                    Log.e(TAG,"---222--ADD_DATA="+new Gson().toJson(mDataShowBeans));
                    // heartresult_view.insertItem(mAddList.poll(), mDataShowBeans.size - 1)
                    // myMqttService!!.setTask(false)
                }
            }

            if(whatCode == REMOVE_DATA){

            }
        }
    };



    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_pk_layout;
    }

    @Override
    protected void initView() {
        findViews();


        //注册广播，显示当前时间
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(broadcastReceiver,intentFilter);

    }

    @Override
    protected void initData() {
        UserContans.classTime = 0;
        UserContans.isPause = false;
        HrDataObservable.getInstance().addObserver(this);
        //tv_name.setText(mclubName)
        DoubleClickUtil.getInstance().initHandler(new Handler(Looper.getMainLooper()));
        tv_man_count.setText(getString(R.string.people, "0"));
        updateViewTime();

        isRestart = getIntent().getBooleanExtra("isRestart", false);

        if (isRestart) {
            restartCourse();
        } else {
            showCourseModel();
            starCourse();
        }
        timerScroll();
    }



    private void timerScroll() {

    }

    private void starCourse() {
        mRemainTime = mCurrentHeartRateClassInfo.getDuration() * 1000;
        startDownTimer();
    }

    //需要考虑暂停时候的逻辑
    private void startDownTimer() {
        mCurrentDownTimer = new CusPKCountDownTimer((mCurrentHeartRateClassInfo.getDuration() ) * 1000L, mDuration);
        mCurrentDownTimer.start();

    }

    @Override
    protected MainActivityPresenter createPresenter() {
        return new MainActivityPresenter();
    }

    //获取俱乐部
    @Override
    public void getClubSuccess(ClubInfo info) {
        if (TextUtils.isEmpty(info.getUid())) {
            tv_name.setText("未知会所");
            //showSettingView();
        } else {
            tv_name.setText(info.getName());
        }
    }




    //获取课程列表
    @Override
    public void getCourseListSuccess(List<CourseList.CourseInfo> courseInfoList) {

    }

    @Override
    public void getAllUserSuccess(List<CourseUserInfo> userInfos) {

    }

    //上传数据成功
    @Override
    public void uploadAllDataSuccess() {

    }


    private void restartCourse(){
        List<DevicesDataShowBean> list = CacheDataUtil.getCourseUserList();

        Log.e(TAG,"-----已经选择的学员="+new Gson().toJson(list));

        if (list != null && list.size() > 0) {
            mDataShowBeans.addAll(list);
            dealPkData();
            // heartresult_view.updateData(mDataShowBeans, isSort, mSortType)
        }

        CourseInfo course = CacheDataUtil.getCourseInfo();
        if (course != null) {
            UserContans.info = course;
        }
        mRemainTime = CacheDataUtil.getRemainTime();
        showCourseModel();
        reStartDowTimer();
    }

    private long mDuration = 1000L;
    private void reStartDowTimer() {
        mCurrentDownTimer = new CusPKCountDownTimer((mCurrentHeartRateClassInfo.getDuration()  - (mCurrentHeartRateClassInfo.getDuration() * 1000 - mRemainTime) / 1000L) * 1000L,
                        mDuration );
        mCurrentDownTimer.start();
    }


    CourseInfo mCurrentHeartRateClassInfo;

    private int mCurrentRangeIndex = 0;
    private int mUnfinishedTime = 0;

    CourseDetail currentCourseDetail = null;
    CourseDetail lastCourseDetail = null;


    private void showCourseModel() {
        Log.e(TAG,"------课程信息="+UserContans.info.toString());
        mCurrentHeartRateClassInfo = UserContans.info;
        if (mCurrentHeartRateClassInfo == null) {
            return;
        }

        UserContans.couserTime = mCurrentHeartRateClassInfo.getDuration();
        tv_course_name.setText(mCurrentHeartRateClassInfo.getCourseName());
        tv_course_leve.setText( UserContans.getDifficultyLevel(mCurrentHeartRateClassInfo.getDifficultyLevel()));
        rl_coure_bottom.setVisibility(View.VISIBLE);


        if (mCurrentHeartRateClassInfo.getTargetRateArray() == null || mCurrentHeartRateClassInfo.getTargetRateArray().size() == 0) {
            mCurrentHeartRateClassInfo.addTargetRateArray(
                    new CourseDetail(0,mCurrentHeartRateClassInfo.getDuration(),10));
            layout_coure_match.setVisibility(View.GONE);
        } else {
            layout_coure_match.setVisibility(View.VISIBLE);
        }
        coursematch_view.setValue(mCurrentHeartRateClassInfo.getDuration(),mCurrentHeartRateClassInfo.getTargetRateArray());
        coursematchpoint_view.setValue(UserContans.info);
    }


    float redSumCal = 0;
    float blueSumCal = 0;


    private void dealPkData() {
        int size = mDataShowBeans.size() / 10;
        if (mDataShowBeans.size() % 10 != 0) {
            size += 1;
        }
        mTotalPage = size;

        Logger.e("doCommonHRTask", sn + "dealPkData=" + mRedDataShowBeans.size() + "mBlueDataShowBeans=" + mBlueDataShowBeans.size() + ",redSumCal=" + redSumCal + "blueSumCal=" + blueSumCal);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRedDataShowBeans.size() > 0) {
                    Collections.sort(mRedDataShowBeans, new Comparator<DevicesDataShowBean>() {
                        @Override
                        public int compare(DevicesDataShowBean o1, DevicesDataShowBean o2) {
                            return o2.getRanking();
                        }
                    });

                }
                if (mBlueDataShowBeans.size() > 0) {
                    Collections.sort(mBlueDataShowBeans, new Comparator<DevicesDataShowBean>() {
                        @Override
                        public int compare(DevicesDataShowBean o1, DevicesDataShowBean o2) {
                            return 0;
                        }
                    });
                }
                mRedCurrentShowBeans.clear();
                mBlueCurrentShowBeans.clear();

                int startRedIndex = (currentPage - 1) * 5;
                int endRedIndex = currentPage * 5;
                if (endRedIndex >= mRedDataShowBeans.size()) {
                    endRedIndex = mRedDataShowBeans.size();
                }

                for(int i = startRedIndex;i<endRedIndex;i++){
                    mRedCurrentShowBeans.add(mRedDataShowBeans.get(i));
                }

//                for (i in startRedIndex until endRedIndex) {
//                    mRedCurrentShowBeans.add(mRedDataShowBeans.get(i))
//                }
                int startBlueIndex = (currentPage - 1) * 5;
                int endBlueIndex = currentPage * 5;
                if (endBlueIndex >= mBlueDataShowBeans.size()) {
                    endBlueIndex = mBlueDataShowBeans.size();
                }

                for(int k = startBlueIndex;k<endBlueIndex;k++){
                    mBlueCurrentShowBeans.add(mBlueDataShowBeans.get(k));
                }

//                for (i in startBlueIndex until endBlueIndex) {
//                    mBlueCurrentShowBeans.add(mBlueDataShowBeans.get(i))
//                }

                Log.e(TAG,"----红队="+new Gson().toJson(mRedCurrentShowBeans)+"\n"+"蓝队="+new Gson().toJson(mBlueCurrentShowBeans));
                newPkBlueView.updateData(mBlueCurrentShowBeans,false,0);


                newPkRedView.updateData(mRedCurrentShowBeans,false,0);

                for(int i = 0;i<mRedCurrentShowBeans.size();i++){

                }


                newPkRedProgressTv.setText(HeartRateConvertUtils.doubleParseStr(redSumCal));
                newPkBlueProgressTv.setText(HeartRateConvertUtils.doubleParseStr(blueSumCal));
                int redIntSumCal = (int) redSumCal;
                int blueIntSumCal = (int) blueSumCal;
                int sumcal = redIntSumCal + blueIntSumCal;
                if (redIntSumCal == blueIntSumCal) {
                    new_pk_progress.setMax(100);
                    new_pk_progress.setProgress(50);
                } else {
                    new_pk_progress.setMax(sumcal);
                    new_pk_progress.setProgress(redIntSumCal);
                }

//                //进度条更新
//                for (i in mRedCurrentShowBeans.size until 5) {
//                    redViewlist.get(i)!!.visibility = View.INVISIBLE
//                }
//                for (i in mBlueCurrentShowBeans.size until 5) {
//                    blueViewList.get(i)!!.visibility = View.INVISIBLE
//                }
            }
        });
    }


    private void findViews(){
        tv_time = findViewById(R.id.tv_time);
        tv_name = findViewById(R.id.tv_name);
        tv_man_count = findViewById(R.id.tv_man_count);
        layout_select_user = findViewById(R.id.layout_select_user);
        layout_setting = findViewById(R.id.layout_setting);
        newPkRedProgressTv = findViewById(R.id.newPkRedProgressTv);
        newPkBlueProgressTv = findViewById(R.id.newPkBlueProgressTv);
        newPkRedView = findViewById(R.id.newPkRedView);
        newPkBlueView = findViewById(R.id.newPkBlueView);
        tv_course_name = findViewById(R.id.tv_course_name);
        tv_course_leve = findViewById(R.id.tv_course_leve);
        tv_remain_time = findViewById(R.id.tv_remain_time);
        rl_coure_bottom = findViewById(R.id.rl_coure_bottom);
        layout_coure_match = findViewById(R.id.layout_coure_match);
        coursematchpoint_view = findViewById(R.id.coursematchpoint_view);
        coursematch_view = findViewById(R.id.coursematch_view);
        new_pk_progress= findViewById(R.id.new_pk_progress);

        layout_select_user.setVisibility(View.VISIBLE);
        layout_select_user.setOnClickListener(this);
        layout_setting.setOnClickListener(this);
    }


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action == null)
                return;
            if(action.equals(Intent.ACTION_TIME_TICK))
                updateViewTime();
        }
    };

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void updateViewTime(){
        Calendar calendar = Calendar.getInstance();
        tv_time.setText(String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY)
                ) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)));
    }


    private  final class CusPKCountDownTimer extends CountDownTimer {

        public CusPKCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            endTime = System.currentTimeMillis();
            doHallModel(UserContans.mSnHrMap);
            //已经走了的时间
            long moverTime = 0;

            Log.e(TAG,"millisInFuture=" + millisUntilFinished / 1000 + " millisUntilFinished=" + millisUntilFinished / 1000 + "mRemainTime/1000=" + mRemainTime / 1000);

            mUnfinishedTime = (int) millisUntilFinished;

            int remove = UserContans.couserTime  - mRemainTime / 1000;

            /**
             * 小米盒子最后一秒不会调用
             */
            if (currentCourseDetail != null && remove >= currentCourseDetail.getBegin() && remove <= currentCourseDetail.getEnd()) {
                moverTime = (currentCourseDetail.getEnd() - remove);
                coursematchpoint_view.setStrTime(TimeUtil.getFormatTimemmss(moverTime * 1000L));
            } else {

                List<CourseDetail> targetRangeArray = mCurrentHeartRateClassInfo.getTargetRateArray();
                for(CourseDetail cd : targetRangeArray){
                    if(remove >=cd.getBegin() && remove <= cd.getEnd()){
                        //移动的时间
                        currentCourseDetail = cd;
                        CacheDataUtil.mCurrentRange = cd.getTargetRange();
                        moverTime = (cd.getEnd() - remove);
                        coursematchpoint_view.setStrTime(TimeUtil.getFormatTimemmss(moverTime * 1000L));
                    }
                }

            }
            if (millisUntilFinished / 1000 != 0L) {
                updateCourseView(millisUntilFinished);
            }


        }

        @Override
        public void onFinish() {
            endTime = System.currentTimeMillis();
            if (mRemainTime / 1000 == 0) {
                //把数据清零
                doHallModel(UserContans.mSnHrMap);
            }


            showEmptyDialog = new ShowEmptyDialog(NewPkActivity.this);
            showEmptyDialog.show();
            showEmptyDialog.setShowTime(3 * 1000);
            showEmptyDialog.setContentTvTxt("挑战结束");
            showEmptyDialog.setOnEmptyDialogListener(new ShowEmptyDialog.OnEmptyDialogListener() {
                @Override
                public void autoDismiss() {
                    showEmptyDialog.dismiss();
                    UserContans.isPause = true;
                    mCurrentDownTimer.cancel();
                    upgradeCourseData();
                }
            });
        }
    }



    long mStateControlTime = 0L;
    private void updateCourseView(Long millisUntilFinished) {
        //LogUtil.e("mStateControlTime=" + mStateControlTime + " millisUntilFinished= " + millisUntilFinished + "mRemainTime=" + mRemainTime)
        if (mStateControlTime == millisUntilFinished / 1000)
            return;
        mStateControlTime = millisUntilFinished / 1000;
        tv_remain_time.setText(TimeUtil.getFormatTime(mRemainTime - 1000L));
        //coursematchpoint_view.setStrTime(TimeUtil.getFormatTimemmss(millisUntilFinished))
        coursematchpoint_view.moveArrow((mRemainTime / 1000));
        //  tv_range_remain_time.text = TimeUtil.getFormatTimemmss(millisUntilFinished)
        mRemainTime = (int) (mRemainTime - mDuration);
        CacheDataUtil.savemRemainTime(mRemainTime);
        //结束上报数据
//        coursematch_view.moveArrow(mRemainTime / 1000)
    }


    private boolean AllData = false;


    String winGroup = "0";

    private void upgradeCourseData() {
        mCurrentDownTimer.cancel();
        Logger.e("upgradeCourseData--------------------------------------");
        if (endTime != 0L) {
            endTime = System.currentTimeMillis();
        }
        AllData = true;

        //取消标签
       // markSnListData();

        mActPresenter.postPk(mRedDataShowBeans, mBlueDataShowBeans,UserContans.info.getId(),
                "",
                "2",
                endTime, redSumCal, blueSumCal
        );
    }


    int age = 0;
    float weight = 0f;
    String gander = "";
    SecondHeartRateBean heartRateBean = null;
    int heartRate = 0;
    double cal= 0.0;
    DevicesDataShowBean dataShowBean = null;
    int dataSize = 0;

    private void doHallModel(ConcurrentHashMap<String, Integer> sources) {
        Log.e(TAG,"--------发送过来的原始数据="+sources.toString());
        //开始的时间
        //查询用户信息
        doCommonHRTask(sources);
        String key = "";
        int pkMode = 0;

        for(Map.Entry<String, SecondHeartRateBean> mp : secondMap.entrySet()){
            key = mp.getKey();
            userInfo = mp.getValue().getUserInfo();

            if (userInfo == null && UserContans.userInfoHashMap.containsKey(key)) {
                userInfo = UserContans.userInfoHashMap.get(key);
            }

            if (userInfo != null) {
                age = userInfo.getAge();
                weight = userInfo.getWeight();
                gander = userInfo.getSex();
                pkMode = userInfo.getCurrentMod();
            }

            //先计算出值
            // 刷新Adapter,position = i；
            heartRateBean = secondMap.get(key);
            //一秒钟的心率值
            heartRate = heartRateBean.getHeart();
            if (heartRate < 30) {
                heartRate = 0;
            }


            for(DevicesDataShowBean db : mDataShowBeans){
                if(db.getDevicesSN().equals(key))
                    dataShowBean = db;
            }

            if (dataShowBean != null) {
                dataShowBean.setLiveHeartRate(heartRate);
                dataShowBean.setAge(age);
                dataShowBean.setWeight(weight);
                dataShowBean.setSex(gander);
                if (userInfo != null) {
                    dataShowBean.setHeight(userInfo.getHeight());// = userInfo!!.height
                    dataShowBean.setHeadUrl(userInfo.getAvatar());//= userInfo!!.avatar
                    dataShowBean.setNikeName(userInfo.getNickname());// = userInfo!!.nickname
                    dataShowBean.setUserId(userInfo.getId());// = userInfo!!.id
                }
                //结果界面只更新实时心率字段
                dataShowBean.setTime(heartRateBean.getTime());// = heartRateBean!!.time
                int precent = HeartRateConvertUtils.hearRate2Percent(
                        heartRate,
                        HeartRateConvertUtils.getMaxHeartRate(age));
                dataShowBean.setPrecent(String.valueOf(precent));// = precent.toString()

                if (heartRate == 0) {
                    dataShowBean.addStageHeart(key,-1);
                } else {
                    dataShowBean.addStageHeart(key,precent);
                }
                dataShowBean.setPkTeam(pkMode);
                dataShowBean.setAllHrList(heartRate);

                Log.e("heartRate", "" + heartRate + "precent=" + precent);
                //计算每一分钟的数据

                if (dataShowBean.getAllHrList().size() == 30 / Constant.REFRESH_RATE) {
                    dataShowBean.setCalAllHrList(dataShowBean.getAllHrList());// = dataShowBean!!.allHrList
                    dataShowBean.getAllHrList().clear();

                    int minHr = 0;
                    int sum = 0;
                    int size = 0;

                    for(Integer it : dataShowBean.getCalAllHrList()){
                        if(it > 30){
                            size++;
                            sum +=it;
                        }
                    }

                    if (sum == 0) {
                        minHr = 0;
                    } else {
                        minHr = sum / size;
                    }
                    dataShowBean.addMinHrList(minHr);

                    if (gander.equals("1")) {
                        cal = HeartRateConvertUtils.hearRate2CaloriForMan(
                                heartRate,
                                age,
                                weight,
                                Constant.REFRESH_RATE,
                                Constant.UNIT_MILLS
                        );
                    } else {
                        cal = HeartRateConvertUtils.hearRate2CaloriForWoman(
                                heartRate,
                                age,
                                weight,
                                Constant.REFRESH_RATE,
                                Constant.UNIT_MILLS
                        );
                    }
                    if (cal < 0) {
                        cal = 0.0;
                    }
                    dataShowBean.setCal(cal);
                }
            }else {
                dataShowBean = new DevicesDataShowBean();

                //判断是否已经再线过
                boolean isSaved = BaseApp.recordHashData.containsKey(key);
                if(isSaved){
                    dataShowBean.setJoinTime(BaseApp.recordHashData.get(key).getJoinTime());// = BaseApp.recordHashData[key]?.joinTime
                    //掉线时间
                    dataShowBean.setTime(BaseApp.recordHashData.get(key).getTime());// = BaseApp.recordHashData[key]?.time!!
                    dataShowBean.setCal(BaseApp.recordHashData.get(key).getCal());
                    //平均心率强度
                    dataShowBean.setAverageHeartPercent(BaseApp.recordHashData.get(key).getAverageHeartPercent());
                    dataShowBean.setPrecent(BaseApp.recordHashData.get(key).getPrecent());
                    int precent2 = HeartRateConvertUtils.hearRate2Percent(
                            heartRate,
                            HeartRateConvertUtils.getMaxHeartRate(age));
                    dataShowBean.setCal(BaseApp.recordHashData.get(key).getCal());
                    dataShowBean.addStageHeart(key,precent2);

                    for(Map.Entry<String,DevicesDataShowBean> bb : BaseApp.recordHashData.entrySet()){
                        dataShowBean.setAllHrList(bb.getValue().getAllHrList());
                    }

                    List<CourseDetail> courseList = BaseApp.recordHashData.get(key).getmDatas();
                    dataShowBean.setmDatas(courseList);

                }else{
                    dataShowBean.setJoinTime(System.currentTimeMillis());
                    dataShowBean.setCal(cal);
                    dataShowBean.addStageHeart(key,0);
                    dataShowBean.setTime(heartRateBean.getTime());
                    //设置课程id
                    dataShowBean.setPrecent(HeartRateConvertUtils.hearRate2Percent(
                            heartRate,
                            HeartRateConvertUtils.getMaxHeartRate(age))+"");
                }
                dataShowBean.setSortType(Constant.TYPE_DEF);
                dataShowBean.setPkTeam(pkMode);
                dataShowBean.setAge(age);
                dataShowBean.setWeight(weight);
                dataShowBean.setSex(gander);

                if (userInfo != null) {
                    dataShowBean.setHeight(userInfo.getHeight());
                    dataShowBean.setHeadUrl(userInfo.getAvatar());//!!.headUrl = userInfo!!.avatar
                    dataShowBean.setNikeName(userInfo.getNickname());//!!.nikeName = userInfo!!.nickname
                    dataShowBean.setUserId(userInfo.getId());//!!.userId = userInfo!!.id
                }

                dataShowBean.setLiveHeartRate(heartRate);
                dataShowBean.setDevicesSN(key);

                if (cal < 0) {
                    cal = 0.0;
                }

                //保存SN
                AllocationApi.getAllSNSet().add(key);
                mAddList.offer(dataShowBean);
                handler.sendEmptyMessage(ADD_DATA);
                isAddItem = true;
            }

        }


        //每10s刷新数据
        dataSize = mDataShowBeans.size();
        handler.post(new Runnable() {
            @Override
            public void run() {
              tv_man_count.setText(getString(R.string.people, "" + dataSize));
            }
        });

        Logger.e("doCommonHRTask", sn + "dataSize=" + dataSize);
        //需要对数据进行分类
        if (dataSize > 0) {
            intervalTime += Constant.REFRESH_RATE;
            Logger.e("doCommonHRTask", "doHallTask-----------");
            doHallTask();
            Logger.e("doCommonHRTask", "dealPkData-----------");
          //  CacheDataUtil.saveCourseUserBean(mDataShowBeans);
            dealPkData();
            /* if (intervalTime * Constant.REFRESH_RATE % 10 != 0) {
                 mHandler.sendEmptyMessage(UPDATE_HALL)
             } else if (intervalTime * Constant.REFRESH_RATE % 10 == 0) {

             }//每10s全部刷新一次*/
        }


    }


    long time = 0L;
    boolean isRemove = false;

    private void doHallTask() {

        try {
            //判断设备是否掉线，掉线的移除
            long currentTime = System.currentTimeMillis();
            //  var isRemove = false
            //接收数据的时间
            reMoveList.clear();
            isRemove = false;

            mRedDataShowBeans.clear();
            mBlueDataShowBeans.clear();
            redSumCal = 0.0f;
            blueSumCal = 0.0f;
            Log.e(TAG,"--------mDataShowBeans="+new Gson().toJson(mDataShowBeans));
            for (int i = 0; i < mDataShowBeans.size(); i++) {
                DevicesDataShowBean dropBean = mDataShowBeans.get(i);
                if (dropBean.getPkTeam() == Constant.MODE_PK_RED) {
                    redSumCal += dropBean.getCal();
                    mRedDataShowBeans.add(dropBean);
                } else {
                    blueSumCal += dropBean.getCal();
                    mBlueDataShowBeans.add(dropBean);
                }

                //已经掉线，直接移除
                sn = dropBean.getDevicesSN();
                if (UserContans.mSnHrTime.containsKey(sn)) {
                    time = UserContans.mSnHrTime.get(sn);
                    if (currentTime - time >= Constant.DROP_TIME) {
                        UserContans.mSnHrMap.put(sn, 0);
                    }
                }

                //如果两分钟没有数据，就把sn值的
                Logger.e("drop", "currentTime - time=" + (currentTime - time));
                if (UserContans.userInfoHashMap.containsKey(sn)) {
                    if (!UserContans.userInfoHashMap.get(sn).isSelect()) {
                        isRemove = true;
                        reMoveList.add(i);
                        AllocationApi.getAllSNSet().remove(dropBean.getDevicesSN());
                    }
                }
                if (isRemove) {
                    handler.sendEmptyMessage(REMOVE_DATA);
                } else {
                    handler.sendEmptyMessage(UPDATE_HALL);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    String sn = "";
    int  hrValue = 0;
    private List<Integer> recordHeartList = new ArrayList<>();

    private void doCommonHRTask(ConcurrentHashMap<String, Integer> sources) {
        for(Map.Entry<String,Integer> mp : sources.entrySet()){
          sn = mp.getKey();
          hrValue = mp.getValue();
            if (!UserContans.userInfoHashMap.containsKey(sn)) {
                if (!UserContans.mCacheMap.containsKey(sn)) {
                    //1.查询用户信息
                }
            }else{
                if (UserContans.userInfoHashMap.containsKey(sn)) {
                    //这里有两个逻辑，一个是红队的用户，一个蓝队的用户  数据选择选择了就不要弄了
                    boolean select = UserContans.userInfoHashMap.get(sn).isSelect();
                    Log.e(TAG, sn + "---select=+" + select);
                    if (select) {
                        //判断是否是已经存在过，即已经上过课程，中途给停掉课程，再次上课
                        boolean isExist = BaseApp.recordHashData.containsKey(sn);

                        SecondHeartRateBean secondHeartRateBean = UserContans.secondHeartRateBeanHashMap.containsKey(sn) ?  UserContans.secondHeartRateBeanHashMap.get(sn) : new SecondHeartRateBean();

                        if(isExist){
                            recordHeartList.clear();
                            recordHeartList.addAll( Objects.requireNonNull(BaseApp.recordHashData.get(sn)).getAllHrList());
                            recordHeartList.add(hrValue);
                            assert secondHeartRateBean != null;
                            secondHeartRateBean.getHeartList().addAll(recordHeartList);
                        }else{
                            assert secondHeartRateBean != null;
                            secondHeartRateBean.getHeartList().add(hrValue);
                        }

                        secondHeartRateBean.setDevicesSN(sn);
                        secondHeartRateBean.setHeart(hrValue);
                        secondHeartRateBean.setTask(false);
                        secondHeartRateBean.setTime(isExist ? Objects.requireNonNull(BaseApp.recordHashData.get(sn)).getJoinTime() : UserContans.mSnHrTime.get(sn));
                        UserContans.secondHeartRateBeanHashMap.put(sn, secondHeartRateBean);


                    } else {
                        if (UserContans.secondHeartRateBeanHashMap.containsKey(sn)) {
                            UserContans.secondHeartRateBeanHashMap.remove(sn);
                        }
                    }

                }
            }

            Log.e(TAG,"----已处理="+UserContans.secondHeartRateBeanHashMap.toString());
        }

        secondMap.clear();
        secondMap.putAll(UserContans.secondHeartRateBeanHashMap);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.layout_select_user){   //添加人数
            Intent intent = new Intent(this, UserSelectActivity.class);
            intent.putExtra("firstCome", false);
            intent.putExtra("currentMode", Constant.MODE_PK_RED);
            startActivity(intent);
        }

        if(v.getId() == R.id.layout_setting){   //退出挑战

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
