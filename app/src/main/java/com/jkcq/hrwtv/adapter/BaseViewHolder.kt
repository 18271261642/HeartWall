package com.jkcq.hrwtv.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * @author Snail
 * @date 2018/6/22
 * @description
 */
open class BaseViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    fun getItemView(): View = itemView
}
