package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.contract.home.CheckInPunchContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/12/13 16:21
 * description
 */
class CheckInPunchPresenter : BasePresenter<CheckInPunchContract.View>(), CheckInPunchContract.Presenter {
    override fun getHouseDetailCheckInPunchData(
        uid: RequestBody, token: RequestBody, id: RequestBody, childen_id: RequestBody?,type: RequestBody, content: RequestBody,
        address: RequestBody, help_time: RequestBody,house_type: RequestBody, parts: MultipartBody.Part
    ) {
        val disposable = RetrofitManager.service.getHouseDetailCheckInPunchData(
            uid, token, id,childen_id, type,content, address, help_time,house_type,parts)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<Boolean>("提交失败") {
                override fun onNext(t: Boolean) {
                    mRootView?.setHouseDetailCheckInPunchData(t, "")
                }
                override fun onFail(e: ApiException) {
                    mRootView?.setHouseDetailCheckInPunchData(false, e.mMessage)
                }
            })
        addSubscription(disposable)
    }
}