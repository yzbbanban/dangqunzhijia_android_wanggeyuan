package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.OrderData

/**
 * Create by   Administrator
 *      on     2018/12/18 16:12
 * description
 */
class WorkContract {
    interface View : IBaseView {
        fun setOrderData(orderData: OrderData)
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        fun getOrderData(admin_id: Int, token: String, status: String, page: String, size: String)
    }
}