package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.VehicleInformationDetailData

/**
 * Create by   Administrator
 *      on     2018/12/19 14:49
 * description
 */
class EditVehicleInformationContract {
    interface View : IBaseView{
        fun setVehicleInformationDetailData(vehicleInformationDetailData: VehicleInformationDetailData)
        fun showError(msg : String,errorCode : Int)
        fun setNationalityListData(nationalityListData: ArrayList<NationalityListData>)
        fun setEditVehicleInformationData(isSuccess : Boolean,msg: String)
    }
    interface Presenter : IPresenter<View>{
        fun getVehicleInformationDetailData(admin_id: Int,token: String,id: Int)
        fun getNationalityListData(admin_id: Int, token: String, key: String)
        fun getEditVehicleInformationData(admin_id: Int,token: String, id: Int,vehicle_type: Int?, lecense_id: String?,
             engine_id: String?,  motor_id: String?, color: String?)
    }
}