package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.HouseDetailData
import com.haidie.gridmember.mvp.contract.home.HouseDetailContract
import com.haidie.gridmember.mvp.event.RefreshHouseDetail
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxBus
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/13 09:10
 * description
 */
class HouseDetailPresenter : BasePresenter<HouseDetailContract.View>(),HouseDetailContract.Presenter{

    override fun attachView(mRootView: HouseDetailContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }
    private fun registerEvent() {
        addSubscription(
            RxBus.getDefault().toFlowable(RefreshHouseDetail::class.java)
                .subscribe { mRootView?.refresh() })
    }
    override fun getHouseDetailData(uid: Int, token: String, id: Int) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getHouseDetailData(uid, token, id)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<HouseDetailData>("获取房间详情失败") {
                override fun onNext(t: HouseDetailData) {
                    mRootView?.apply {
                        dismissLoading()
                        setHouseDetailData(t)
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

    override fun getEditHouseStatusData(uid: Int, token: String, id: Int, type: Int) {
        val disposable = RetrofitManager.service.getEditHouseStatusData(uid, token, id, type)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<Boolean>("变更失败") {
                override fun onNext(t: Boolean) {
                    mRootView?.setEditHouseStatusData(t, "")
                }
                override fun onFail(e: ApiException) {
                    mRootView?.setEditHouseStatusData(false, e.mMessage)
                }
            })
        addSubscription(disposable)
    }
}