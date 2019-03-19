package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.ReportManagementData

/**
 * Create by   Administrator
 *      on     2019/01/08 09:31
 * description
 */
class ReportManagementContract {
    interface View : IBaseView{
        fun setReportManagementData(reportManagementData: ReportManagementData)
        fun showError(msg : String,errorCode :Int)
    }
    interface Presenter :IPresenter<View>{
        fun getReportManagementData(admin_id: Int,token: String,year_month: String)
    }
}