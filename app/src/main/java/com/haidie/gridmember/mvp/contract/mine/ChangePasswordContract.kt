package com.haidie.gridmember.mvp.contract.mine

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter

/**
 * Create by   Administrator
 *      on     2018/08/28 10:57
 * description
 */
class ChangePasswordContract {
    interface View : IBaseView {
        fun changePasswordSuccess()
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        fun getChangePasswordData(uid: Int,token: String,oldpwd: String,newpwd: String, repwd: String)
    }
}