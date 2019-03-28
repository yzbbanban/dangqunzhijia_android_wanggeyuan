package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.CareCountData
import com.haidie.gridmember.mvp.bean.OrderDetailData
import com.haidie.gridmember.mvp.bean.WorkDetailData
import com.haidie.gridmember.mvp.contract.home.GetCareContract
import com.haidie.gridmember.mvp.contract.home.GetOrderDetailContract
import com.haidie.gridmember.mvp.contract.home.GetWorkDetailContract
import com.haidie.gridmember.mvp.contract.home.WorkContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/13 16:21
 * description
 */
class OrderDetailPresenter : BasePresenter<GetOrderDetailContract.View>(), GetOrderDetailContract.Presenter {

    override fun getOrderDetailData(admin_id: Int, token: String, id: String, datatype: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getOrderDetailData(admin_id, token, id, datatype)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<OrderDetailData>("获取数据失败") {
                override fun onNext(t: OrderDetailData) {
                    mRootView?.apply {
                        dismissLoading()
                        setOrderDetailData(t)
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