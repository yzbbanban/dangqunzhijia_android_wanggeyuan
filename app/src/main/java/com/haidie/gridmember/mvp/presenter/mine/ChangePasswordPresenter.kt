package com.haidie.gridmember.mvp.presenter.mine

import com.haidie.gridmember.Constants
import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.contract.mine.ChangePasswordContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiErrorCode
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils
import com.haidie.gridmember.utils.Preference

/**
 * Create by   Administrator
 *      on     2018/08/28 10:59
 * description
 */
class ChangePasswordPresenter : BasePresenter<ChangePasswordContract.View>(), ChangePasswordContract.Presenter {
    private var loginPassword: String by Preference(Constants.PASSWORD, Constants.EMPTY_STRING)
    private var loginStatus: Boolean by Preference(Constants.LOGIN_STATUS, false)
    override fun getChangePasswordData(uid: Int, token: String, oldpwd: String, newpwd: String, repwd: String) {
        checkViewAttached()
        val disposable = RetrofitManager.service.getChangePasswordData(uid, token, oldpwd, newpwd, repwd)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<String>("修改失败") {
                override fun onNext(t: String) {
                    mRootView!!.apply {
                        loginStatus = false
                        changePasswordSuccess()
                        loginPassword = newpwd
                    }
                }
                override fun onFail(e: ApiException) {
                    mRootView!!.apply {
                        if (e.errorCode == ApiErrorCode.SUCCESS) {
                            loginStatus = false
                            changePasswordSuccess()
                            loginPassword = newpwd
                        } else {
                            showError(e.mMessage, e.errorCode)
                        }
                    }
                }
            })
        addSubscription(disposable)
    }
}