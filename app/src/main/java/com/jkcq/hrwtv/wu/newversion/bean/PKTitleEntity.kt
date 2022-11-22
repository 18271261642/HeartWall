package com.jkcq.hrwtv.wu.newversion.bean


/**
 * @Author  Snail
 * @Date 2019-10-01
 * @Description
 **/
open class PKTitleEntity(
    var pkCal: Int,
    var pkRes: Int,
    var pkTitle: String,
    var pkValue: String,
    var pkCalValue: String,
    var pkValueColor: Int,
    var bgRes: Int

) : DetailsEntity() {
    init {
        TYPE = DetailsEntity.TYPE_TITLE
    }
}