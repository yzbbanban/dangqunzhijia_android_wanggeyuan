package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.OrderDepartData

/**
 * Create by   Administrator
 *      on     2018/12/18 16:12
 * description
 */
class GetOrderDepartContract {
    interface View : IBaseView {
        fun setOrderDepartData(orderDepartData: ArrayList<OrderDepartData>)
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        fun getOrderDepartData(admin_id: Int, token: String)
    }
}