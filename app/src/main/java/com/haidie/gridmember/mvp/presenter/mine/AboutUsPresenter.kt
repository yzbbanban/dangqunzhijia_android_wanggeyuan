package com.haidie.gridmember.mvp.presenter.mine

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.CheckVersionData
import com.haidie.gridmember.mvp.contract.mine.AboutUsContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/09/18 08:53
 * description
 */
class AboutUsPresenter : BasePresenter<AboutUsContract.View>(), AboutUsContract.Presenter {
    override fun getCheckVersionData(appType: Int) {
        checkViewAttached()
        val disposable = RetrofitManager.service.getCheckVersionData(appType)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<CheckVersionData>("检查更新失败") {
                override fun onNext(t: CheckVersionData) {
                    mRootView!!.setCheckVersionData(t)
                }
                override fun onFail(e: ApiException) {
                    mRootView!!.showError(e.mMessage, e.errorCode)
                }
            })
        addSubscription(disposable)
    }
}