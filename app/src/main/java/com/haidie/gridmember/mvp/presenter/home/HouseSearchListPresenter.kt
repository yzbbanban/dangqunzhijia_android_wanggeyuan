package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.HouseList
import com.haidie.gridmember.mvp.bean.HouseListData
import com.haidie.gridmember.mvp.contract.home.HouseSearchListContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/12 18:09
 * description
 */
class HouseSearchListPresenter : BasePresenter<HouseSearchListContract.View>(),HouseSearchListContract.Presenter{
    override fun getHouseList(uid: Int, token: String, page: Int, size: Int, space: String, unit: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getHouseList(uid, token, page, size, space, unit)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<HouseList>("获取数据失败") {
                override fun onNext(t: HouseList) {
                    mRootView?.apply {
                        dismissLoading()
                        setHouseList(t)
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
    override fun getHouseListData(uid: Int, token: String, title: String, unit: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getHouseListData(uid, token, title, unit)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ArrayList<HouseListData>>("获取房子的列表失败") {
                override fun onNext(t: ArrayList<HouseListData>) {
                    mRootView?.apply {
                        dismissLoading()
                        setHouseListData(t)
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