package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.PersonalInfoData
import com.haidie.gridmember.mvp.bean.ProvinceListData
import com.haidie.gridmember.mvp.contract.home.EditBasicInfoContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/12/19 17:19
 * description
 */
class EditBasicInfoPresenter : BasePresenter<EditBasicInfoContract.View>(),EditBasicInfoContract.Presenter{

    override fun getPersonalInfoData(admin_id: Int, token: String, id: Int, type: Int?) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getPersonalInfoData(admin_id, token, id, type)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<PersonalInfoData>("获取数据失败") {
                override fun onNext(t: PersonalInfoData) {
                    mRootView?.apply {
                        dismissLoading()
                        setPersonalInfoData(t)
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
    override fun getEditBasicInfoSubmitData(
        admin_id: RequestBody, token: RequestBody,id: RequestBody,
        name: RequestBody?,gender: RequestBody?,identity: RequestBody?,birthday: RequestBody?,
        phone: RequestBody?,nationality: RequestBody?, house_relation: RequestBody?,live_address_type: RequestBody?,
        register_address_province: RequestBody?, register_address_city: RequestBody?,
        register_address_area: RequestBody?,live_address_province: RequestBody?,
        live_address_city: RequestBody?,live_address_area: RequestBody?,
        house_id: RequestBody?,space_id: RequestBody?,marriage_status: RequestBody?,
        religion: RequestBody?,help_info: RequestBody?,parts: MultipartBody.Part?) {
        val disposable = RetrofitManager.service.getEditBasicInfoSubmitData(
            admin_id,token,id,
            name, gender,identity,birthday,phone,nationality,
            house_relation,live_address_type,register_address_province,
            register_address_city,register_address_area,live_address_province,live_address_city,
            live_address_area,house_id,space_id,marriage_status,religion, help_info,parts)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<Boolean>("提交失败") {
                override fun onNext(t: Boolean) {
                    mRootView?.setEditBasicInfoSubmitData(t, "")
                }

                override fun onFail(e: ApiException) {
                    mRootView?.setEditBasicInfoSubmitData(false, e.mMessage)
                }
            })
        addSubscription(disposable)
    }
}