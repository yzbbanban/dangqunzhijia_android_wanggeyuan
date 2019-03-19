package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.PersonalInfoData

/**
 * Create by   Administrator
 *      on     2018/12/17 19:49
 * description
 */
class EducationalWorkInfoContract {
    interface View : IBaseView{
        fun setPersonalInfoData(personalInfoData: PersonalInfoData)
        fun setNationalityListData(nationalityListData: ArrayList<NationalityListData>)
        fun showError(msg: String, errorCode: Int)
        fun setEditPersonalInfoData(isSuccess : Boolean,msg: String)
    }
    interface Presenter :IPresenter<View>{
        fun getPersonalInfoData( admin_id: Int, token: String, id: Int,type: Int)
        fun getNationalityListData(admin_id: Int, token: String, key: String)
        fun getEditPersonalInfoData(
            admin_id: Int, token: String, id: Int,

            politics_status: Int?, entry_party_time: String?, org_relation_address_province: Int?,
            org_relation_address_city: Int?, org_relation_address_area: Int?,
            org_relation_address: String?, commend_status: String?,

            education: Int?, company: String?, job: String?, special_skills: Int?
        )
    }
}