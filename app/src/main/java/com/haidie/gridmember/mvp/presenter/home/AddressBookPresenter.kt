package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.AddressBookData
import com.haidie.gridmember.mvp.contract.home.AddressBookContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/18 16:13
 * description
 */
class AddressBookPresenter : BasePresenter<AddressBookContract.View>(),AddressBookContract.Presenter{
    override fun getAddressBookData(admin_id: Int, token: String) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getAddressBookData(admin_id, token)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ArrayList<AddressBookData>>("获取数据失败") {
                override fun onNext(t: ArrayList<AddressBookData>) {
                    mRootView?.apply {
                        dismissLoading()
                        setAddressBookData(t)
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