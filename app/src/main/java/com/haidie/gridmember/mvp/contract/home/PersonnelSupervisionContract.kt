package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.SuperviseCountData

/**
 * Create by   Administrator
 *      on     2018/12/20 19:39
 * description
 */
class PersonnelSupervisionContract {
    interface View : IBaseView{
        fun setSuperviseCountData(superviseCountData: ArrayList<SuperviseCountData>)
        fun showError(msg : String,errorCode : Int)
    }
    interface Presenter : IPresenter<View>{
        fun getSuperviseCountData(admin_id: Int,token: String)
    }
}