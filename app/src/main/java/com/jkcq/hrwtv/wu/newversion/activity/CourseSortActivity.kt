package com.jkcq.hrwtv.wu.newversion.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.base.mvp.BaseMVPActivity
import com.jkcq.hrwtv.eventBean.EventConstant
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean
import com.jkcq.hrwtv.heartrate.model.CourseResultView
import com.jkcq.hrwtv.heartrate.model.MainActivityView
import com.jkcq.hrwtv.heartrate.presenter.MainActivityPresenter
import com.jkcq.hrwtv.http.bean.CourseInfo
import com.jkcq.hrwtv.service.UserContans
import com.jkcq.hrwtv.util.CacheDataUtil
import com.jkcq.hrwtv.util.GetJsonDataUtil
import com.jkcq.hrwtv.util.LogUtil
import com.jkcq.hrwtv.util.TimeUtil
import com.jkcq.hrwtv.wu.manager.Preference
import com.jkcq.hrwtv.wu.newversion.NewPagingScrollHelper
import com.jkcq.hrwtv.wu.newversion.adapter.CourseResultAdapter
import com.jkcq.hrwtv.wu.newversion.adapter.CourseResultAdapterJava
import kotlinx.android.synthetic.main.activity_course_sort.*
import kotlinx.android.synthetic.main.include_head_course_sort.*
import timber.log.Timber
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 课程结束后的展示排序页面
 */
class CourseSortActivity : BaseMVPActivity<MainActivityView, MainActivityPresenter>(),
    CourseResultView, View.OnFocusChangeListener {

    var mclubName: String by Preference(Preference.clubName, "")
    private var mDotViews = ArrayList<ImageView>();
    var mDataShowBeans = CopyOnWriteArrayList<DevicesDataShowBean>()
    private var mCheckIndex = 0;
    private var isReset = true;


//    lateinit var mAdapter: CourseResultAdapterJava

    lateinit var mAdapter: CourseResultAdapter

    override fun onDestroy() {
        super.onDestroy()
        scrollHelper?.release()
        mDataShowBeans.clear()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView() {
        rl_img_back.setOnClickListener {
            finish()
        }



        mDataShowBeans.addAll(CacheDataUtil.sUploadHeartData)
        rv_sort_result.layoutManager =
            LinearLayoutManager(this)
//        mDataShowBeans.sortWith(compareBy<DevicesDataShowBean?>({ it?.point },{ it?.age }).reversed())


        mAdapter = CourseResultAdapter(this, mDataShowBeans)
        rv_sort_result.adapter = mAdapter

     //   mAdapter.sortDataAndResetView(EventConstant.SORT_DATA_POINT)

    }

    override fun initEvent() {


        tv_match.onFocusChangeListener = this
        tv_point.onFocusChangeListener = this
        tv_cal.onFocusChangeListener = this
        tv_heart_strength.onFocusChangeListener = this
        tv_hr.onFocusChangeListener = this
    }

    override fun initData() {
        if (CacheDataUtil.mCurrentRange > 5) {
            tv_match.visibility = View.GONE
            tv_title_match.visibility = View.GONE
            tv_cal.nextFocusRightId = R.id.tv_point
            tv_point.nextFocusLeftId = R.id.tv_cal
        } else {

            tv_cal.nextFocusRightId = R.id.tv_match

            tv_match.nextFocusUpId = R.id.tv_match
            tv_match.nextFocusDownId = R.id.tv_match
            tv_match.nextFocusLeftId = R.id.tv_cal
            tv_match.nextFocusRightId = R.id.tv_point
            tv_point.nextFocusLeftId = R.id.tv_match
            rl_img_back.nextFocusLeftId = R.id.rl_img_back
            rl_img_back.nextFocusRightId = R.id.tv_cal
            tv_match.visibility = View.VISIBLE
            tv_title_match.visibility = View.VISIBLE
        }


        //rl_img_back.requestFocus()

        rl_img_back.nextFocusUpId = R.id.rl_img_back
        rl_img_back.nextFocusDownId = R.id.rl_img_back
        rl_img_back.nextFocusLeftId = R.id.rl_img_back
        rl_img_back.nextFocusRightId = R.id.tv_cal

        tv_cal.nextFocusUpId = R.id.tv_cal
        tv_cal.nextFocusDownId = R.id.tv_cal
        tv_cal.nextFocusLeftId = R.id.rl_img_back



        tv_point.nextFocusUpId = R.id.tv_point
        tv_point.nextFocusDownId = R.id.tv_point
        tv_point.nextFocusRightId = R.id.tv_heart_strength

        tv_heart_strength.nextFocusUpId = R.id.tv_heart_strength
        tv_heart_strength.nextFocusDownId = R.id.tv_heart_strength
        tv_heart_strength.nextFocusLeftId = R.id.tv_point
        tv_heart_strength.nextFocusRightId = R.id.tv_hr


        tv_hr.nextFocusUpId = R.id.tv_hr
        tv_hr.nextFocusDownId = R.id.tv_hr
        tv_hr.nextFocusLeftId = R.id.tv_heart_strength
        tv_hr.nextFocusRightId = R.id.tv_hr


        rl_img_back.nextFocusDownId = R.id.rv_sort_result
        tv_cal.nextFocusDownId = R.id.rv_sort_result
        tv_point.nextFocusDownId = R.id.rv_sort_result
        tv_heart_strength.nextFocusDownId = R.id.rv_sort_result
        tv_hr.nextFocusDownId = R.id.rv_sort_result


        rv_sort_result.nextFocusUpId = R.id.tv_cal


        tv_name.text = mclubName

        showCourseModel()
        tv_cal.requestFocus()
        var size = mDataShowBeans.size / 5
        if (mDataShowBeans.size % 5 != 0) {
            size += 1
        }
        mTotalPage = size
        setDotView()
        scrollHelper = NewPagingScrollHelper()
//        scrollHelper?.setSeparate(5)
//        scrollHelper?.setUpRecycleView(rv_sort_result)
//        scrollHelper?.setmOnScrollListener(mChangeListener)


    }

    var mPageCournt = 12

    @Volatile
    private var mCurrentPage = 1;
    var mTotalPage = 0
    var isScroll = false
    val mChangeListener =
        NewPagingScrollHelper.OnPageListerner { totalPage, currentPage ->
            LogUtil.e("mChangeListener", "totalPage=" + totalPage + "currentPage=" + currentPage)


            if (mTotalPage == totalPage) {
                setDotView(true, currentPage - 1)
            } else {
                mTotalPage = totalPage
                setDotView(true, currentPage - 1)
            }
            if (mTotalPage <= mPageCournt) {
                isScroll = false
            } else {
                isScroll = true
            }
            mCurrentPage = currentPage;
        }

    /**
     * 轮播点的绘制
     */

    private fun setDotView(isReset: Boolean = true, indexChecked: Int = 0) {
//        Log.e("test", "isrReset=" + isReset + "indexChecked=" + indexChecked + "dotSize=" + mDotViews.size)
        if (mTotalPage < 1) {
            return;
        }
        if (isReset) {
            ll_point.removeAllViews()
            mDotViews.clear()
            for (i in 0..(mTotalPage - 1)) {
                val image = ImageView(context)
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.topMargin = 8
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

    lateinit var mCurrentHeartRateClassInfo: CourseInfo
    private fun showCourseModel() {
        mCurrentHeartRateClassInfo = UserContans.info
        tv_course_name.text = mCurrentHeartRateClassInfo.courseName
        tv_lever.text =
            UserContans.getDifficultyLevel(mCurrentHeartRateClassInfo.difficultyLevel)
        tv_course_time.text =
            TimeUtil.getFormatTimeHHMMSS(mCurrentHeartRateClassInfo.duration * 1L)
    }


    override fun createPresenter(): MainActivityPresenter {
        return MainActivityPresenter()
    }

    override fun getLayoutId(): Int = R.layout.activity_course_sort


    override fun uploadAllDataSuccess() {
        LogUtil.e(TAG, "uploadAllDataSuccess")
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        var view = v as TextView
        if (!hasFocus) {
            //  view.setBackgroundResource(R.drawable.shape_btn_unselected_bg)
        } else {

            //   view.setBackgroundResource(R.drawable.shape_btn_cheak)
            when (view.id) {
                R.id.tv_match -> {
                   // mAdapter.sortDataAndResetView(EventConstant.SORT_DATA_MATCH)
                    sortDataAndResetView(EventConstant.SORT_DATA_MATCH)
                }
                R.id.tv_point -> {
                   // mAdapter.sortDataAndResetView(EventConstant.SORT_DATA_POINT)
                    sortDataAndResetView(EventConstant.SORT_DATA_POINT)
                }
                R.id.tv_cal -> {
                    //mAdapter.sortDataAndResetView(EventConstant.SORT_DATA_CAL)
                    sortDataAndResetView(EventConstant.SORT_DATA_CAL)
                }
                R.id.tv_heart_strength -> {
                   // mAdapter.sortDataAndResetView(EventConstant.SORT_DATA_HR_STRENGTH)
                    sortDataAndResetView(EventConstant.SORT_DATA_HR_STRENGTH)
                }
                R.id.tv_hr -> {
                   // mAdapter.sortDataAndResetView(EventConstant.SORT_DATA_HR)
                    sortDataAndResetView(EventConstant.SORT_DATA_HR)
                }

            }
        }

    }



    private var tempList = mutableListOf<DevicesDataShowBean>()
    @SuppressLint("NewApi")
    fun sortDataAndResetView(sortType: Int) {
        tempList.clear()
        tempList.addAll(mDataShowBeans)
        when (sortType) {
            EventConstant.SORT_DATA_CAL -> {
                Collections.sort(tempList,object : Comparator<DevicesDataShowBean>{
                    override fun compare(p0: DevicesDataShowBean, p1: DevicesDataShowBean): Int {
                        val cal = p1.cal.toString().compareTo(p0.cal.toString())
                        val point = p1.point.toString().compareTo(p0.point.toString())
                        //等于0表明相等
                        if(cal == 0){
                            return point
                        }else{
                            return cal
                        }
                    }
                })


//                mDatas = mDatas.sortedByDescending { it.cal  }
            }
            EventConstant.SORT_DATA_POINT -> {
//                mDatas = mDatas.sortedByDescending { it.point }
                Collections.sort(tempList,object : Comparator<DevicesDataShowBean>{
                    override fun compare(p0: DevicesDataShowBean, p1: DevicesDataShowBean): Int {
                        val cal = p1.cal.toString().compareTo(p0.cal.toString())
                        val point = p1.point.toString().compareTo(p0.point.toString())
                        if(point == 0){
                            return cal
                        }else{
                            return point
                        }
                    }
                })
            }
            EventConstant.SORT_DATA_MATCH -> {
//                mDatas = mDatas.sortedByDescending { it.matchRate }
                Collections.sort(tempList,object : Comparator<DevicesDataShowBean>{
                    override fun compare(p0: DevicesDataShowBean, p1: DevicesDataShowBean): Int {
                        val match = p1.matchCount.toString().compareTo(p0.matchCount.toString())
                        val point = p1.point.toString().compareTo(p0.point.toString())
                        if(match == 0){
                            return point
                        }else{
                            return match
                        }
                    }
                })

            }
            EventConstant.SORT_DATA_HR -> {
                //mDatas = mDatas.sortedByDescending { it.averageHeartRate }
                Collections.sort(tempList,object : Comparator<DevicesDataShowBean>{
                    override fun compare(p0: DevicesDataShowBean, p1: DevicesDataShowBean): Int {
                        val hr = p1.averageHeartRate.toString().compareTo(p0.averageHeartRate.toString())
                        val point = p1.point.toString().compareTo(p0.point.toString())
                        if(hr == 0){
                            return point
                        }else{
                            return hr
                        }
                    }
                })

            }
            EventConstant.SORT_DATA_HR_STRENGTH -> {
//                mDatas = mDatas.sortedByDescending { it.averageHeartPercent }
                Collections.sort(tempList,object : Comparator<DevicesDataShowBean>{
                    override fun compare(p0: DevicesDataShowBean, p1: DevicesDataShowBean): Int {
                        val strength = p1.averageHeartPercent.toString().compareTo(p0.averageHeartPercent.toString())
                        val point = p1.point.toString().compareTo(p0.point.toString())
                        if(strength == 0){
                            return point
                        }else{
                            return strength
                        }
                    }
                })

            }
        }

        mDataShowBeans.clear()
        mDataShowBeans.addAll(tempList)
        mDataShowBeans.forEachIndexed { index, devicesDataShowBean ->

            Timber.e("--------排序="+devicesDataShowBean.devicesSN+" "+devicesDataShowBean.point+" 卡路里="+devicesDataShowBean.cal)
        }
        val filePath = Environment.getExternalStorageDirectory().path+"/"+"DCIM/"
        GetJsonDataUtil().writeTxtToFile(Gson().toJson(mDataShowBeans),filePath,sortType.toString()+"wall.json")
        mAdapter.notifyDataSetChanged()
    }


    private var scrollHelper: NewPagingScrollHelper? = null

    /**
     * 轮播点的绘制
     */
    private fun setDotView() {
        if (mTotalPage < 1) {
            return
        }
        if (isReset) {
            ll_point.removeAllViews()
            mDotViews.clear()
            for (i in 0 until mTotalPage) {
                val image = ImageView(this)
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.topMargin = 8
                if (i == mCheckIndex) {
                    image.setImageResource(R.drawable.icon_point_select)
                } else {
                    image.setImageResource(R.drawable.icon_point_unselect)
                }
                ll_point.addView(image, params)
                mDotViews.add(image)
            }
        } else {
            for (i in 0..(mTotalPage)) {
                if (i == mCheckIndex) {
                    mDotViews.get(i).setImageResource(R.drawable.icon_point_select)
                } else {
                    mDotViews.get(i).setImageResource(R.drawable.icon_point_unselect)
                }
            }
        }

    }
}
