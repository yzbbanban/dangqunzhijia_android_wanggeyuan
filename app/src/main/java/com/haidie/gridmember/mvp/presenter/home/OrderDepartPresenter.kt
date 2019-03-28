package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.CareCountData
import com.haidie.gridmember.mvp.bean.OrderDepartData
import com.haidie.gridmember.mvp.bean.OrderDetailData
import com.haidie.gridmember.mvp.bean.WorkDetailData
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
class OrderDepartPresenter : BasePresenter<GetOrderDepartContract.View>(), GetOrderDepartContract.Presenter {

    override fun getOrderDepartData(admin_id: Int, token: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getOrderDepartData(admin_id, token)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ArrayList<OrderDepartData>>("获取数据失败") {
                override fun onNext(t: ArrayList<OrderDepartData>) {
                    mRootView?.apply {
                        dismissLoading()
                        setOrderDepartData(t)
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