package com.haidie.gridmember.mvp.contract.main

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter

/**
 * Create by   Administrator
 *      on     2018/12/28 18:08
 * description
 */
class MainContract {
    interface View :IBaseView{
        fun initXG()
    }
    interface Presenter : IPresenter<View>{
        fun getXGData( admin_id: Int,  device_token: String,token: String)
    }
}