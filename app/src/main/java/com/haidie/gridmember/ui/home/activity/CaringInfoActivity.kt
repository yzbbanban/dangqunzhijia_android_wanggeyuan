package com.haidie.gridmember.ui.home.activity

import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.PersonalInfoData
import com.haidie.gridmember.mvp.contract.home.CaringInfoContract
import com.haidie.gridmember.mvp.presenter.home.CaringInfoPresenter
import com.haidie.gridmember.utils.LogHelper
import com.haidie.gridmember.utils.Preference
import kotlinx.android.synthetic.main.activity_caring_info.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim

/**
 * Create by   Administrator
 *      on     2018/12/20 09:11
 * description  关爱信息
 */
class CaringInfoActivity : BaseActivity(), CaringInfoContract.View {

    private var keyType = ""
    private var mId: Int? = null
    private var type: Int? = null
    private var typeStudentSubject: Int = -1
    private var etSchool: EditText? = null
    private var etGrade: EditText? = null
    private var etClass: EditText? = null
    private var etStudentSpecialSkills: EditText? = null
    private var etStudentHobby: EditText? = null
    private var etStudentCharacter: EditText? = null
    private var llStudentScore: LinearLayout? = null
    private var tvStudentScore: TextView? = null
    private var llStudentGoodSubject: LinearLayout? = null
    private var tvStudentGoodSubject: TextView? = null
    private var llStudentBadSubject: LinearLayout? = null
    private var tvStudentBadSubject: TextView? = null
    private var rgIsSingleParent: RadioGroup? = null
    private var rgIsAllergy: RadioGroup? = null
    private var llProblemYouthType: LinearLayout? = null
    private var tvProblemYouthType: TextView? = null
    private var llHugeSickStatus: LinearLayout? = null
    private var tvHugeSickStatus: TextView? = null
    private var llSpecificType: LinearLayout? = null
    private var tvSpecificType: TextView? = null
    private var llWomanDiseaseType: LinearLayout? = null
    private var tvWomanDiseaseType: TextView? = null
    private var llWomanDiseaseCategory: LinearLayout? = null
    private var tvWomanDiseaseCategory: TextView? = null
    private var llDifficultMassesType: LinearLayout? = null
    private var tvDifficultMassesType: TextView? = null
    private var llDifficultMassesReason: LinearLayout? = null
    private var tvDifficultMassesReason: TextView? = null
    private var llDisabledPersonType: LinearLayout? = null
    private var tvDisabledPersonType: TextView? = null
    private var llDisabledPersonRank: LinearLayout? = null
    private var tvDisabledPersonRank: TextView? = null
    private var llMentalRetardationRank: LinearLayout? = null
    private var tvMentalRetardationRank: TextView? = null
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { CaringInfoPresenter() }
    private var mPvOptionsCaringType: OptionsPickerView<String>? = null
    private var mPvOptions: OptionsPickerView<String>? = null
    private var isChildren: Int? = null
    private var studentScore: Int? = null
    private var studentGoodSubject: Int? = null
    private var studentBadSubject: Int? = null
    private var isSingleParent: Int? = null
    private var isAllergy: Int? = null
    private var problemYouthType: Int? = null
    private var diseasePatientsType: Int? = null
    private var oldTeacherOldPartyType: Int? = null
    private var womanDiseaseType: Int? = null
    private var womanDiseaseCategory: Int? = null
    private var difficultMassesType: Int? = null
    private var difficultMassesReason: Int? = null
    private var disabledPersonType: Int? = null
    private var disabledPersonRank: Int? = null
    private var mentalRetardationRank: Int? = null
    //关爱类型
    private val texts = arrayOf(
        "留守儿童", "问题青少年", "空巢老人", "疾病患者", "妇女", "特困群众",
        "失业人员", "残疾人员", "智力障碍人员", "老教师老干部", "军烈属"
    )

    override fun getLayoutId(): Int = R.layout.activity_caring_info
    override fun initData() {
        mId = intent.getIntExtra(Constants.ID, -1)
        type = intent.getIntExtra(Constants.TYPE, -1)
        LogHelper.d("====initData=>  ")

    }

    override fun initView() {
        LogHelper.d("====initView=>  ")

        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener { onBackPressed() }
        tvTitle.text = "关爱信息"

        initCaringTypePicker()
        llCaringType.setOnClickListener {
            if (mPvOptionsCaringType != null && !mPvOptionsCaringType!!.isShowing) {
                mPvOptionsCaringType?.show()
            }
        }
        tvSubmitBasic.setOnClickListener {
            CircularAnim.hide(tvSubmitBasic)
                .endRadius(progressBar.height / 2f)
                .go { progressBar.visibility = View.VISIBLE }
            mPresenter.getEditCaringPersonalInfoData(
                uid, token, mId!!, isChildren,
                if (etSchool == null || etSchool?.text.toString().isEmpty()) null else etSchool?.text.toString(),
                if (etGrade == null || etGrade?.text.toString().isEmpty()) null else etGrade?.text.toString(),
                if (etClass == null || etClass?.text.toString().isEmpty()) null else etClass?.text.toString(),
                if (etStudentSpecialSkills == null || etStudentSpecialSkills?.text.toString().isEmpty()) null else etStudentSpecialSkills?.text.toString(),
                if (etStudentHobby == null || etStudentHobby?.text.toString().isEmpty()) null else etStudentHobby?.text.toString(),
                if (etStudentCharacter == null || etStudentCharacter?.text.toString().isEmpty()) null else etStudentCharacter?.text.toString(),
                studentScore, studentGoodSubject, studentBadSubject,
                isSingleParent, isAllergy, etFamilyInformation.text.toString(),
                problemYouthType, diseasePatientsType,
                womanDiseaseType, womanDiseaseCategory,
                difficultMassesType, difficultMassesReason,
                disabledPersonType, disabledPersonRank,
                mentalRetardationRank, oldTeacherOldPartyType
            )
        }
    }

    private fun initCaringTypePicker() {
        LogHelper.d("====initCaringTypePicker=>  ")
        val data = ArrayList<String>()
        texts.forEach {
            data.add(it)
        }
        mPvOptionsCaringType =
            OptionsPickerBuilder(this@CaringInfoActivity, OnOptionsSelectListener { options1, _, _, _ ->
                tvCaringType.text = data[options1]
                llCaringTypeContent.removeAllViews()
                isChildren = options1 + 1
                clear()
                when (options1) {
                    0 -> {
                        val view = View.inflate(this, R.layout.caring_type_children_view, null)
                        studentStatus(view)
                        llCaringTypeContent.addView(view)
                    }
                    1 -> {
                        val view = View.inflate(this, R.layout.caring_type_problem_youth_view, null)
                        studentStatus(view)
                        problemYouthType(view)
                        llCaringTypeContent.addView(view)
                    }
                    3 -> {
                        val view = View.inflate(this, R.layout.caring_type_disease_patients_view, null)
                        diseasePatientsType(view)
                        llCaringTypeContent.addView(view)
                    }
                    4 -> {
                        val view = View.inflate(this, R.layout.caring_type_woman_view, null)
                        womanDiseaseType(view)
                        llCaringTypeContent.addView(view)
                    }
                    5 -> {
                        val view = View.inflate(this, R.layout.caring_type_difficult_masses_view, null)
                        difficultMassesType(view)
                        llCaringTypeContent.addView(view)
                    }
                    7 -> {
                        val view = View.inflate(this, R.layout.caring_type_disabled_person_view, null)
                        disabledPersonType(view)
                        llCaringTypeContent.addView(view)
                    }
                    8 -> {
                        val view = View.inflate(this, R.layout.caring_type_mental_retardation_person_view, null)
                        mentalRetardationPersonType(view)
                        llCaringTypeContent.addView(view)
                    }
                    9 -> {
                        val view = View.inflate(this, R.layout.caring_type_old_teacher_old_party_view, null)
                        oldTeacherOldPartyType(view)
                        llCaringTypeContent.addView(view)
                    }
                }
            }).build()
        mPvOptionsCaringType!!.setPicker(data)
    }

    private fun clear() {
        typeStudentSubject = -1
        womanDiseaseType = null
        etSchool = null
        etGrade = null
        etClass = null
        etStudentSpecialSkills = null
        etStudentHobby = null
        etStudentCharacter = null
        studentScore = null
        studentGoodSubject = null
        studentBadSubject = null
        isSingleParent = null
        isAllergy = null
        problemYouthType = null
        diseasePatientsType = null
        oldTeacherOldPartyType = null
        womanDiseaseType = null
        womanDiseaseCategory = null
        difficultMassesType = null
        difficultMassesReason = null
        disabledPersonType = null
        disabledPersonRank = null
        mentalRetardationRank = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mPresenter.detachView()
    }

    override fun start() {
        mPresenter.getPersonalInfoData(uid, token, mId!!, type!!)
    }

    //    是否是留守儿童,默认0否,1是,2问题青少年,3空巢老人,4疾病患者,5.关爱妇女
    //    6.特困群众,7失业人员,8.残疾人员,9智力障碍人员,10.党员干部,11.军烈属
    override fun setPersonalInfoData(personalInfoData: PersonalInfoData) {
        val isChildren = personalInfoData.is_children
        etFamilyInformation?.text = getEditable(personalInfoData.family_information)
        when (isChildren) {
//            留守儿童
            1 -> {
                tvCaringType.text = texts[0]
                val view = View.inflate(this, R.layout.caring_type_children_view, null)
                studentStatus(view)
                setStudentInfo(personalInfoData)
                llCaringTypeContent.addView(view)
            }
//            问题青少年
            2 -> {
                tvCaringType.text = texts[1]
                val view = View.inflate(this, R.layout.caring_type_problem_youth_view, null)
                studentStatus(view)
                setStudentInfo(personalInfoData)
                problemYouthType(view)
                tvProblemYouthType?.text = personalInfoData.is_problem_youth_type
                llCaringTypeContent.addView(view)
            }
//            空巢老人
            3 -> tvCaringType.text = texts[2]
//            疾病患者
            4 -> {
                tvCaringType.text = texts[3]
                val view = View.inflate(this, R.layout.caring_type_disease_patients_view, null)
                diseasePatientsType(view)
                tvHugeSickStatus?.text = personalInfoData.is_disease_patients_type
                llCaringTypeContent.addView(view)
            }
            //妇女
            5 -> {
                tvCaringType.text = texts[4]
                val view = View.inflate(this, R.layout.caring_type_woman_view, null)
                womanDiseaseType(view)
                tvWomanDiseaseType?.text = personalInfoData.is_voman_name
                tvWomanDiseaseCategory?.text = personalInfoData.is_voman_category
                llCaringTypeContent.addView(view)
            }
            //特困群众
            6 -> {
                tvCaringType.text = texts[5]
                val view = View.inflate(this, R.layout.caring_type_difficult_masses_view, null)
                difficultMassesType(view)
                tvDifficultMassesType?.text = personalInfoData.is_difficult_masses_type
                tvDifficultMassesReason?.text = personalInfoData.is_difficult_masses_reason
                llCaringTypeContent.addView(view)
            }
            //失业人员
            7 -> tvCaringType.text = texts[6]
            //残疾人员
            8 -> {
                tvCaringType.text = texts[7]
                val view = View.inflate(this, R.layout.caring_type_disabled_person_view, null)
                disabledPersonType(view)
                tvDisabledPersonType?.text = personalInfoData.is_disabled_person_type
                tvDisabledPersonRank?.text = personalInfoData.is_disabled_person_rank
                llCaringTypeContent.addView(view)
            }
            //智力障碍人员
            9 -> {
                tvCaringType.text = texts[8]
                val view = View.inflate(this, R.layout.caring_type_mental_retardation_person_view, null)
                mentalRetardationPersonType(view)
                tvMentalRetardationRank?.text = personalInfoData.is_mental_retardation_rank
                llCaringTypeContent.addView(view)
            }
            //老党员老干部
            10 -> {
                tvCaringType.text = texts[9]
                val view = View.inflate(this, R.layout.caring_type_old_teacher_old_party_view, null)
                oldTeacherOldPartyType(view)
                llCaringTypeContent.addView(view)
            }
            //军烈属
            11 -> tvCaringType.text = texts[10]
        }
    }

    private fun setStudentInfo(personalInfoData: PersonalInfoData) {
        etSchool?.text = getEditable(personalInfoData.school)
        etGrade?.text = getEditable(personalInfoData.grade)
        etClass?.text = getEditable(personalInfoData.clazz)
        etStudentSpecialSkills?.text = getEditable(personalInfoData.student_special_skills)
        etStudentHobby?.text = getEditable(personalInfoData.student_hobby)
        etStudentCharacter?.text = getEditable(personalInfoData.student_character)
        tvStudentScore?.text = personalInfoData.student_score
        tvStudentGoodSubject?.text = personalInfoData.student_good_subject
        tvStudentBadSubject?.text = personalInfoData.student_bad_subject
        val singleParent = personalInfoData.is_single_parent
        if ("否" == singleParent) {
            rgIsSingleParent?.check(R.id.rbIsNotSingleParent)
        } else {
            rgIsSingleParent?.check(R.id.rbIsSingleParent)
        }
        val allergy = personalInfoData.is_allergy
        if ("否" == allergy) {
            rgIsAllergy?.check(R.id.rbIsNotAllergy)
        } else {
            rgIsAllergy?.check(R.id.rbIsAllergy)
        }
    }

    private fun mentalRetardationPersonType(view: View) {
        llMentalRetardationRank = view.findViewById(R.id.llMentalRetardationRank)
        tvMentalRetardationRank = view.findViewById(R.id.tvMentalRetardationRank)
        llMentalRetardationRank?.setOnClickListener {
            keyType = Constants.MIND
            mPresenter.getNationalityListData(uid, token, keyType)
        }
    }

    private fun disabledPersonType(view: View) {
        llDisabledPersonType = view.findViewById(R.id.llDisabledPersonType)
        tvDisabledPersonType = view.findViewById(R.id.tvDisabledPersonType)
        llDisabledPersonRank = view.findViewById(R.id.llDisabledPersonRank)
        tvDisabledPersonRank = view.findViewById(R.id.tvDisabledPersonRank)
        llDisabledPersonType?.setOnClickListener {
            keyType = Constants.DISABILITY_STATUS
            mPresenter.getNationalityListData(uid, token, keyType)
        }
        llDisabledPersonRank?.setOnClickListener {
            keyType = Constants.DISABLE_LEVEL
            mPresenter.getNationalityListData(uid, token, keyType)
        }
    }

    private fun difficultMassesType(view: View) {
        llDifficultMassesType = view.findViewById(R.id.llDifficultMassesType)
        tvDifficultMassesType = view.findViewById(R.id.tvDifficultMassesType)
        llDifficultMassesReason = view.findViewById(R.id.llDifficultMassesReason)
        tvDifficultMassesReason = view.findViewById(R.id.tvDifficultMassesReason)
        llDifficultMassesType?.setOnClickListener {
            keyType = Constants.IS_POOR
            mPresenter.getNationalityListData(uid, token, keyType)
        }
        llDifficultMassesReason?.setOnClickListener {
            keyType = Constants.POOR_REASON
            mPresenter.getNationalityListData(uid, token, keyType)
        }
    }

    private fun womanDiseaseType(view: View) {
        llWomanDiseaseType = view.findViewById(R.id.llWomanDiseaseType)
        tvWomanDiseaseType = view.findViewById(R.id.tvWomanDiseaseType)
        llWomanDiseaseCategory = view.findViewById(R.id.llWomanDiseaseCategory)
        tvWomanDiseaseCategory = view.findViewById(R.id.tvWomanDiseaseCategory)
        llWomanDiseaseType?.setOnClickListener {
            keyType = Constants.HEALTH
            mPresenter.getNationalityListData(uid, token, keyType)
        }
        llWomanDiseaseCategory?.setOnClickListener {
            if (womanDiseaseType == null) {
                showShort("请先选择妇科疾病类型")
                return@setOnClickListener
            }
            keyType = Constants.HEALTH
            mPresenter.getWomanDiseaseCategoryListData(uid, token, keyType, womanDiseaseType.toString())
        }
    }

    private fun diseasePatientsType(view: View) {
        llHugeSickStatus = view.findViewById(R.id.llHugeSickStatus)
        tvHugeSickStatus = view.findViewById(R.id.tvHugeSickStatus)
        llHugeSickStatus?.setOnClickListener {
            keyType = Constants.HUGE_SICK_STATUS
            mPresenter.getNationalityListData(uid, token, keyType)
        }
    }

    private fun oldTeacherOldPartyType(view: View) {
        llSpecificType = view.findViewById(R.id.llSpecificType)
        tvSpecificType = view.findViewById(R.id.tvSpecificType)
        llSpecificType?.setOnClickListener {
            keyType = Constants.OLD_TEACHER_OLD_PARTY
            mPresenter.getNationalityListData(uid, token, keyType)
        }
    }

    private fun problemYouthType(view: View) {
        llProblemYouthType = view.findViewById(R.id.llProblemYouthType)
        tvProblemYouthType = view.findViewById(R.id.tvProblemYouthType)
        llProblemYouthType?.setOnClickListener {
            keyType = Constants.PROBLEM_YOUTH_TYPE
            mPresenter.getNationalityListData(uid, token, keyType)
        }
    }

    private fun studentStatus(view: View) {
        etSchool = view.findViewById(R.id.etSchool)
        etGrade = view.findViewById(R.id.etGrade)
        etClass = view.findViewById(R.id.etClass)
        etStudentSpecialSkills = view.findViewById(R.id.etStudentSpecialSkills)
        etStudentHobby = view.findViewById(R.id.etStudentHobby)
        etStudentCharacter = view.findViewById(R.id.etStudentCharacter)
        llStudentScore = view.findViewById(R.id.llStudentScore)
        tvStudentScore = view.findViewById(R.id.tvStudentScore)
        llStudentGoodSubject = view.findViewById(R.id.llStudentGoodSubject)
        tvStudentGoodSubject = view.findViewById(R.id.tvStudentGoodSubject)
        llStudentBadSubject = view.findViewById(R.id.llStudentBadSubject)
        tvStudentBadSubject = view.findViewById(R.id.tvStudentBadSubject)
        rgIsSingleParent = view.findViewById(R.id.rgIsSingleParent)
        rgIsAllergy = view.findViewById(R.id.rgIsAllergy)

        llStudentScore?.setOnClickListener {
            keyType = Constants.STUDENT_SCORE
            mPresenter.getNationalityListData(uid, token, keyType)
        }
        llStudentGoodSubject?.setOnClickListener {
            typeStudentSubject = 1
            keyType = Constants.STUDENT_SUBJECT
            mPresenter.getNationalityListData(uid, token, keyType)
        }
        llStudentBadSubject?.setOnClickListener {
            typeStudentSubject = 2
            keyType = Constants.STUDENT_SUBJECT
            mPresenter.getNationalityListData(uid, token, keyType)
        }
        rgIsSingleParent?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbIsSingleParent -> {
                    isSingleParent = 1
                }
                R.id.rbIsNotSingleParent -> {
                    isSingleParent = 0
                }
            }
        }
        rgIsAllergy?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbIsAllergy -> {
                    isAllergy = 1
                }
                R.id.rbIsNotAllergy -> {
                    isAllergy = 0
                }
            }
        }
    }

    override fun setNationalityListData(nationalityListData: ArrayList<NationalityListData>) {
        val data = ArrayList<String>()
        nationalityListData.forEach {
            data.add(it.keyname)
        }
        mPvOptions = OptionsPickerBuilder(this@CaringInfoActivity, OnOptionsSelectListener { options1, _, _, _ ->
            when (keyType) {
                Constants.STUDENT_SCORE -> {
                    tvStudentScore?.text = data[options1]
                    studentScore = nationalityListData[options1].value
                }
                Constants.STUDENT_SUBJECT -> {
                    if (typeStudentSubject == 1) {
                        tvStudentGoodSubject?.text = data[options1]
                        studentGoodSubject = nationalityListData[options1].value
                    } else if (typeStudentSubject == 2) {
                        tvStudentBadSubject?.text = data[options1]
                        studentBadSubject = nationalityListData[options1].value
                    }
                }
                Constants.PROBLEM_YOUTH_TYPE -> {
                    tvProblemYouthType?.text = data[options1]
                    problemYouthType = nationalityListData[options1].value
                }
                Constants.HUGE_SICK_STATUS -> {
                    tvHugeSickStatus?.text = data[options1]
                    diseasePatientsType = nationalityListData[options1].value
                }
                Constants.HEALTH -> {
                    tvWomanDiseaseType?.text = data[options1]
                    womanDiseaseType = nationalityListData[options1].value
                }
                Constants.IS_POOR -> {
                    tvDifficultMassesType?.text = data[options1]
                    difficultMassesType = nationalityListData[options1].value
                }
                Constants.POOR_REASON -> {
                    tvDifficultMassesReason?.text = data[options1]
                    difficultMassesReason = nationalityListData[options1].value
                }
                Constants.DISABILITY_STATUS -> {
                    tvDisabledPersonType?.text = data[options1]
                    disabledPersonType = nationalityListData[options1].value
                }
                Constants.DISABLE_LEVEL -> {
                    tvDisabledPersonRank?.text = data[options1]
                    disabledPersonRank = nationalityListData[options1].value
                }
                Constants.MIND -> {
                    tvMentalRetardationRank?.text = data[options1]
                    mentalRetardationRank = nationalityListData[options1].value
                }
                Constants.OLD_TEACHER_OLD_PARTY -> {
                    tvSpecificType?.text = data[options1]
                    oldTeacherOldPartyType = nationalityListData[options1].value
                }
            }
        }).build()
        mPvOptions!!.setPicker(data)
        if (mPvOptions != null && !mPvOptions!!.isShowing) {
            mPvOptions?.show()
        }
    }

    override fun setWomanDiseaseCategoryListData(nationalityListData: ArrayList<NationalityListData>) {
        val data = ArrayList<String>()
        nationalityListData.forEach {
            data.add(it.keyname)
        }
        mPvOptions = OptionsPickerBuilder(this@CaringInfoActivity, OnOptionsSelectListener { options1, _, _, _ ->
            tvWomanDiseaseCategory?.text = data[options1]
            womanDiseaseCategory = nationalityListData[options1].value
        }).build()
        mPvOptions!!.setPicker(data)
        if (mPvOptions != null && !mPvOptions!!.isShowing) {
            mPvOptions?.show()
        }
    }

    override fun setEditCaringPersonalInfoData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            showShort("提交成功")
            finish()
        } else {
            showShort(if (msg.isEmpty()) "提交失败" else msg)
            showSubmit()
        }
    }

    private fun showSubmit() {
        CircularAnim.show(tvSubmitBasic).go()
        progressBar.visibility = View.GONE
    }

    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
    }

    override fun showLoading() {}
    override fun dismissLoading() {}
}