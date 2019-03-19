package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.NationalityListData

/**
 * Create by   Administrator
 *      on     2018/12/19 13:26
 * description
 */
class NewVehicleInformationContract {
    interface View : IBaseView {
        fun setNationalityListData(nationalityListData: ArrayList<NationalityListData>)
        fun showError(msg: String, errorCode: Int)
        fun setNewVehicleInformationData(isSuccess: Boolean, msg: String)
    }
    interface Presenter : IPresenter<View> {
        fun getNationalityListData(admin_id: Int, token: String, key: String)
        fun getNewVehicleInformationData(
            admin_id: Int, token: String, cid: Int,
            vehicle_type: Int, lecense_id: String, engine_id: String, motor_id: String, color: String
        )
    }
}