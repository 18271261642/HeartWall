package com.jkcq.hrwtv.wu.newversion.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.configure.Constant
import com.jkcq.hrwtv.http.bean.CourseInfo
import com.jkcq.hrwtv.service.UserContans
import com.jkcq.hrwtv.util.CacheDataUtil
import com.jkcq.hrwtv.util.DisplayUtils
import com.jkcq.hrwtv.util.LoadImageUtil
import com.jkcq.hrwtv.util.TimeUtil
import com.jkcq.hrwtv.wu.newversion.activity.UserSelectActivity
import com.jkcq.hrwtv.wu.newversion.animation.AnimationUtil
import com.jkcq.hrwtv.wu.newversion.view.DialogFactory

//课程选择adapter
class CourseSelectAdapter(
    private val mContext: Activity,
    private var mDatas: List<CourseInfo>,
    private var isPk: Boolean
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<CourseSelectAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_course_select1, parent, false)
        )
    }

    var isecletListen: Isecelt? = null

    fun setIsecletListen1(isecletListen: Isecelt?) {
        this.isecletListen = isecletListen
    }

    public interface Isecelt {
        fun cancleCheackBox(isSelect: Boolean, position: Int)
        fun fouce(position: Int)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val info = mDatas[position]
        holder.tv_course_name.text = info.courseName
        holder.tv_time.text =
            mContext.resources.getString(
                R.string.course_time_minute,
                TimeUtil.getFormatTimeHHMMSS(info.duration * 1L)
            )
//      LoadImageUtil.getInstance().loadTransform(mContext, info.iconUrl, holder.iv_course_pic, R.mipmap.main_bg, R.mipmap.main_bg)


        LoadImageUtil.getInstance()
            .loadCirc(
                mContext,
                info.coverImage,
                holder.iv_course_pic,
                mContext.resources.getDimension(com.jkcq.hrwtv.base.R.dimen.dp8)
            )

        /*  LoadImageUtil.getInstance().loadRoundImg(
              holder.iv_course_pic,
              info.coverImage,
              R.mipmap.main_bg,
              R.mipmap.main_bg,
              DisplayUtils.dip2px(mContext, 8f)
          )*/
//        LoadImageUtil.getInstance().load(mContext, info.iconUrl, holder.iv_course_pic)

        var strength = info.difficultyLevel
        if (strength == 0) {
            holder.tv_level.text = "难度：新手";
        } else if (strength == 1) {
            holder.tv_level.text = "难度：入门";
        } else if (strength == 2) {
            holder.tv_level.text = "难度：中级"
        } else if (strength == 3) {
            holder.tv_level.text = "难度：高阶"
        } else if (strength == 4) {
            holder.tv_level.text = "难度：挑战"
        } else if (strength == 5) {
            holder.tv_level.text = "难度：地狱"
        }
        holder.itemView.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View, hasFocus: Boolean) {

                if (isecletListen != null) {
                    isecletListen!!.fouce(position)
                }
                if (hasFocus) {
//                  holder.itemView.setBackgroundResource(R.drawable.shape_linear_elevate)
                    holder.itemView.setBackgroundResource(R.drawable.shape_layout_selected_linear_bg)
                    AnimationUtil.ScaleUpViewCourse(holder.itemView)

                } else {
                    holder.itemView.setBackgroundColor(mContext.resources.getColor(com.jkcq.hrwtv.base.R.color.transparent))
                    AnimationUtil.ScaleDownView(holder.itemView)
                }
            }
        })
        holder.itemView.setOnClickListener {

            //下线两分钟的人需要去除调
            UserContans.mSnHrTime.forEach {
                if ((System.currentTimeMillis() - it.value) / 1000 > 2 * 60) {
                    UserContans.mSnHrMap.remove(it.key)
                    UserContans.userInfoHashMap.remove(it.key)
                    UserContans.mCacheMap.remove(it.key)
                }
            }

            val intent = Intent(mContext, UserSelectActivity::class.java)
            intent.putExtra("firstCome", true)
            if (isPk) {
                intent.putExtra("currentMode", Constant.MODE_PK_RED)
            } else {
                intent.putExtra("currentMode", Constant.MODE_COURSE)
            }
            CacheDataUtil.saveCouseInfo(info)
            UserContans.info = info
            mContext.startActivity(intent)
            // DialogFactory.getInstance().showCourseDialog(mContext, info)
        }
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    fun replaceData(data: List<CourseInfo>) {
        mDatas = data;
        notifyDataSetChanged()
    }


    class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var iv_course_pic: ImageView
        var tv_course_name: TextView
        var tv_time: TextView
        var tv_level: TextView


        init {
            tv_course_name = itemView.findViewById(R.id.tv_course_name)
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_level = itemView.findViewById(R.id.tv_level);
            iv_course_pic = itemView.findViewById(R.id.iv_course_pic);
        }
    }
}
