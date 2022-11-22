package com.jkcq.hrwtv.wu.newversion.view

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import com.google.gson.Gson
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean
import com.jkcq.hrwtv.wu.newversion.adapter.HallAdapter
import timber.log.Timber
import java.util.concurrent.CopyOnWriteArrayList

class HeartResultView : BaseHeartResultView {

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
//        mSize = 8;
        mSpanCount = 24;
        mSpaceItemMargin = 15f
        mPageCournt = 12
        mShowType = SHOW_TYPE_HALL
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
        mAdapter?.notifyDataSetChanged()
        mAdapter?.compareToMove(0)


    }

    fun removeItem(position: Int) {
        try {
            if(position<mShowHeartRateData.size)
              mShowHeartRateData.removeAt(position)
            mAdapter?.notifyItemRemoved(position)
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    fun removeItem(data: ArrayList<Int>) {

        /*val list: ListIterator<*> = arr.listIterator()
        println("----------------下")
        while (list.hasNext()) {
            println(list.next())
        }
        println("----------------上")
        while (list.hasPrevious()) {
            println(list.previous())
        }*/


        for (value in data) {
            mShowHeartRateData.removeAt(value)
            mAdapter?.notifyItemRemoved(value)
        }

    }

    fun insertItem(data: DevicesDataShowBean?, position: Int) {
        if (data == null) {
            return
        }
        mShowHeartRateData.add(data)
        mAdapter?.notifyItemInserted(mShowHeartRateData.size - 1)
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
       Timber.e("------更新所有数据="+Gson().toJson(data))
        updateRecyclerView()
    }
}