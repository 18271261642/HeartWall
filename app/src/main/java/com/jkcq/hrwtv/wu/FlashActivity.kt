package com.jkcq.hrwtv.wu

import android.content.*
import android.os.Build
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.jkcq.managersetting.PreferenceUtil
import com.blankj.utilcode.util.TimeUtils
import com.google.gson.Gson
import com.hjq.permissions.XXPermissions
import com.jkcq.hrwtv.AllocationApi
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.app.BaseApp
import com.jkcq.hrwtv.base.mvp.BaseMVPActivity
import com.jkcq.hrwtv.configure.Constant
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean
import com.jkcq.hrwtv.heartrate.bean.UserBean
import com.jkcq.hrwtv.heartrate.model.MainActivityView
import com.jkcq.hrwtv.heartrate.presenter.MainActivityPresenter
import com.jkcq.hrwtv.http.bean.*
import com.jkcq.hrwtv.http.widget.BaseObserver
import com.jkcq.hrwtv.service.BaseMqttservice
import com.jkcq.hrwtv.service.MyMqttService
import com.jkcq.hrwtv.service.UserContans
import com.jkcq.hrwtv.ui.view.DialogReg
import com.jkcq.hrwtv.ui.view.DialogVersion
import com.jkcq.hrwtv.util.*
import com.jkcq.hrwtv.wu.manager.Preference
import com.jkcq.hrwtv.wu.mvp.FlashContract
import com.jkcq.hrwtv.wu.mvp.FlashPresenter
import com.jkcq.hrwtv.wu.newversion.activity.mamager.ManagerSettingActivity
import com.jkcq.hrwtv.wu.newversion.observa.UnRegObservable
import com.jkcq.hrwtv.wu.obsever.AddUseObservable
import com.jkcq.hrwtv.wu.obsever.HrDataObservable
import com.jkcq.hrwtv.http.RetrofitHelper
import com.jkcq.hrwtv.http.bean.BaseResponse
import com.jkcq.hrwtv.service.OperateUserSnService
import com.jkcq.hrwtv.test.TestOne
import com.jkcq.hrwtv.ui.view.ShowEmptyDialog
import com.jkcq.hrwtv.wu.newversion.activity.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_flash.*
import kotlinx.android.synthetic.main.activity_flash.layout_setting
import kotlinx.android.synthetic.main.activity_flash.tv_name
import kotlinx.android.synthetic.main.activity_flash.tv_time
import kotlinx.android.synthetic.main.activity_ncourse.*
import kotlinx.android.synthetic.main.ninclude_title.*
import okhttp3.MediaType
import okhttp3.RequestBody
import timber.log.Timber
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap

//首页面
class FlashActivity : BaseMVPActivity<FlashContract.FlashView, FlashPresenter>(),
    FlashContract.FlashView, MainActivityView {

    private val tags = "FlashActivity"


    private val mPreset: MainActivityPresenter by lazy {
        MainActivityPresenter()
    }


    var mToken: String by Preference(Preference.token, "")
    var mbrandName: String by Preference(Preference.brandName, "")
    var mbrandId: String by Preference(Preference.brandId, "")
    var mclubId: String by Preference(Preference.clubId, "")
    var mclubName: String by Preference(Preference.clubName, "")
    var mUserId: String by Preference(Preference.userId, "")
    var mNikeName: String by Preference(Preference.nickname, "")
    var reMoveList = mutableListOf<String>()

    var currentFocusView: View? = null
    var isReg = false;
    var strStringSn = StringBuffer()

    val instance by lazy { this }
    private var autoDialogView : ShowEmptyDialog ?= null


    override fun createPresenter(): FlashPresenter = FlashPresenter()

    override fun getLayoutId(): Int = R.layout.activity_flash


    override fun onResume() {
        super.onResume()



        Timber.e("---------version="+Build.VERSION.SDK_INT)


        isReg = !TextUtils.isEmpty(mToken)
        AllocationApi.getAllSNSet().clear()
        UserContans.clearMap()
        Log.e("onResume", "isReg" + isReg)


        doAgainRegister()

        if (isReg) {
            mActPresenter?.getClubInfo()
        } else {
            mActPresenter?.getClubInfo()
        }
        showClubnam()

        unMarkTags()
    }

    var dialogReg : DialogReg ?=null

    private fun doAgainRegister(){
        if(dialogReg == null)
            dialogReg = DialogReg(this)
        val uName = PreferenceUtil.getInstance().getString("user_name")
        val uPwd = PreferenceUtil.getInstance().getString("user_pwd")
        if(!TextUtils.isEmpty(uName) && !TextUtils.isEmpty(uPwd)){
            dialogReg!!.regDevice(uName,uPwd
            ) {
               // clearReClub()
                isReg = false
                ToastUtils.showToast(instance,"账号被禁用，请重新登录!")
                showRegDialog() }
        }

    }


    private fun unMarkTags(){
        //清楚记录掉线再上线记录的数据
        BaseApp.recordHashData.clear()


        //判断已打标签是否为空，不为空取消标签
        if(UserContans.privateMarkTagsMap.isEmpty()){
            return
        }
        val para = HashMap<String,List<String>>()

        //遍历集合，得到已经选择的用户
        val selectList = emptyList<String>()
        val unSelectList = arrayListOf<String>()
        UserContans.privateMarkTagsMap.forEach { (t: String?, u: Int?) ->
            unSelectList.add(t)
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
                        if(t.data == true){
                            UserContans.privateMarkTagsMap.clear()
                        }
                    }
                }

                override fun dealError(msg: String?) {
                    super.dealError(msg)
                }
            })
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

    fun showClubnam() {
        if (TextUtils.isEmpty(mclubName)) {
            tv_name.visibility = View.INVISIBLE
        } else {
            tv_name.visibility = View.VISIBLE
            tv_name.text = mclubName
        }
    }

    override fun initView() {
        UnRegObservable.getInstance().addObserver(this)
        LogUtil.e("mac" + DeviceUtil.getMac(this))
        tv_time.text = TimeUtils.getNowString(SimpleDateFormat("HH:mm", Locale.getDefault()))

        //系统设置
        layout_setting.setOnClickListener {
            if (isReg) {
                startActivity(Intent(this, ManagerSettingActivity::class.java))
            } else {
                showRegDialog()
            }
        }
        layout_setting.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                currentFocusView = layout_setting
                LogUtil.e("currentFocusView=" + currentFocusView)
                layout_setting.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_selected_bg)
            } else {
                layout_setting.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_unselected_bg)
            }
        }

        //大厅模式
        layout_hall.setOnClickListener {    //大厅模式
            // startActivity(Intent(this@FlashActivity, CourseSortActivity::class.java))
            if (isReg) {
                CacheDataUtil.sHeartModel = Constant.HALL_MODEL
                //这里把大于两分钟都没有啥更新数据的数据清除掉

                startActivity(Intent(this@FlashActivity, NHallActivity::class.java))
                clearUserInfo()
                //   startActivity(Intent(this@FlashActivity, PkResultActivity::class.java))
            } else {
                showRegDialog()
            }


        }

        //课程模式
        layout_course.setOnClickListener {
            if (isReg) {

                CacheDataUtil.sHeartModel = Constant.COURSE_MODEL
                var intent = Intent(this@FlashActivity, CourseSelectActivity::class.java)
                intent.putExtra("isPK", false)
                startActivity(intent)
                CacheDataUtil.sHeartModel = Constant.COURSE_MODEL
                // startActivity(Intent(this@FlashActivity, NCourseActivity::class.java))
            } else {
                showRegDialog()
            }

        }

        //PK模式
        layout_pk.setOnClickListener {
            if (isReg) {

                var intent = Intent(this@FlashActivity, CourseSelectActivity::class.java)
                intent.putExtra("isPK", true)
                startActivity(intent)
                CacheDataUtil.sHeartModel = Constant.COURSE_MODEL
                // startActivity(Intent(this@FlashActivity, NCourseActivity::class.java))
            } else {
                showRegDialog()
            }


        }
        layout_hall.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                currentFocusView = layout_hall
                LogUtil.e("currentFocusView= setOnFocusChangeListener$currentFocusView")
                layout_hall.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_cheak)
            } else {
                layout_hall.setBackgroundColor(this.resources.getColor(com.jkcq.hrwtv.base.R.color.transparent))
            }
        }
        layout_course.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                currentFocusView = layout_course
                LogUtil.e("currentFocusView=" + currentFocusView)
                layout_course.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_cheak)
            } else {
                layout_course.setBackgroundColor(this.resources.getColor(com.jkcq.hrwtv.base.R.color.transparent))
            }
        }
        layout_pk.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                currentFocusView = layout_pk
                LogUtil.e("currentFocusView=" + currentFocusView)
                layout_pk.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_cheak)
            } else {
                layout_pk.setBackgroundColor(this.resources.getColor(com.jkcq.hrwtv.base.R.color.transparent))
            }
        }

        LogUtil.e(
            "screenwith=" + DisplayUtils.getScreenWidth(this) + "screenHeight=" + DisplayUtils.getScreenHeight(
                this
            )
        )
        layout_setting.requestFocus()



        tv_name.setOnLongClickListener {
           // startActivity(Intent(this@FlashActivity, NewPkActivity::class.java))
//            val intent = Intent();
//            intent.action = OperateUserSnService.CLEAR_SM_MARK_ACTION
//            sendBroadcast(intent)
            startActivity(Intent(this@FlashActivity,TestOne::class.java))
            return@setOnLongClickListener true
        }

        requestPermission()
    }


    fun showRegDialog() {
        layout_top.visibility = View.INVISIBLE
        layout_content.visibility = View.INVISIBLE
        mToken = ""
        DialogReg(this, object : DialogReg.OnClickShowView {
            override fun showView(isSuccess: Boolean, token: String) {
                hideDialog(isSuccess)
                if (isSuccess) {
                    Log.e("showRegDialog", "mToken" + mToken + ",token=" + token)
                    mToken = token
                    isReg = !TextUtils.isEmpty(mToken)
                    mActPresenter?.getClubInfo()
                }
            }
        }).show()
    }


    fun hideDialog(isSuccess: Boolean) {
        layout_top.visibility = View.VISIBLE
        layout_content.visibility = View.VISIBLE
        if(currentFocusView != null){
            LogUtil.e("currentFocusView=" + currentFocusView)
            currentFocusView!!.isFocusable = true
            currentFocusView!!.isFocusableInTouchMode = true
            currentFocusView!!.requestFocus()
        }



        /*if (isSuccess) {
            startActivity(Intent(this, ManagerSettingActivity::class.java))
        }*/
    }

    override fun initData() {

        HrDataObservable.getInstance().addObserver(this)
        isReg = true
        BaseApp.DEVICE_TOKEN = DeviceUtil.getMac(BaseApp.getApp())
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_TIME_TICK)
        registerReceiver(mTimeReceiver, filter)

    }

    fun clearReClub() {
        mToken = ""
        mbrandId = ""
        mbrandName = ""
        mclubId = ""
        mclubName = ""
    }

    private var myMqttService: BaseMqttservice? = null
    private val conn = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {

        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            try {
                //返回一个MsgService对象
                myMqttService = (service as MyMqttService.MyMqttServiceBinder).collectionServices
                myMqttService?.setTask(false)
                //  myMqttService?.setOnHeartRateListener(heartRateListener)
            }catch (e : Exception){
                e.printStackTrace()
            }

        }
    }


    //注销后需要把连接取消

    override fun getClubSuccess(info: AloneClubInfoBean) {

        mbrandId = info.brandId
        mbrandName = info.brandName
        mclubId = info.clubId
        mclubName = info.clubName
        mUserId = info.id
        mNikeName = info.nickname
        mToken = info.token
        isReg = !TextUtils.isEmpty(mToken)
        getClubInfo()
        showClubnam()

        var mRedDataShowBeans = ArrayList<DevicesDataShowBean>();
        var mBlueDataShowBeans = ArrayList<DevicesDataShowBean>();
        var list = CacheDataUtil.getCourseUserList()
        if (list != null && list.size > 0) {
            //异常退出是否重新进入

            var str = "当前有正在进行的课程，是否继续上课?"

            DialogVersion(this, object : DialogVersion.OnButtonClick {
                override fun onButtonClickCancel() {

                    //保存数据

                    var coureseBean = CacheDataUtil.getCourseInfo()
                    var courseId = ""
                    if (coureseBean != null) {
                        courseId = coureseBean.id
                    } else {
                        return
                    }

                    if (list.get(0).pkTeam == 0) { //课程模式
                        mPreset.postCourse(
                            list as ArrayList<DevicesDataShowBean>?,
                            courseId,
                            "2",
                            System.currentTimeMillis()
                        )

                    } else {
                        var redSumCal = 0.0
                        var blueSumCal = 0.0
                        for (i in 0 until list.size) {
                            val dropBean = list.get(i);
                            if (dropBean.pkTeam == Constant.MODE_PK_RED) {
                                redSumCal += dropBean.cal
                                mRedDataShowBeans.add(dropBean)
                            } else {
                                blueSumCal += dropBean.cal
                                mBlueDataShowBeans.add(dropBean)
                            }
                            //已经掉线，直接移除
                        }
                        mPreset.postPk(
                            mRedDataShowBeans, mBlueDataShowBeans,
                            courseId,
                            "",
                            "2",
                            System.currentTimeMillis(), redSumCal, blueSumCal
                        )
                    }
                    CacheDataUtil.clearCourseList()
                }

                override fun onButtonClickSure() {


                    CacheDataUtil.getUserMap()
                    if (list.get(0).pkTeam == 0) { //课程模式
                        var intent = Intent(
                            this@FlashActivity,
                            NCourseActivity::class.java
                        )
                        intent.putExtra("isRestart", true)
                        startActivity(
                            intent
                        )
                    } else {
                        //PK模式
//                        var intent = Intent(
//                            this@FlashActivity,
//                            NPkActivity::class.java
//                        )

                        var intent = Intent(
                            this@FlashActivity,
                            NewPkActivity::class.java
                        )

                        intent.putExtra("isRestart", true)
                        startActivity(
                            intent
                        )

                    }
                }
            }, str, "进入").show()

        } else {
            CacheDataUtil.clearCourseList()
        }
        // PreferenceUtil.getInstance().putString(PreferenceUtil.CLUB_ID, info.uid);
        /* if (Constant.REGIMENT_ROOM == info.funcId) {
             CacheDataUtil.sHeartModel = Constant.COURSE_MODEL
             startActivity(Intent(this@FlashActivity, NCourseActivity::class.java))
         } else if (Constant.PAD_BICYCLE_DEVICE == info.funcId) {
             CacheDataUtil.sHeartModel = Constant.COURSE_MODEL
             startActivity(Intent(this@FlashActivity, NCourseActivity::class.java))
         } else {
             CacheDataUtil.sHeartModel = Constant.HALL_MODEL
 //            startActivity(Intent(this@FlashActivity, HallActivity::class.java))
             startActivity(Intent(this@FlashActivity, NHallActivity::class.java))
         }*/
        // finish()
    }

    override fun getClubFailed() {
        //showClubnam()
        LogUtil.e("getClubFailed")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    /*fun showRegDialog() {
        val mMenuViewBirth: BaseDialog = BaseDialog.Builder(this)
                .setContentView(R.layout.dialog_user_alone_reg_layout)
                .fullWidth()
                .setCanceledOnTouchOutside(false)
                .fromBottom(true)
                .setOnClickListener(R.id.btn_cancel, DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.cancel() })
                .setOnClickListener(R.id.btn_sure, DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.cancel() })
                .show()

    }*/
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!DoubleClickUtil.getInstance().backExit(context)) {
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mTimeReceiver)
        HrDataObservable.getInstance().deleteObserver(this)
        clearServcie()
        AllocationApi.getAllSNSet().clear()
        UnRegObservable.getInstance().deleteObserver(this)
    }

    fun clearServcie() {
        if (myMqttService != null) {
            myMqttService = null
            if (conn != null) {
                unbindService(conn)
            }
        }
    }

    fun getClubInfo() {

        if (myMqttService != null) {
            return
        }
        val intent = Intent(this@FlashActivity, MyMqttService::class.java)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

    var count = 0

    override fun update(o: Observable?, arg: Any?) {
        super.update(o, arg)
        if (o is HrDataObservable) {
            if (count == 0 || count % 10 == 0) {    //每10s刷新一次
                doCommonHRTask(arg as ConcurrentHashMap<String, Int>)
            }
            count++
            if (count > 10) {
                count = 0
            }
        } else if (o is UnRegObservable) {
            clearServcie()
            clearReClub()
        }
    }

    var sn = "";
    var hrValue = 0
    private fun doCommonHRTask(sources: ConcurrentHashMap<String, Int>) {

        Timber.e("ANT发送过来的SN码="+sources.toString())

        strStringSn.delete(0, strStringSn.length)
        sources.forEach {
            sn = it.key
            hrValue = it.value

            if (!UserContans.userInfoHashMap.containsKey(sn)) {
                if (!UserContans.mCacheMap.containsKey(sn)) {
                    //1.查询用户信息
                    strStringSn.append(sn).append(",")
                }
            }
            //总数据
        }
        if (strStringSn.isNotEmpty()) {
            strStringSn.deleteCharAt(strStringSn.lastIndex)
            uploadDevciesSN(strStringSn.toString())
        }
    }

    fun clearUserInfo() {
        reMoveList.clear()
        UserContans.mSnHrMap.forEach {
            if (UserContans.userInfoHashMap.containsKey(it.key)) {
                if (System.currentTimeMillis() - UserContans.mSnHrTime.get(it.key)!! > Constant.DROP_wall_TIME) {
                    reMoveList.add(it.key)
                }
            }
        }
        reMoveList.forEach {
            UserContans.mSnHrMap.remove(it)
            UserContans.userInfoHashMap.remove(it)
            UserContans.mSnHrTime.remove(it)
        }
    }


    open fun uploadDevciesSN(devicesSN: String) {

        //Log.e(tags,"---22----ANT发送过来的SN码="+devicesSN)

        //查询SN值对应的userinfo
        RetrofitHelper.service.getSnListToUserInfo(
            devicesSN
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<BaseResponse<List<UserBean>>>() {
                override fun onSuccess(baseResponse: BaseResponse<List<UserBean>>) {

                    Log.e(tags,"-----获取成功="+Gson().toJson(baseResponse))


                    baseResponse.data?.let {
                        CacheDataUtil.getUserMap()
                        it.forEach {
                            if (!TextUtils.isEmpty(it.id)) {
                                it.age =
                                    UserInfoUtil.parseAge(it.birthday)
                                it.joinTime = System.currentTimeMillis()
                                if (!UserContans.userInfoHashMap.containsKey(it.sn)) {
                                    UserContans.userInfoHashMap.put(it.sn, it)
                                }
                                //动态添加数据发送广播
                            }
                            //如果是异常退出

                            UserContans.mCacheMap.put(it.sn, it.sn)
                        }
                        Log.e("AddUseObservable", "addUserNotify1")


                        CacheDataUtil.saveUserMap()

                        AddUseObservable.getInstance().addUserNotify()

                    }
                }
            })

    }

    override fun getClubSuccess(info: ClubInfo?) {
    }

    override fun getCourseListSuccess(courseInfoList: MutableList<CourseList.CourseInfo>?) {
    }

    override fun getAllUserSuccess(userInfos: MutableList<CourseUserInfo>?) {
    }

    override fun uploadAllDataSuccess() {
    }


    //请求权限
    private fun requestPermission(){
        XXPermissions.with(this).permission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE).request { permissions, all ->  }
    }
}
