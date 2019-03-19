package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.PersonalInfoData
import com.haidie.gridmember.mvp.bean.ProvinceListData
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/12/19 17:19
 * description
 */
class EditBasicInfoContract {
    interface View : IBaseView{
        fun setPersonalInfoData(personalInfoData: PersonalInfoData)
        fun showError(msg: String, errorCode: Int)
        fun setNationalityListData(nationalityListData: ArrayList<NationalityListData>)
        fun setProvinceListData(provinceListData: ArrayList<ProvinceListData>)
        fun setEditBasicInfoSubmitData(isSuccess : Boolean,msg: String)
    }
    interface Presenter : IPresenter<View>{
        fun getPersonalInfoData( admin_id: Int, token: String, id: Int,type: Int?)
        fun getNationalityListData(admin_id: Int, token: String, key: String)
        fun getProvinceListData( admin_id: Int, token: String, province: String?, city: String?)
        fun getEditBasicInfoSubmitData(
            admin_id: RequestBody, token: RequestBody, id: RequestBody,

            name: RequestBody?, gender: RequestBody?, identity: RequestBody?,
            birthday: RequestBody?, phone: RequestBody?, nationality: RequestBody?, house_relation: RequestBody?,
            live_address_type: RequestBody?, register_address_province: RequestBody?,
            register_address_city: RequestBody?, register_address_area: RequestBody?,
            live_address_province: RequestBody?, live_address_city: RequestBody?,
            live_address_area: RequestBody?, house_id: RequestBody?,space_id: RequestBody?,
            marriage_status: RequestBody?, religion: RequestBody?,
            help_info: RequestBody?, parts: MultipartBody.Part?
        )
    }
}