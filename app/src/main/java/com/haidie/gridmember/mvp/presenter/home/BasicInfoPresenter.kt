package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.ProvinceListData
import com.haidie.gridmember.mvp.contract.home.BasicInfoContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/12/14 13:09
 * description
 */
class BasicInfoPresenter : BasePresenter<BasicInfoContract.View>(),BasicInfoContract.Presenter{

    override fun getNationalityListData(admin_id: Int, token: String, key: String) {
        checkViewAttached()
        val disposable = RetrofitManager.service.getNationalityListData(admin_id, token, key)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ArrayList<NationalityListData>>("获取数据失败") {
                override fun onNext(t: ArrayList<NationalityListData>) {
                    mRootView?.setNationalityListData(t)
                }
                override fun onFail(e: ApiException) {
                    mRootView?.showError(e.mMessage, e.errorCode)
                }
            })
        addSubscription(disposable)
    }

    override fun getProvinceListData(admin_id: Int, token: String, province: String?, city: String?) {
        val disposable = RetrofitManager.service.getProvinceListData(admin_id, token, province, city)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ArrayList<ProvinceListData>>("获取数据失败") {
                override fun onNext(t: ArrayList<ProvinceListData>) {
                    mRootView?.setProvinceListData(t)
                }

                override fun onFail(e: ApiException) {
                    mRootView?.showError(e.mMessage, e.errorCode)
                }
            })
        addSubscription(disposable)
    }

    override fun getBasicInfoSubmitData(
        admin_id: RequestBody,token: RequestBody,name: RequestBody, gender: RequestBody?,
        identity: RequestBody?,birthday: RequestBody?, phone: RequestBody?, nationality: RequestBody?,
        house_relation: RequestBody?, register_address_province: RequestBody?,
        register_address_city: RequestBody?,register_address_area: RequestBody?,
        register_address_street: RequestBody?,live_address_province: RequestBody?,
        live_address_city: RequestBody?,live_address_area: RequestBody?,
        live_address_street: RequestBody?,live_address_type: RequestBody?,
        marriage_status: RequestBody?,religion: RequestBody?,help_info: RequestBody?,
        house_id: RequestBody?,space_id: RequestBody?,parts: MultipartBody.Part?) {
        val disposable = RetrofitManager.service.getBasicInfoSubmitData(
            admin_id, token, name, gender, identity, birthday, phone,
            nationality, house_relation, register_address_province, register_address_city, register_address_area,
            register_address_street, live_address_province, live_address_city, live_address_area, live_address_street,
            live_address_type, marriage_status,religion,help_info,house_id,space_id, parts
        )
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<Boolean>("提交失败") {
                override fun onNext(t: Boolean) {
                    mRootView?.setBasicInfoSubmitData(t, "")
                }
                override fun onFail(e: ApiException) {
                    mRootView?.setBasicInfoSubmitData(false, e.mMessage)
                }
            })
        addSubscription(disposable)
    }
}