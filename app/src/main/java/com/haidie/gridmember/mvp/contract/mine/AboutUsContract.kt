package com.haidie.gridmember.mvp.contract.mine

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.CheckVersionData

/**
 * Create by   Administrator
 *      on     2018/09/18 08:53
 * description
 */
class AboutUsContract {
    interface View : IBaseView {
        fun setCheckVersionData(checkVersionData: CheckVersionData)
        fun showError(msg : String,errorCode : Int)
    }

    interface Presenter : IPresenter<View> {
        fun getCheckVersionData(appType: Int)
    }
}