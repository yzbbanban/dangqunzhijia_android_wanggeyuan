package com.haidie.gridmember.mvp.presenter.main

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.CheckVersionData
import com.haidie.gridmember.mvp.contract.main.SplashContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils


/**
 * Create by   Administrator
 *      on     2018/08/29 10:11
 * description
 */
class SplashPresenter : BasePresenter<SplashContract.View>(), SplashContract.Presenter {
    override fun getCheckVersionData(appType: Int) {
        val disposable = RetrofitManager.service.getCheckVersionData(appType)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<CheckVersionData>("获取数据失败") {
                override fun onNext(data: CheckVersionData) {
                    mRootView!!.setCheckVersionData(data)
                }
                override fun onFail(e: ApiException) {
                    mRootView!!.showError(e.mMessage, e.errorCode)
                }
            })
        addSubscription(disposable)
    }
}