package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.contract.ReturnVisitContract
import com.haidie.gridmember.mvp.contract.home.ReturnVisitRegistrationContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/12/21 09:09
 * description
 */
class ReturnVisitPresenter : BasePresenter<ReturnVisitContract.View>(), ReturnVisitContract.Presenter{
    override fun getReturnVisitData(
        admin_id: RequestBody,
        token: RequestBody,
        id: RequestBody,
        type: RequestBody,
        content: RequestBody,
        address: RequestBody,
        pic: MultipartBody.Part
    ) {
        val disposable = RetrofitManager.service.getVisitData(
            admin_id, token, id, type,content, address,pic)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<Boolean>("提交失败") {
                override fun onNext(t: Boolean) {
                    mRootView?.setReturnVisitData(t, "")
                }
                override fun onFail(e: ApiException) {
                    mRootView?.setReturnVisitData(false, e.mMessage)
                }
            })
        addSubscription(disposable)
    }


}