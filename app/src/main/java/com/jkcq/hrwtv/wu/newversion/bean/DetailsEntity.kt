package com.jkcq.hrwtv.wu.newversion.bean


/**
 * @Author  Snail
 * @Date 2019-10-01
 * @Description
 **/
open class DetailsEntity {
    protected var TYPE = TYPE_TITLE

    companion object {
        const val TYPE_HEAD = 0
        const val TYPE_TITLE = 1
        const val TYPE_ITEM = 2
    }

    fun getType(): Int = TYPE

    fun setType(type: Int) {
        TYPE = type
    }
}