package com.jkcq.hrwtv.http.bean

/**
 * created by wq on 2019/5/8
 */
class BaseResponse<T> {

    /**
     * version : 100
     * code : 2002
     * message : 会员APP首页H5配置缺失
     * obj : null
     */

    var version: Int = 0
    var code: Int = 0
    var msg: String? = null
    var data: T? = null
    override fun toString(): String {
        return "ManagerBaseResponse{version:$version, code:$code, message:$msg, obj:${data.toString()})"
    }


}
