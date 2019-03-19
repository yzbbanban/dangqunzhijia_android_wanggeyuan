package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.HouseList
import com.haidie.gridmember.mvp.bean.HouseListData

/**
 * Create by   Administrator
 *      on     2018/12/12 18:09
 * description
 */
class HouseSearchListContract {
    interface View : IBaseView{
        fun setHouseListData(houseListData: ArrayList<HouseListData>)
        fun setHouseList(houseList: HouseList)
        fun showError(msg : String,errorCode : Int)
    }
    interface Presenter : IPresenter<View>{
        fun getHouseListData(uid: Int,token: String,title: String,unit: String)
        fun getHouseList(uid: Int, token: String, page: Int,size: Int, space: String, unit: String)
    }
}