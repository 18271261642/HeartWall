package com.jkcq.hrwtv.wu.newversion.adapter

import android.app.Activity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jkcq.hrwtv.R
import com.jkcq.hrwtv.configure.Constant
import com.jkcq.hrwtv.heartrate.bean.UserBean
import com.jkcq.hrwtv.util.LoadImageUtil
import com.jkcq.hrwtv.wu.newversion.animation.AnimationUtil
import com.jkcq.hrwtv.wu.newversion.bean.SelectUserBean


class UserSelectAdapter(private val mContext: Activity, private var mDatas: List<SelectUserBean>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<UserSelectAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_select, parent, false)
        )
    }

    var mode = Constant.MODE_ALL

    fun setCurrentMode(mode: Int) {
        this.mode = mode;
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val info = mDatas[position]

        holder.tv_name.text = info.nickname
        //设置用户头像和昵称
        var url = ""
        if (TextUtils.isEmpty(info.avatar)) {
            url = ""
        } else {
            url = info.avatar
        }
        if (info.isSelect) {
            when (mode) {
                Constant.MODE_ALL -> {
                    holder.iv_select.visibility = View.INVISIBLE
                }
                Constant.MODE_COURSE -> {
                    holder.iv_select.visibility = View.VISIBLE
                    holder.iv_select.setImageResource(R.mipmap.icon_user_select)
                }
                Constant.MODE_PK_RED -> {
                    holder.iv_select.visibility = View.VISIBLE
                    holder.iv_select.setImageResource(R.mipmap.icon_pk_red)
                }
                Constant.MODE_PK_BLUE -> {
                    holder.iv_select.visibility = View.VISIBLE
                    holder.iv_select.setImageResource(R.mipmap.icon_pk_blue)
                }
                else -> {
                }
            }
        } else {
            holder.iv_select.visibility = View.INVISIBLE
        }


        LoadImageUtil.getInstance()
            .loadCirc(mContext, url, holder.iv_head, mContext.resources.getDimension(com.jkcq.hrwtv.base.R.dimen.dp8))
        LoadImageUtil.getInstance()
            .loadCirc(mContext, url, holder.iv_bg, mContext.resources.getDimension(com.jkcq.hrwtv.base.R.dimen.dp15))
        holder.itemView.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                //                  holder.itemView.setBackgroundResource(R.drawable.shape_linear_elevate)
                if (isecletListen != null) {
                    isecletListen!!.fouce(position)
                }
                holder.itemView.setBackgroundResource(R.drawable.shape_layout_selected_linear_bg)
                //AnimationUtil.ScaleUpView(holder.itemView)

            } else {
                holder.itemView.setBackgroundResource(com.jkcq.hrwtv.base.R.color.transparent)
                //AnimationUtil.ScaleDownView(holder.itemView)
            }
        }


        holder.itemView.setOnClickListener {
            info.isSelect = !info.isSelect
            if (info.isSelect) {
                when (mode) {
                    Constant.MODE_ALL -> {
                        holder.iv_select.visibility = View.INVISIBLE
                    }
                    Constant.MODE_COURSE -> {
                        holder.iv_select.visibility = View.VISIBLE
                        holder.iv_select.setImageResource(R.mipmap.icon_user_select)
                    }
                    Constant.MODE_PK_RED -> {
                        holder.iv_select.visibility = View.VISIBLE
                        holder.iv_select.setImageResource(R.mipmap.icon_pk_red)
                    }
                    Constant.MODE_PK_BLUE -> {
                        holder.iv_select.visibility = View.VISIBLE
                        holder.iv_select.setImageResource(R.mipmap.icon_pk_blue)
                    }
                    else -> {
                    }
                }
            } else {
                holder.iv_select.visibility = View.INVISIBLE
            }
            if (isecletListen != null) {
                isecletListen!!.cancleCheackBox(info.isSelect, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    var isecletListen: Isecelt? = null

    fun setIsecletListen1(isecletListen: Isecelt?) {
        this.isecletListen = isecletListen
    }

    public interface Isecelt {
        fun cancleCheackBox(isSelect: Boolean, position: Int)
        fun fouce(position: Int)
    }


    fun replaceData(data: List<SelectUserBean>) {
        mDatas = data;
        notifyDataSetChanged()
    }


    class MyViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var iv_bg: ImageView = itemView.findViewById(R.id.iv_bg)
        var iv_head: ImageView
        var iv_select: ImageView
        var tv_name: TextView


        init {
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_head = itemView.findViewById(R.id.iv_head);
            iv_select = itemView.findViewById(R.id.iv_select);
        }
    }


    //全选或全不选
    public fun setAllSelectStatus(isSelect: Boolean){

        for(index in 0..mDatas.size){

        }

        mDatas.forEach {
            it.isSelect = isSelect
            notifyDataSetChanged()

        }
    }
}
