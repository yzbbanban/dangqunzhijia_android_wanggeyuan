package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.NoticeListData

/**
 * Create by   Administrator
 *      on     2018/12/20 18:36
 * description
 */
class NoticeListContract {
    interface View : IBaseView{
        fun setNoticeListData(noticeListData: ArrayList<NoticeListData>)
        fun showError(msg : String,errorCode :Int)
    }
    interface Presenter : IPresenter<View>{
        fun getNoticeListData(admin_id: Int,token: String)
    }
}