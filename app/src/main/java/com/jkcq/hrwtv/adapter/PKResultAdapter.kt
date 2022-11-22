package com.jkcq.hrwtv.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.configure.Constant
import com.jkcq.hrwtv.service.UserContans
import com.jkcq.hrwtv.util.CacheDataUtil
import com.jkcq.hrwtv.util.LoadImageUtil
import com.jkcq.hrwtv.wu.newversion.bean.DetailsEntity
import com.jkcq.hrwtv.wu.newversion.bean.PKTitleEntity
import com.jkcq.hrwtv.wu.newversion.bean.PKValueEntity
import com.jkcq.hrwtv.wu.newversion.view.CourseMatchView
import com.jkcq.hrwtv.wu.newversion.view.HeartStrengthView
import kotlinx.android.synthetic.main.item_course_sort.view.*
import kotlinx.android.synthetic.main.item_pk_result.view.*

/**
 * @Author  Snail
 * @Date 2019-10-01
 * @Description
 **/
class PKResultAdapter(context: Context, data: ArrayList<DetailsEntity>) :
    BaseRecyclerViewAdapter<DetailsEntity, BaseViewHolder>(context, data) {

    override fun getItemViewType(position: Int): Int {
        return data[position].getType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            DetailsEntity.TYPE_TITLE -> DetailsHeadViewHolder(
                inflater.inflate(
                    R.layout.item_pk_result,
                    parent,
                    false
                )
            )
            DetailsEntity.TYPE_HEAD -> DetailsHeadViewHolder(
                inflater.inflate(
                    R.layout.item_pk_result,
                    parent,
                    false
                )
            )
            else ->
                DetailsLimbsViewHolder(
                    inflater.inflate(
                        R.layout.item_course_sort,
                        parent,
                        false
                    )
                )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (getItemViewType(position)) {
            DetailsEntity.TYPE_TITLE -> {
                holder as DetailsHeadViewHolder
                val bean = data[position] as PKTitleEntity
                holder.iv_pk_state.setImageResource(bean.pkRes)
                holder.tv_pk_cal_value.text = bean.pkCalValue
                holder.iv_pk_cal.setImageResource(bean.pkCal)
                holder.tv_pk_title.text = bean.pkTitle
                holder.tv_pk_value.text = bean.pkValue
                holder.layout_top.setBackgroundResource(bean.bgRes)
                holder.tv_pk_value.setTextColor(bean.pkValueColor)
            }
            DetailsEntity.TYPE_ITEM -> {
                holder as DetailsLimbsViewHolder
                val bean = data[position] as PKValueEntity
                var info = bean.showBean
                holder.tv_cal.text = "" + info.cal.toInt() + "kcal"
                if (CacheDataUtil.mCurrentRange > 5) {
                    holder.tv_match.visibility = View.GONE
                } else {
                    holder.tv_match.visibility = View.VISIBLE
                }
                if (info.pkTeam == Constant.MODE_PK_RED) {
                    holder.layout_item_top.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_red_unselected_bg)
                } else {
                    holder.layout_item_top.setBackgroundResource(com.jkcq.hrwtv.base.R.drawable.shape_blue_unselected_bg)
                }
                holder.tv_match.text = "" + info.matchRate + "%"
                holder.tv_point.text = "" + info.point.toInt()
                holder.tv_hr.setText("" + info.averageHeartRate + "bpm")
                holder.tv_heart_strength.setText("" + info.averageHeartPercent + "%")
                holder.tv_name.text = info.nikeName
                holder.tv_rank.setText("" + info.ranking)
                holder.heart_strenght_view.setValue(info.getmDatas())
                LoadImageUtil.getInstance()
                    .loadCirc(
                        context,
                        info.headUrl,
                        holder.iv_rank_head,
                        context.resources.getDimension(com.jkcq.hrwtv.base.R.dimen.dp8)
                    )


            }
            else -> {

            }


        }

    }


    inner class DetailsHeadViewHolder(view: View) : BaseViewHolder(view) {
        val iv_pk_state: ImageView = view.iv_pk_state
        val layout_top: RelativeLayout = view.layout_top
        val iv_pk_cal: ImageView = view.iv_pk_cal
        val tv_pk_title: TextView = view.tv_pk_title
        val tv_pk_value: TextView = view.tv_pk_value
        val tv_pk_cal_value: TextView = view.tv_pk_cal_value
    }

    inner class DetailsLimbsViewHolder(view: View) : BaseViewHolder(view) {
        val iv_rank_head: ImageView = view.iv_rank_head
        val layout_item_top: RelativeLayout = view.layout_item_top
        val tv_rank: TextView = view.tv_rank
        val tv_name: TextView = view.tv_name
        val tv_match: TextView = view.tv_match
        val tv_point: TextView = view.tv_point
        val tv_cal: TextView = view.tv_cal
        val tv_heart_strength: TextView = view.tv_heart_strength
        val tv_hr: TextView = view.tv_hr
        val heart_strenght_view: CourseMatchView = view.heart_strenght_view


    }


    private interface OnFootListener {
        fun onClick(position: Int)
    }
}