package com.jkcq.hrwtv.http

import com.blankj.utilcode.util.Utils
import com.jkcq.hrwtv.AllocationApi
import com.juxun.physical.http.BuildConfig
import com.juxun.physical.http.interceptor.CacheInterceptor
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
object RetrofitHelper {


    private var retrofit: Retrofit? = null
    private var noAuthretrofit: Retrofit? = null

    val service: ApiService by lazy { getRetrofit()!!.create(ApiService::class.java) }
    val noAuthservice: ApiService by lazy { getNoAutoRetrofit()!!.create(ApiService::class.java) }

    private fun getRetrofit(): Retrofit? {
        if (retrofit == null) {
            synchronized(RetrofitHelper::class.java) {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                        .baseUrl(AllocationApi.BaseUrl)  // baseUrl
                        .client(getOkHttpClient(true))
                        .addConverterFactory(GsonConverterFactory.create())
//                            .addConverterFactory(MoshiConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
                }
            }
        }
        return retrofit
    }

    private fun getNoAutoRetrofit(): Retrofit? {
        if (noAuthretrofit == null) {
            synchronized(RetrofitHelper::class.java) {
                if (noAuthretrofit == null) {
                    noAuthretrofit = Retrofit.Builder()
                        .baseUrl(AllocationApi.BaseUrl)  // baseUrl
                        .client(getOkHttpClient(false))
                        .addConverterFactory(GsonConverterFactory.create())
//                            .addConverterFactory(MoshiConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
                }
            }
        }
        return noAuthretrofit
    }

    /**
     * 获取 OkHttpClient
     */
    private fun getOkHttpClient(isToken: Boolean): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        //设置 请求的缓存的大小跟位置
        val cacheFile = File(Utils.getApp().cacheDir, "cache")
        val cache = Cache(cacheFile, BuildConfig.MAX_CACHE_SIZE)
        if (isToken) {
            builder.run {
                addInterceptor(httpLoggingInterceptor)
                addInterceptor(CommentInterceptor())
                addInterceptor(CacheInterceptor())
//            cache(cache)  //添加缓存
                connectTimeout(BuildConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                readTimeout(BuildConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                writeTimeout(BuildConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                retryOnConnectionFailure(true) // 错误重连
                // cookieJar(CookieManager())
            }
        } else {
            builder.run {
                addInterceptor(httpLoggingInterceptor)
                addInterceptor(CacheInterceptor())
//            cache(cache)  //添加缓存
                connectTimeout(BuildConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                readTimeout(BuildConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                writeTimeout(BuildConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                retryOnConnectionFailure(true) // 错误重连
                // cookieJar(CookieManager())
            }
        }

        return builder.build()
    }

}