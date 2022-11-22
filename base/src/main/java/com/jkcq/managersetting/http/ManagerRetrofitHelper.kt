package com.jkcq.managersetting.http

import com.jkcq.managersetting.http.interceptor.CacheInterceptor
import com.blankj.utilcode.util.Utils
import com.jkcq.hrwtv.base.AllocationApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


/**
 *created by wq on 2019/4/1
 */
object ManagerRetrofitHelper {

    /**
     * BuildConfig
     */
    const val DEBUG=true;
    const val DEFAULT_TIMEOUT: Long = 15
    const val MAX_CACHE_SIZE: Long = 1024 * 1024 * 50 // 50M 的缓存大小

    private var retrofit: Retrofit? = null

    val service: ApiService by lazy { getRetrofit()!!.create(ApiService::class.java) }

    private fun getRetrofit(): Retrofit? {
        if (retrofit == null) {
            synchronized(ManagerRetrofitHelper::class.java) {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                            .baseUrl(AllocationApi.BaseUrl)  // baseUrl
                            .client(getOkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
//                            .addConverterFactory(MoshiConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build()
                }
            }
        }
        return retrofit
    }

    /**
     * 获取 OkHttpClient
     */
    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        //设置 请求的缓存的大小跟位置
        val cacheFile = File(Utils.getApp().cacheDir, "cache")
        val cache = Cache(cacheFile, MAX_CACHE_SIZE)
        builder.run {
           addInterceptor(httpLoggingInterceptor)
            addInterceptor(CacheInterceptor())
            cache(cache)  //添加缓存
            connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true) // 错误重连
            // cookieJar(CookieManager())
        }
        return builder.build()
    }

}