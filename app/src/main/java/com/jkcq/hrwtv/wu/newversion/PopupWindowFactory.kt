package com.jkcq.hrwtv.wu.newversion

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.app.BaseApp
import com.jkcq.hrwtv.configure.Constant
import com.jkcq.hrwtv.eventBean.EventConstant
import com.jkcq.hrwtv.util.CacheDataUtil
import com.jkcq.hrwtv.wu.newversion.animation.AnimationUtil
import org.greenrobot.eventbus.EventBus

object PopupWindowFactory {

    fun showMoreView(activity: Activity, locationView: View) {
        var popupWindow = PopupWindow(activity)
        val view = LayoutInflater.from(activity).inflate(R.layout.layout_menuview, null)
        val ll_data_main = view.findViewById<LinearLayout>(R.id.ll_data_main);
        val ll_data_sort = view.findViewById<LinearLayout>(R.id.ll_data_sort);
        val ll_data_select = view.findViewById<LinearLayout>(R.id.ll_data_select);
        val iv_close = view.findViewById<ImageView>(R.id.iv_close);
        ll_data_main.onFocusChangeListener = View.OnFocusChangeListener() { view: View, hasFocus: Boolean ->
            if (hasFocus) {
                ll_data_main.setBackgroundResource(com.jkcq.hrwtv.base.R.color.white_35)
            } else {
                ll_data_main.setBackgroundResource(com.jkcq.hrwtv.base.R.color.transparent)
            }
        }
        ll_data_sort.onFocusChangeListener = View.OnFocusChangeListener() { view: View, hasFocus: Boolean ->
            if (hasFocus) {
                ll_data_sort.setBackgroundResource(com.jkcq.hrwtv.base.R.color.white_35)
            } else {
                ll_data_sort.setBackgroundResource(com.jkcq.hrwtv.base.R.color.transparent)
            }
        }
        ll_data_select.onFocusChangeListener = View.OnFocusChangeListener() { view: View, hasFocus: Boolean ->
            if (hasFocus) {
                ll_data_select.setBackgroundResource(com.jkcq.hrwtv.base.R.color.white_35)
            } else {
                ll_data_select.setBackgroundResource(com.jkcq.hrwtv.base.R.color.transparent)
            }
        }
        iv_close.onFocusChangeListener = View.OnFocusChangeListener() { view: View, hasFocus: Boolean ->
            if (hasFocus) {
                iv_close.setImageResource(R.mipmap.icon_close_selected)
            } else {
                iv_close.setImageResource(R.mipmap.icon_close_unselected)
            }
        }
        ll_data_main.setOnClickListener {
            popupWindow.dismiss()
            Log.e("NHallActivity=", "ll_data_main")
            EventBus.getDefault().post(TypeEvent(EventConstant.SHOW_POPUP_MAIN_DATA))
        }
        ll_data_select.setOnClickListener {
            popupWindow.dismiss()
            EventBus.getDefault().post(TypeEvent(EventConstant.SHOW_POPUP_MAN_SELECT))
        }
        ll_data_sort.setOnClickListener {
            popupWindow.dismiss()
            EventBus.getDefault().post(TypeEvent(EventConstant.SHOW_POPUP_SORT))
        }
        iv_close.setOnClickListener { popupWindow.dismiss() }
        popupWindow.setContentView(view)
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
        popupWindow.setBackgroundDrawable(ColorDrawable(0x00000000))
        popupWindow.setOutsideTouchable(true)
        popupWindow.setFocusable(true)
        popupWindow.setOnDismissListener(PopupWindow.OnDismissListener { })
//        popupWindow.showAtLocation(activity.window.decorView, Gravity.CENTER, 0, 0);
        popupWindow.showAsDropDown(locationView)

    }

    fun showMainSelectView(activity: Activity) {
        var popupWindow = PopupWindow(activity)
        val view = LayoutInflater.from(activity).inflate(R.layout.layout_sortselect_view, null)
        val ll_heartstrength = view.findViewById<RelativeLayout>(R.id.ll_heartstrength);
        val ll_heartrate = view.findViewById<RelativeLayout>(R.id.ll_heartrate);
        val ll_point = view.findViewById<RelativeLayout>(R.id.ll_point);
        val ll_cal = view.findViewById<RelativeLayout>(R.id.ll_cal);
        val iv_close = view.findViewById<ImageView>(R.id.iv_close);
        val iv_heartstrength = view.findViewById<ImageView>(R.id.iv_heartstrength);
        val iv_heartrate = view.findViewById<ImageView>(R.id.iv_heartrate);
        val iv_cal = view.findViewById<ImageView>(R.id.iv_cal);
        val iv_point = view.findViewById<ImageView>(R.id.iv_point);
        ll_heartstrength.onFocusChangeListener = View.OnFocusChangeListener() { view: View, hasFocus: Boolean ->
            if (hasFocus) {
                AnimationUtil.ScaleUpView(view)
                iv_heartstrength.setImageResource(R.mipmap.icon_sort_selected)
            } else {
                AnimationUtil.ScaleDownView(view)
                iv_heartstrength.setImageResource(R.mipmap.icon_sort_unselected)
            }
        }
        ll_heartrate.onFocusChangeListener = View.OnFocusChangeListener() { view: View, hasFocus: Boolean ->
            if (hasFocus) {
                AnimationUtil.ScaleUpView(view)
                iv_heartrate.setImageResource(R.mipmap.icon_sort_selected)
            } else {
                iv_heartrate.setImageResource(R.mipmap.icon_sort_unselected)
                AnimationUtil.ScaleDownView(view)
            }
        }
        ll_point.onFocusChangeListener = View.OnFocusChangeListener() { view: View, hasFocus: Boolean ->
            if (hasFocus) {
                AnimationUtil.ScaleUpView(view)
                iv_point.setImageResource(R.mipmap.icon_sort_selected)
            } else {
                AnimationUtil.ScaleDownView(view)
                iv_point.setImageResource(R.mipmap.icon_sort_unselected)
            }
        }
        ll_cal.onFocusChangeListener = View.OnFocusChangeListener() { view: View, hasFocus: Boolean ->
            if (hasFocus) {
                AnimationUtil.ScaleUpView(view)
                iv_cal.setImageResource(R.mipmap.icon_sort_selected)
            } else {
                AnimationUtil.ScaleDownView(view)
                iv_cal.setImageResource(R.mipmap.icon_sort_unselected)
            }
        }

        iv_close.onFocusChangeListener = View.OnFocusChangeListener() { view: View, hasFocus: Boolean ->
            if (hasFocus) {
                AnimationUtil.ScaleUpView(view)
                iv_close.setImageResource(R.mipmap.icon_close_selected)
            } else {
                AnimationUtil.ScaleDownView(view)
                iv_close.setImageResource(R.mipmap.icon_close_unselected)
            }
        }
        ll_heartstrength.setOnClickListener {
            popupWindow.dismiss()
            EventBus.getDefault().post(TypeEvent(EventConstant.MAIN_DATA_HR_STRENGTH))
        }
        ll_heartrate.setOnClickListener {
            popupWindow.dismiss()
            EventBus.getDefault().post(TypeEvent(EventConstant.MAIN_DATA_HR))
        }
        ll_point.setOnClickListener {
            popupWindow.dismiss()
            EventBus.getDefault().post(TypeEvent(EventConstant.MAIN_DATA_POINT))
        }
        ll_cal.setOnClickListener {
            popupWindow.dismiss()
            EventBus.getDefault().post(TypeEvent(EventConstant.MAIN_DATA_CAL))
        }

        var mainData = AdapterUtil.getMainDataStateMap().get(Constant.LOCATION_CENTER)
        if (Constant.TYPE_PERCENT.equals(mainData)) {
            ll_heartstrength.requestFocus()
        } else if (Constant.TYPE_CAL.equals(mainData)) {
            ll_cal.requestFocus()
        } else if (Constant.TYPE_POINT.equals(mainData)) {
            ll_point.requestFocus()
        } else if (Constant.TYPE_HR.equals(mainData)) {
            ll_heartrate.requestFocus()
        } else {
            ll_heartstrength.requestFocus()
        }
        iv_close.setOnClickListener { popupWindow.dismiss() }
        popupWindow.setContentView(view)
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
        popupWindow.setBackgroundDrawable(ColorDrawable(0x00000000))
        popupWindow.setOutsideTouchable(true)
        popupWindow.setFocusable(true)
        popupWindow.setOnDismissListener(PopupWindow.OnDismissListener { })
        popupWindow.showAtLocation(activity.window.decorView, Gravity.CENTER, 0, 0);
//        popupWindow.showAsDropDown(locationView)


    }

    fun showSortView(activity: Activity) {
        var popupWindow = PopupWindow(activity)
        val view = LayoutInflater.from(activity).inflate(R.layout.layout_sortselect_view, null)
        val ll_match = view.findViewById<RelativeLayout>(R.id.ll_match);
        val ll_heartstrength = view.findViewById<RelativeLayout>(R.id.ll_heartstrength);
        val ll_heartrate = view.findViewById<RelativeLayout>(R.id.ll_heartrate);
        val ll_point = view.findViewById<RelativeLayout>(R.id.ll_point);
        val ll_cal = view.findViewById<RelativeLayout>(R.id.ll_cal);
        val iv_close = view.findViewById<ImageView>(R.id.iv_close);
        val iv_match = view.findViewById<ImageView>(R.id.iv_match);
        val iv_heartstrength = view.findViewById<ImageView>(R.id.iv_heartstrength);
        val iv_heartrate = view.findViewById<ImageView>(R.id.iv_heartrate);
        val iv_cal = view.findViewById<ImageView>(R.id.iv_cal);
        val iv_point = view.findViewById<ImageView>(R.id.iv_point);

        ll_heartstrength.onFocusChangeListener = View.OnFocusChangeListener() { view: View, hasFocus: Boolean ->
            if (hasFocus) {
                AnimationUtil.ScaleUpView(view)
                iv_heartstrength.setImageResource(R.mipmap.icon_sort_selected)
            } else {
                AnimationUtil.ScaleDownView(view)
                iv_heartstrength.setImageResource(R.mipmap.icon_sort_unselected)
            }
        }
        ll_heartrate.onFocusChangeListener = View.OnFocusChangeListener() { view: View, hasFocus: Boolean ->
            if (hasFocus) {
                AnimationUtil.ScaleUpView(view)
                iv_heartrate.setImageResource(R.mipmap.icon_sort_selected)
            } else {
                AnimationUtil.ScaleDownView(view)
                iv_heartrate.setImageResource(R.mipmap.icon_sort_unselected)
            }
        }
        ll_point.onFocusChangeListener = View.OnFocusChangeListener() { view: View, hasFocus: Boolean ->
            if (hasFocus) {
                AnimationUtil.ScaleUpView(view)
                iv_point.setImageResource(R.mipmap.icon_sort_selected)
            } else {
                AnimationUtil.ScaleDownView(view)
                iv_point.setImageResource(R.mipmap.icon_sort_unselected)
            }
        }
        ll_cal.onFocusChangeListener = View.OnFocusChangeListener() { view: View, hasFocus: Boolean ->
            if (hasFocus) {
                AnimationUtil.ScaleUpView(view)
                iv_cal.setImageResource(R.mipmap.icon_sort_selected)
            } else {
                AnimationUtil.ScaleDownView(view)
                iv_cal.setImageResource(R.mipmap.icon_sort_unselected)
            }
        }

        iv_close.onFocusChangeListener = View.OnFocusChangeListener() { view: View, hasFocus: Boolean ->
            if (hasFocus) {
                AnimationUtil.ScaleUpView(view)
                iv_close.setImageResource(R.mipmap.icon_close_selected)
            } else {
                AnimationUtil.ScaleDownView(view)
                iv_close.setImageResource(R.mipmap.icon_close_unselected)
            }
        }
        /**
         * 课程模式才有根据匹配度排序
         */
        if (!CacheDataUtil.sHeartModel.equals(com.jkcq.hrwtv.configure.Constant.COURSE_MODEL)) {
            ll_match.visibility = View.GONE
        } else {
            ll_match.visibility = View.VISIBLE
            ll_match.setOnClickListener {
                popupWindow.dismiss()
                EventBus.getDefault().post(TypeEvent(EventConstant.SORT_DATA_MATCH))
            }
            ll_match.onFocusChangeListener = View.OnFocusChangeListener() { view: View, hasFocus: Boolean ->
                if (hasFocus) {
                    AnimationUtil.ScaleUpView(view)
                    iv_match.setImageResource(R.mipmap.icon_sort_selected)
                } else {
                    AnimationUtil.ScaleDownView(view)
                    iv_match.setImageResource(R.mipmap.icon_sort_unselected)
                }
            }
        }
        /**
         * 当前排序选中
         */
        when (BaseApp.sSortType) {
            EventConstant.SORT_DATA_CAL -> ll_cal.requestFocus()
            EventConstant.SORT_DATA_HR -> ll_heartrate.requestFocus()
            EventConstant.SORT_DATA_HR_STRENGTH -> ll_heartstrength.requestFocus()
            EventConstant.SORT_DATA_POINT -> ll_point.requestFocus()
            EventConstant.SORT_DATA_MATCH -> ll_match.requestFocus()
            else -> {
                ll_cal.requestFocus()
            }
        }
        ll_heartstrength.setOnClickListener {
            popupWindow.dismiss()
            EventBus.getDefault().post(TypeEvent(EventConstant.SORT_DATA_HR_STRENGTH))
        }
        ll_heartrate.setOnClickListener {
            popupWindow.dismiss()
            EventBus.getDefault().post(TypeEvent(EventConstant.SORT_DATA_HR))
        }
        ll_point.setOnClickListener {
            popupWindow.dismiss()
            EventBus.getDefault().post(TypeEvent(EventConstant.SORT_DATA_POINT))
        }
        ll_cal.setOnClickListener {
            popupWindow.dismiss()
            EventBus.getDefault().post(TypeEvent(EventConstant.SORT_DATA_CAL))
        }
        ll_match.setOnClickListener {
            popupWindow.dismiss()
            EventBus.getDefault().post(TypeEvent(EventConstant.SORT_DATA_MATCH))
        }


        iv_close.setOnClickListener { popupWindow.dismiss() }
        popupWindow.setContentView(view)
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
        popupWindow.setBackgroundDrawable(ColorDrawable(0x00000000))
        popupWindow.setOutsideTouchable(true)
        popupWindow.setFocusable(true)
        popupWindow.setOnDismissListener(PopupWindow.OnDismissListener { })
        popupWindow.showAtLocation(activity.window.decorView, Gravity.CENTER, 0, 0);
//        popupWindow.showAsDropDown(locationView)

    }
}