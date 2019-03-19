package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.ReportManagementData
import com.haidie.gridmember.mvp.contract.home.ReportManagementContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2019/01/08 09:31
 * description
 */
class ReportManagementPresenter : BasePresenter<ReportManagementContract.View>(),ReportManagementContract.Presenter{
    override fun getReportManagementData(admin_id: Int, token: String, year_month: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getReportManagementData(admin_id, token, year_month)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ReportManagementData>("获取数据失败") {
                override fun onNext(t: ReportManagementData) {
                    mRootView?.apply {
                        dismissLoading()
                        setReportManagementData(t)
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