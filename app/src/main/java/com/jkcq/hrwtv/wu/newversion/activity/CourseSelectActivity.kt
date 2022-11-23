package com.jkcq.hrwtv.wu.newversion.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.http.bean.CourseInfo
import com.jkcq.hrwtv.http.widget.BaseObserver
import com.jkcq.hrwtv.service.UserContans
import com.jkcq.hrwtv.util.DisplayUtils
import com.jkcq.hrwtv.wu.newversion.adapter.CourseSelectAdapter
import com.jkcq.hrwtv.wu.newversion.adapter.CourseSelectItemDecoration
import com.jkcq.hrwtv.http.RetrofitHelper
import com.jkcq.hrwtv.http.bean.BaseResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.include_course_select_title.*
import kotlinx.android.synthetic.main.layout_course_select.*
import java.util.*
import kotlin.collections.ArrayList


//课程或PK模式选择课程
class CourseSelectActivity : AppCompatActivity() {

    var isPk = false
    var mCourseDatas = arrayListOf<CourseInfo>()
    lateinit var mAdapter: CourseSelectAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_select)
        initView()
        initData()
    }

    override fun onResume() {
        super.onResume()
        //需要把选中的人都清除
        UserContans.userInfoHashMap.forEach {
            it.value.isSelect = false
        }

        //重新获取课程信息
        getHeartRateClass()
    }

    //计算当前的position在第第几页
    fun getCurrentPage(position: Int): Int {
        for (i in 0..(mTotalPage - 1)) {
            if (position >= i * 6 && position < (i + 1) * 6) {
                return i
            }
        }
        return 0
    }

    fun initView() {
        layout_red.visibility = View.INVISIBLE
        layout_blue.visibility = View.INVISIBLE
        tv_title.visibility = View.GONE
        fl_sure.visibility = View.INVISIBLE
        courseAllSelectLayout.visibility = View.INVISIBLE
        tv_number_count.visibility = View.GONE
        tv_name.text = "请选择课程"
        val layoutManager = GridLayoutManager(this, 3)
        tv_recyclerview.layoutManager = layoutManager
        isPk = intent.getBooleanExtra("isPK", false)
        mAdapter = CourseSelectAdapter(this, mCourseDatas, isPk)

//        tv_recyclerview.addItemDecoration(
//            CourseSelectItemDecoration(
//                DisplayUtils.dip2px(
//                    this,
//                    20f
//                )
//            )
//        )
        mAdapter.setIsecletListen1(object : CourseSelectAdapter.Isecelt {
            override fun fouce(position: Int) {
                Log.e("fouce", "position=" + position)
                setDotView(false, getCurrentPage(position))
            }

            override fun cancleCheackBox(isSelect: Boolean, position: Int) {
            }


        })
        tv_recyclerview.adapter = mAdapter
        updateTime()
        var intent = getIntent()
        /* tv_man_count.setText(getString(R.string.people, "" + manCount))
         if (!TextUtils.isEmpty(clubName)) {
             tv_name.setText(clubName)
         }*/
        initEvent()
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

    fun initEvent() {
        try {
            fl_back.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    fl_back.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_selected_bg)
                } else {
                    fl_back.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_btn_unselected_bg)
                }
            }
            fl_back.setOnClickListener {
                finish()
            }
            if(fl_back!=null){
                fl_back.requestFocus()
            }

        }catch (e : Exception){
            e.printStackTrace()
        }

    }

    /**
     * 更新标题栏的时间显示
     */
    private fun updateTime() {
        val calendar = Calendar.getInstance()
        /* tv_time.setText(
             String.format(
                 "%02d",
                 calendar.get(Calendar.HOUR_OF_DAY)
             ) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE))
         )*/
    }




    fun initData() {


    }

    fun getHeartRateClass() {
        RetrofitHelper.service.getCourseList(
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<BaseResponse<List<CourseInfo>>>() {
                override fun onSuccess(baseResponse: BaseResponse<List<CourseInfo>>) {
                    baseResponse.data?.let {
                        mCourseDatas = it as ArrayList<CourseInfo>
                        mAdapter.replaceData(mCourseDatas)
                        var size = mCourseDatas.size / 6
                        if (mCourseDatas.size % 6 != 0) {
                            size += 1
                        }
                        mTotalPage = size
                        setDotView(true, 0)
                        Log.e("test", baseResponse.data.toString())
                    }
                }
            })
    }
}
