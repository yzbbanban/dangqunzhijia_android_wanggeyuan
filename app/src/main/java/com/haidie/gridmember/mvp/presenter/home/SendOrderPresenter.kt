package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.contract.home.*
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/13 16:21
 * description
 */
class SendOrderPresenter : BasePresenter<SendOrderContract.View>(), SendOrderContract.Presenter {
    override fun sendOrderData(admin_id: Int, token: String, id: String, datatype: String, assign_id: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.sendOrderDetailData(admin_id, token, id, datatype, assign_id)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<Object>("获取数据失败") {
                override fun onNext(t: Object) {
                    mRootView?.apply {
                        dismissLoading()
                        setOrderResultData(t)
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