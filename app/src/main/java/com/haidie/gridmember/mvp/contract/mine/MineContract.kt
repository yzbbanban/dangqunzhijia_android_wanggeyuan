package com.haidie.gridmember.mvp.contract.mine

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter

/**
 * Create by   Administrator
 *      on     2018/12/01 14:55
 * description
 */
class MineContract {
    interface View : IBaseView {
        fun setLogoutData(isSuccess: Boolean, msg: String)
        fun refresh()
    }

    interface Presenter : IPresenter<View> {
        fun getLogoutData(uid: Int)
    }
}