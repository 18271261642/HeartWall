package com.jkcq.hrwtv.wu.newversion.activity.mamager

import android.content.Context

/**
 *created by wq on 2019/6/28
 */
class ManagerSettingPresenter(var mBaseView: ManagerMainView?) : ManagerMainModel {


    override fun checkUpdate(type: String) {
    }

    override fun clearCache(context: Context?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun release() {
        mBaseView = null;
    }
}