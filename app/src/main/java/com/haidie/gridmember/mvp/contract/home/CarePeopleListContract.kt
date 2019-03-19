package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.CarePeopleData
import com.haidie.gridmember.mvp.bean.FlowPeopleData
import com.haidie.gridmember.mvp.bean.FlowPeopleListData

/**
 * Create by   Administrator
 *      on     2018/12/18 16:12
 * description
 */
class CarePeopleListContract {
    interface View : IBaseView {
        fun setCareData(carePeopleData: CarePeopleData)
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        fun getCarePeoData(admin_id: Int, token: String, status: String, is_children: String)
    }
}