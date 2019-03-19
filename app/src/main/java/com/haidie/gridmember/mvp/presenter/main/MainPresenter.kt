package com.haidie.gridmember.mvp.presenter.main

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.contract.main.MainContract
import com.haidie.gridmember.mvp.event.ReloadMineEvent
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxBus
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/28 18:08
 * description
 */
class MainPresenter : BasePresenter<MainContract.View>(),MainContract.Presenter{
    override fun attachView(mRootView: MainContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }
    private fun registerEvent() {
        addSubscription(
            RxBus.getDefault().toFlowable(ReloadMineEvent::class.java)
                .subscribe { mRootView?.initXG() })
    }
    override fun getXGData(admin_id: Int, device_token: String,token: String) {
        val disposable = RetrofitManager.service.getXGData(admin_id, device_token,token)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<String>("") {
                override fun onFail(e: ApiException) {}
                override fun onNext(t: String) {}
            })
        addSubscription(disposable)
    }
}