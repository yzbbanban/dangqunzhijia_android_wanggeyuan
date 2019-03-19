package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.contract.home.NewVehicleInformationContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/19 13:26
 * description
 */
class NewVehicleInformationPresenter : BasePresenter<NewVehicleInformationContract.View>(),NewVehicleInformationContract.Presenter{

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

    override fun getNewVehicleInformationData(
        admin_id: Int,token: String,cid: Int,vehicle_type: Int,lecense_id: String,
        engine_id: String,motor_id: String,color: String
    ) {
        val disposable = RetrofitManager.service.getNewVehicleInformationData(
            admin_id,token,cid,vehicle_type,lecense_id,engine_id,motor_id,color)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<Boolean>("新增失败") {
                override fun onNext(t: Boolean) {
                    mRootView?.setNewVehicleInformationData(t, "")
                }
                override fun onFail(e: ApiException) {
                    mRootView?.setNewVehicleInformationData(false, e.mMessage)
                }
            })
        addSubscription(disposable)
    }
}