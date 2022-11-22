package com.jkcq.hrwtv.http

import com.juxun.physical.http.BuildConfig


/**
 * @author Snail
 */
object Logger {

    private val TAG = "TAG"
    private val isShow = BuildConfig.DEBUG
//    private val isShow =false

    fun v(msg: String) {
        if (isShow) {
            android.util.Log.v(TAG, msg)
        }
    }

    fun d(msg: String) {
        if (isShow) {
            android.util.Log.d(TAG, msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (isShow) {
            android.util.Log.d(tag, msg)
        }
    }

    fun i(msg: String) {
        if (isShow) {
            android.util.Log.i(TAG, msg)
        }
    }


    fun e( msg: String) {
        if (isShow) {
            android.util.Log.e(TAG, msg)
        }
    }
    fun e(tag:String, msg: String) {
        if (isShow) {
            android.util.Log.e(tag, msg)
        }
    }


    fun e(msg: String, e: Throwable) {
        if (isShow) {
            android.util.Log.e(TAG, msg, e)
        }
    }
}
