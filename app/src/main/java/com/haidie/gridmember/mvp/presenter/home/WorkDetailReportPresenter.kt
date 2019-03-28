package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.contract.home.WorkDetailReportContract
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
class WorkDetailReportPresenter : BasePresenter<WorkDetailReportContract.View>(), WorkDetailReportContract.Presenter {
    override fun getWorkDetailReportData(
        admin_id: RequestBody,
        token: RequestBody,
        id: RequestBody,
        datatype: RequestBody,
        handle_detail: RequestBody,
        image: ArrayList<MultipartBody.Part>
    ) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable =
            RetrofitManager.service.workDetailReportData(admin_id, token, id, datatype, handle_detail, image)
                .compose(SchedulerUtils.ioToMain())
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<Object>("获取数据失败") {
                    override fun onNext(t: Object) {
                        mRootView?.apply {
                            dismissLoading()
                            setWorkDetailReportData(t)
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