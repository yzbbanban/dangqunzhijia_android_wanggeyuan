package com.haidie.gridmember.mvp.presenter.mine

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.ModifyAvatarData
import com.haidie.gridmember.mvp.contract.mine.ModifyAvatarContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2019/01/04 10:29
 * description
 */
class ModifyAvatarPresenter : BasePresenter<ModifyAvatarContract.View>(),ModifyAvatarContract.Presenter{
    override fun getModifyAvatarData(admin_id: RequestBody, token: RequestBody, parts: MultipartBody.Part?) {
        checkViewAttached()
        val disposable = RetrofitManager.service.getModifyAvatarData(admin_id, token, parts)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ModifyAvatarData>("修改失败") {
                override fun onNext(t: ModifyAvatarData) {
                    mRootView?.setModifyAvatarData(t)
                }
                override fun onFail(e: ApiException) {
                    mRootView?.showError(e.mMessage,e.errorCode)
                }
            })
        addSubscription(disposable)
    }
}