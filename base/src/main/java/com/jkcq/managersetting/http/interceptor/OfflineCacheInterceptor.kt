package com.jkcq.managersetting.http.interceptor

import com.blankj.utilcode.util.Utils
import com.jkcq.managersetting.http.NetWorkUtil
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @desc OfflineCacheInterceptor: 没有网时缓存
 */
class OfflineCacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        if (!NetWorkUtil.isNetworkAvailable(Utils.getApp())) {
            // 无网络时，设置超时为4周  只对get有用,post没有缓冲
            val maxStale = 60 * 60 * 24 * 28
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .removeHeader("nyn")
                    .build()
        }

        return response
    }
}