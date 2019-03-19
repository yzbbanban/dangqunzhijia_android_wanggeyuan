package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.PersonalInfoData
import com.haidie.gridmember.mvp.bean.ProvinceListData
import com.haidie.gridmember.mvp.contract.home.PartyMemberInfoContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/18 10:59
 * description
 */
class PartyMemberInfoPresenter : BasePresenter<PartyMemberInfoContract.View>(),PartyMemberInfoContract.Presenter{

    override fun getPersonalInfoData(admin_id: Int, token: String, id: Int, type: Int) {
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

    override fun getEditPersonalInfoData(
        admin_id: Int,token: String,id: Int,
        politics_status: Int?, entry_party_time: String?,
        org_relation_address_province: Int?,org_relation_address_city: Int?,
        org_relation_address_area: Int?,org_relation_address: String?, commend_status: String?,

        education: Int?, company: String?, job: String?, special_skills: Int?) {
        val disposable = RetrofitManager.service.getEditPersonalInfoData(
            admin_id,token,id,
            politics_status,entry_party_time,
            org_relation_address_province,org_relation_address_city,
            org_relation_address_area,org_relation_address,commend_status,

            education, company, job, special_skills)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<Boolean>("提交失败") {
                override fun onNext(t: Boolean) {
                    mRootView?.setEditPersonalInfoData(t, "")
                }
                override fun onFail(e: ApiException) {
                    mRootView?.setEditPersonalInfoData(false, e.mMessage)
                }
            })
        addSubscription(disposable)
    }
}