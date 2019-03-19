package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.CareCountData

/**
 * Create by   Administrator
 *      on     2018/12/21 12:10
 * description
 */
class GetCareContract {
    interface View : IBaseView {
        fun setCareListData(careCountData: ArrayList<CareCountData>)
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        fun getCareListData(admin_id: Int, token: String)
    }
}