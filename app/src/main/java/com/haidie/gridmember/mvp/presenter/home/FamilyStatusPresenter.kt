package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.PersonalInfoData
import com.haidie.gridmember.mvp.contract.home.FamilyStatusContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/18 13:21
 * description
 */
class FamilyStatusPresenter : BasePresenter<FamilyStatusContract.View>(), FamilyStatusContract.Presenter {

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
    override fun getEditFamilyPersonalInfoData(
        admin_id: Int,token: String,id: Int,
        family_income_source: Int?,
        family_income: String?,family_wage_income: String?,
        family_operational_income: String?,family_low_income: String?,
        family_pension_income: String?,family_area_num: String?,
        family_person_num: String?,family_area_time: String?,
        family_area_flow_num: String?,
        is_enter_baseic_old_insurance: Int?,is_enter_baseic_medical_insurance: Int?,
        is_enter_new_insurance: Int?
    ) {
        val disposable = RetrofitManager.service.getEditFamilyPersonalInfoData(
            admin_id, token, id, family_income_source,
            family_income, family_wage_income, family_operational_income, family_low_income, family_pension_income,
            family_area_num, family_person_num, family_area_time, family_area_flow_num, is_enter_baseic_old_insurance,
            is_enter_baseic_medical_insurance, is_enter_new_insurance
        )
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<Boolean>("提交失败") {
                override fun onNext(t: Boolean) {
                    mRootView?.setEditFamilyPersonalInfoData(t, "")
                }
                override fun onFail(e: ApiException) {
                    mRootView?.setEditFamilyPersonalInfoData(false, e.mMessage)
                }
            })
        addSubscription(disposable)
    }
}