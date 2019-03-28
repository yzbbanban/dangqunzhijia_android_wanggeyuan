package com.haidie.gridmember.mvp.presenter.main

import com.haidie.gridmember.Constants
import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.GridMemberData
import com.haidie.gridmember.mvp.contract.main.LoginContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils
import com.haidie.gridmember.utils.Preference

/**
 * Create by   Administrator
 *      on     2018/11/30 16:01
 * description
 */
class LoginPresenter : BasePresenter<LoginContract.View>(),
    LoginContract.Presenter {
    private var loginAccount by Preference(Constants.ACCOUNT, Constants.EMPTY_STRING)
    private var loginPassword by Preference(Constants.PASSWORD, Constants.EMPTY_STRING)
    private var loginStatus by Preference(Constants.LOGIN_STATUS, false)
    private var avatar by Preference(Constants.AVATAR, Constants.EMPTY_STRING)
    private var nickname by Preference(Constants.NICKNAME, Constants.EMPTY_STRING)
    private var mobile by Preference(Constants.MOBILE, Constants.EMPTY_STRING)
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var group_id by Preference(Constants.GROUP_ID, -1)
    override fun getLoginData(
        username: String,
        password: String,
        app_type: String,
        device_id: String,
        device_type: String
    ) {
        checkViewAttached()
        val disposable = RetrofitManager.service.getLoginData(username, password, app_type, device_id, device_type)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<GridMemberData>("登录失败") {
                override fun onNext(data: GridMemberData) {
                    loginAccount = data.userinfo.username
                    loginPassword = password
                    loginStatus = true
                    uid = data.userinfo.admin_id
                    token = data.userinfo.token
                    group_id = data.userinfo.group_id
                    avatar = data.userinfo.avatar
                    nickname = data.userinfo.nickname
                    mobile = data.userinfo.mobile
                    mRootView?.setLoginResult(true, "")
                }

                override fun onFail(e: ApiException) {
                    mRootView?.setLoginResult(false, e.mMessage)
                }
            })
        addSubscription(disposable)
    }
}