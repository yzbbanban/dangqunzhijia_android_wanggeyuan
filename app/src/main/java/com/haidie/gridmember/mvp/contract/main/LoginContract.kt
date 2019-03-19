package com.haidie.gridmember.mvp.contract.main

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter

/**
 * Create by   Administrator
 *      on     2018/11/30 15:58
 * description
 */
class LoginContract {
    interface View : IBaseView {
        fun setLoginResult(isSuccess : Boolean,msg : String)
    }
    interface Presenter : IPresenter<View> {
        fun getLoginData(username: String, password: String,app_type: String, device_id: String,device_type: String)
    }
}