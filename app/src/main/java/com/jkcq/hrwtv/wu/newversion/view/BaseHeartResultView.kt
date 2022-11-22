package com.jkcq.hrwtv.wu.newversion.view

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.eventBean.EventConstant
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean
import com.jkcq.hrwtv.util.DisplayUtils
import com.jkcq.hrwtv.wu.newversion.AdapterUtil
import com.jkcq.hrwtv.wu.newversion.NewPagingScrollHelper
import com.jkcq.hrwtv.wu.newversion.TypeEvent
import com.jkcq.hrwtv.wu.newversion.adapter.HWallDecoration
import com.jkcq.hrwtv.wu.newversion.adapter.HallAdapter
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.android.synthetic.main.include_nodevice.view.*
import kotlinx.android.synthetic.main.layout_heartresult_view.view.*
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.CopyOnWriteArrayList

open class BaseHeartResultView : RelativeLayout {

    protected var mContext: Context

    /**
     * recyclerview 相关
     */
    public var mShowHeartRateData = CopyOnWriteArrayList<DevicesDataShowBean>()
    private var scrollHelper: NewPagingScrollHelper? = null
    private var isScroll = false
    private var mTotalPage = 0;

    protected var mSize = 0;
    protected var mSpanCount = 24;
    protected var mSpaceItemMargin = 0f
    protected var mPageCournt = 12

    @Volatile
    private var mCurrentPage = 1;

    companion object {
        val SHOW_TYPE_HALL = 0;
        val SHOW_TYPE_COURSE = 1;
    }

    protected var mShowType = 2;

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

    fun init() {
        LayoutInflater.from(context).inflate(R.layout.layout_heartresult_view, this, true)
    }

    fun initView() {
        setStateData()

        Log.e("initView ", " mSpanCount =" + mSpanCount)
        var layoutManager =
            androidx.recyclerview.widget.GridLayoutManager(context, mSpanCount)
        if (mShowType == SHOW_TYPE_HALL) {
            layoutManager.spanSizeLookup = object : androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    mSize = mShowHeartRateData.size
                    val count: Int
                    if (mSize >= 0 && mSize <= 6) {
                        count = 8
                    } else {
                        count = 6
                    }
                    return count
                }
            }
        } else if (mShowType == SHOW_TYPE_COURSE) {
            layoutManager.spanSizeLookup = object : androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    mSize = mShowHeartRateData.size
                    val count: Int
                    if (mSize >= 0 && mSize <= 6) {
                        count = 8
                    } else {
                        count = 6
                    }
                    return count
                }
            }
        } else {
            layoutManager.spanSizeLookup = object : androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    mSize = mShowHeartRateData.size
                    val count: Int
                    if (mSize >= 0 && mSize <= 6) {
                        count = 8
                    } else {
                        count = 6
                    }
                    return 2
                }
            }
        }
        recyclerview_hall.layoutManager = layoutManager

        recyclerview_hall.addItemDecoration(
            HWallDecoration(
                DisplayUtils.dip2px(
                    context,
                    15f
                )
            )
        )
        /*recyclerview_hall.addItemDecoration(
            NewSpaceItemDecoration(
                DisplayUtils.dip2px(
                    context,
                    mSpaceItemMargin
                )
            )
        )*/
//       recyclerview_hall.addItemDecoration(SpaceItemDecoration(DisplayUtils.dip2px(context, context.resources.getDimension(R.dimen.dp20))))

//        var animator = DefaultItemAnimator()
        var animator = ScaleInAnimator()
//        var animator = LandingAnimator()
        //设置移动动画的时间
//        animator.moveDuration = 2000;

        recyclerview_hall.itemAnimator = animator
        recyclerview_hall.itemAnimator?.apply {
            addDuration = 1000
            removeDuration = 1000
            moveDuration = 800
            changeDuration = 1000
        }
        setAdapter(recyclerview_hall)
        recyclerview_hall.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //滑动结束
                isScroll = false
            }
        })
        scrollHelper = NewPagingScrollHelper()
        scrollHelper?.setSeparate(mPageCournt)
        scrollHelper?.setUpRecycleView(recyclerview_hall)
        scrollHelper?.setmOnScrollListener(mChangeListener)

        if (mShowHeartRateData.size <= mPageCournt) {
            isScroll = false
        }
//        setDotView()
    }

    open fun setStateData() {
        mSize = 48;
        mSpanCount = 24;
        mSpaceItemMargin = 20.0f
        mPageCournt = 12
    }

    open fun setAdapter(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        recyclerview_hall.adapter = HallAdapter(mContext, mShowHeartRateData);
    }

    val mChangeListener = object : NewPagingScrollHelper.OnPageListerner {
        override fun onChangePage(totalPage: Int, currentPage: Int) {
            if (mTotalPage == totalPage) {
                setDotView(false, currentPage - 1)
            } else {
                mTotalPage = totalPage
                setDotView(true, currentPage - 1)
            }
            if (mShowHeartRateData.size <= mPageCournt) {
                isScroll = false
            } else {
                isScroll = true
            }
            mCurrentPage = currentPage;

        }

    }

    var isShowParticle = false;

    /**
     * 判断是否要撒花
     */
    fun drawParticle() {

        var showStartIndex = (mCurrentPage - 1) * mPageCournt
        //显示最后一页
        if (mCurrentPage * mPageCournt > mShowHeartRateData.size) {
            //显示的个数
            var showCount = (mShowHeartRateData.size % mPageCournt);

            if ((mCurrentPage == 1 && showCount < 2)) {
                return
            }
            //最后一页不足9个，会把上一页的显示下来
            if (mCurrentPage > 1) {
                if (showCount <= 4) {
                    showStartIndex = showStartIndex - 8
                } else if (showCount <= 8) {
                    showStartIndex = showStartIndex - 4
                }
            }
            var showLast = (mCurrentPage - 1) * mPageCournt + showCount
//        Log.e("testP", "showLast=" + showLast + "mShowHeartRateData.size=" + mShowHeartRateData.size);
            for (i in showStartIndex until (showLast - 1)) {
//             Log.e("testP", "index=" + i+" sn="+mShowHeartRateData.get(i).devicesSN);
                if(mShowHeartRateData.size-1>i){
                    if (AdapterUtil.convertRang((mShowHeartRateData.get(i).precent).toFloat())
                        != AdapterUtil.convertRang((mShowHeartRateData.get(i + 1).precent).toFloat())
                    ) {
                        isShowParticle = false
                        return
                    }
                }

            }
        } else {
            val showLast = mCurrentPage * mPageCournt - 1
            for (j in showStartIndex until showLast) {
//              Log.e("testPLast", "index=" + j+ " sn="+mShowHeartRateData.get(j).devicesSN);
                if (AdapterUtil.convertRang((mShowHeartRateData.get(j).precent).toFloat())
                    != AdapterUtil.convertRang((mShowHeartRateData.get(j + 1).precent).toFloat())
                ) {
                    isShowParticle = false
                    return
                }

            }
        }
        if (!isShowParticle) {
            isShowParticle = true
            if(showStartIndex < mShowHeartRateData.size-1){
                EventBus.getDefault().post(
                    TypeEvent(
                        EventConstant.PARTICLE, mShowHeartRateData.get(showStartIndex).precent
                    )
                )
            }

        }

    }

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

    open fun updateRecyclerView() {
    }

    open fun updateData(
        data: CopyOnWriteArrayList<DevicesDataShowBean>,
        isSort: Boolean = false,
        type: Int = EventConstant.SORT_DATA_DEFUT
    ) {

    }

    fun sortData(data: MutableList<DevicesDataShowBean>, type: Int) {
        data.sortBy { it.joinTime }
        /*when (type) {
            EventConstant.SORT_DATA_HR_STRENGTH -> {
                data.sortBy { it.precent }
            }
            EventConstant.SORT_DATA_HR -> {
                data.sortBy { it.liveHeartRate }
            }
            EventConstant.SORT_DATA_POINT -> {
                data.sortBy { it.point }
            }
            EventConstant.SORT_DATA_CAL -> {
                data.sortBy { it.cal }
            }
        }*/

    }


    open fun getAdapterData(): CopyOnWriteArrayList<DevicesDataShowBean> {
        if (mShowHeartRateData == null) {
            mShowHeartRateData = CopyOnWriteArrayList<DevicesDataShowBean>()
        }
        return mShowHeartRateData
    }

    /**
     * 是否未检测到设备
     */
    open fun isNoDevice(isNODevice: Boolean = false) {
        /*if (!isNODevice) {
            ll_data.visibility = View.VISIBLE
           rl_nodevice .visibility = View.GONE
        } else {
            ll_data.visibility = View.GONE
            rl_nodevice.visibility = View.VISIBLE
        }*/
        ll_data.visibility = View.VISIBLE
        rl_nodevice.visibility = View.GONE
    }

    fun release() {
        ll_point.removeAllViews();
        scrollHelper?.release()
        mDotViews.clear()
    }

}