package com.haidie.gridmember.mvp.presenter.order

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.OrderData
import com.haidie.gridmember.mvp.contract.home.OrderContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/18 16:13
 * description
 */
class OrderPresenter : BasePresenter<OrderContract.View>(), OrderContract.Presenter {

    override fun getOrderData(admin_id: Int, token: String, status: String, page: String, size: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getOrderData(admin_id, token, status, page, size)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<OrderData>("获取数据失败") {
                override fun onNext(t: OrderData) {
                    mRootView?.apply {
                        dismissLoading()
                        setOrderData(t)
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