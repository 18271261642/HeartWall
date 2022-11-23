package com.jkcq.hrwtv.wu.newversion.view

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean
import com.jkcq.hrwtv.service.UserContans
import com.jkcq.hrwtv.util.CacheDataUtil
import com.jkcq.hrwtv.wu.newversion.adapter.HallAdapter
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.ArrayList

class CourseHeartResultView : BaseHeartResultView {

    constructor(context: Context) : super(context) {
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    override fun setStateData() {
        mSpanCount = 24;
        mSpaceItemMargin = 15f
        mPageCournt = 12
        mShowType = SHOW_TYPE_COURSE
    }

    var mAdapter: HallAdapter? = null
    override fun setAdapter(recyclerView: RecyclerView) {
        mAdapter = HallAdapter(mContext, mShowHeartRateData)
        recyclerView.adapter = mAdapter
    }

    override fun updateRecyclerView() {
        //一闪一闪
//        for (i in 0 until mShowHeartRateData.size) {
//            mAdapter?.notifyItemChanged(i)
//        }
        drawParticle()

       // mShowHeartRateData.sortBy { it.joinTime}

        mAdapter?.notifyDataSetChanged()
        mAdapter?.compareToMove(0)
    }

    fun removeItem(position: Int) {
        mShowHeartRateData.removeAt(position)
        mAdapter?.notifyItemRemoved(position)
    }

    fun removeItem(data: ArrayList<String>) {
        data.forEach {
            loop@ for (i in 0 until mShowHeartRateData.size) {
                if (mShowHeartRateData.get(i).devicesSN.equals(it)) {
                    removeItem(i)
                    break@loop
                }

            }
        }
        CacheDataUtil.saveCourseUserBean(mShowHeartRateData)
        mAdapter?.notifyDataSetChanged()
    }

    fun insertItem(data: DevicesDataShowBean?, position: Int = 0) {
        if (data == null) {
            return
        }
        if (mShowHeartRateData.size == 0) {
            UserContans.classTime = data.joinTime
        }
        mShowHeartRateData.add(data)
        mAdapter?.notifyItemInserted(mShowHeartRateData.size - 1)

        CacheDataUtil.saveCourseUserBean(mShowHeartRateData)
    }

    override fun updateData(
        data: CopyOnWriteArrayList<DevicesDataShowBean>,
        isSort: Boolean,
        type: Int
    ) {
//        mShowHeartRateData=data
//        mShowHeartRateData.clear()
//        mShowHeartRateData.addAll(data)
//        if (isSort) {
//            sortData(mShowHeartRateData, type)
//        }
        updateRecyclerView()
        CacheDataUtil.saveCourseUserBean(mShowHeartRateData)
    }
}