package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.ResidentInfoSearchListData

/**
 * Create by   Administrator
 *      on     2018/12/25 09:52
 * description
 */
class ResidentInfoSearchContract {
    interface View : IBaseView{
        fun setResidentInfoSearchListData(residentInfoSearchListData: ArrayList<ResidentInfoSearchListData>)
        fun showError(msg : String,errorCode : Int)
    }
    interface Presenter :IPresenter<View>{
        fun getResidentInfoSearchListData(admin_id: Int,token: String, name: String)
    }
}