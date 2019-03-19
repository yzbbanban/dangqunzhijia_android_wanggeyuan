package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.CarePeopleData
import com.haidie.gridmember.mvp.bean.FlowPeopleData
import com.haidie.gridmember.mvp.bean.FlowPeopleListData
import com.haidie.gridmember.mvp.contract.home.CarePeopleListContract
import com.haidie.gridmember.mvp.contract.home.FlowPeopleContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils
import com.just.agentweb.LogUtils

class CarePeoplePresenter : BasePresenter<CarePeopleListContract.View>(), CarePeopleListContract.Presenter {

    override fun getCarePeoData(admin_id: Int, token: String, status: String, is_children: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getCarePeopleListData(admin_id, token, status, is_children)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<CarePeopleData>("获取数据失败") {
                override fun onNext(t: CarePeopleData) {
                    mRootView?.apply {
                        dismissLoading()
                        setCareData(t)
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