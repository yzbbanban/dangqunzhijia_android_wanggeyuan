package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.OrderDepartData

/**
 * Create by   Administrator
 *      on     2018/12/18 16:12
 * description
 */
class SendOrderContract {
    interface View : IBaseView {
        fun setOrderResultData(obj: Object)
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        fun sendOrderData(admin_id: Int, token: String, id: String, datatype: String, assign_id: String)
    }
}