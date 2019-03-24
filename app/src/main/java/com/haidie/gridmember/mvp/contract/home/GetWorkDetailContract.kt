package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.WorkDetailData

/**
 * Create by   Administrator
 *      on     2018/12/18 16:12
 * description
 */
class GetWorkDetailContract {
    interface View : IBaseView {
        fun setWorkDetailData(workDetailData: WorkDetailData)
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        fun getWorkDetailData(admin_id: Int, token: String, id: String, datatype: String)
    }
}