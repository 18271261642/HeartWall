package com.jkcq.hrwtv.wu.newversion.adapter

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.eventBean.EventConstant
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean
import com.jkcq.hrwtv.util.CacheDataUtil
import com.jkcq.hrwtv.util.LoadImageUtil
import com.jkcq.hrwtv.wu.newversion.view.CircleImageView
import com.jkcq.hrwtv.wu.newversion.view.CourseMatchView
import kotlinx.android.synthetic.main.activity_course_sort.*
import timber.log.Timber
import java.util.*
import java.util.stream.Collectors
import kotlin.Comparator

class CourseResultAdapter(
    private val mContext: Context,
    private var mDatas: List<DevicesDataShowBean>
) : RecyclerView.Adapter<CourseResultAdapter.MyViewHolder>() {

//    init {
//        mDatas = mDatas.sortedByDescending { it.point }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_course_sort, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val info = mDatas[position]
        holder.tv_cal.text = "" + info.cal.toInt() + "kcal"

        if (CacheDataUtil.mCurrentRange > 5) {
            holder.tv_match.visibility = View.GONE
        } else {
            holder.tv_match.visibility = View.VISIBLE
        }

        holder.tv_match.text = "" + info.matchRate + "%"
        holder.tv_point.text = "" + info.point.toInt()
        holder.tv_hr.setText("" + info.averageHeartRate + "bpm")
        holder.tv_heart_strength.setText("" + info.averageHeartPercent + "%")
        holder.tv_name.text = info.nikeName
        holder.tv_rank.setText("" + (position + 1))

        holder.heart_strenght_view.setValue(info.getmDatas())
        LoadImageUtil.getInstance()
            .loadCirc(
                mContext,
                info.headUrl,
                holder.iv_rank_head,
                mContext.resources.getDimension(com.jkcq.hrwtv.base.R.dimen.dp8)
            )
        // LoadImageUtil.getInstance().load(mContext, info.headUrl, holder.iv_rank_head);

    }

    override fun getItemCount(): Int {
        return mDatas.size
    }


    private var tempList = mutableListOf<DevicesDataShowBean>()

    @SuppressLint("NewApi")
    fun sortDataAndResetView(sortType: Int) {
        tempList.clear()
        tempList.addAll(mDatas)
        when (sortType) {
            EventConstant.SORT_DATA_CAL -> {
                Collections.sort(mDatas,object : Comparator<DevicesDataShowBean>{
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
                Collections.sort(mDatas,object : Comparator<DevicesDataShowBean>{
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
                Collections.sort(mDatas,object : Comparator<DevicesDataShowBean>{
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
                Collections.sort(mDatas,object : Comparator<DevicesDataShowBean>{
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
                Collections.sort(mDatas,object : Comparator<DevicesDataShowBean>{
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

        mDatas.forEachIndexed { index, devicesDataShowBean ->

            Timber.e("--------排序="+devicesDataShowBean.devicesSN+" "+devicesDataShowBean.point+" 卡路里="+devicesDataShowBean.cal)
        }

        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var heart_strenght_view: CourseMatchView
        var iv_rank_head: ImageView
        var tv_name: TextView
        var tv_rank: TextView
        var tv_cal: TextView
        var tv_match: TextView
        var tv_point: TextView
        var tv_hr: TextView
        var tv_heart_strength: TextView


        init {
            heart_strenght_view = itemView.findViewById(R.id.heart_strenght_view)
            iv_rank_head = itemView.findViewById(R.id.iv_rank_head)
            tv_name = itemView.findViewById(R.id.tv_name)
            tv_rank = itemView.findViewById(R.id.tv_rank)
            tv_cal = itemView.findViewById(R.id.tv_cal)
            tv_match = itemView.findViewById(R.id.tv_match)
            tv_point = itemView.findViewById(R.id.tv_point)
            tv_hr = itemView.findViewById(R.id.tv_hr)
            tv_heart_strength = itemView.findViewById(R.id.tv_heart_strength)

        }
    }
}
