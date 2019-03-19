package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.ToDoListData
import com.haidie.gridmember.mvp.contract.home.ToDoListContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/21 09:52
 * description
 */
class ToDoListPresenter : BasePresenter<ToDoListContract.View>(),ToDoListContract.Presenter{
    override fun getToDoListData(admin_id: Int, token: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getToDoListData(admin_id, token)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ToDoListData>("获取数据失败") {
                override fun onNext(t: ToDoListData) {
                    mRootView?.apply {
                        dismissLoading()
                        setToDoListData(t)
                    }
                }

                override fun onFail(e: ApiException) {
                    mRootView?.apply {
                        dismissLoading()
                        showError(e.mMessage, e.errorCode)
                    }
                }
            })
        addSubscription(disposable)
    }
}