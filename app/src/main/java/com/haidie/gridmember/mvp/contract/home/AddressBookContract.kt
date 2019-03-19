package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.AddressBookData

/**
 * Create by   Administrator
 *      on     2018/12/18 16:12
 * description
 */
class AddressBookContract {
    interface View : IBaseView{
        fun setAddressBookData(addressBookData: ArrayList<AddressBookData>)
        fun showError(msg : String,errorCode : Int)
    }
    interface Presenter : IPresenter<View>{
        fun getAddressBookData(admin_id: Int, token: String)
    }
}