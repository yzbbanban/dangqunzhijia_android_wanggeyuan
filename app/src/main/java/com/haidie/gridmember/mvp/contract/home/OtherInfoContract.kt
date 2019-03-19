package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.PersonalInfoData

/**
 * Create by   Administrator
 *      on     2018/12/19 09:01
 * description
 */
class OtherInfoContract {
    interface View : IBaseView{
        fun setPersonalInfoData(personalInfoData: PersonalInfoData)
        fun setNationalityListData(nationalityListData: ArrayList<NationalityListData>)
        fun showError(msg: String, errorCode: Int)
    }
    interface Presenter : IPresenter<View>{
        fun getPersonalInfoData( admin_id: Int, token: String, id: Int,type: Int)
        fun getNationalityListData(admin_id: Int, token: String, key: String)
    }
}