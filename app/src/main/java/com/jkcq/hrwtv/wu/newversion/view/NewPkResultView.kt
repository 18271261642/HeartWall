package com.jkcq.hrwtv.wu.newversion.view

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean
import com.jkcq.hrwtv.wu.newversion.adapter.HallAdapter
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by Admin
 *Date 2022/1/4
 */
class NewPkResultView : BaseHeartResultView {

    var mAdapter: HallAdapter? = null


    constructor(context: Context) : super(context) {

    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }


    override fun setAdapter(recyclerView: RecyclerView) {
        super.setAdapter(recyclerView)
        mAdapter = HallAdapter(mContext,mShowHeartRateData)
        recyclerView.adapter = mAdapter

    }

    override fun updateRecyclerView() {
        mAdapter?.notifyDataSetChanged()
    }

    override fun updateData(
        data: CopyOnWriteArrayList<DevicesDataShowBean>,
        isSort: Boolean,
        type: Int
    ) {
       updateRecyclerView()

    }
}