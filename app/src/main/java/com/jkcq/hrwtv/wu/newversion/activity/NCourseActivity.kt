package com.jkcq.hrwtv.wu.newversion.activity

import android.annotation.SuppressLint
import android.content.*
import android.os.*
import androidx.appcompat.app.AlertDialog
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.google.gson.Gson
import com.jkcq.hrwtv.AllocationApi
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.app.BaseApp
import com.jkcq.hrwtv.configure.Constant
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean
import com.jkcq.hrwtv.heartrate.bean.SecondHeartRateBean
import com.jkcq.hrwtv.heartrate.bean.UserBean
import com.jkcq.hrwtv.heartrate.model.MainActivityView
import com.jkcq.hrwtv.heartrate.presenter.MainActivityPresenter
import com.jkcq.hrwtv.http.RetrofitHelper
import com.jkcq.hrwtv.http.bean.*
import com.jkcq.hrwtv.http.widget.BaseObserver
import com.jkcq.hrwtv.service.UserContans
import com.jkcq.hrwtv.ui.view.DialogYesOrNo
import com.jkcq.hrwtv.ui.view.ShowEmptyDialog
import com.jkcq.hrwtv.util.*
import com.jkcq.hrwtv.wu.newversion.view.CourseHeartResultView
import com.jkcq.hrwtv.wu.obsever.HrDataObservable
import com.jkcq.hrwtv.wu.util.MatchUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ncourse.*
import kotlinx.android.synthetic.main.include_course_bottom.*
import kotlinx.android.synthetic.main.layout_heartresult_view.*
import kotlinx.android.synthetic.main.ninclude_title.*
import okhttp3.MediaType
import okhttp3.RequestBody
import timber.log.Timber
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.ArrayList

//课程模式开始页面
class NCourseActivity : AbsNewHeartResultActivity(), MainActivityView {

    private val tags = "NCourseActivity"

    private var intervalTime = 0
    var reMoveList = ArrayList<String>()


    //  private var myMqttService: BaseMqttservice? = null
    private val UPDATE_HALL = 115
    private val REMOVE_DATA = 116;
    private val ADD_DATA = 117;


    private var mAddList = ConcurrentLinkedQueue<DevicesDataShowBean>()
    private var removemAddList = ArrayList<DevicesDataShowBean>()
    private var mCurrentRemoveIndex = 0
    private var isAddItem = false;

    private val secondMap = ConcurrentHashMap<String, SecondHeartRateBean>()


    //显示并自动结束的dialog
    private var autoDialogView : ShowEmptyDialog?=null

    val instance by lazy {this}


    //    var dataShowBeans = ArrayList<DevicesDataShowBean>()
    var mDataShowBeans = CopyOnWriteArrayList<DevicesDataShowBean>();

    private val mHandler = object : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_HALL -> {
                    //原意是只更新改变的数据，但实际上更新了所有数据
                    heartresult_view.updateData(mDataShowBeans, isSort, mSortType)

                    mDataShowBeans.forEach {
                      if(!BaseApp.recordHashData.containsKey(it.devicesSN)){
                          BaseApp.recordHashData[it.devicesSN] = it
                      }   else{

                      }
                    }


                    // myMqttService!!.setTask(false)
                }
                ADD_DATA -> {

                    if (mAddList.size > 0) {

                        var addDevice = mAddList.poll();
                        var hasExsit = false
                        mDataShowBeans.forEach {
                            if (it.devicesSN.equals(addDevice.devicesSN)) {
                                hasExsit = true
                            }

                        }

                        if (!hasExsit) {
                            heartresult_view.insertItem(
                                addDevice,
                                mDataShowBeans.size - 1
                            )
                        }
                    }

                }
                REMOVE_DATA -> {
//                    reMoveList.forEach {
//                        removemAddList.add(mDataShowBeans[it])
//                    }
                    heartresult_view.removeItem(reMoveList)

                    // myMqttService!!.setTask(false)
                }
            }
        }
    }


    override fun getClubSuccess(info: ClubInfo?) {
        if (TextUtils.isEmpty(info?.getUid())) {
            tv_name.setText("未知会所")
            //showSettingView();
        } else {
            tv_name.setText(info?.getName())
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
        BaseApp.recordHashData.clear()
        //跳转到结果页面
       // startActivity(Intent(this, CourseSortActivity::class.java))

        startActivity(Intent(this,NewSortActivity::class.java))
        finish()
    }


    var AllData = false;

    override fun createPresenter(): MainActivityPresenter {
        return MainActivityPresenter()
    }

    override fun getLayoutId(): Int = R.layout.activity_ncourse
    override fun initView() {
        super.initView()
        mDataShowBeans = heartresult_view.getAdapterData()
        layout_select_user.visibility = View.VISIBLE
        layout_select_user.setOnClickListener {
            var intent = Intent(this, UserSelectActivity::class.java)
            intent.putExtra("firstCome", false)
            intent.putExtra("currentMode", Constant.MODE_COURSE)
            startActivity(intent)
        }

        layout_setting.setOnClickListener {
            // 上课时长＜10%课程时长，提示不保存数 本次课程时间过短，退出将不保存运动数据
            back()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            recyclerview_hall.focusable = 1
        }

    }

    fun showNoupdate() {
        DialogYesOrNo(this, object : DialogYesOrNo.OnButtonClick {
            override fun onButtonClickCancel() {
                UserContans.isPause = false
            }

            override fun onButtonClickSure() {
                UserContans.isPause = true;
                mCurrentDownTimer?.cancel()
                BaseApp.recordHashData.clear()
                //取消标记
                markSnListData(true)
              //  finish()
            }
        }, "本次课程时间过短，退出将不保存运动数据").show()
    }

    private fun showDialog() {
        DialogYesOrNo(this, object : DialogYesOrNo.OnButtonClick {
            override fun onButtonClickCancel() {
                UserContans.isPause = false
            }

            override fun onButtonClickSure() {
                UserContans.isPause = true;
                mCurrentDownTimer?.cancel()
                upgradeCourseData()
            }
        }, "确认结束课程？").show()
    }

    private fun back() {
        var toalTime = mCurrentHeartRateClassInfo.duration * 1000L
        if (toalTime - mRemainTime <= toalTime * 0.1) {
            showNoupdate()
        } else {
            Log.e("toalTime==", "toalTime=" + toalTime + "mRemainTime=" + mRemainTime)
            showDialog()
        }
    }

    var endTime = System.currentTimeMillis()

    private fun upgradeCourseData() {

        Logger.e("upgradeCourseData--------------------------------------")
        if (endTime != 0L) {
            endTime = System.currentTimeMillis()
        }
        AllData = true;
        removemAddList.clear()
        mDataShowBeans.forEach {
            removemAddList.add(it)
        }

        //取消标记
        markSnListData(false)

        mActPresenter.postCourse(
            removemAddList,
            UserContans.info.id,
            "1",
            endTime
        )

    }

    var isRestart = false;

    override fun initData() {
        UserContans.classTime = 0
        UserContans.isPause = false;
        HrDataObservable.getInstance().addObserver(this)
        tv_name.setText(mclubName)
        DoubleClickUtil.getInstance().initHandler(Handler(Looper.getMainLooper()))
        tv_man_count.setText(getString(R.string.people, "0"))
        updateTime()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_TIME_TICK)
        registerReceiver(mTimeReceiver, filter)

        isRestart = intent.getBooleanExtra("isRestart", false)
        if (isRestart) {
            restartCourse()
        } else {
            showCourseModel()
            starCourse()
        }
    }


    private fun restartCourse() {
        var list = CacheDataUtil.getCourseUserList()
        if (list != null && list.size > 0) {
            mDataShowBeans.addAll(list)
            heartresult_view.updateData(mDataShowBeans, isSort, mSortType)
        }

        var course = CacheDataUtil.getCourseInfo()
        if (course != null) {
            UserContans.info = course
        }
        mRemainTime = CacheDataUtil.getRemainTime()
        showCourseModel()

        reStartDowTimer()
    }


    /**
     * 系统的一个广播，每分钟会发送一个广播
     */
    private var mTimeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
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
        tv_time.setText(
            String.format(
                "%02d",
                calendar.get(Calendar.HOUR_OF_DAY)
            ) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE))
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        BaseApp.recordHashData.clear()
        mCurrentDownTimer?.cancel()
        HrDataObservable.getInstance().deleteObserver(this)
        unregisterReceiver(mTimeReceiver)
        AllocationApi.getAllSNSet().clear()
        UserContans.courseClearMap()
        mCurrentRemoveIndex = 0
        heartresult_view.release()
    }

    override fun getHeartResultView(): CourseHeartResultView {
        return heartresult_view;
    }


    override fun update(o: Observable?, arg: Any?) {
        super.update(o, arg)

        Log.e("Ncouse", "UserContans.isPause" + UserContans.isPause)

        /*if (UserContans.isPause) {
            return
        }
        if (o is HrDataObservable) {


            if (UserContans.classTime == 0L) {
                endTime = System.currentTimeMillis()
                doHallModel(arg as ConcurrentHashMap<String, Int>)
            }
            if ((System.currentTimeMillis() - UserContans.classTime) / 1000 <= UserContans.couserTime) {
                endTime = System.currentTimeMillis()
                doHallModel(arg as ConcurrentHashMap<String, Int>)
            }
        }*/
    }

    var userInfo: UserBean? = null
    var age = 0
    var weight = 0.0f
    var gander = ""
    var heartRateBean: SecondHeartRateBean? = null
    var heartRate = 0
    var cal: Double = 0.0
    var dataShowBean: DevicesDataShowBean? = null
    var dataSize = 0


    private fun showUsers() {
        Log.e("Ncouse", "secondMap" + secondMap.size)
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
                gander = userInfo!!.sex
            }
            //先计算出值
            // 刷新Adapter,position = i；
            heartRateBean = secondMap[key]
            //一秒钟的心率值
            heartRate = heartRateBean!!.heart
            if (heartRate < 30) {
                heartRate = 0;
            }

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
                    dataShowBean!!.time = heartRateBean!!.time
                    var precent = HeartRateConvertUtils.hearRate2Percent(
                        heartRate,
                        HeartRateConvertUtils.getMaxHeartRate(age).toDouble()
                    ).toInt()
                    dataShowBean!!.precent = precent.toString()

                    if (heartRate == 0) {
                        dataShowBean!!.addStageHeart(key, -1)
                    } else {
                        dataShowBean!!.addStageHeart(key, precent)
                    }

                    dataShowBean!!.setAllHrList(heartRate)

                    //计算每一分钟的数据 60已改为30
                    if (dataShowBean!!.allHrList.size == 10 / Constant.REFRESH_RATE) {
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
                        if (gander.equals("1")) {   //男 卡路里
                            cal = HeartRateConvertUtils.hearRate2CaloriForMan(
                                minHr,
                                age,
                                weight,
                                Constant.REFRESH_RATE,
                                Constant.UNIT_MILLS
                            )
                        } else {  //女 卡路里
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
                        dataShowBean!!.cal = cal.toFloat().toDouble()

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
                //判断是否已经再线过
                val isSaved = BaseApp.recordHashData.containsKey(key)
                if(isSaved){
                    //开始加入的时间
                    dataShowBean!!.joinTime = BaseApp.recordHashData[key]?.joinTime
                    //掉线时间
                    dataShowBean!!.time =  heartRateBean!!.time//BaseApp.recordHashData[key]?.time!!
                    dataShowBean!!.cal = BaseApp.recordHashData[key]?.cal!!
                    //平均心率强度
                    dataShowBean!!.averageHeartPercent = BaseApp.recordHashData[key]?.averageHeartPercent!!
                    dataShowBean!!.precent = BaseApp.recordHashData[key]?.precent!!
                    var precent2 = HeartRateConvertUtils.hearRate2Percent(
                        heartRate,
                        HeartRateConvertUtils.getMaxHeartRate(age).toDouble()
                    ).toInt()
                    dataShowBean!!.addStageHeart(key,precent2)

                    val courseList = BaseApp.recordHashData[key]?.getmDatas()
                    Timber.e("-----课程集合="+Gson().toJson(courseList))
                    if (courseList != null) {
                        BaseApp.recordHashData[key]?.let { it1 ->
                            val originalList = it1.allHrList

                            /**
                             * 判断间隔，即从下线时间到现在的间隔
                             */
                            val dropInterval = System.currentTimeMillis()- heartRateBean!!.time


                            val repairTime = dropInterval / 1000

                            Timber.e("---------下线的间隔时间长="+repairTime)
                            for(i in 0 until  repairTime){
                                originalList.add(32)
                            }
                            dataShowBean!!.allHrList.addAll(originalList)

                            courseList.add(CourseDetail(0, repairTime.toInt(),-1))
                        }
                    }

                    dataShowBean!!.setmDatas(courseList)

                    //经验值
                    dataShowBean!!.point = BaseApp.recordHashData[key]?.point!!

                }else{
                    dataShowBean!!.joinTime = System.currentTimeMillis()
                    dataShowBean!!.time =  heartRateBean!!.time
                    if (cal < 0) {
                        cal = 0.0
                    }
                    dataShowBean!!.cal = cal.toFloat().toDouble()
                    //设置课程id
                    dataShowBean!!.precent = HeartRateConvertUtils.hearRate2Percent(
                        heartRate,
                        HeartRateConvertUtils.getMaxHeartRate(age).toDouble()
                    ).toInt().toString()
                    dataShowBean!!.addStageHeart(key, 0)

                    var joinTime : Long ?= null
                    if(BaseApp.recordHashData.size>0){
                        BaseApp.recordHashData.forEach {
                            joinTime = it.value.joinTime
                        }
                    }

                    if(joinTime != null){
                        dataShowBean!!.joinTime = joinTime

                        /**
                         * 判断间隔，即从下线时间到现在的间隔
                         */
                        val dropInterval =  System.currentTimeMillis() - joinTime!!

                        Timber.e("-------dropINer="+dropInterval)
                        val tempCourList = mutableListOf<CourseDetail>()
                        tempCourList.add(CourseDetail(0, (dropInterval/1000).toInt(),-1))
                        dataShowBean!!.setmDatas(tempCourList)
                    }

                }


                dataShowBean!!.age = age

                dataShowBean!!.weight = weight
                dataShowBean!!.sex = gander
                if (userInfo != null) {
                    dataShowBean!!.height = userInfo!!.height
                    dataShowBean!!.headUrl = userInfo!!.avatar
                    dataShowBean!!.nikeName = userInfo!!.nickname
                    dataShowBean!!.userId = userInfo!!.id
                }
                dataShowBean!!.liveHeartRate = heartRate
                dataShowBean!!.devicesSN = key



                //保存SN
                AllocationApi.getAllSNSet().add(key)
                mAddList.offer(dataShowBean)
                mHandler.sendEmptyMessage(ADD_DATA)
                isAddItem = true
            }
        }
        //每10s刷新数据
        if (dataSize > 0) {
            intervalTime += Constant.REFRESH_RATE
            doHallTask()
            /* if (intervalTime * Constant.REFRESH_RATE % 10 != 0) {
                 mHandler.sendEmptyMessage(UPDATE_HALL)
             } else if (intervalTime * Constant.REFRESH_RATE % 10 == 0) {

             }//每10s全部刷新一次*/
        }
    }


    private fun doHallModel(
        sources: ConcurrentHashMap<String, Int>
    ) {
        //开始的时间
        //查询用户信息
        doCommonHRTask(sources)
        showUsers()

    }

    var sn = ""
    var hrValue = 0


     private var recordHeartList = CopyOnWriteArrayList<Int>()

    @SuppressLint("LongLogTag")
    private fun doCommonHRTask(sources: ConcurrentHashMap<String, Int>) {

        var isRecord = BaseApp.recordHashData.isNotEmpty()

        sources.forEach {
            sn = it.key
            hrValue = it.value
            if (!UserContans.userInfoHashMap.containsKey(sn)) {
                if (!UserContans.mCacheMap.containsKey(sn)) {
                    //1.查询用户信息
                }
            } else {
                if (UserContans.userInfoHashMap.containsKey(sn)) {
                    //是已经选择的就显示没有选择的就去掉

                    val select = UserContans.userInfoHashMap[sn]!!.isSelect
                    Log.e("Ncouse", "select" + select)


                    //判断是否是已经存在过，即已经上过课程，中途给停掉课程，再次上课
                    val isExist = BaseApp.recordHashData.containsKey(sn)

                    if (select) {
                        val secondHeartRateBean: SecondHeartRateBean =
                            if (UserContans.secondHeartRateBeanHashMap.containsKey(sn)) {
                                UserContans.secondHeartRateBeanHashMap[sn]!!
                            } else {
                                SecondHeartRateBean()
                            }
                        if(isExist){
                            recordHeartList.clear()

                            BaseApp.recordHashData[sn]?.let { it1 ->
                                Log.e(tags,"------已经存在的集合="+Gson().toJson(it1))
                                recordHeartList.addAll(it1.allHrList) }
                            recordHeartList.add(hrValue)
                            secondHeartRateBean.heartList.addAll(recordHeartList)
                        }else{
                            secondHeartRateBean.heartList.add(hrValue)
                        }
                        //接收到的所有心率数据
                        secondHeartRateBean.devicesSN = sn
                        secondHeartRateBean.heart =  hrValue
                        secondHeartRateBean.isTask = false
                        secondHeartRateBean.time = if(isExist) BaseApp.recordHashData[sn]?.joinTime!! else UserContans.mSnHrTime[sn]!!
                        //接收到的数据的时间戳
                        UserContans.secondHeartRateBeanHashMap[sn] = secondHeartRateBean
                    } else {
                        if (UserContans.secondHeartRateBeanHashMap.containsKey(sn)) {
                            UserContans.secondHeartRateBeanHashMap.remove(sn)
                        }
                    }

                }


            }
        }

        Logger.e(tags,"-------已处理="+Gson().toJson(UserContans.secondHeartRateBeanHashMap))


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

    var time = 0L
    var isRemove = false;
    private fun doHallTask() {
        var markMap = UserContans.markTagsMap

        //判断设备是否掉线，掉线的移除
        val currentTime = System.currentTimeMillis()
        //  var isRemove = false
        //接收数据的时间
        reMoveList.clear()
        isRemove = false
        for (i in 0 until mDataShowBeans.size) {
            val dropBean = mDataShowBeans.get(i);
            //已经掉线，直接移除
            sn = dropBean.devicesSN
            if (UserContans.mSnHrTime.containsKey(sn)) {
                time = UserContans.mSnHrTime.get(sn)!!
                if (currentTime - time >= Constant.DROP_TIME) {
                    UserContans.mSnHrMap.put(sn, 0)
                }
            }
            //如果两分钟没有数据，就把sn值的
            Timber.e("------------掉线去除时间="+(currentTime - time))
            if (UserContans.userInfoHashMap.containsKey(sn)) {
                if (!UserContans.userInfoHashMap.get(sn)!!.isSelect ) {
                    isRemove = true
                    reMoveList.add(sn)
                    //清除SN码的值
                    //清除
                    /* UserContans.mSnHrMap.remove(sn)
                 UserContans.mSnHrTime.remove(sn)
                 UserContans.mCacheMap.remove(sn)
                //UserContans.secondHeartRateBeanHashMap.remove(sn)
                */
                    AllocationApi.getAllSNSet().remove(dropBean.devicesSN)
                }
            }
        }
        if (isRemove) {
            mHandler.sendEmptyMessage(REMOVE_DATA)
        } else {
            mHandler.sendEmptyMessage(UPDATE_HALL)
        }
    }

    private fun showCourseModel() {
        if(UserContans.info == null)
            return
        mCurrentHeartRateClassInfo = UserContans.info


        UserContans.couserTime = mCurrentHeartRateClassInfo.duration
        tv_course_name.text = mCurrentHeartRateClassInfo.courseName
        tv_course_leve.text =
            UserContans.getDifficultyLevel(mCurrentHeartRateClassInfo.difficultyLevel)
        rl_coure_bottom.visibility = View.VISIBLE
        if (mCurrentHeartRateClassInfo.targetRateArray == null || mCurrentHeartRateClassInfo.targetRateArray.size == 0) {
            mCurrentHeartRateClassInfo.addTargetRateArray(
                CourseDetail(
                    0,
                    mCurrentHeartRateClassInfo.duration,
                    10
                )
            )
            layout_coure_match.visibility = View.GONE
        } else {
            layout_coure_match.visibility = View.VISIBLE
        }
        coursematch_view.setValue(
            mCurrentHeartRateClassInfo.duration,
            mCurrentHeartRateClassInfo.targetRateArray
        )
        coursematchpoint_view.setValue(UserContans.info)
        /* tv_course_time.text =
             resources.getString(
                 R.string.course_time_minute,
                 "" + mCurrentHeartRateClassInfo?.duration / 60
             )*/

//        coursematch_view.moveArrow()
    }

    private fun starCourse() {
        mRemainTime = mCurrentHeartRateClassInfo.duration * 1000
        startDownTimer()
    }

    lateinit var mCurrentHeartRateClassInfo: CourseInfo
    private var mRemainTime = 0
    private var mCurrentRangeIndex = 0
    private var mUnfinishedTime = 0


    var currentCourseDetail: CourseDetail? = null
    var lastCourseDetail: CourseDetail? = null


    inner class CourseDownTimer(var millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            LogUtil.e("onFinish=" + ",isPase" + UserContans.isPause + "  mRemainTime / 1000=" + (mRemainTime / 1000))

            endTime = System.currentTimeMillis()

            if (mRemainTime / 1000 <= 0) {
                //把数据清零
                doHallModel(UserContans.mSnHrMap)
            }

            autoDialogView = ShowEmptyDialog(instance)
            autoDialogView!!.show()
            autoDialogView!!.setShowTime(3 * 1000)
            autoDialogView!!.setContentTvTxt("挑战结束")
            autoDialogView!!.setOnEmptyDialogListener {
                autoDialogView!!.dismiss()
                //完成后自动结束课程
                UserContans.isPause = true;
                mCurrentDownTimer?.cancel()
                upgradeCourseData()
            }

        }

        override fun onTick(millisUntilFinished: Long) {
            endTime = System.currentTimeMillis()
            doHallModel(UserContans.mSnHrMap)

            //已经走了的时间
            var moverTime = 0

            LogUtil.e("millisInFuture=" + millisInFuture / 1000 + " millisUntilFinished=" + millisUntilFinished / 1000 + "mRemainTime/1000=" + mRemainTime / 1000)

            mUnfinishedTime = millisUntilFinished.toInt()

            var remove = UserContans.couserTime + 1 - mRemainTime / 1000
            /**
             * 小米盒子最后一秒不会调用
             */
            if (currentCourseDetail != null && remove >= currentCourseDetail!!.begin && remove <= currentCourseDetail!!.end) {
                moverTime = (currentCourseDetail!!.end - remove).toInt()
                removeview(moverTime)
            } else {
                mCurrentHeartRateClassInfo.targetRateArray.forEach {
                    //LogUtil.e("it.begin=" + it.begin + " it.end=" + it.end + "userTime=" + userTime + ",moverTime=" + moverTime)
                    if (remove >= it.begin && remove <= it.end) {
                        //移动的时间
                        currentCourseDetail = it;
                        CacheDataUtil.mCurrentRange = it.targetRange
                        moverTime = (it.end - remove).toInt()
                        removeview(moverTime)
                    }

                }
            }

            Timber.e("--------时间="+(millisUntilFinished / 1000))

            updateCourseView(millisUntilFinished)

            if(millisUntilFinished/1000<=1) {
                coursematchpoint_view.moveArrow(0L)
            }
        }

    }


    private fun removeview(moveTime: Int) {

        coursematchpoint_view.setStrTime(TimeUtil.getFormatTimemmss(moveTime * 1000L))
        //  coursematchpoint_view.moveArrow((mRemainTime / 1000).toLong())
    }

    private val mDuration = 1000L
    var mCurrentDownTimer: CourseDownTimer? = null
    private fun reStartDowTimer() {
        LogUtil.e("mCurrentRangeIndex=" + mCurrentRangeIndex + " size=" + mCurrentHeartRateClassInfo.targetRateArray.size + " ,(mCurrentHeartRateClassInfo.duration * 1000 - mRemainTime) / 1000L=" + (mCurrentHeartRateClassInfo.duration * 1000 - mRemainTime) / 1000L)

        mCurrentDownTimer =
            CourseDownTimer(
                (mCurrentHeartRateClassInfo.duration - (mCurrentHeartRateClassInfo.duration * 1000 - mRemainTime) / 1000L) * 1000L,
                mDuration
            )
        mCurrentDownTimer?.start()
    }

    //需要考虑暂停时候的逻辑
    private fun startDownTimer() {
        mCurrentDownTimer =
            CourseDownTimer(
                (mCurrentHeartRateClassInfo.duration ) * 1000L,
                mDuration
            )
        mCurrentDownTimer?.start()
        //}
    }

    var mStateControlTime = 0L
    private fun updateCourseView(millisUntilFinished: Long) {
        //LogUtil.e("mStateControlTime=" + mStateControlTime + " millisUntilFinished= " + millisUntilFinished + "mRemainTime=" + mRemainTime)
        if (mStateControlTime == millisUntilFinished / 1000) return
        mStateControlTime = millisUntilFinished / 1000
        tv_remain_time.text = TimeUtil.getFormatTime(mRemainTime - 1000L)
        //coursematchpoint_view.setStrTime(TimeUtil.getFormatTimemmss(millisUntilFinished))
        coursematchpoint_view.moveArrow((mRemainTime / 1000).toLong())
        //  tv_range_remain_time.text = TimeUtil.getFormatTimemmss(millisUntilFinished)
        mRemainTime = (mRemainTime - mDuration).toInt();
        CacheDataUtil.savemRemainTime(mRemainTime)
        //结束上报数据
//        coursematch_view.moveArrow(mRemainTime / 1000)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back()
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun markSnListData(isFinish : Boolean){
        //取消累计记录
        BaseApp.recordHashData.clear()
        if(mDataShowBeans.size == 0)
            return
        var para = HashMap<String,List<String>>()

        //遍历集合，得到已经选择的用户
        var selectList = emptyList<String>()
        var unSelectList = arrayListOf<String>()

        for(index in 0 until mDataShowBeans.size ){
            unSelectList.add(mDataShowBeans[index].devicesSN)
        }

        para["markList"] = selectList
        para["unmarkList"] = unSelectList
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json"), Gson().toJson(para))
        RetrofitHelper.service.markSnActiveTags(requestBody)
            .subscribeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<BaseResponse<Boolean>>(){
                override fun onSuccess(t: BaseResponse<Boolean>?) {
                    if (t != null) {
                        Log.e(tags,"---是否成功="+t.data)
                        if(t.data == true && isFinish){
                            UserContans.markTagsMap.clear()
                            UserContans.privateMarkTagsMap.clear()
                            finish()
                        }
                    }
                }

                override fun dealError(msg: String?) {
                    super.dealError(msg)
                }
            })

    }


}
