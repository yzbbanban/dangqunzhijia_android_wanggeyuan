package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.VehicleInformationDetailData
import com.haidie.gridmember.mvp.contract.home.EditVehicleInformationContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/19 14:49
 * description
 */
class EditVehicleInformationPresenter : BasePresenter<EditVehicleInformationContract.View>(),EditVehicleInformationContract.Presenter{
    override fun getEditVehicleInformationData(
        admin_id: Int,token: String,id: Int,vehicle_type: Int?,lecense_id: String?,engine_id: String?,motor_id: String?, color: String?
    ) {
        val disposable = RetrofitManager.service.getEditVehicleInformationData(admin_id,token,id,vehicle_type,lecense_id,engine_id,motor_id,color)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<Boolean>("编辑失败") {
                override fun onNext(t: Boolean) {
                    mRootView?.setEditVehicleInformationData(t, "")
                }
                override fun onFail(e: ApiException) {
                    mRootView?.setEditVehicleInformationData(false, e.mMessage)
                }
            })
        addSubscription(disposable)
    }

    override fun getVehicleInformationDetailData(admin_id: Int, token: String, id: Int) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getVehicleInformationDetailData(admin_id, token, id)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<VehicleInformationDetailData>("获取数据失败") {
                override fun onNext(t: VehicleInformationDetailData) {
                    mRootView?.apply {
                        dismissLoading()
                        setVehicleInformationDetailData(t)
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

    override fun getNationalityListData(admin_id: Int, token: String, key: String) {
        checkViewAttached()
        val disposable = RetrofitManager.service.getNationalityListData(admin_id, token, key)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ArrayList<NationalityListData>>("获取数据失败") {
                override fun onNext(t: ArrayList<NationalityListData>) {
                    mRootView?.setNationalityListData(t)
                }
                override fun onFail(e: ApiException) {
                    mRootView?.showError(e.mMessage, e.errorCode)
                }
            })
        addSubscription(disposable)
    }
}