package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.ProvinceListData
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/12/14 13:09
 * description
 */
class BasicInfoContract {
    interface View : IBaseView {
        fun setNationalityListData(nationalityListData: ArrayList<NationalityListData>)
        fun showError(msg: String, errorCode: Int)
        fun setProvinceListData(provinceListData: ArrayList<ProvinceListData>)
        fun setBasicInfoSubmitData(isSuccess : Boolean,msg : String)
    }

    interface Presenter : IPresenter<View> {
        fun getNationalityListData(admin_id: Int, token: String, key: String)
        fun getProvinceListData( admin_id: Int, token: String, province: String?, city: String?)
        fun getBasicInfoSubmitData(
            admin_id: RequestBody, token: RequestBody, name: RequestBody, gender: RequestBody?,
            identity: RequestBody?, birthday: RequestBody?, phone: RequestBody?, nationality: RequestBody?,
            house_relation: RequestBody?, register_address_province: RequestBody?,
            register_address_city: RequestBody?, register_address_area: RequestBody?,
            register_address_street: RequestBody?, live_address_province: RequestBody?,
            live_address_city: RequestBody?, live_address_area: RequestBody?,
            live_address_street: RequestBody?, live_address_type: RequestBody?,
            marriage_status: RequestBody?,religion: RequestBody?, help_info: RequestBody?,
            house_id: RequestBody?,space_id: RequestBody?, parts: MultipartBody.Part?
        )
    }
}