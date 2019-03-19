package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.HomeBannerData

class HomeContract {
    interface View : IBaseView {
        fun setHomeBannerData( homeBannerData: HomeBannerData)
        fun showError(msg: String, errorCode: Int)
        fun toProblemReporting(type: String)
    }

    interface Presenter : IPresenter<View> {
        fun getHomeBannerData( admin_id: Int,  token: String)
    }
}