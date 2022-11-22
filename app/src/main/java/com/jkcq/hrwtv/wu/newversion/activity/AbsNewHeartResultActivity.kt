package com.jkcq.hrwtv.wu.newversion.activity

import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import com.jkcq.hrwtv.app.BaseApp
import com.jkcq.hrwtv.base.mvp.BaseMVPActivity
import com.jkcq.hrwtv.configure.Constant
import com.jkcq.hrwtv.eventBean.EventConstant
import com.jkcq.hrwtv.heartrate.model.MainActivityView
import com.jkcq.hrwtv.heartrate.presenter.MainActivityPresenter
import com.jkcq.hrwtv.ui.view.DialogLogin
import com.jkcq.hrwtv.util.DoubleClickUtil
import com.jkcq.hrwtv.util.LogUtil
import com.jkcq.hrwtv.wu.manager.Preference
import com.jkcq.hrwtv.wu.newversion.AdapterUtil
import com.jkcq.hrwtv.wu.newversion.PopupWindowFactory
import com.jkcq.hrwtv.wu.newversion.TypeEvent
import com.jkcq.hrwtv.wu.newversion.activity.mamager.ManagerSettingActivity
import com.jkcq.hrwtv.wu.newversion.animation.AnimationUtil
import com.jkcq.hrwtv.wu.newversion.view.BaseHeartResultView
import com.jkcq.hrwtv.wu.newversion.view.SelectPopupWindow
import kotlinx.android.synthetic.main.ninclude_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

abstract class AbsNewHeartResultActivity :
    BaseMVPActivity<MainActivityView, MainActivityPresenter>(),
    Observer {

    var mToken: String by Preference(Preference.token, "")
    var mbrandName: String by Preference(Preference.brandName, "")
    var mbrandId: String by Preference(Preference.brandId, "")
    var mclubId: String by Preference(Preference.clubId, "")
    var mclubName: String by Preference(Preference.clubName, "")
    var mUserId: String by Preference(Preference.userId, "")
    var mNikeName: String by Preference(Preference.nickname, "")

    protected var isSort = false;
    protected var mSortType = EventConstant.SORT_DATA_DEFUT
    abstract fun getHeartResultView(): BaseHeartResultView


    override fun initView() {
        EventBus.getDefault().register(this)
        DoubleClickUtil.getInstance().initHandler(Handler(Looper.getMainLooper()))
        hideNavigation()
    }

    protected fun showSettingView() {
        DialogLogin(this, DialogLogin.OnClickShowView {

        }).show()
        //        DialogFactory.getInstance().showLoginDialog(this);
    }

    override fun update(o: Observable?, arg: Any?) {
        super.update(o, arg)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        getHeartResultView().release()
        super.onDestroy()
    }

    /*  override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
          if (keyCode == KeyEvent.KEYCODE_BACK) {
              if (!DoubleClickUtil.getInstance().backExit(context)) {
                  return true
              }
          }
          return super.onKeyDown(keyCode, event)
      }
  */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(type: TypeEvent) {

        LogUtil.e("type=" + type.type)
        if (type.type == EventConstant.EVENT_LOGIN) {
            startActivity(Intent(this, ManagerSettingActivity::class.java))
            finish()
        }
        if (type.type == EventConstant.PARTICLE) {
            AnimationUtil.particleView(this, ll_title, type.state)
        }
        when (type.type) {
            EventConstant.SHOW_POPUP_SORT -> {
                PopupWindowFactory.showSortView(this)
            }
            EventConstant.SHOW_POPUP_MAN_SELECT -> {
                SelectPopupWindow(this).showDefault()
            }
            EventConstant.SHOW_POPUP_MAIN_DATA -> {
                PopupWindowFactory.showMainSelectView(this)
            }
            EventConstant.MAIN_DATA_HR_STRENGTH -> {
                AdapterUtil.setMainDataStateMap(Constant.TYPE_PERCENT)
                getHeartResultView().updateRecyclerView()
            }
            EventConstant.MAIN_DATA_HR -> {
                AdapterUtil.setMainDataStateMap(Constant.TYPE_HR)
                getHeartResultView().updateRecyclerView()
            }
            EventConstant.MAIN_DATA_POINT -> {
                AdapterUtil.setMainDataStateMap(Constant.TYPE_POINT)
                getHeartResultView().updateRecyclerView()
            }
            EventConstant.MAIN_DATA_CAL -> {
                AdapterUtil.setMainDataStateMap(Constant.TYPE_CAL)
                getHeartResultView().updateRecyclerView()

            }
            EventConstant.SORT_DATA_CAL -> {
                isSort = true
                mSortType = EventConstant.SORT_DATA_CAL
                BaseApp.sSortType = EventConstant.SORT_DATA_CAL

            }
            EventConstant.SORT_DATA_HR -> {
                isSort = true
                mSortType = EventConstant.SORT_DATA_HR
                BaseApp.sSortType = EventConstant.SORT_DATA_HR
            }
            EventConstant.SORT_DATA_HR_STRENGTH -> {
                isSort = true
                mSortType = EventConstant.SORT_DATA_HR_STRENGTH
                BaseApp.sSortType = EventConstant.SORT_DATA_HR_STRENGTH
            }
            EventConstant.SORT_DATA_POINT -> {
                isSort = true
                mSortType = EventConstant.SORT_DATA_POINT
                BaseApp.sSortType = EventConstant.SORT_DATA_POINT
            }
            EventConstant.SORT_DATA_MATCH -> {
                isSort = true
                mSortType = EventConstant.SORT_DATA_MATCH
                BaseApp.sSortType = EventConstant.SORT_DATA_MATCH
            }
        }
    }

    private fun hideNavigation() {
        if (Build.VERSION.SDK_INT >= 19) {
            val decorView = window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = option
        }
    }



}
