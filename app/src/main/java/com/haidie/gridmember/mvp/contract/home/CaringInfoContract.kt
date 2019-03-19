package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.PersonalInfoData

/**
 * Create by   Administrator
 *      on     2018/12/20 09:21
 * description
 */
class CaringInfoContract {
    interface View : IBaseView{
        fun setPersonalInfoData(personalInfoData: PersonalInfoData)
        fun showError(msg: String, errorCode: Int)
        fun setNationalityListData(nationalityListData: ArrayList<NationalityListData>)
        fun setWomanDiseaseCategoryListData(nationalityListData: ArrayList<NationalityListData>)
        fun setEditCaringPersonalInfoData(isSuccess : Boolean,msg: String)
    }
    interface Presenter : IPresenter<View>{
        fun getPersonalInfoData( admin_id: Int, token: String, id: Int,type: Int)
        fun getNationalityListData(admin_id: Int, token: String, key: String)
        fun getWomanDiseaseCategoryListData(admin_id: Int,token: String,key: String,value: String)
        fun getEditCaringPersonalInfoData(
             admin_id: Int, token: String,id: Int,

             is_children: Int?,school: String?,
             grade: String?,clazz: String?,
             student_special_skills: String?,student_hobby: String?,
             student_character: String?,student_score: Int?,
             student_good_subject: Int?,student_bad_subject: Int?,
             is_single_parent: Int?,is_allergy: Int?,family_information: String?,

            is_problem_youth_type: Int?,

             is_disease_patients_type: Int?,

             is_voman_disease_type: Int?,
             is_voman_disease_category: Int?,

             is_difficult_masses_type: Int?,
             is_difficult_masses_reason: Int?,

             is_disabled_person_type: Int?,
             is_disabled_person_rank: Int?,

             is_mental_retardation_rank: Int?,

             old_teacher_old_party: Int?
        )
    }
}