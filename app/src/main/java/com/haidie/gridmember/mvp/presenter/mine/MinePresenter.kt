package com.haidie.gridmember.mvp.presenter.mine

import com.haidie.gridmember.Constants
import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.contract.mine.MineContract
import com.haidie.gridmember.mvp.event.ReloadMineEvent
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiErrorCode
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxBus
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils
import com.haidie.gridmember.utils.Preference

/**
 * Create by   Administrator
 *      on     2018/12/01 14:55
 * description
 */
class MinePresenter : BasePresenter<MineContract.View>(),
    MineContract.Presenter{
    private var loginStatus: Boolean by Preference(Constants.LOGIN_STATUS, false)
    override fun attachView(mRootView: MineContract.View) {
        super.attachView(mRootView)
        registerEvent()
    }
    private fun registerEvent() {
        addSubscription(
            RxBus.getDefault().toFlowable(ReloadMineEvent::class.java)
                .subscribe { mRootView?.refresh() })
    }
    override fun getLogoutData(uid: Int) {
        checkViewAttached()
        val disposable = RetrofitManager.service.getLogoutData(uid)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<String>("退出失败") {

                override fun onNext(t: String) {
                    loginStatus = false
                    mRootView?.setLogoutData(true,"")
                }
                override fun onFail(e: ApiException) {
                    mRootView?.apply {
                        if (e.errorCode == ApiErrorCode.SUCCESS) {
                            loginStatus = false
                            mRootView?.setLogoutData(true,e.mMessage)
                        } else {
                            mRootView?.setLogoutData(false,e.mMessage)
                        }
                    }
                }
            })
        addSubscription(disposable)
    }
}