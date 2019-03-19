package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.VehicleInformationListData

/**
 * Create by   Administrator
 *      on     2018/12/19 11:19
 * description
 */
class VehicleInformationListContract {
    interface View : IBaseView{
        fun setVehicleInformationListData(vehicleInformationListData: ArrayList<VehicleInformationListData>)
        fun showError(msg : String,errorCode : Int)
        fun refresh()
        fun setDeleteVehicleInformationData(isSuccess : Boolean,msg: String)
    }
    interface Presenter : IPresenter<View>{
        fun getVehicleInformationListData( admin_id: Int, token: String, id: Int)
        fun getDeleteVehicleInformationData( admin_id: Int,token: String,id: Int)
    }
}