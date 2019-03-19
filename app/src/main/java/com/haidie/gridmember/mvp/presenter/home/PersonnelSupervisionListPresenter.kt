package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.PersonnelSupervisionListData
import com.haidie.gridmember.mvp.contract.home.PersonnelSupervisionListContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/18 18:39
 * description
 */
class PersonnelSupervisionListPresenter : BasePresenter<PersonnelSupervisionListContract.View>(),
    PersonnelSupervisionListContract.Presenter {
    override fun getPersonnelSupervisionData(admin_id: Int, token: String, page: Int, size: Int, help_info: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable =
            RetrofitManager.service.getPersonnelSupervisionListData(admin_id, token, page, size, help_info)
                .compose(SchedulerUtils.ioToMain())
                .compose(RxUtils.handleResult())
                .subscribeWith(object : BaseObserver<PersonnelSupervisionListData>("获取数据列表失败") {
                    override fun onNext(t: PersonnelSupervisionListData) {
                        mRootView?.apply {
                            dismissLoading()
                            setPersonnelSupervisionData(t)
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