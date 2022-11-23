package com.jkcq.hrwtv.wu.newversion.activity

import android.os.Build
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.base.mvp.BaseMVPActivity
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean
import com.jkcq.hrwtv.heartrate.model.CourseResultView
import com.jkcq.hrwtv.heartrate.model.MainActivityView
import com.jkcq.hrwtv.heartrate.presenter.MainActivityPresenter
import com.jkcq.hrwtv.util.CacheDataUtil
import com.jkcq.hrwtv.wu.manager.Preference
import com.jkcq.hrwtv.wu.newversion.adapter.CourseResultAdapterJava
import kotlinx.android.synthetic.main.activity_course_sort.*
import kotlinx.android.synthetic.main.activity_new_sort_layout.*
import kotlinx.android.synthetic.main.activity_new_sort_layout.rl_img_back
import kotlinx.android.synthetic.main.activity_new_sort_layout.tv_cal
import kotlinx.android.synthetic.main.activity_new_sort_layout.tv_heart_strength
import kotlinx.android.synthetic.main.activity_new_sort_layout.tv_hr
import kotlinx.android.synthetic.main.activity_new_sort_layout.tv_match
import kotlinx.android.synthetic.main.activity_new_sort_layout.tv_name
import kotlinx.android.synthetic.main.activity_new_sort_layout.tv_point
import kotlinx.android.synthetic.main.include_head_course_sort.*
import timber.log.Timber
import java.util.*
import kotlin.Comparator

/**
 * Created by Admin
 *Date 2022/11/23
 */
class NewSortActivity : BaseMVPActivity<MainActivityView, MainActivityPresenter>(),
    CourseResultView, View.OnFocusChangeListener {

    private var adapter : CourseResultAdapterJava ?= null
    private var list = mutableListOf<DevicesDataShowBean>()
    var mclubName: String by Preference(Preference.clubName, "")

    override fun getLayoutId(): Int {
        return R.layout.activity_new_sort_layout
    }

    override fun initView() {
        rl_img_back.setOnClickListener {
            finish()
        }


        list.clear()
        list.addAll(CacheDataUtil.sUploadHeartData)
       val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        newSortRecyclerView.layoutManager = linearLayoutManager

        if(list.size == 0){
            adapter = CourseResultAdapterJava(this,list)
            newSortRecyclerView.adapter = adapter
            return
        }

        list.sortWith(compareBy({it.point},{it.cal}))
     //   list.reversed()

        list.forEach {
            Timber.e("------排序后="+it.cal+" "+it.point+" "+it.devicesSN)
        }
        adapter = CourseResultAdapterJava(this,list.reversed())
        newSortRecyclerView.adapter = adapter
    }

    override fun initData() {
        if (CacheDataUtil.mCurrentRange > 5) {
            tv_match.visibility = View.GONE
            tv_title_match.visibility = View.GONE
            tv_cal.nextFocusRightId = R.id.tv_point
            tv_point.nextFocusLeftId = R.id.tv_cal
        } else {

//            tv_cal.nextFocusRightId = R.id.tv_match
//
//            tv_match.nextFocusUpId = R.id.tv_match
//            tv_match.nextFocusDownId = R.id.tv_match
//            tv_match.nextFocusLeftId = R.id.tv_cal
//            tv_match.nextFocusRightId = R.id.tv_point
//            tv_point.nextFocusLeftId = R.id.tv_match
//            rl_img_back.nextFocusLeftId = R.id.rl_img_back
//            rl_img_back.nextFocusRightId = R.id.tv_cal
//            tv_match.visibility = View.VISIBLE
//            tv_title_match.visibility = View.VISIBLE
        }


        //rl_img_back.requestFocus()
//
//        rl_img_back.nextFocusUpId = R.id.rl_img_back
//        rl_img_back.nextFocusDownId = R.id.rl_img_back
//        rl_img_back.nextFocusLeftId = R.id.rl_img_back
//        rl_img_back.nextFocusRightId = R.id.tv_cal
//
//        tv_cal.nextFocusUpId = R.id.tv_cal
//        tv_cal.nextFocusDownId = R.id.tv_cal
//        tv_cal.nextFocusLeftId = R.id.rl_img_back
//
//
//
//        tv_point.nextFocusUpId = R.id.tv_point
//        tv_point.nextFocusDownId = R.id.tv_point
//        tv_point.nextFocusRightId = R.id.tv_heart_strength
//
//        tv_heart_strength.nextFocusUpId = R.id.tv_heart_strength
//        tv_heart_strength.nextFocusDownId = R.id.tv_heart_strength
//        tv_heart_strength.nextFocusLeftId = R.id.tv_point
//        tv_heart_strength.nextFocusRightId = R.id.tv_hr
//
//
//        tv_hr.nextFocusUpId = R.id.tv_hr
//        tv_hr.nextFocusDownId = R.id.tv_hr
//        tv_hr.nextFocusLeftId = R.id.tv_heart_strength
//        tv_hr.nextFocusRightId = R.id.tv_hr
//
//
//        rl_img_back.nextFocusDownId = R.id.rv_sort_result
//        tv_cal.nextFocusDownId = R.id.rv_sort_result
//        tv_point.nextFocusDownId = R.id.rv_sort_result
//        tv_heart_strength.nextFocusDownId = R.id.rv_sort_result
//        tv_hr.nextFocusDownId = R.id.rv_sort_result
//
//
//        rv_sort_result.nextFocusUpId = R.id.tv_cal


        tv_name.text = mclubName

      //  showCourseModel()
    }

    override fun createPresenter(): MainActivityPresenter {
        return MainActivityPresenter()
    }

    override fun uploadAllDataSuccess() {

    }

    override fun onFocusChange(p0: View?, p1: Boolean) {

    }


}