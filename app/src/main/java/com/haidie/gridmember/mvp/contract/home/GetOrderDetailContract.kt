package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.OrderDetailData
import com.haidie.gridmember.mvp.bean.WorkDetailData

/**
 * Create by   Administrator
 *      on     2018/12/18 16:12
 * description
 */
class GetOrderDetailContract {
    interface View : IBaseView {
        fun setOrderDetailData(orderDetailData: OrderDetailData)
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        fun getOrderDetailData(admin_id: Int, token: String, id: String, datatype: String)
    }
}