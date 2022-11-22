package com.jkcq.hrwtv.wu.newversion.activity

import android.annotation.SuppressLint
import android.content.*
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.recyclerview.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.gson.Gson
import com.jkcq.hrwtv.AllocationApi
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.app.BaseApp
import com.jkcq.hrwtv.base.mvp.BaseMVPActivity
import com.jkcq.hrwtv.configure.Constant
import com.jkcq.hrwtv.eventBean.EventConstant
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
import com.jkcq.hrwtv.wu.manager.Preference
import com.jkcq.hrwtv.wu.newversion.view.PKItemView
import com.jkcq.hrwtv.wu.obsever.HrDataObservable
import com.jkcq.hrwtv.wu.util.MatchUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_ncourse.*
import kotlinx.android.synthetic.main.activity_new_pk_layout.*
import kotlinx.android.synthetic.main.activity_new_pk_layout.newPkBlueView
import kotlinx.android.synthetic.main.activity_new_pk_layout.newPkRedView
import kotlinx.android.synthetic.main.activity_npk.*
import kotlinx.android.synthetic.main.activity_pk_content.*
import kotlinx.android.synthetic.main.include_course_bottom.*
import kotlinx.android.synthetic.main.include_new_pk_progress_layout.*
import kotlinx.android.synthetic.main.item_user_card.view.*
import kotlinx.android.synthetic.main.layout_course_select.*
import kotlinx.android.synthetic.main.layout_course_select.ll_point
import kotlinx.android.synthetic.main.ninclude_title.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * PK模式页面
 */
class NPkActivity : BaseMVPActivity<MainActivityView, MainActivityPresenter>(), MainActivityView {


    private val tags = "NPkActivity"

    private val decimalFormat = DecimalFormat("#.##")

    var mToken: String by Preference(Preference.token, "")
    var mbrandName: String by Preference(Preference.brandName, "")
    var mbrandId: String by Preference(Preference.brandId, "")
    var mclubId: String by Preference(Preference.clubId, "")
    var mclubName: String by Preference(Preference.clubName, "")
    var mUserId: String by Preference(Preference.userId, "")
    var mNikeName: String by Preference(Preference.nickname, "")
    private var intervalTime = 0
    var reMoveList = mutableListOf<Int>()


    var redViewlist = ConcurrentHashMap<Int, PKItemView>()
    var blueViewList = ConcurrentHashMap<Int, PKItemView>()


    //显示并自动结束的dialog
    private var autoDialogView : ShowEmptyDialog?=null


    //  private var myMqttService: BaseMqttservice? = null
    private val UPDATE_HALL = 115
    private val REMOVE_DATA = 116;
    private val ADD_DATA = 117;

    private var mAddList = ConcurrentLinkedQueue<DevicesDataShowBean>()
    private var mCurrentRemoveIndex = 0
    private var isAddItem = false;

    private val secondMap = ConcurrentHashMap<String, SecondHeartRateBean>()

    //    var dataShowBeans = ArrayList<DevicesDataShowBean>()
    var mDataShowBeans = CopyOnWriteArrayList<DevicesDataShowBean>();
    var mRedDataShowBeans = ArrayList<DevicesDataShowBean>();
    var mBlueDataShowBeans = ArrayList<DevicesDataShowBean>();

    var mRedCurrentShowBeans = ArrayList<DevicesDataShowBean>();
    var mBlueCurrentShowBeans = ArrayList<DevicesDataShowBean>();


    var showResultRedList = CopyOnWriteArrayList<DevicesDataShowBean>()
    var showBlueResultList = CopyOnWriteArrayList<DevicesDataShowBean>()

    var redAdapter : NewPkAdapter ?= null
    var blueAdapter : NewPkAdapter ?= null




    private val mHandler = object : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_HALL -> {
                    mDataShowBeans.forEach {
                        if(!BaseApp.recordHashData.containsKey(it.devicesSN)){
                            BaseApp.recordHashData[it.devicesSN] = it
                        }
                    }
                    Log.e(tags,"-----UPDATE_HALL="+mDataShowBeans.size)
                    //原意是只更新改变的数据，但实际上更新了所有数据
//                    newPkRedView.updateData(mDataShowBeans, false, 3)
//                    newPkBlueView.updateData(mDataShowBeans, false, 3)
                    // myMqttService!!.setTask(false)
                }
                ADD_DATA -> {
                    if (mAddList.size > 0) {

                        var addDevice = mAddList.poll();
                        var hasExsit = false

                        if (mDataShowBeans.size == 0) {
                            UserContans.classTime = addDevice.joinTime
                        }

                        mDataShowBeans.forEach {
                            if (it.devicesSN.equals(addDevice.devicesSN)) {
                                hasExsit = true
                            }

                        }
                        if (!hasExsit) {
                            mDataShowBeans.add(addDevice)
                        }
//                        if(addDevice.pkTeam == 2){
//
//                            newPkRedView.insertItem(addDevice,mRedDataShowBeans.size-1)
//                        }else{
//                            newPkBlueView.insertItem(addDevice,mRedDataShowBeans.size-1)
//                        }


                        // heartresult_view.insertItem(mAddList.poll(), mDataShowBeans.size - 1)
                        // myMqttService!!.setTask(false)
                    }

                }
                REMOVE_DATA -> {
                    Log.e(tags,"------移除="+Gson().toJson(reMoveList))
                    reMoveList.forEach {
                        //removemAddList.add(mDataShowBeans[it])
                       // heartresult_view.removeItem(it)
                        if(it<mDataShowBeans.size)
                            mDataShowBeans.removeAt(it)
                    }
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


        //跳转到结果页面
        startActivity(Intent(this, PkResultActivity::class.java))
        finish()
    }


    var AllData = false;

    override fun createPresenter(): MainActivityPresenter {
        return MainActivityPresenter()
    }

    override fun getLayoutId(): Int = R.layout.activity_npk

    override fun initView() {
        // mDataShowBeans = heartresult_view.getAdapterData()
        layout_select_user.visibility = View.VISIBLE
        layout_select_user.setOnClickListener {
            var intent = Intent(this, UserSelectActivity::class.java)
            intent.putExtra("firstCome", false)
            intent.putExtra("currentMode", Constant.MODE_PK_RED)
            startActivity(intent)
        }

        layout_setting.setOnClickListener {
            back()
        }

        val redGridLayout =
            androidx.recyclerview.widget.GridLayoutManager(instance, 2)
        newPkRedRYView.layoutManager = redGridLayout
        redAdapter = NewPkAdapter(instance,mRedCurrentShowBeans)
        newPkRedRYView.adapter = redAdapter
        newPkRedRYView.itemAnimator

        val blueGridLayout =
            androidx.recyclerview.widget.GridLayoutManager(instance, 2)
        newPkBlueRyView.layoutManager = blueGridLayout
        blueAdapter = NewPkAdapter(instance,mBlueCurrentShowBeans)
        newPkBlueRyView.adapter = blueAdapter

    }

    fun showNoupdate() {
        DialogYesOrNo(this, object : DialogYesOrNo.OnButtonClick {
            override fun onButtonClickCancel() {
                UserContans.isPause = false
            }

            override fun onButtonClickSure() {
                BaseApp.recordHashData.clear()
                UserContans.isPause = true;
                mCurrentDownTimer?.cancel()
                finish()
            }
        }, "本次课程时间过短，退出将不保存运动数据").show()
    }

    fun showDialog() {
        DialogYesOrNo(this, object : DialogYesOrNo.OnButtonClick {
            override fun onButtonClickCancel() {
                UserContans.isPause = false
            }

            override fun onButtonClickSure() {
                UserContans.isPause = true;
                mCurrentDownTimer?.cancel()
                upgradeCourseData()
            }
        }, "确认结束PK？").show()
    }

    fun back() {
        var toalTime = mCurrentHeartRateClassInfo.duration * 1000L
        if (toalTime - mRemainTime <= toalTime * 0.1) {
            showNoupdate()
        } else {
            Log.e("toalTime==", "toalTime=" + toalTime + "mRemainTime=" + mRemainTime)
            showDialog()
        }
    }


    var endTime = 0L
    var winGroup = "0"
    fun upgradeCourseData() {
        mCurrentDownTimer?.cancel()
        Logger.e(tags,"upgradeCourseData--------------------------------------")
        if (endTime != 0L) {
            endTime = System.currentTimeMillis()
        }
        AllData = true;

        //取消标签
        markSnListData()

        mActPresenter.postPk(
            mRedDataShowBeans, mBlueDataShowBeans,
            UserContans.info.id,
            "",
            "2",
            endTime, redSumCal, blueSumCal
        )
    }


    //删除一个人需要重新排序
    //添加一个人也需要重新排序

    fun UpdateAllData() {

    }

    fun addData() {

    }

    //人数没有变化的时候数据刷新
    fun updateData() {
        //AdapterUtil.setItemBg(holder.l_layout, Integer.parseInt(info.getPrecent()), mContext, mContext.getResources().getDimension(R.dimen.dp8), holder.iv_current_stren, isCard1);
    }

    var isRestart = false;
    override fun initData() {


        redViewlist.put(0, red_1)
        redViewlist.put(1, red_2)
        redViewlist.put(2, red_3)
        redViewlist.put(3, red_4)
        redViewlist.put(4, red_5)


        blueViewList.put(0, blue_1)
        blueViewList.put(1, blue_2)
        blueViewList.put(2, blue_3)
        blueViewList.put(3, blue_4)
        blueViewList.put(4, blue_5)

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
        timerScroll()
    }

    fun starCourse() {
        mRemainTime = mCurrentHeartRateClassInfo.duration * 1000
        startDownTimer()
    }

    fun restartCourse() {
        var list = CacheDataUtil.getCourseUserList()
        if (list != null && list.size > 0) {
            mDataShowBeans.addAll(list)
            dealPkData()
            // heartresult_view.updateData(mDataShowBeans, isSort, mSortType)
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
        mCurrentDownTimer?.cancel()
        HrDataObservable.getInstance().deleteObserver(this)
        unregisterReceiver(mTimeReceiver)
        AllocationApi.getAllSNSet().clear()
        UserContans.courseClearMap()
        mCurrentRemoveIndex = 0
        // heartresult_view.release()
    }


    override fun update(o: Observable?, arg: Any?) {
        super.update(o, arg)
        /*  if (UserContans.isPause) {
              return
          }
          if (o is HrDataObservable) {

              if (UserContans.classTime == 0L) {
                  endTime = System.currentTimeMillis()
                  doHallModel(arg as ConcurrentHashMap<String, Int>)
              }
              if ((System.currentTimeMillis() - UserContans.classTime) / 1000 <= UserContans.couserTime) {
                  //
                  endTime = System.currentTimeMillis()
                  doHallModel(arg as ConcurrentHashMap<String, Int>)
              }
          }*/
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

    private fun doHallModel(
        sources: ConcurrentHashMap<String, Int>
    ) {
        //开始的时间
        //查询用户信息
        doCommonHRTask(sources)
        Logger.e(tags, sn + "secondMap=" + secondMap.size)
        //最新1秒钟的数据，1秒内接收到的所有设备的所有心率 已经获取到心率数据和用户数据的 map
        var key = ""
        var pkMode = 0
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
                pkMode = userInfo!!.currentMod;
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
                dataShowBean!!.pkTeam = pkMode
                dataShowBean!!.setAllHrList(heartRate)

                Log.e("heartRate", "" + heartRate + "precent=" + precent)
                //计算每一分钟的数据
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
                        cal = HeartRateConvertUtils.hearRate2CaloriForMan(
                            heartRate,
                            age,
                            weight,
                            Constant.REFRESH_RATE,
                            Constant.UNIT_MILLS
                        )
                    } else {
                        cal = HeartRateConvertUtils.hearRate2CaloriForWoman(
                            heartRate,
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
            } else {
                dataShowBean = DevicesDataShowBean()

                //判断是否已经再线过
                val isSaved = BaseApp.recordHashData.containsKey(key)
                if(isSaved){
                    dataShowBean!!.joinTime = BaseApp.recordHashData[key]?.joinTime

                    //掉线时间
                    dataShowBean!!.time = BaseApp.recordHashData[key]?.time!!
                    dataShowBean!!.cal = BaseApp.recordHashData[key]?.cal!!
                    //平均心率强度
                    dataShowBean!!.averageHeartPercent = BaseApp.recordHashData[key]?.averageHeartPercent!!
                    dataShowBean!!.precent = BaseApp.recordHashData[key]?.precent!!
                    var precent2 = HeartRateConvertUtils.hearRate2Percent(
                        heartRate,
                        HeartRateConvertUtils.getMaxHeartRate(age).toDouble()
                    ).toInt()
                    dataShowBean!!.addStageHeart(key,precent2)
                    BaseApp.recordHashData[key]?.let { it1 -> dataShowBean!!.allHrList.addAll(it1.allHrList) }

                    val courseList = BaseApp.recordHashData[key]?.getmDatas()
                    dataShowBean!!.setmDatas(courseList)
                    dataShowBean!!.point = BaseApp.recordHashData[key]?.point!!

                }else{
                    dataShowBean!!.joinTime = System.currentTimeMillis()
                    dataShowBean!!.cal = cal
                    dataShowBean!!.addStageHeart(key, 0)
                    dataShowBean!!.time = heartRateBean!!.time
                    //设置课程id
                    dataShowBean!!.precent = HeartRateConvertUtils.hearRate2Percent(
                        heartRate,
                        HeartRateConvertUtils.getMaxHeartRate(age).toDouble()
                    ).toInt().toString()
                }
                dataShowBean!!.sortType = Constant.TYPE_DEF
                dataShowBean!!.pkTeam = pkMode
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
                if (cal < 0) {
                    cal = 0.0
                }

                //保存SN
                AllocationApi.getAllSNSet().add(key)
                mAddList.offer(dataShowBean)
                mHandler.sendEmptyMessage(ADD_DATA)
                isAddItem = true
            }
        }
        //每10s刷新数据
        dataSize = mDataShowBeans.size
        mHandler.post(Runnable {
            //  heartresult_view.isNoDevice(dataSize < 1)
            tv_man_count.setText(getString(R.string.people, "" + dataSize))
        })

        Logger.e(tags, sn + "dataSize=" + dataSize)
        //需要对数据进行分类
        if (dataSize > 0) {

            intervalTime += Constant.REFRESH_RATE
            Logger.e(tags, "doHallTask-----------")
            doHallTask()
            Logger.e(tags, "dealPkData-----------")
            CacheDataUtil.saveCourseUserBean(mDataShowBeans)
            dealPkData()
            /* if (intervalTime * Constant.REFRESH_RATE % 10 != 0) {
                 mHandler.sendEmptyMessage(UPDATE_HALL)
             } else if (intervalTime * Constant.REFRESH_RATE % 10 == 0) {

             }//每10s全部刷新一次*/
        }
    }

    var redSumCal = 0.0
    var blueSumCal = 0.0

    //红队的经验值
    var redExperience = 0.0
    //蓝队的经验值
    private var blueExperience = 0.0

    fun dealPkData() {

        var size = mDataShowBeans.size / 10
        if (mDataShowBeans.size % 10 != 0) {
            size += 1
        }
        mTotalPage = size
        setDotView(true, currentPage - 1)
        Logger.e(
            tags,
            sn + "dealPkData=" + mRedDataShowBeans.size + "mBlueDataShowBeans=" + mBlueDataShowBeans.size + ",redSumCal=" + redSumCal + "blueSumCal=" + blueSumCal
        )

        runOnUiThread {
            if (mRedDataShowBeans.size > 0) {
                mRedDataShowBeans.sort()
            }
            if (mBlueDataShowBeans.size > 0) {
                mBlueDataShowBeans.sort()
            }
            mRedCurrentShowBeans.clear()
            mBlueCurrentShowBeans.clear()

//            mRedCurrentShowBeans = mRedDataShowBeans
//            mBlueCurrentShowBeans = mBlueDataShowBeans

            mRedCurrentShowBeans.addAll(mRedDataShowBeans)
            mBlueCurrentShowBeans.addAll(mBlueDataShowBeans)
            redExperience = 0.0
            //计算红队的经验值
            mRedCurrentShowBeans.forEach {
                val redPoint = it.point;

               // redExperience = Arith.add(redExperience,redPoint)

                redExperience = Arith.add(redExperience,redPoint)
            }

            blueExperience = 0.0
            //计算蓝队的经验值
            mBlueCurrentShowBeans.forEach {
                val bluePoint = it.point
                blueExperience = Arith.add(blueExperience,it.point)
            }

//            var startRedIndex = (currentPage - 1) * 5
//            var endRedIndex = currentPage * 5
//            if (endRedIndex >= mRedDataShowBeans.size) {
//                endRedIndex = mRedDataShowBeans.size
//            }
//            for (i in startRedIndex until endRedIndex) {
//                mRedCurrentShowBeans.add(mRedDataShowBeans.get(i))
//            }
//            var startBlueIndex = (currentPage - 1) * 5
//            var endBlueIndex = currentPage * 5
//            if (endBlueIndex >= mBlueDataShowBeans.size) {
//                endBlueIndex = mBlueDataShowBeans.size
//            }
//            for (i in startBlueIndex until endBlueIndex) {
//                mBlueCurrentShowBeans.add(mBlueDataShowBeans.get(i))
//            }
//            for (i in 0 until mRedCurrentShowBeans.size) {
//                redViewlist.get(i)!!.visibility = View.VISIBLE
//                redViewlist.get(i)!!.updateAllData(mRedCurrentShowBeans.get(i))
//            }
//            for (i in 0 until mBlueCurrentShowBeans.size) {
//                blueViewList.get(i)!!.visibility = View.VISIBLE
//                blueViewList.get(i)!!.updateAllData(mBlueCurrentShowBeans.get(i))
//            }

            Logger.e(
                tags,
                sn + "mRedCurrentShowBeans=" + Gson().toJson(mRedCurrentShowBeans) + "mBlueCurrentShowBeans=" + Gson().toJson(mBlueCurrentShowBeans)
            )

            mRedCurrentShowBeans.sortBy {
                it.joinTime
            }

            mBlueCurrentShowBeans.sortBy {
                it.joinTime
            }

            blueAdapter?.notifyDataSetChanged()
            redAdapter?.notifyDataSetChanged()

            tv_red_cal.setText(HeartRateConvertUtils.doubleParseStr(redSumCal));
            tv_blue_cal.setText(HeartRateConvertUtils.doubleParseStr(blueSumCal));


            var redIntSumCal = redExperience.toInt();
            var blueIntSumCal = blueExperience.toInt();
            var sumcal = redIntSumCal + blueIntSumCal;


            if (redIntSumCal == blueIntSumCal) {
                view_progress.max = 100
                view_progress.progress = 50

                new_pk_progress.max = 100
                new_pk_progress.progress = 50

            } else {
                view_progress.max = sumcal
                view_progress.progress = redExperience.toInt()

                new_pk_progress.max = sumcal
                new_pk_progress.progress = redExperience.toInt()
            }


            newPkRedProgressTv.text = HeartRateConvertUtils.doubleParseStr(redExperience)
            newPkBlueProgressTv.text = HeartRateConvertUtils.doubleParseStr(blueExperience)

            //+"\n"+HeartRateConvertUtils.doubleParseStr(Arith.add(redExperience,blueExperience)
            newPkAllCalTv.text = "经验值"

            //进度条更新
            for (i in mRedCurrentShowBeans.size until 5) {
                redViewlist.get(i)!!.visibility = View.INVISIBLE
            }
            for (i in mBlueCurrentShowBeans.size until 5) {
                blueViewList.get(i)!!.visibility = View.INVISIBLE
            }

        }


    }

    var sn = ""
    var hrValue = 0

    private var recordHeartList = CopyOnWriteArrayList<Int>()
    @SuppressLint("LongLogTag")
    private fun doCommonHRTask(sources: ConcurrentHashMap<String, Int>) {

        sources.forEach {

            sn = it.key
            hrValue = it.value
            if (!UserContans.userInfoHashMap.containsKey(sn)) {
                if (!UserContans.mCacheMap.containsKey(sn)) {
                    //1.查询用户信息
                }
            } else {
                if (UserContans.userInfoHashMap.containsKey(sn)) {
                    //这里有两个逻辑，一个是红队的用户，一个蓝队的用户  数据选择选择了就不要弄了
                    var select = UserContans.userInfoHashMap.get(sn)!!.isSelect
                    Logger.e(tags, sn + "select=+" + select)
                    if (select) {

                        //判断是否是已经存在过，即已经上过课程，中途给停掉课程，再次上课
                        val isExist = BaseApp.recordHashData.containsKey(sn)



                        val secondHeartRateBean: SecondHeartRateBean =
                            if (UserContans.secondHeartRateBeanHashMap.containsKey(sn)) {
                                UserContans.secondHeartRateBeanHashMap.get(sn)!!
                            } else {
                                SecondHeartRateBean()
                            }


                        if(isExist){
                            recordHeartList.clear()

                            BaseApp.recordHashData[sn]?.let { it1 ->
                                recordHeartList.addAll(it1.allHrList) }
                            recordHeartList.add(hrValue)
                            secondHeartRateBean.heartList.addAll(recordHeartList)
                        }else{
                            secondHeartRateBean.heartList.add(hrValue)
                        }

                        //接收到的所有心率数据
                        secondHeartRateBean.devicesSN = sn
                        secondHeartRateBean.heart = hrValue
                        secondHeartRateBean.isTask = false
                        secondHeartRateBean.time = if(isExist) BaseApp.recordHashData[sn]?.joinTime!! else UserContans.mSnHrTime[sn]!!
                        //接收到的数据的时间戳
                        UserContans.secondHeartRateBeanHashMap.put(sn, secondHeartRateBean)

                    } else {
                        if (UserContans.secondHeartRateBeanHashMap.containsKey(sn)) {
                            UserContans.secondHeartRateBeanHashMap.remove(sn)
                        }
                    }

                }


            }
        }

        Log.e(tags,"--------处理完成的secondMap="+Gson().toJson(secondMap))
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

        try {
            //判断设备是否掉线，掉线的移除
            val currentTime = System.currentTimeMillis()
            //  var isRemove = false
            //接收数据的时间
            reMoveList.clear()
            isRemove = false

            mRedDataShowBeans.clear()
            mBlueDataShowBeans.clear()
            redSumCal = 0.0
            blueSumCal = 0.0
            for (i in 0 until mDataShowBeans.size) {
                val dropBean = mDataShowBeans.get(i);
                if (dropBean.pkTeam == Constant.MODE_PK_RED) {
                    redSumCal += dropBean.cal
                    mRedDataShowBeans.add(dropBean)
                  //  redExperience = Arith.add(redExperience,dropBean.point)
                } else {
                    blueSumCal += dropBean.cal
                    mBlueDataShowBeans.add(dropBean)
                  //  blueExperience = Arith.add(redExperience,dropBean.point)
                }

                //已经掉线，直接移除
                sn = dropBean.devicesSN
                if (UserContans.mSnHrTime.containsKey(sn)) {
                    time = UserContans.mSnHrTime.get(sn)!!
                    if (currentTime - time >= Constant.DROP_TIME) {
                        UserContans.mSnHrMap.put(sn, 0)
                    }
                }
                //如果两分钟没有数据，就把sn值的
                Logger.e("drop", "currentTime - time=" + (currentTime - time))
                if (UserContans.userInfoHashMap.containsKey(sn)) {
                    if (!UserContans.userInfoHashMap.get(sn)!!.isSelect) {
                        isRemove = true
                        reMoveList.add(i)
                        AllocationApi.getAllSNSet().remove(dropBean.devicesSN)
                    }
                }
            }
            if (isRemove) {
                mHandler.sendEmptyMessage(REMOVE_DATA)
            } else {
                mHandler.sendEmptyMessage(UPDATE_HALL)
            }

        } catch (e: CloneNotSupportedException) {

        }
    }

    fun showCourseModel() {
        mCurrentHeartRateClassInfo = UserContans.info
        if (mCurrentHeartRateClassInfo == null) {
            return
        }
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

    lateinit var mCurrentHeartRateClassInfo: CourseInfo
    private var mRemainTime = 0
    private var mCurrentRangeIndex = 0
    private var mUnfinishedTime = 0

    var currentCourseDetail: CourseDetail? = null
    var lastCourseDetail: CourseDetail? = null

    val instance by lazy { this } //这里使用了委托，表示只有使用到instance才会执行该段代码


    inner class CourseDownTimer(var millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {


        override fun onFinish() {
            LogUtil.e(tags,"--onFinish=" + ",isPase" + UserContans.isPause + "mRemainTime / 1000" + mRemainTime / 1000)

            endTime = System.currentTimeMillis()

            if (mRemainTime / 1000 == 0) {
                //把数据清零
                doHallModel(UserContans.mSnHrMap)
            }

            autoDialogView = ShowEmptyDialog(instance)
            autoDialogView!!.show()
            autoDialogView!!.setShowTime(3 * 1000)
            autoDialogView!!.setContentTvTxt("PK结束")
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

            LogUtil.e(tags,"millisInFuture=" + millisInFuture / 1000 + " millisUntilFinished=" + millisUntilFinished / 1000 + "mRemainTime/1000=" + mRemainTime / 1000)

            mUnfinishedTime = millisUntilFinished.toInt()

            var remove = UserContans.couserTime  - mRemainTime / 1000
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
            if (millisUntilFinished / 1000 != 0L) {
                updateCourseView(millisUntilFinished)
            }

        }

    }


    fun removeview(moveTime: Int) {
        coursematchpoint_view.setStrTime(TimeUtil.getFormatTimemmss(moveTime * 1000L))
        //  coursematchpoint_view.moveArrow((mRemainTime / 1000).toLong())
    }

    private val mDuration = 1000L
    var mCurrentDownTimer: CourseDownTimer? = null
    fun reStartDowTimer() {
        LogUtil.e("mCurrentRangeIndex=" + mCurrentRangeIndex + " size=" + mCurrentHeartRateClassInfo.targetRateArray.size + " ,(mCurrentHeartRateClassInfo.duration * 1000 - mRemainTime) / 1000L=" + (mCurrentHeartRateClassInfo.duration * 1000 - mRemainTime) / 1000L)

        mCurrentDownTimer =
            CourseDownTimer(
                (mCurrentHeartRateClassInfo.duration  - (mCurrentHeartRateClassInfo.duration * 1000 - mRemainTime) / 1000L) * 1000L,
                mDuration
            )


        mCurrentDownTimer?.start()

    }

    //需要考虑暂停时候的逻辑
    fun startDownTimer() {
        mCurrentDownTimer =
            CourseDownTimer(
                    (mCurrentHeartRateClassInfo.duration ) * 1000L,
                mDuration
            )
        mCurrentDownTimer?.start()
        //}
    }

    var mStateControlTime = 0L
    fun updateCourseView(millisUntilFinished: Long) {
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


    var mTotalPage = 0

    /**
     * 轮播点的绘制
     */
    private var mDotViews = ArrayList<ImageView>();

    private fun setDotView(isReset: Boolean = true, indexChecked: Int = 0) {
//        Log.e("test", "isrReset=" + isReset + "indexChecked=" + indexChecked + "dotSize=" + mDotViews.size)
        if (mTotalPage < 1) {
            return;
        }
        if (isReset) {
            ll_point.removeAllViews()
            mDotViews.clear()
            for (i in 0..(mTotalPage - 1)) {
                val image = ImageView(this)
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.topMargin = DisplayUtils.dip2px(this, 8f)
                if (i == indexChecked) {
                    image.setImageResource(R.drawable.icon_point_select)
                } else {
                    image.setImageResource(R.drawable.icon_point_unselect)
                }
                ll_point.addView(image, params)
                mDotViews.add(image)
            }
        } else {
            for (i in 0..(mTotalPage - 1)) {
                if (i == indexChecked) {
                    mDotViews.get(i).setImageResource(R.drawable.icon_point_select)
                } else {
                    mDotViews.get(i).setImageResource(R.drawable.icon_point_unselect)
                }
            }
        }
    }

    var currentPage = 1

    private var mSecondDisposable: Disposable? = null
    fun timerScroll() {
        mSecondDisposable = io.reactivex.Observable.interval(10, 10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread()) // 由于interval默认在新线程，所以我们应该切回主线程
            .subscribe(Consumer {
                if (mTotalPage == currentPage) {
                    currentPage = 1
                } else {
                    currentPage++
                }
            })
    }



    //取消sn标签
    private fun markSnListData(){
        //取消累计记录
        BaseApp.recordHashData.clear()
        if(mRedDataShowBeans.isEmpty())
            return

        val para = HashMap<String,List<String>>()

        //遍历集合，得到已经选择的用户
        val selectList = emptyList<String>()
        val unSelectList = arrayListOf<String>()
        Log.e(tags,"--------真实的="+Gson().toJson(mRedDataShowBeans)+" "+Gson().toJson(mBlueDataShowBeans))

        for(index in 0 until mRedDataShowBeans.size ){
           unSelectList.add(mRedDataShowBeans[index].devicesSN)
        }

        for(index in 0 until mBlueDataShowBeans.size){
            unSelectList.add(mBlueDataShowBeans[index].devicesSN)
        }

        para["markList"] = selectList
        para["unmarkList"] = unSelectList

        Log.e(tags,"------c参数="+para.toString())
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json"), Gson().toJson(para))
        RetrofitHelper.service.markSnActiveTags(requestBody)
            .subscribeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<BaseResponse<Boolean>>(){
                override fun onSuccess(t: BaseResponse<Boolean>?) {
                    if (t != null) {
                        Log.e(tags,"---是否成功="+t.data)
                        if(t.data == true){
                            UserContans.markTagsMap.clear()
                        }
                    }
                }

                override fun dealError(msg: String?) {
                    super.dealError(msg)
                }
            })

    }

}
