package com.jkcq.managersetting.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import com.jkcq.managersetting.OnDialogClickListener

import com.blankj.utilcode.util.ScreenUtils
import com.jkcq.hrwtv.base.R
import kotlinx.android.synthetic.main.dialog_update_layout.*


/**
 * @author WuJianhua
 */
class UpdateDialog(private val mContext: Context, type: Int, values: String) :
    Dialog(mContext, R.style.MyDialogStyle) {

    internal var layoutMid: LinearLayout? = null
    internal var type: Int = 0

    private var clickListener: OnDialogClickListener? = null

    init {
        setContentView(R.layout.dialog_update_layout)
        btn_cancel.setOnClickListener {
            onClick()
        }
    }


    fun updateProgress(progress: Float) {
        tv_progress_value.setText((progress * 100).toInt().toString() + "%")
        view_progress.progress = ((progress * 100).toInt())

    }

    fun setTvPackgeSize(size: Double) {
        val tips =
            String.format(mContext.resources.getString(R.string.package_size), size.toString())
        tv_packge.setText(tips)
    }

    override fun show() {
        super.show()
        val layoutParams = window!!.attributes
        layoutParams.gravity = Gravity.CENTER
//        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = (ScreenUtils.getScreenHeight() * 0.5).toInt()
        layoutParams.width = (ScreenUtils.getScreenWidth() * 0.4).toInt()
//        layoutParams.height =(ScreenUtils.getScreenHeight()*0.5).toInt()
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.attributes = layoutParams

    }

    fun setOnDialogClickListener(clickListener: OnDialogClickListener) {
        this.clickListener = clickListener
    }

    fun onClick() {
        clickListener?.dialogClickType(1)
    }
}
