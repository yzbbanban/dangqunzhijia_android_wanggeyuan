package com.haidie.gridmember.ui.home.activity

import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.PersonalInfoData
import com.haidie.gridmember.mvp.contract.home.EducationalWorkInfoContract
import com.haidie.gridmember.mvp.presenter.home.EducationalWorkInfoPresenter
import com.haidie.gridmember.utils.Preference
import kotlinx.android.synthetic.main.activity_educational_work_info.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim

/**
 * Create by   Administrator
 *      on     2018/12/17 19:35
 * description  教育工作信息
 */
class EducationalWorkInfoActivity : BaseActivity(),EducationalWorkInfoContract.View {

    private var keyType = ""
    private var mId: Int? = null
    private var type: Int? = null
    private var education: Int? = null
    private var specialSkill: Int? = null
    private var mPvOptions: OptionsPickerView<String>? = null
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { EducationalWorkInfoPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_educational_work_info
    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,-1)
        type = intent.getIntExtra(Constants.TYPE,-1)
    }

    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = "教育工作信息"
        llEducationalLevel.setOnClickListener {
            //通过接口获取√hightest_education
            keyType = Constants.HEIGHT_EDUCATION
            mPresenter.getNationalityListData(uid, token,keyType)
        }
        llSpecialty.setOnClickListener {
            keyType = Constants.SPECIALTY
            mPresenter.getNationalityListData(uid, token,keyType)
        }

        tvSubmitBasic.setOnClickListener {
            CircularAnim.hide(tvSubmitBasic)
                .endRadius(progressBar.height / 2f)
                .go { progressBar.visibility = View.VISIBLE }
            mPresenter.getEditPersonalInfoData(uid,token,mId!!,
                null,null,null,
                null,null,null,null,
                education,etCompany.text.toString(),etJob.text.toString(),specialSkill)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getPersonalInfoData(uid,token,mId!!,type!!)
    }
    override fun setPersonalInfoData(personalInfoData: PersonalInfoData) {
        tvEducationalLevel.text = personalInfoData.education
        etCompany.text = getEditable(personalInfoData.company)
        val job = personalInfoData.job
        if (job != null) {
            etJob.text = getEditable(job)
        }
        tvSpecialty.text = personalInfoData.special_skills
    }
    override fun setEditPersonalInfoData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            showShort("提交成功")
            finish()
        }else{
            showShort(if (msg.isEmpty()) "提交失败" else msg)
            showSubmit()
        }
    }
    private fun showSubmit() {
        CircularAnim.show(tvSubmitBasic).go()
        progressBar.visibility = View.GONE
    }
    override fun setNationalityListData(nationalityListData: ArrayList<NationalityListData>) {
        val data = ArrayList<String>()
        nationalityListData.forEach {
            data.add(it.keyname)
        }
        mPvOptions = OptionsPickerBuilder(this@EducationalWorkInfoActivity, OnOptionsSelectListener { options1, _, _, _ ->
            when (keyType) {
                Constants.HEIGHT_EDUCATION -> {
                    tvEducationalLevel.text = data[options1]
                    education = nationalityListData[options1].value
                }
                Constants.SPECIALTY -> {
                    tvSpecialty.text = data[options1]
                    specialSkill = nationalityListData[options1].value
                }
            }
        }).build()
        mPvOptions!!.setPicker(data)
        if (mPvOptions != null && !mPvOptions!!.isShowing ) {
            mPvOptions?.show()
        }
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}