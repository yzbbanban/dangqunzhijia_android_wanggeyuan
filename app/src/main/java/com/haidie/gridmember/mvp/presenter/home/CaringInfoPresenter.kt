package com.haidie.gridmember.mvp.presenter.home

import com.haidie.gridmember.base.BaseObserver
import com.haidie.gridmember.base.BasePresenter
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.PersonalInfoData
import com.haidie.gridmember.mvp.contract.home.CaringInfoContract
import com.haidie.gridmember.net.RetrofitManager
import com.haidie.gridmember.net.exception.ApiException
import com.haidie.gridmember.rx.RxUtils
import com.haidie.gridmember.rx.SchedulerUtils

/**
 * Create by   Administrator
 *      on     2018/12/20 09:21
 * description
 */
class CaringInfoPresenter : BasePresenter<CaringInfoContract.View>(),CaringInfoContract.Presenter{

    override fun getPersonalInfoData(admin_id: Int, token: String, id: Int, type: Int) {
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.getPersonalInfoData(admin_id, token, id,type)
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

    override fun getWomanDiseaseCategoryListData(admin_id: Int, token: String, key: String, value: String) {
        val disposable = RetrofitManager.service.getWomanDiseaseCategoryListData(admin_id, token, key, value)
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<ArrayList<NationalityListData>>("获取数据失败") {
                override fun onNext(t: ArrayList<NationalityListData>) {
                    mRootView?.setWomanDiseaseCategoryListData(t)
                }
                override fun onFail(e: ApiException) {
                    mRootView?.showError(e.mMessage, e.errorCode)
                }
            })
        addSubscription(disposable)
    }
    override fun getEditCaringPersonalInfoData(
        admin_id: Int,token: String,id: Int,
        is_children: Int?,school: String?,grade: String?,clazz: String?,student_special_skills: String?,
        student_hobby: String?,student_character: String?,student_score: Int?,
        student_good_subject: Int?,student_bad_subject: Int?,
        is_single_parent: Int?,is_allergy: Int?,
        family_information: String?,
        is_problem_youth_type: Int?,
        is_disease_patients_type: Int?,
        is_voman_disease_type: Int?,is_voman_disease_category: Int?,
        is_difficult_masses_type: Int?,is_difficult_masses_reason: Int?,
        is_disabled_person_type: Int?,is_disabled_person_rank: Int?,
        is_mental_retardation_rank: Int?,

        old_teacher_old_party: Int?
    ) {
        val disposable = RetrofitManager.service.getEditCaringPersonalInfoData(
            admin_id, token, id, is_children, school, grade, clazz,
            student_special_skills, student_hobby, student_character, student_score, student_good_subject,
            student_bad_subject, is_single_parent, is_allergy, family_information, is_problem_youth_type,
            is_disease_patients_type, is_voman_disease_type, is_voman_disease_category, is_difficult_masses_type,
            is_difficult_masses_reason, is_disabled_person_type, is_disabled_person_rank, is_mental_retardation_rank,

            old_teacher_old_party
        )
            .compose(SchedulerUtils.ioToMain())
            .compose(RxUtils.handleResult())
            .subscribeWith(object : BaseObserver<Boolean>("提交失败") {
                override fun onNext(t: Boolean) {
                    mRootView?.setEditCaringPersonalInfoData(t, "")
                }
                override fun onFail(e: ApiException) {
                    mRootView?.setEditCaringPersonalInfoData(false, e.mMessage)
                }
            })
        addSubscription(disposable)
    }
}