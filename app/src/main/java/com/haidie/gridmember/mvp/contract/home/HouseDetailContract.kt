package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.HouseDetailData

/**
 * Create by   Administrator
 *      on     2018/12/13 09:10
 * description
 */
class HouseDetailContract {
    interface View : IBaseView{
        fun setHouseDetailData(houseDetailData: HouseDetailData)
        fun showError(msg : String,errorCode : Int)
        fun setEditHouseStatusData(isSuccess : Boolean,msg : String)
        fun refresh()
    }
    interface Presenter : IPresenter<View>{
        fun getHouseDetailData( uid: Int,  token: String,  id: Int)
        fun getEditHouseStatusData( uid: Int, token: String, id: Int, type: Int)
    }
}