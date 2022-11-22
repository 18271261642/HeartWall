package com.jkcq.managersetting.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.jkcq.hrwtv.base.R
import com.jkcq.managersetting.OnDialogClickListener

import kotlinx.android.synthetic.main.dialog_view_layout.*
import kotlinx.android.synthetic.main.view_bottom_view.*


/**
 * @author WuJianhua
 */
class TipsDialog(private val mContext: Context, internal var type: Int, values: String) : Dialog(mContext, R.style.SimpleDialogStyle), View.OnClickListener {


    private var clickListener: OnDialogClickListener? = null


    init {
        setContentView(R.layout.dialog_layout)
        btn_cancel.setText(R.string.cancel)
        tv_club_name.setText(values)

        if (type == 1) {
            layout_start.setVisibility(View.VISIBLE)
            btn_sure.setText(R.string.option_login_out)
        } else if (type == 2) {
            btn_sure.setText(R.string.sure)
        } else if (type == 3) {
            btn_sure.setText(R.string.update)
        }
        btn_cancel.setOnClickListener(this)
        btn_sure.setOnClickListener(this)
    }


    override fun show() {
        super.show()
        val layoutParams = window!!.attributes
        layoutParams.gravity = Gravity.CENTER
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

        window!!.decorView.setPadding(0, 0, 0, 0)

        window!!.attributes = layoutParams

    }

    fun setOnDialogClickListener(clickListener: OnDialogClickListener) {
        this.clickListener = clickListener
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_cancel -> clickListener!!.dialogClickType(0)
            R.id.btn_sure -> clickListener!!.dialogClickType(type)
        }
    }
}
