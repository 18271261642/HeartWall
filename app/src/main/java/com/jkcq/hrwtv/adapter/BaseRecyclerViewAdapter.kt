package com.jkcq.hrwtv.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import com.exercise.ems.listener.OnItemClickListener


abstract class BaseRecyclerViewAdapter<T, V : BaseViewHolder> constructor(@JvmField val context: Context, @JvmField var data: MutableList<T>) :
   androidx.recyclerview.widget.RecyclerView.Adapter<V>() {

    protected var onItemClickListener: OnItemClickListener<T>? = null
    protected var inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemCount(): Int = data.size

    fun replaceData(data: MutableList<T>) {
        this.data = data
        notifyDataSetChanged()
    }

    fun addData(data: MutableList<T>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(index: Int, data: MutableList<T>) {
        this.data.addAll(index, data)
        notifyDataSetChanged()
    }


    fun removeData(data: MutableList<T>) {
        this.data.removeAll(data)
        notifyDataSetChanged()
    }

    fun removeData(t: T) {
        this.data.remove(t)
        notifyDataSetChanged()
    }

    fun removeData(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }




    fun setOnItemClickListener(onItemClickListener: (T) -> Unit) {
        this.onItemClickListener = object : OnItemClickListener<T> {
            override fun onItem(t: T, position: Int) {
                onItemClickListener.invoke(t)
            }
        }
    }

    fun addOnItemClickListener(onItemClickListener: (Int,T) -> Unit) {
        this.onItemClickListener = object : OnItemClickListener<T> {
            override fun onItem(t: T, position: Int) {
                onItemClickListener.invoke(position,t)
            }
        }
    }

}