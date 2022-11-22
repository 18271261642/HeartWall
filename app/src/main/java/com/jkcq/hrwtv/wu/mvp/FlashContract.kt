package com.jkcq.hrwtv.wu.mvp

import com.jkcq.hrwtv.base.mvp.BaseView
import com.jkcq.hrwtv.http.bean.AloneClubInfoBean
import com.jkcq.hrwtv.http.bean.ClubInfo

interface FlashContract {

    interface FlashView : BaseView {
        fun getClubSuccess(info: AloneClubInfoBean)
        fun getClubFailed();
    }

    interface IFlashModel {
        fun getClubInfo()
    }
}
