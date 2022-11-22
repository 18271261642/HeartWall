package com.jkcq.hrwtv.wu.newversion.bean

import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean


/**
 * @Author  Snail
 * @Date 2019-10-01
 * @Description
 **/
open class PKValueEntity(
    var showBean: DevicesDataShowBean

) : DetailsEntity() {
    init {
        TYPE = DetailsEntity.TYPE_ITEM
    }
}