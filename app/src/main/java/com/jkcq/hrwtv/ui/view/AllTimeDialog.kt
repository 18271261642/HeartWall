package com.jkcq.hrwtv.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.jkcq.managersetting.R
import java.util.*


/**
 * @Author  Snail
 * @Date 2019-10-10
 * @Description
 **/
@SuppressLint("ValidFragment")
class AllTimeDialog @SuppressLint("ValidFragment") constructor() :
    DialogFragment() {
    var onClickShowView: OnClickShowView? = null

    init {
        setStyle(STYLE_NO_TITLE, R.style.BaseDialog)
    }

    private val layout_brand_id: LinearLayout? = null
    private var layout_club_id: LinearLayout? = null
    private var layout_accout: LinearLayout? = null
    private var layout_pwd: LinearLayout? = null
    private val et_accout: EditText? = null
    private var et_pwd: EditText? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.dialog_user_alone_reg_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    override fun onResume() {
        super.onResume()
        arguments?.let {
        }
    }

    private fun initData() {
        /* et_pwd?.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
             if (hasFocus) {
                 layout_pwd.setBackgroundResource(R.drawable.shape_btn_selected_bg)
             } else {
                 layout_pwd.setBackgroundResource(R.drawable.shape_btn_unselected_bg)
             }
         }
         et_accout?.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
             if (hasFocus) {
                 layout_accout.setBackgroundResource(R.drawable.shape_btn_selected_bg)
             } else {
                 layout_accout.setBackgroundResource(R.drawable.shape_btn_unselected_bg)
             }
         }
         btn_sure?.setOnClickListener { regDevice(et_accout.text.toString(), et_pwd.text.toString()) }
         btn_sure.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
             if (hasFocus) {
                 btn_sure.setBackgroundResource(R.drawable.shape_btn_selected_bg)
             } else {
                 btn_sure.setBackgroundResource(R.drawable.shape_btn_unselected_bg)
             }
         }
         btn_cancel.setOnClickListener { hideDialog(false) }
         btn_cancel.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
             if (hasFocus) {
                 btn_cancel.setBackgroundResource(R.drawable.shape_btn_selected_bg)
             } else {
                 btn_cancel.setBackgroundResource(R.drawable.shape_btn_unselected_bg)
             }
         }*/
    }

    /**
     *设置已经训练了的时长
     */
//    fun setMineTime(value: Int) {
//        this.minTime = (value / (60 * 1000L)).toInt()
//    }

    fun addListener(listener: OnClickShowView) {
        onClickShowView = object : OnClickShowView {

            override fun showView(isSuccess: Boolean) {
            }
        }
    }

    interface OnClickShowView {
        fun showView(isSuccess: Boolean)
    }

    /**
     * {
     * "mac": "buzhidao",
     * "password": "buzhidao",
     * "type": 1, 心率墙为：0  接收器：1
     * "username": "yudaoyuanma"
     * }
     *
     * @param pwd
     */

    private fun hideDialog(isSuccess: Boolean) {
        dismiss()
        if (onClickShowView != null) {
            onClickShowView!!.showView(isSuccess)
        }
    }
}