package com.jkcq.managersetting.bean

/**
 * created by wq on 2019/5/8
 */
class ManagerBaseResponse<T> {

    /**
     * version : 100
     * code : 2002
     * message : 会员APP首页H5配置缺失
     * obj : null
     */

    var version: Int = 0
    var code: Int = 0
    var message: String? = null
    var obj: T? = null
    override fun toString(): String {
        return "ManagerBaseResponse(version=$version, code=$code, message=$message, obj=${obj?.toString()})"
    }


}
