package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.MessageListData

/**
 * Create by   Administrator
 *      on     2018/12/21 12:10
 * description
 */
class MessageListContract {
    interface View : IBaseView{
        fun setMessageListData(messageListData: ArrayList<MessageListData>)
        fun showError(msg : String,errorCode : Int)
    }
    interface Presenter :IPresenter<View>{
        fun getMessageListData(admin_id: Int,token: String)
    }
}