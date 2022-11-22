package com.jkcq.managersetting.http

import com.jkcq.managersetting.bean.*
import io.reactivex.Observable
import retrofit2.http.*


/**
 *created by wq on 2019/3/25
 */
interface ApiService {

    /**
     * 获取版本信息
     */
    @GET("/api/heartratewall/device_version/latest_version")
    fun getVersionInfo(@Query("deviceType") deviceTypeId: String): Observable<ManagerBaseResponse<VersionInfo>>


}