package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.FlowPeopleData
import com.haidie.gridmember.mvp.bean.FlowPeopleListData
import com.haidie.gridmember.mvp.contract.home.FlowPeopleContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils
import com.just.agentweb.LogUtils

class FlowPeoplePresenter : BasePresenter<FlowPeopleContract.View>(), FlowPeopleContract.Presenter {

    override fun getFlowPeoData(admin_id: Int, token: String, status: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getFlowPeopleData(admin_id, token, status)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<FlowPeopleData>("获取数据失败") {
                override fun onNext(t: FlowPeopleData) {
                    mRootView?.apply {
                        dismissLoading()
                        setFlowPeoData(t)
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