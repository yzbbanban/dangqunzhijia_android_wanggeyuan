package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.ToDoListData

/**
 * Create by   Administrator
 *      on     2018/12/21 09:52
 * description
 */
class ToDoListContract {
    interface View : IBaseView{
        fun setToDoListData(toDoListData: ToDoListData)
        fun showError(msg : String,errorCode :Int)
    }
    interface Presenter : IPresenter<View>{
        fun getToDoListData(admin_id: Int,token: String)
    }
}