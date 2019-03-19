package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.ResidentInfoSearchListData
import com.haidie.gridmember.mvp.contract.home.ResidentInfoSearchContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/25 09:52
 * description
 */
class ResidentInfoSearchPresenter : BasePresenter<ResidentInfoSearchContract.View>(),
    ResidentInfoSearchContract.Presenter {
    override fun getResidentInfoSearchListData(admin_id: Int, token: String, name: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getResidentInfoSearchListData(admin_id, token, name)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ArrayList<ResidentInfoSearchListData>>("搜索居民信息失败") {
                override fun onNext(t: ArrayList<ResidentInfoSearchListData>) {
                    mRootView?.apply {
                        dismissLoading()
                        setResidentInfoSearchListData(t)
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