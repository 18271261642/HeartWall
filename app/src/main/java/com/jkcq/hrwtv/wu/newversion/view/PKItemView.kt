package com.jkcq.hrwtv.wu.newversion.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.configure.Constant
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean
import com.jkcq.hrwtv.service.UserContans
import com.jkcq.hrwtv.util.CacheDataUtil
import com.jkcq.hrwtv.util.HeartRateConvertUtils
import com.jkcq.hrwtv.util.LoadImageUtil
import com.jkcq.hrwtv.util.TimeUtil
import com.jkcq.hrwtv.wu.newversion.AdapterUtil
import kotlinx.android.synthetic.main.item_heartrate4.view.*

class PKItemView : RelativeLayout, View.OnFocusChangeListener {


    private var mContext: Context

    constructor(context: Context) : super(context) {
        mContext = context
        init()
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        init()
    }

    private fun init() {
        LayoutInflater.from(mContext).inflate(R.layout.item_heartrate4, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        layout_top.setOnFocusChangeListener(this)
    }

    fun setVaule(textview: TextView, data: DevicesDataShowBean?, type: String?) {
        if (data == null) {
            return
        }
        when (type) {
            Constant.TYPE_CAL -> //                textview.setText("920");
                textview.text = HeartRateConvertUtils.doubleParseStr(data.cal)
            Constant.TYPE_HR -> //                textview.setText("146");
                if (data.liveHeartRate == 0) {
                    textview.text = "--"
                } else {
                    textview.text = data.liveHeartRate.toString()
                }
            Constant.TYPE_PERCENT -> //                textview.setText("85");
                if (data.liveHeartRate == 0) {
                    textview.text = "--"
                } else {
                    textview.text = data.precent
                }
            Constant.TYPE_POINT -> //                textview.setText("37");
                textview.text = HeartRateConvertUtils.doubleParseStr(data.point)
        }
    }


    fun updateAllData(info: DevicesDataShowBean) {


        AdapterUtil.setVaule(tv_one, info, Constant.TYPE_CAL)
        AdapterUtil.setVaule(tv_two, info, Constant.TYPE_HR)
        AdapterUtil.setVaule(tv_point_value, info, Constant.TYPE_PERCENT)
        tv_name.setText(info.nikeName)

        AdapterUtil.setItemBg(
            l_layout,
            info.getPrecent().toInt(),
            mContext,
            mContext.resources.getDimension(com.jkcq.hrwtv.base.R.dimen.dp8),
            iv_current_stren,
            false
        )

        var url = ""
        //设置用户头像和昵称
        if (info.headUrl == null) {
            url = ""
        } else {
            url = info.headUrl
        }

        LoadImageUtil.getInstance()
            .loadCirc(mContext, url, iv_head, mContext.resources.getDimension(com.jkcq.hrwtv.base.R.dimen.dp4))

        var currenttime = System.currentTimeMillis()
        if (CacheDataUtil.mCurrentRange > 5) {
            tv_time.setText(TimeUtil.getHallTime(info.joinTime))
        } else {
            if ((currenttime - info.joinTime) / 1000 >= UserContans.couserTime) {
                tv_time.setText(TimeUtil.getHallTime(currenttime - UserContans.couserTime * 1000))
            } else {
                tv_time.setText(TimeUtil.getHallTime(info.joinTime))
            }
        }

        setMatchPercent(info)
        hrStageView.setToalSecend(info.getCourseTime())
        hrStageView.setValue(info.getmDatas(), true)
    }

    private fun setMatchPercent(info: DevicesDataShowBean) {
        var point = 0.0
        //Log.e("setMatchPercent", "CacheDataUtil.mCurrentRange" + CacheDataUtil.mCurrentRange)
        if (CacheDataUtil.mCurrentRange > 5) {
            point = HeartRateConvertUtils.hearRate2Point(
                info.cal,
                Constant.MODE_ALL, 0f
            )
           // Log.e("setMatchPercentFree", "info.getCal()" + info.cal + "point" + point)
            info.point = point
            tv_point.text = HeartRateConvertUtils.doubleParseStr(point)
            return
        }
        val hearStrength = Integer.valueOf(info.precent)
        var type = 0
        if (hearStrength < 50) {
            type = 0
        } else if (hearStrength in 50..59) {
            type = 1
        } else if (hearStrength in 60..69) {
            type = 2
        } else if (hearStrength in 70..79) {
            type = 3
        } else if (hearStrength in 80..89) {
            type = 4
        } else if (hearStrength >= 90) {
            type = 5
        }
        info.setTotalCount()
        if (type == CacheDataUtil.mCurrentRange) {
            info.setMatchCount()
        }
//        Log.e(
//            "setMatchPercentCourse",
//            "info.getCal()" + info.cal + "point" + point + ",info.getTotalCount()=" + info.totalCount + ",info.getMatchCount=" + info.matchCount + "type=" + type + ", CacheDataUtil.mCurrentRange=" + CacheDataUtil.mCurrentRange
//        )
        info.matchRate = info.matchCount * 100 / info.totalCount
        point = HeartRateConvertUtils.hearRate2Point(
            info.cal,
            Constant.MODE_COURSE, (
                    info.matchCount * 100 / info.totalCount
                    ).toFloat()
        )
        info.point = point
        tv_point.text = HeartRateConvertUtils.doubleParseStr(point)
    }

    fun updateData() {

    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        layout_top.setBackgroundResource(com.jkcq.hrwtv.base.R.color.transparent);
        if (hasFocus) {
            when (v.id) {
            }
        }
    }
}
