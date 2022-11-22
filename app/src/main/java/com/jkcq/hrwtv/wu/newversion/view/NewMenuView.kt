package com.jkcq.hrwtv.wu.newversion.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.jkcq.hrwtv.R
import kotlinx.android.synthetic.main.layout_menuview.view.*

class NewMenuView : RelativeLayout, View.OnFocusChangeListener {


    private var mContext: Context

    constructor(context: Context) : super(context) {
        mContext = context
        init()
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        mContext = context
        init()
    }

    private fun init() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_menuview, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        ll_data_main.setOnFocusChangeListener(this)
        ll_data_sort.setOnFocusChangeListener(this)
        ll_data_select.setOnFocusChangeListener(this)
        iv_close.setOnFocusChangeListener(this)
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        ll_data_main.setBackgroundResource(com.jkcq.hrwtv.base.R.color.transparent);
        ll_data_sort.setBackgroundResource(com.jkcq.hrwtv.base.R.color.transparent);
        ll_data_select.setBackgroundResource(com.jkcq.hrwtv.base.R.color.transparent);
        iv_close.setImageResource(R.mipmap.icon_close_unselected);
        if (hasFocus) {
            when (v.id) {
                R.id.ll_data_main -> {
                    ll_data_main.setBackgroundResource(com.jkcq.hrwtv.base.R.color.white_35);
                }
                R.id.ll_data_sort -> {
                    ll_data_sort.setBackgroundResource(com.jkcq.hrwtv.base.R.color.white_35);
                }
                R.id.ll_data_select -> {
                    ll_data_select.setBackgroundResource(com.jkcq.hrwtv.base.R.color.white_35);
                }
                R.id.iv_close -> {
                    iv_close.setImageResource(R.mipmap.icon_close_selected);
                }
            }
        }
    }
}
