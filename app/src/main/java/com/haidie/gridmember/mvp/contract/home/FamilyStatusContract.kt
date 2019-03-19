package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.PersonalInfoData

/**
 * Create by   Administrator
 *      on     2018/12/18 13:21
 * description
 */
class FamilyStatusContract {
    interface View : IBaseView{
        fun setPersonalInfoData(personalInfoData: PersonalInfoData)
        fun setNationalityListData(nationalityListData: ArrayList<NationalityListData>)
        fun showError(msg: String, errorCode: Int)
        fun setEditFamilyPersonalInfoData(isSuccess : Boolean,msg: String)
    }
    interface Presenter : IPresenter<View>{
        fun getPersonalInfoData( admin_id: Int, token: String, id: Int,type: Int)
        fun getNationalityListData(admin_id: Int, token: String, key: String)
        fun getEditFamilyPersonalInfoData(
            admin_id: Int, token: String, id: Int,
            family_income_source: Int?,
            family_income: String?, family_wage_income: String?,
            family_operational_income: String?, family_low_income: String?,
            family_pension_income: String?, family_area_num: String?,
            family_person_num: String?, family_area_time: String?,
            family_area_flow_num: String?, is_enter_baseic_old_insurance: Int?,
            is_enter_baseic_medical_insurance: Int?, is_enter_new_insurance: Int?
        )
    }
}