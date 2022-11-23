package com.jkcq.hrwtv.wu.newversion.activity.mamager

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.jkcq.managersetting.ApkDownLoadManager
import com.jkcq.managersetting.OnDialogClickListener
import com.jkcq.managersetting.PreferenceUtil
import com.jkcq.managersetting.bean.*
import com.jkcq.managersetting.util.DeviceUtil
import com.jkcq.managersetting.view.DialogFactory
import com.jkcq.managersetting.view.TipsDialog
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ToastUtils
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.http.widget.BaseObserver
import com.jkcq.hrwtv.ui.view.DialogLogin
import com.jkcq.hrwtv.ui.view.DialogVersion
import com.jkcq.hrwtv.ui.view.SigleDialog
import com.jkcq.hrwtv.wu.manager.Preference
import com.jkcq.hrwtv.wu.newversion.observa.UnRegObservable
import com.jkcq.hrwtv.http.RetrofitHelper
import com.jkcq.hrwtv.http.bean.BaseResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_manager_main_layout.*
import java.util.*


//设置页面
class ManagerSettingActivity : AppCompatActivity(), ManagerMainView,
    View.OnClickListener {


    var mToken: String by Preference(Preference.token, "")
    var mbrandName: String by Preference(Preference.brandName, "")
    var mbrandId: String by Preference(Preference.brandId, "")
    var mclubId: String by Preference(Preference.clubId, "")
    var mclubName: String by Preference(Preference.clubName, "")
    var mUserId: String by Preference(Preference.userId, "")
    var mNikeName: String by Preference(Preference.nickname, "")

    override fun onRespondError(message: String?) {}

    internal var cleanCacheDialog: Dialog? = null

    internal var dialog: TipsDialog? = null
    private var apkDownloadUrl = ""
    private var isRegister = false

    private val mRegisterMap = HashMap<String, String>()
    private var mClubInfos: ArrayList<DeviceTypeInfo>? = ArrayList()
    private var mClassroomInfos: ArrayList<ClassRoomInfo>? = ArrayList()
    private var mBrandInfos: ArrayList<BrandInfo>? = ArrayList()
    private val mHandler = Handler()
    private var isGetClub = false
    private var mActPresenter: ManagerSettingPresenter? = null


    private val clickListener =
        OnDialogClickListener { type ->
            if (dialog != null && dialog!!.isShowing) {
                dialog!!.dismiss()
            }
            when (type) {
                0 -> {
                }
                1 -> {
                    cleanCacheDialog = DialogFactory.getInstance().createDialog(
                        this@ManagerSettingActivity,
                        com.jkcq.managersetting.R.mipmap.icon_yello_loading,
                        getString(com.jkcq.managersetting.R.string.unregister_ing)
                    )
                    cleanCacheDialog!!.show()
                }
                2 -> {
                    cleanCacheDialog = DialogFactory.getInstance().createDialog(
                        this@ManagerSettingActivity,
                        com.jkcq.managersetting.R.mipmap.icon_yello_loading,
                        resources.getString(com.jkcq.managersetting.R.string.clean_cache_ing)
                    )
                    cleanCacheDialog!!.show()
                    mActPresenter?.clearCache(this@ManagerSettingActivity)
                }
                3 -> ApkDownLoadManager(this@ManagerSettingActivity).startDownLoad(apkDownloadUrl)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_main_layout)
//        if ("".equals(PreferenceUtil.getInstance().getString(PreferenceUtil.KEY_MAC))) {
        PreferenceUtil.getInstance()
            .putString(PreferenceUtil.KEY_MAC, DeviceUtil.getMac(this@ManagerSettingActivity))
//        }
        mActPresenter = ManagerSettingPresenter(this)
        init()
        tv_unregister.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                Log.e("test", "ll_manager_state =$hasFocus")
                if (hasFocus) {
                    tv_unregister.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_selected_bg)
                    ll_manager_state.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_manage_fouce_bg_unselecte_bg)
                } else {
                    tv_unregister.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_unselected_bg)
                    ll_manager_state.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_manage_bg_unselecte_bg)
                }
            }
        })
        layout_update.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    layout_version.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_manage_fouce_bg_unselecte_bg)
                    layout_update.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_layout_selected_bg)

                } else {
                    layout_version.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_manage_bg_unselecte_bg)
                    layout_update.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_layout_unselected_bg)
                }
            }
        })

        layout_clear_cache.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    layout_clear_cache.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_layout_selected_bg)
                    layout_clear.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_manage_fouce_bg_unselecte_bg)
                } else {
                    layout_clear_cache.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_layout_unselected_bg)
                    layout_clear.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_manage_bg_unselecte_bg)
                }
            }
        })
        tv_exit.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    tv_exit.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_selected_bg)
//                    tv_exit.setTextColor(Color.WHITE)
                } else {
                    tv_exit.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_unselected_bg)
//                    tv_exit.setTextColor(resources.getColor(R.color.green_bg))
                }
            }
        })
    }

    override fun onDestroy() {
        mActPresenter?.release()
        mRegisterMap.clear()
        mClubInfos!!.clear()
        mClassroomInfos!!.clear()
        isGetClub = false
        isRegister = false
        super.onDestroy()
    }

    private fun init() {

//        ll_manager_state.setVisibility(View.INVISIBLE)
        tv_version_name.setText(getString(com.jkcq.managersetting.R.string.app_version, AppUtils.getAppVersionName()))
        tv_unregister.setOnClickListener(this)
        layout_update.setOnClickListener(this)
        layout_clear_cache.setOnClickListener(this)
        tv_exit.setOnClickListener(this)
        ll_manager_state.setOnClickListener(this)


        tv_club_name.text = if(!TextUtils.isEmpty(mclubName)) "注册门店: $mclubName" else "--"
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.layout_update -> {
                //              mActPresenter.checkUpdate(Constant.DEVICE_TYPE);
                //111为心率墙
                //112接收器
                checkVersion("heart_rate_wall")
                // mActPresenter?.checkUpdate("heart_rate_wall")
            }
            R.id.layout_clear_cache -> {

                var dialog = AlertDialog.Builder(this).setTitle("提示").setMessage("确定要清除缓存吗？")
                    .setPositiveButton("确认", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog?.dismiss()
                            ToastUtils.showLong("清除成功")
                        }

                    })
                    .setNegativeButton("取消", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog?.dismiss()
                        }

                    }).create()
                dialog.show()
//                dialog = TipsDialog(this, 2, resources.getString(R.string.dialog_cache_tips))
//                dialog!!.setOnDialogClickListener(clickListener)
//                dialog!!.show()
            }
            R.id.tv_exit -> this.finish()

            R.id.ll_manager_state, R.id.tv_unregister -> {
                showUnbindView()
            }
        }//                Beta.checkUpgrade();
    }


    private fun showUnbindView() {

        DialogLogin(this, DialogLogin.OnClickShowView {
            if (it) {
                UnRegObservable.getInstance().sendUnRe()
                finish()
            }
        }).show()
    }


    open fun checkVersion(devicesSN: String) {


        //查询SN值对应的userinfo
        RetrofitHelper.noAuthservice.getVersionInfo(
            devicesSN
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<BaseResponse<VersionInfo>>() {
                override fun onSuccess(baseResponse: BaseResponse<VersionInfo>) {
                    baseResponse.data?.let {

                        onCheckUpdateSuccess(it)

                    }
                }
            })

    }


    override fun onCheckUpdateSuccess(versionBean: VersionInfo) {
        val currentCode = AppUtils.getAppVersionCode()
        if (versionBean.appVersionCode > currentCode && versionBean.isFocusUpdate) {

            var str = "是否更新到最新版本固件" + versionBean.appVersionName + "?\n" + getString(
                com.jkcq.managersetting.R.string.app_version,
                AppUtils.getAppVersionName()
            )

            DialogVersion(this, object : DialogVersion.OnButtonClick {
                override fun onButtonClickCancel() {
                }

                override fun onButtonClickSure() {
                    ApkDownLoadManager(this@ManagerSettingActivity).startDownLoad(versionBean.downloadUrl)
                }
            }, str).show()


        } else {

            var str = getString(
                com.jkcq.managersetting.R.string.app_version,
                AppUtils.getAppVersionName()
            ) + "\n" + getString(com.jkcq.managersetting.R.string.is_latest_version)
            SigleDialog(this, object : SigleDialog.OnButtonClick {
                override fun onButtonClickCancel() {
                }

                override fun onButtonClickSure() {
                }
            }, str).show()

        }
    }

    override fun onClearCacheSuccess() {
        if (cleanCacheDialog != null && cleanCacheDialog!!.isShowing) {
            cleanCacheDialog!!.dismiss()
        }
        DialogFactory.getInstance().createDialog(this, com.jkcq.managersetting.R.mipmap.icon_finish, "清除成功").show()
    }


}
