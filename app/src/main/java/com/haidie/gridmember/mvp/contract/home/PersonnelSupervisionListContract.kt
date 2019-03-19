package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.PersonnelSupervisionListData

/**
 * Create by   Administrator
 *      on     2018/12/18 18:39
 * description
 */
class PersonnelSupervisionListContract {
    interface View : IBaseView{
        fun setPersonnelSupervisionData(personnelSupervisionListData: PersonnelSupervisionListData)
        fun showError(msg : String,errorCode :Int)
    }
    interface Presenter : IPresenter<View>{
        fun getPersonnelSupervisionData( admin_id: Int,  token: String,  page: Int, size: Int,  help_info: String)
    }
}