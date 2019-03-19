package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.CareCountData
import com.haidie.gridmember.mvp.contract.home.GetCareContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/13 16:21
 * description
 */
class GetCarePresenter : BasePresenter<GetCareContract.View>(), GetCareContract.Presenter {

    override fun getCareListData(admin_id: Int, token: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getCareCountData(admin_id, token)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ArrayList<CareCountData>>("获取数据失败") {
                override fun onNext(t: ArrayList<CareCountData>) {
                    mRootView?.apply {
                        dismissLoading()
                        setCareListData(t)
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