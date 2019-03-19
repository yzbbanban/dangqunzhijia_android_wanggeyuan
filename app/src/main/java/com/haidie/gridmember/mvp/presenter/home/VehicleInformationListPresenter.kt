package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.VehicleInformationListData
import com.haidie.gridmember.mvp.contract.home.VehicleInformationListContract
import com.haidie.gridmember.mvp.event.ReloadVehicleInformationListEvent
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxBus
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/19 11:19
 * description
 */
class VehicleInformationListPresenter : BasePresenter<VehicleInformationListContract.View>(),VehicleInformationListContract.Presenter{

    override fun attachView(mRootView: VehicleInformationListContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }
    private fun registerEvent() {
        addSubscription(
            RxBus.getDefault().toFlowable(ReloadVehicleInformationListEvent::class.java)
            .subscribe { mRootView?.refresh() })
    }
    override fun getVehicleInformationListData(admin_id: Int, token: String, id: Int) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getVehicleInformationListData(admin_id, token, id)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ArrayList<VehicleInformationListData>>("获取数据失败") {
                override fun onNext(t: ArrayList<VehicleInformationListData>) {
                    mRootView?.apply {
                        dismissLoading()
                        setVehicleInformationListData(t)
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
    override fun getDeleteVehicleInformationData(admin_id: Int, token: String, id: Int) {
        val disposable = RetrofitManager.service.getDeleteVehicleInformationData(admin_id, token, id)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<Boolean>("删除失败") {
                override fun onNext(t: Boolean) {
                    mRootView?.setDeleteVehicleInformationData(t, "")
                }
                override fun onFail(e: ApiException) {
                    mRootView?.setDeleteVehicleInformationData(false, e.mMessage)
                }
            })
        addSubscription(disposable)
    }
}