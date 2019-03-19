package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.MessageListData
import com.haidie.gridmember.mvp.contract.home.MessageListContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/21 12:11
 * description
 */
class MessageListPresenter : BasePresenter<MessageListContract.View>(),MessageListContract.Presenter{
    override fun getMessageListData(admin_id: Int, token: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getMessageListData(admin_id, token)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ArrayList<MessageListData>>("获取消息列表失败") {
                override fun onNext(t: ArrayList<MessageListData>) {
                    mRootView?.apply {
                        dismissLoading()
                        setMessageListData(t)
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