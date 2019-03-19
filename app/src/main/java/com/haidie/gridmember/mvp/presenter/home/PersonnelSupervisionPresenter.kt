package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.SuperviseCountData
import com.haidie.gridmember.mvp.contract.home.PersonnelSupervisionContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/20 19:39
 * description
 */
class PersonnelSupervisionPresenter : BasePresenter<PersonnelSupervisionContract.View>(),PersonnelSupervisionContract.Presenter{
    override fun getSuperviseCountData(admin_id: Int, token: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getSuperviseCountData(admin_id, token)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ArrayList<SuperviseCountData>>("获取数据失败") {
                override fun onNext(t: ArrayList<SuperviseCountData>) {
                    mRootView?.apply {
                        dismissLoading()
                        setSuperviseCountData(t)
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