package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.CareCountData
import com.haidie.gridmember.mvp.bean.WorkDetailData
import com.haidie.gridmember.mvp.contract.home.GetCareContract
import com.haidie.gridmember.mvp.contract.home.GetWorkDetailContract
import com.haidie.gridmember.mvp.contract.home.WorkContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/13 16:21
 * description
 */
class WorkDetailPresenter : BasePresenter<GetWorkDetailContract.View>(), GetWorkDetailContract.Presenter {

    override fun getWorkDetailData(admin_id: Int, token: String, id: String, datatype: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getWorkDetailData("" + admin_id, token, id, datatype)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<WorkDetailData>("获取数据失败") {
                override fun onNext(t: WorkDetailData) {
                    mRootView?.apply {
                        dismissLoading()
                        setWorkDetailData(t)
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