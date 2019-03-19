package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.HomeBannerData
import com.haidie.gridmember.mvp.contract.home.HomeContract
import com.haidie.gridmember.mvp.event.ToProblemReportingEvent
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxBus
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

class HomePresenter : BasePresenter<HomeContract.View>() , HomeContract.Presenter {
    override fun attachView(mRootView: HomeContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }
    private fun registerEvent() {
        addSubscription(
            RxBus.getDefault().toFlowable(ToProblemReportingEvent::class.java)
                .subscribe { mRootView?.toProblemReporting(it.type) })
    }
    override fun getHomeBannerData(admin_id: Int, token: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getHomeBannerData(admin_id, token)
                .compose(SchedulerUtils.ioToMain())
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<HomeBannerData>("获取首页轮播数据失败"){
                    override fun onNext(t: HomeBannerData) {
                        mRootView?.apply {
                            dismissLoading()
                            setHomeBannerData(t)
                        }
                    }
                    override fun onFail(e: ApiException) {
                        mRootView?.apply {
                            dismissLoading()
                            showError(e.mMessage,e.errorCode)
                        }
                    }
                })
        addSubscription(disposable)
    }
}