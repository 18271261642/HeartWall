package com.jkcq.hrwtv.wu.newversion.activity


import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.adapter.PKResultAdapter
import com.jkcq.hrwtv.base.mvp.BaseMVPActivity
import com.jkcq.hrwtv.configure.Constant
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean
import com.jkcq.hrwtv.heartrate.model.CourseResultView
import com.jkcq.hrwtv.heartrate.model.MainActivityView
import com.jkcq.hrwtv.heartrate.presenter.MainActivityPresenter
import com.jkcq.hrwtv.http.bean.CourseInfo
import com.jkcq.hrwtv.service.UserContans
import com.jkcq.hrwtv.util.*
import com.jkcq.hrwtv.wu.manager.Preference
import com.jkcq.hrwtv.wu.newversion.NewPagingScrollHelper
import com.jkcq.hrwtv.wu.newversion.bean.DetailsEntity
import com.jkcq.hrwtv.wu.newversion.bean.PKTitleEntity
import com.jkcq.hrwtv.wu.newversion.bean.PKValueEntity
import kotlinx.android.synthetic.main.activity_course_sort.*
import kotlinx.android.synthetic.main.include_head_course_sort.*
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * PK结果页面
 */
class PkResultActivity : BaseMVPActivity<MainActivityView, MainActivityPresenter>(),
    CourseResultView, View.OnFocusChangeListener {

    var mclubName: String by Preference(Preference.clubName, "")

    private var mDotViews = ArrayList<ImageView>();
    private val data = ArrayList<DetailsEntity>()
    var mDataShowBeans = CopyOnWriteArrayList<DevicesDataShowBean>()
    private var mCheckIndex = 0;
    private var isReset = true;
    lateinit var mAdapter: PKResultAdapter

    override fun onDestroy() {
        super.onDestroy()
        mDataShowBeans.clear()
    }


    private fun winTeam(wingroup: Int) {
        var cal = ""
        var tem = "红队"
        var winTeam = Constant.MODE_PK_BLUE
        if (wingroup == Constant.PK_RED) {
            winTeam = Constant.MODE_PK_RED
            tem = "红队"
            cal = HeartRateConvertUtils.doubleParseStr(Constant.sumRedCal)
            var winBean = PKTitleEntity(
                R.mipmap.icon_pk_result_red,
                R.mipmap.icon_pk_win,
                "胜利 . ",
                tem,
                cal,
                this.resources.getColor(com.jkcq.hrwtv.base.R.color.sport_state_six), com.jkcq.hrwtv.base.R.drawable.shape_red_unselected_bg
            )
            data.add(winBean)
        } else {
            tem = "蓝队"
            winTeam = Constant.MODE_PK_BLUE
            cal = HeartRateConvertUtils.doubleParseStr(Constant.sumBlueCal)
            var winBean = PKTitleEntity(
                R.mipmap.icon_pk_result_blue,
                R.mipmap.icon_pk_win,
                "胜利 . ",
                tem,
                cal,
                this.resources.getColor(com.jkcq.hrwtv.base.R.color.sport_state_three),
                com.jkcq.hrwtv.base.R.drawable.shape_blue_unselected_bg
            )
            data.add(winBean)
        }


//        mDataShowBeans.sort(Comparator.comparing { obj: DevicesDataShowBean -> obj.averageHeartRate }
//            .reversed().thenComparing { obj: DevicesDataShowBean -> obj.point }.reversed())
//        mDataShowBeans.sortByDescending { it.point }
        mDataShowBeans.forEach {
            if (it.pkTeam == winTeam) {
                data.add(PKValueEntity(it))
            }
        }
    }

   private fun lowerTeam(wingroup: Int) {
        var cal = ""
        var tem = "红队"
        var winTeam = Constant.MODE_PK_BLUE
        if (wingroup == Constant.PK_RED) {
            winTeam = Constant.MODE_PK_RED
            tem = "红队"
            cal = HeartRateConvertUtils.doubleParseStr(Constant.sumRedCal)
            var lowBean = PKTitleEntity(
                R.mipmap.icon_pk_result_red,
                R.mipmap.icon_pk_lower,
                "失败 . ",
                tem,
                cal,
                this.resources.getColor(com.jkcq.hrwtv.base.R.color.sport_state_six), com.jkcq.hrwtv.base.R.drawable.shape_red_unselected_bg
            )
            data.add(lowBean)
        } else {
            winTeam = Constant.MODE_PK_BLUE
            tem = "蓝队"
            cal = HeartRateConvertUtils.doubleParseStr(Constant.sumBlueCal)
            var lowBean = PKTitleEntity(
                R.mipmap.icon_pk_result_blue,
                R.mipmap.icon_pk_lower,
                "失败 . ",
                tem,
                cal,
                this.resources.getColor(com.jkcq.hrwtv.base.R.color.sport_state_three),
                com.jkcq.hrwtv.base.R.drawable.shape_blue_unselected_bg
            )
            data.add(lowBean)
        }
        mDataShowBeans.forEach {
            if (it.pkTeam == winTeam) {
                data.add(PKValueEntity(it))
            }
        }
    }

    override fun initView() {
        rl_img_back.requestFocus()
        rl_img_back.nextFocusLeftId = R.id.rl_img_back
        rl_img_back.nextFocusRightId = R.id.rl_img_back
        rl_img_back.nextFocusDownId = R.id.rv_sort_result
        rl_img_back.nextFocusUpId = R.id.rl_img_back

        rv_sort_result.nextFocusUpId = R.id.rl_img_back

        if (CacheDataUtil.mCurrentRange > 5) {
            tv_title_match.visibility = View.GONE
        } else {
            tv_title_match.visibility = View.VISIBLE
        }
        layout_item.visibility = View.INVISIBLE
        rl_img_back.setOnClickListener { finish() }
        mDataShowBeans = CacheDataUtil.sUploadHeartData
        if (Constant.WIM_TEAM == Constant.PK_RED) {
            winTeam(Constant.PK_RED)
            lowerTeam(Constant.PK_BLUE)
        } else {
            winTeam(Constant.PK_BLUE)
            lowerTeam(Constant.PK_RED)
        }
        rv_sort_result.layoutManager =
            LinearLayoutManager(this)

        mAdapter = PKResultAdapter(this, data)
        rv_sort_result.adapter = mAdapter
    }

    override fun initEvent() {
    }

    var mPageCournt = 12

    @Volatile
    private var mCurrentPage = 1;
    var mTotalPage = 0
    override fun initData() {
        // uploadCourseData()
        tv_name.text = mclubName

        showCourseModel()
        var size = mDataShowBeans.size / 5
        if (mDataShowBeans.size % 5 != 0) {
            size += 1
        }
        mTotalPage = size
        setDotView()
        scrollHelper = NewPagingScrollHelper()
        scrollHelper?.setSeparate(5)
        scrollHelper?.setUpRecycleView(rv_sort_result)
        scrollHelper?.setmOnScrollListener(mChangeListener)
    }

    /**
     * 轮播点的绘制
     */
    private var scrollHelper: NewPagingScrollHelper? = null
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

    var isScroll = false
    val mChangeListener = object : NewPagingScrollHelper.OnPageListerner {
        override fun onChangePage(totalPage: Int, currentPage: Int) {
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


    override fun onFocusChange(v: View, hasFocus: Boolean) {
        var view = v as TextView
        if (!hasFocus) {
            //  view.setBackgroundResource(R.drawable.shape_btn_unselected_bg)
        } else {

            //   view.setBackgroundResource(R.drawable.shape_btn_cheak)
        }

    }

    /**
     * 上传心率数据
     */

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
