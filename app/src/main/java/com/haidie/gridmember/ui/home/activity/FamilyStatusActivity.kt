package com.haidie.gridmember.ui.home.activity

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bigkoo.pickerview.view.TimePickerView
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.PersonalInfoData
import com.haidie.gridmember.mvp.contract.home.FamilyStatusContract
import com.haidie.gridmember.mvp.presenter.home.FamilyStatusPresenter
import com.haidie.gridmember.utils.DateUtils
import com.haidie.gridmember.utils.Preference
import kotlinx.android.synthetic.main.activity_family_status.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim
import java.util.*

/**
 * Create by   Administrator
 *      on     2018/12/18 11:56
 * description  家庭状况
 */
class FamilyStatusActivity : BaseActivity(),FamilyStatusContract.View {

    private var keyType = ""
    private var type = -1
    private var mId: Int? = null
    private var typ: Int? = null
    private var mPvOptions: OptionsPickerView<String>? = null
    private var pvTime: TimePickerView? = null
    private var mPvOptionsIsEnter: OptionsPickerView<String>? = null
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { FamilyStatusPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_family_status

    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,-1)
        typ = intent.getIntExtra(Constants.TYPE,-1)
    }

    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = "家庭状况"
        llIncomeSource.setOnClickListener {
            //通过接口获取√income_source
            keyType = Constants.INCOME_SOURCE
            mPresenter.getNationalityListData(uid, token,keyType)
        }
        initTimePicker()
        llFamilyAreaTime.setOnClickListener {
            if (pvTime != null) {
                pvTime!!.show()
            }
        }
        initIsEnterPicker()
        llIsEnterBasicOldInsurance.setOnClickListener {
            type = 0
            if (mPvOptionsIsEnter != null && !mPvOptionsIsEnter!!.isShowing) {
                mPvOptionsIsEnter?.show()
            }
        }
        llIsEnterBasicMedicalInsurance.setOnClickListener {
            type = 1
            if (mPvOptionsIsEnter != null && !mPvOptionsIsEnter!!.isShowing) {
                mPvOptionsIsEnter?.show()
            }
        }
        llIsEnterNewInsurance.setOnClickListener {
            type = 2
            if (mPvOptionsIsEnter != null && !mPvOptionsIsEnter!!.isShowing) {
                mPvOptionsIsEnter?.show()
            }
        }

        tvSubmitBasic.setOnClickListener {
            CircularAnim.hide(tvSubmitBasic)
                .endRadius(progressBar.height / 2f)
                .go { progressBar.visibility = View.VISIBLE }
            mPresenter.getEditFamilyPersonalInfoData(uid,token,mId!!,
                familyIncomeSource,etFamilyIncome.text.toString(),
                etFamilyWageIncome.text.toString(),etFamilyOperationalIncome.text.toString(),
                etFamilyLowIncome.text.toString(),etFamilyPensionIncome.text.toString(),
                etFamilyAreaNum.text.toString(),
                etFamilyPersonNum.text.toString(),tvFamilyAreaTime.text.toString(),
                etFamilyAreaFlowNum.text.toString(),
                isEnterBasicOldInsurance,isEnterBasicMedicalInsurance,isEnterNewInsurance)
        }
    }
    private var familyIncomeSource: Int? = null
    private var isEnterBasicOldInsurance: Int? = null
    private var isEnterBasicMedicalInsurance: Int? = null
    private var isEnterNewInsurance: Int? = null
    private fun initTimePicker() {
        val endDate = Calendar.getInstance()
        val nowTime = DateUtils.getNowTime()
        val strings = nowTime.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = strings[0].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        endDate.set(Integer.parseInt(calendar[0]), Integer.parseInt(calendar[1]) - 1,
            Integer.parseInt(calendar[2]))
        val start = Calendar.getInstance()
        start.set(1950, 0, 1)
        pvTime = TimePickerBuilder(this) { date, _ ->
            tvFamilyAreaTime.text = DateUtils.dateToStr(date)
        }.isDialog(true)
            .setDate(start)
            .setRangDate(start, endDate)
            .build()
        val mDialog = pvTime!!.dialog
        if (mDialog != null) {
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM)
            params.leftMargin = 0
            params.rightMargin = 0
            pvTime!!.dialogContainerLayout.layoutParams = params
            val dialogWindow = mDialog.window
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim)//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM)//改成Bottom,底部显示
            }
        }
    }
    private fun initIsEnterPicker() {
        val data = ArrayList<String>()
        data.add("否")
        data.add("是")
        mPvOptionsIsEnter = OptionsPickerBuilder(this@FamilyStatusActivity, OnOptionsSelectListener { options1, _, _, _ ->
            when (type) {
                0 -> {
                    tvIsEnterBasicOldInsurance.text = data[options1]
                    isEnterBasicOldInsurance = options1
                }
                1 -> {
                    tvIsEnterBasicMedicalInsurance.text = data[options1]
                    isEnterBasicMedicalInsurance = options1
                }
                2 -> {
                    tvIsEnterNewInsurance.text = data[options1]
                    isEnterNewInsurance = options1
                }
            }

        }).build()
        mPvOptionsIsEnter!!.setPicker(data)
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getPersonalInfoData(uid,token,mId!!,typ!!)
    }
    override fun setPersonalInfoData(personalInfoData: PersonalInfoData) {
        tvIncomeSource.text = personalInfoData.family_income_source
        etFamilyIncome.text = getEditable(personalInfoData.family_income.toString())
        etFamilyWageIncome.text = getEditable(personalInfoData.family_wage_income.toString())
        etFamilyOperationalIncome.text = getEditable(personalInfoData.family_operational_income.toString())
        etFamilyLowIncome.text = getEditable(personalInfoData.family_low_income.toString())
        etFamilyPensionIncome.text = getEditable(personalInfoData.family_pension_income.toString())
        etFamilyAreaNum.text = getEditable(personalInfoData.family_area_num.toString())
        etFamilyPersonNum.text = getEditable(personalInfoData.family_person_num.toString())
        tvFamilyAreaTime.text = getEditable(personalInfoData.family_area_time)
        etFamilyAreaFlowNum.text = getEditable(personalInfoData.family_area_flow_num.toString())
        tvIsEnterBasicOldInsurance.text = personalInfoData.is_enter_baseic_old_insurance
        tvIsEnterBasicMedicalInsurance.text = personalInfoData.is_enter_baseic_medical_insurance
        tvIsEnterNewInsurance.text = personalInfoData.is_enter_new_insurance
    }
    override fun setEditFamilyPersonalInfoData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            showShort("提交成功")
            finish()
        }else{
            showShort(if (msg.isEmpty()) "提交失败" else msg)
            showLoginSubmit()
        }
    }
    private fun showLoginSubmit() {
        CircularAnim.show(tvSubmitBasic).go()
        progressBar.visibility = View.GONE
    }
    override fun setNationalityListData(nationalityListData: ArrayList<NationalityListData>) {
        val data = ArrayList<String>()
        nationalityListData.forEach {
            data.add(it.keyname)
        }
        mPvOptions = OptionsPickerBuilder(this@FamilyStatusActivity, OnOptionsSelectListener { options1, _, _, _ ->
            when (keyType) {
                Constants.INCOME_SOURCE -> {
                    tvIncomeSource.text = data[options1]
                    familyIncomeSource = nationalityListData[options1].value
                }
            }
        }).build()
        mPvOptions!!.setPicker(data)
        if (mPvOptions != null && !mPvOptions!!.isShowing) {
            mPvOptions?.show()
        }
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}