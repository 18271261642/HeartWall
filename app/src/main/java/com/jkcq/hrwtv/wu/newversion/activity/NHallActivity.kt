package com.jkcq.hrwtv.wu.newversion.activity

import android.annotation.SuppressLint
import android.content.*
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.annotation.RequiresApi
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import com.google.gson.Gson
import com.jkcq.hrwtv.AllocationApi
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.configure.Constant
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean
import com.jkcq.hrwtv.heartrate.bean.SecondHeartRateBean
import com.jkcq.hrwtv.heartrate.bean.UserBean
import com.jkcq.hrwtv.heartrate.model.MainActivityView
import com.jkcq.hrwtv.heartrate.presenter.MainActivityPresenter
import com.jkcq.hrwtv.http.bean.ClubInfo
import com.jkcq.hrwtv.http.bean.CourseList
import com.jkcq.hrwtv.http.bean.CourseUserInfo
import com.jkcq.hrwtv.service.UserContans
import com.jkcq.hrwtv.ui.view.DialogYesOrNo
import com.jkcq.hrwtv.util.*
import com.jkcq.hrwtv.wu.newversion.view.HeartResultView
import com.jkcq.hrwtv.wu.obsever.HrDataObservable
import com.jkcq.hrwtv.wu.util.MatchUtils
import com.jkcq.hrwtv.wu.util.ShapeUtil
import kotlinx.android.synthetic.main.activity_flash.*
import kotlinx.android.synthetic.main.activity_nhall.*
import kotlinx.android.synthetic.main.layout_heartresult_view.*
import kotlinx.android.synthetic.main.ninclude_title.*
import kotlinx.android.synthetic.main.ninclude_title.layout_setting
import kotlinx.android.synthetic.main.ninclude_title.tv_name
import kotlinx.android.synthetic.main.ninclude_title.tv_time
import timber.log.Timber
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CopyOnWriteArrayList

//大厅模式页面
class NHallActivity : AbsNewHeartResultActivity(), MainActivityView {

    private val tags = "NHallActivity"


    private var intervalTime = 0
    var reMoveList = mutableListOf<Int>()

    //  private var myMqttService: BaseMqttservice? = null
    private val UPDATE_HALL = 115
    private val REMOVE_DATA = 116;
    private val ADD_DATA = 117;

    private var mAddList = ConcurrentLinkedQueue<DevicesDataShowBean>()
    private var removemAddList = ArrayList<DevicesDataShowBean>()
    private var mCurrentRemoveIndex = 0
    private var isAddItem = false;

    private val secondMap = ConcurrentHashMap<String, SecondHeartRateBean>()

    //    var dataShowBeans = ArrayList<DevicesDataShowBean>()
    var mDataShowBeans = CopyOnWriteArrayList<DevicesDataShowBean>();
    private val mHandler = object : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_HALL -> {
                    //原意是只更新改变的数据，但实际上更新了所有数据
                    heartresult_view.updateData(mDataShowBeans, isSort, mSortType)
                    // myMqttService!!.setTask(false)
                }
                ADD_DATA -> {
                    if (mAddList.size > 0) {
                        heartresult_view.insertItem(mAddList.poll(), mDataShowBeans.size - 1)
                        // myMqttService!!.setTask(false)
                    }

                }
                REMOVE_DATA -> {
                    removemAddList.clear()
                    reMoveList.forEach {
                        if(mDataShowBeans.size>it){
                            removemAddList.add(mDataShowBeans[it])
                            heartresult_view.removeItem(it)
                        }

                    }
                    //掉线需要上传数据
                    mActPresenter.postHwall(removemAddList, "0")
                    // myMqttService!!.setTask(false)
                }
            }
        }
    }

    override fun getClubSuccess(info: ClubInfo?) {
        if (TextUtils.isEmpty(info?.getUid())) {
            tv_name.text = "未知会所"
            //showSettingView();
        } else {
            tv_name.text = info?.name
        }
    }


    //课程模式
    override fun getCourseListSuccess(courseInfoList: MutableList<CourseList.CourseInfo>?) {
    }

    //课程模式
    override fun getAllUserSuccess(userInfos: MutableList<CourseUserInfo>?) {
    }

    //课程模式
    override fun uploadAllDataSuccess() {

        if (AllData) {
            finish()
        }

    }


    var AllData = false;

    override fun createPresenter(): MainActivityPresenter {
        return MainActivityPresenter()
    }

    override fun getLayoutId(): Int = R.layout.activity_nhall
    override fun initView() {
        super.initView()

        layout_setting.nextFocusLeftId = R.id.layout_setting
        layout_setting.nextFocusRightId = R.id.layout_setting
        layout_setting.nextFocusDownId = R.id.recyclerview_hall
        layout_setting.nextFocusUpId = R.id.layout_setting

        recyclerview_hall.nextFocusUpId = R.id.layout_setting

        mDataShowBeans = heartresult_view.getAdapterData()

        layout_setting.setOnClickListener {
            back()
        }

        ShapeUtil.setColorRec(
            tv_one,
            this.resources.getColor(com.jkcq.hrwtv.base.R.color.sport_state_one),
            this.resources.getDimension(com.jkcq.hrwtv.base.R.dimen.dp15)
        )
        ShapeUtil.setColorRec(
            tv_two,
            this.resources.getColor(com.jkcq.hrwtv.base.R.color.sport_state_two),
            this.resources.getDimension(com.jkcq.hrwtv.base.R.dimen.dp15)
        )
        ShapeUtil.setColorRec(
            tv_point,
            this.resources.getColor(com.jkcq.hrwtv.base.R.color.sport_state_three),
            this.resources.getDimension(com.jkcq.hrwtv.base.R.dimen.dp15)
        )
        ShapeUtil.setColorRec(
            tv_four,
            this.resources.getColor(com.jkcq.hrwtv.base.R.color.sport_state_four),
            this.resources.getDimension(com.jkcq.hrwtv.base.R.dimen.dp15)
        )
        ShapeUtil.setColorRec(
            tv_five,
            this.resources.getColor(com.jkcq.hrwtv.base.R.color.sport_state_five),
            this.resources.getDimension(com.jkcq.hrwtv.base.R.dimen.dp15)
        )
        ShapeUtil.setColorRec(
            tv_six,
            this.resources.getColor(com.jkcq.hrwtv.base.R.color.sport_state_six),
            this.resources.getDimension(com.jkcq.hrwtv.base.R.dimen.dp15)
        )
    }

    fun showDialog() {
        DialogYesOrNo(this, object : DialogYesOrNo.OnButtonClick {
            override fun onButtonClickCancel() {
                UserContans.isPause = false
            }

            override fun onButtonClickSure() {
                UserContans.isPause = true;
                AllData = true;
                removemAddList.clear()
                mDataShowBeans.forEach {
                    removemAddList.add(it)
                }
                mActPresenter.postHwall(removemAddList, "0")
            }
        }, "确认退出大厅模式？").show()
    }

    fun back() {
        showDialog()
    }

    override fun initData() {
        UserContans.isPause = false
        UserContans.couserTime = 0
        CacheDataUtil.mCurrentRange = 10
        UserContans.couserTime = 60 * 60 / Constant.REFRESH_RATE
        HrDataObservable.getInstance().addObserver(this)
        tv_name.text = mclubName
        DoubleClickUtil.getInstance().initHandler(Handler(Looper.getMainLooper()))
        tv_man_count.text = getString(R.string.people, "0")
        updateTime()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_TIME_TICK)
        registerReceiver(mTimeReceiver, filter)
    }

    /**
     * 系统的一个广播，每分钟会发送一个广播
     */
    var mTimeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_TIME_TICK) {
                updateTime()
            }
        }
    }

    /**
     * 更新标题栏的时间显示
     */
    @SuppressLint("SetTextI18n")
    private fun updateTime() {
        val calendar = Calendar.getInstance()
        tv_time.text = String.format(
            "%02d",
            calendar.get(Calendar.HOUR_OF_DAY)
        ) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE))
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.e("onDestroy", "NHallActivity onDestroy------")

        HrDataObservable.getInstance().deleteObserver(this)
        unregisterReceiver(mTimeReceiver)
        /*AllocationApi.getAllSNSet().clear()
        UserContans.clearMap()*/
        mCurrentRemoveIndex = 0
        heartresult_view.release()
    }

    override fun getHeartResultView(): HeartResultView {
        return heartresult_view;
    }


    override fun update(o: Observable?, arg: Any?) {
        super.update(o, arg)
        if (UserContans.isPause) {
            return
        }
        if (o is HrDataObservable) {
            doHallModel(arg as ConcurrentHashMap<String, Int>)
        }
    }

    var userInfo: UserBean? = null
    var age = 0
    var weight = 0f
    var gander = ""
    var heartRateBean: SecondHeartRateBean? = null
    var heartRate = 0
    var cal: Double = 0.0
    var dataShowBean: DevicesDataShowBean? = null
    var dataSize = 0

    @RequiresApi(Build.VERSION_CODES.N)
    private fun doHallModel(
        sources: ConcurrentHashMap<String, Int>
    ) {
        Timber.e("-------大厅模式接收的原始数据="+sources)

        //查询用户信息
        doCommonHRTask(sources)

        dataSize = mDataShowBeans.size
        mHandler.post(Runnable {
            heartresult_view.isNoDevice(dataSize < 1)
            tv_man_count.setText(getString(R.string.people, "" + dataSize))
        })
        //最新1秒钟的数据，1秒内接收到的所有设备的所有心率 已经获取到心率数据和用户数据的 map
        var key = ""
        secondMap.forEach {
            key = it.key
            userInfo = secondMap[key]!!.userInfo
            if (userInfo == null && UserContans.userInfoHashMap.containsKey(key)) {
                userInfo = UserContans.userInfoHashMap[key]
            }
            if (userInfo != null) {
                age = userInfo!!.age
                weight = userInfo!!.weight
                gander = if(userInfo!!.sex == null ) "1" else userInfo!!.sex
            }
            //先计算出值
            // 刷新Adapter,position = i；
            heartRateBean = secondMap[key]
            //一秒钟的心率值
            heartRate = heartRateBean!!.heart


            dataShowBean = mDataShowBeans.findLast {
                it.devicesSN == key
            }
            if (dataShowBean != null) {
                dataShowBean!!.liveHeartRate = heartRate
                dataShowBean!!.age = age
                dataShowBean!!.weight = weight
                dataShowBean!!.sex = gander
                if (userInfo != null) {
                    dataShowBean!!.height = userInfo!!.height
                    dataShowBean!!.headUrl = userInfo!!.avatar
                    dataShowBean!!.nikeName = userInfo!!.nickname
                    dataShowBean!!.userId = userInfo!!.id

                }
                //结果界面只更新实时心率字段

                if (!AllocationApi.isTaskResult) {
                    var precent = HeartRateConvertUtils.hearRate2Percent(
                        heartRate,
                        HeartRateConvertUtils.getMaxHeartRate(age).toDouble()
                    ).toInt()
                    dataShowBean!!.time = heartRateBean!!.time
                    dataShowBean!!.precent = precent.toString()

                    dataShowBean!!.addStageHeart(key, precent)
                    dataShowBean!!.setAllHrList(heartRate)
                    /**
                     * 计算每一分钟的数据 60  改成30S计算一次
                     */

                    if (dataShowBean!!.allHrList.size == 30 / Constant.REFRESH_RATE) {
                        dataShowBean!!.calAllHrList = dataShowBean!!.allHrList
                        dataShowBean!!.allHrList.clear()
                        var minHr = 0
                        var sum = 0
                        var size = 0
                        dataShowBean!!.calAllHrList.forEach {
                            if (it > 30) {
                                size++
                                sum += it
                            }
                        }
                        if (sum == 0) {
                            minHr = 0
                        } else {
                            minHr = sum / size
                        }
                        dataShowBean!!.addMinHrList(minHr)

                        if (gander.equals("1")) {
                            Log.e(
                                "上传数据:",
                                "一分钟数据整合 Male=" + dataShowBean!!.minHrList
                            )

                            cal = HeartRateConvertUtils.hearRate2CaloriForMan(
                                minHr,
                                age,
                                weight,
                                Constant.REFRESH_RATE,
                                Constant.UNIT_MILLS
                            )
                        } else {
                            Log.e(
                                "上传数据:",
                                "一分钟数据整合 FeMale=" + dataShowBean!!.minHrList
                            )
                            cal = HeartRateConvertUtils.hearRate2CaloriForWoman(
                                minHr,
                                age,
                                weight,
                                Constant.REFRESH_RATE,
                                Constant.UNIT_MILLS
                            )
                        }



                        if (cal < 0) {
                            cal = 0.0
                        }
                        dataShowBean!!.cal = cal


                        Log.e(tags,"-----30s计算一次="+Gson().toJson(dataShowBean!!.calAllHrList))

                        val heartPoint = dataShowBean!!.point;
                        var compPoint = 0.0
                        if(userInfo != null){
                            //计算的经验值
                             compPoint =   MatchUtils.matchHeartPoint(if(userInfo?.sex.equals("1")) 0 else 1, userInfo!!.age,
                                dataShowBean!!.calAllHrList)


                        }
                        dataShowBean!!.point = Arith.add(heartPoint,compPoint)
                        Log.e(tags,"-------计算的经验值="+compPoint+" 原有的经验值="+heartPoint+" 总的="+dataShowBean!!.point)

                    }

                }
            } else {
                dataShowBean = DevicesDataShowBean()
                dataShowBean!!.age = age
                dataShowBean!!.sex = gander
                dataShowBean!!.joinTime = System.currentTimeMillis()
                dataShowBean!!.weight = weight
                if (userInfo != null) {
                    dataShowBean!!.height = userInfo!!.height
                    dataShowBean!!.headUrl = userInfo!!.avatar
                    dataShowBean!!.nikeName = userInfo!!.nickname
                    dataShowBean!!.userId = userInfo!!.id
                }
                dataShowBean!!.liveHeartRate = heartRate
                dataShowBean!!.devicesSN = key
                if (cal < 0) {
                    cal = 0.0
                }
                dataShowBean!!.cal = cal
                dataShowBean!!.addStageHeart(key, 0)
                dataShowBean!!.time = heartRateBean!!.time
                //设置课程id
                dataShowBean!!.precent = HeartRateConvertUtils.hearRate2Percent(
                    heartRate,
                    HeartRateConvertUtils.getMaxHeartRate(age).toDouble()
                ).toInt().toString()
                //保存SN
                AllocationApi.getAllSNSet().add(key)
                mAddList.offer(dataShowBean)
                mHandler.sendEmptyMessage(ADD_DATA)
                isAddItem = true
            }
        }
        //每10s刷新数据
        // if (dataSize > 0) {
        doHallTask()
        /* intervalTime += Constant.REFRESH_RATE
         if (intervalTime * Constant.REFRESH_RATE % 10 != 0) {
             mHandler.sendEmptyMessage(UPDATE_HALL)
         } else if (intervalTime * Constant.REFRESH_RATE % 10 == 0) {
             doHallTask()
         }//每10s全部刷新一次*/
        // }
    }


    var sn = ""
    var hrValue = 0


    @RequiresApi(Build.VERSION_CODES.N)
    private fun doCommonHRTask(sources: ConcurrentHashMap<String, Int>) {

        var markList = UserContans.markTagsMap;

        //Log.e(tags,"-----000source="+sources.toString()+"\n"+"已保存集合="+UserContans.userInfoHashMap.toString()+"\n"+"已打标签集合="+markList.toString())

        if(!markList.isEmpty()){
           markList.forEach { (t: String?, u: Int?) ->
               if(sources[t] != null){
                   sources[t] = -1
               }
           }
        }
        Log.e(tags, "-----处理后的source=$sources")


        sources.forEach {
            Logger.e("doCommonHRTask", sn)
            sn = it.key
            hrValue = it.value
           // Log.e(tags,"------查询用户信息="+it.toString())

            if (!UserContans.userInfoHashMap.containsKey(sn)) {
                if (!UserContans.mCacheMap.containsKey(sn)) {
                    //1.查询用户信息
//                        for (i in 0 until mDataShowBeans.size){
//                            if(sn.equals(mDataShowBeans[i])){
//                                reMoveList.add(i)
//                            }
//                        }
//
//                        isRemove = true
//                        //清除SN码的值
//                        //清除
//                        UserContans.mSnHrMap.remove(sn)
//                        UserContans.mSnHrTime.remove(sn)
//                        UserContans.mCacheMap.remove(sn)
//                        UserContans.secondHeartRateBeanHashMap.remove(sn)
//                        AllocationApi.getAllSNSet().remove(sn)
//                        mHandler.sendEmptyMessage(REMOVE_DATA)
                    }

            } else {

                if(hrValue != -1){
                    val secondHeartRateBean: SecondHeartRateBean =
                        if (UserContans.secondHeartRateBeanHashMap.containsKey(sn)) {
                            UserContans.secondHeartRateBeanHashMap.get(sn)!!
                        } else {
                            SecondHeartRateBean()
                        }
                    //接收到的所有心率数据
                    secondHeartRateBean.devicesSN = sn
                    secondHeartRateBean.heartList.add(hrValue)
                    secondHeartRateBean.heart = hrValue
                    secondHeartRateBean.isTask = false
                    //接收到的数据的时间戳
                    secondHeartRateBean.time = UserContans.mSnHrTime.get(sn)!!
                    UserContans.secondHeartRateBeanHashMap.put(sn, secondHeartRateBean)
                }


            }
        }

        //这里需要
        secondMap.clear()
        secondMap.putAll(UserContans.secondHeartRateBeanHashMap)

        // sources.clear();

        //总数据
    }

    /**
     * 大厅模式
     *
     * @param sources
     */

    //时间，赋值为当前时间，防止异常掉线
    var time = System.currentTimeMillis()//0L
    var isRemove = false;
    private fun doHallTask() {
        try {
            var isDrop = !UserContans.markTagsMap.isEmpty()
            //判断设备是否掉线，掉线的移除
            val currentTime = System.currentTimeMillis()
            //  var isRemove = false
            //接收数据的时间
            reMoveList.clear()
            isRemove = false
            if (mDataShowBeans.size == 0)
                return
            for (i in 0 until mDataShowBeans.size) {
                val dropBean = mDataShowBeans[i]
                //已经掉线，直接移除
                sn = dropBean.devicesSN
                if(UserContans.mSnHrTime.containsKey(sn)){
                    time = UserContans.mSnHrTime[sn]!!
                }


                // || (isDrop && UserContans.markTagsMap.containsKey(sn))
                if (currentTime - time >= Constant.DROP_TIME || (isDrop && UserContans.markTagsMap.containsKey(sn))) {
                    if (UserContans.userInfoHashMap.containsKey(dropBean.devicesSN)) {
                        UserContans.userInfoHashMap.remove(dropBean.devicesSN);
                    }
                    Log.e(tags, "--------掉线移除------=$dropBean")
                    isRemove = true
                    reMoveList.add(i)
                    //清除SN码的值
                    //清除
                    UserContans.mSnHrMap.remove(sn)
                    UserContans.mSnHrTime.remove(sn)
                    UserContans.mCacheMap.remove(sn)
                    UserContans.secondHeartRateBeanHashMap.remove(sn)
                    AllocationApi.getAllSNSet().remove(dropBean.devicesSN)

                }
            }

            if (isRemove) {
                mHandler.sendEmptyMessage(REMOVE_DATA)
            } else {
                mHandler.sendEmptyMessage(UPDATE_HALL)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back()
        }
        return super.onKeyDown(keyCode, event)
    }

}

