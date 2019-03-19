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
import com.haidie.gridmember.mvp.bean.ProvinceListData
import com.haidie.gridmember.mvp.contract.home.PartyMemberInfoContract
import com.haidie.gridmember.mvp.presenter.home.PartyMemberInfoPresenter
import com.haidie.gridmember.utils.DateUtils
import com.haidie.gridmember.utils.Preference
import kotlinx.android.synthetic.main.activity_party_member_info.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim
import java.util.*
import kotlin.collections.ArrayList

/**
 * Create by   Administrator
 *      on     2018/12/18 10:31
 * description  党员信息
 */
class PartyMemberInfoActivity : BaseActivity(), PartyMemberInfoContract.View {

    private var keyType = ""
    private var mPvOptions: OptionsPickerView<String>? = null
    private var pvTime: TimePickerView? = null
    private var type: Int = Constants.PROVINCE
    private var provinceId: Int = -1
    private var cityId: Int = -1
    private var mId: Int? = null
    private var typ: Int? = null
    private var politicsStatus: Int? = null
    private var orgRelationAddressProvince: Int? = null
    private var orgRelationAddressCity: Int? = null
    private var orgRelationAddressArea: Int? = null
    private var mPvOptionsProvince: OptionsPickerView<String>? = null
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { PartyMemberInfoPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_party_member_info

    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,-1)
        typ = intent.getIntExtra(Constants.TYPE,-1)
    }

    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = "党员信息"

        llPoliticsStatus.setOnClickListener {
            //通过接口获取√politics_status
            keyType = Constants.POLITICS_STATUS
            mPresenter.getNationalityListData(uid, token,keyType)
        }
        initTimePicker()
        llTime.setOnClickListener {
            if (pvTime != null) {
                pvTime!!.show()
            }
        }
        llProvince.setOnClickListener {
            type = Constants.PROVINCE
            mPresenter.getProvinceListData(uid,token,null,null)
        }
        llCity.setOnClickListener {
            if (provinceId == -1) {
                showShort("请选择组织关系所在省")
                return@setOnClickListener
            }
            type = Constants.CITY
            mPresenter.getProvinceListData(uid,token,provinceId.toString(),null)
        }
        llArea.setOnClickListener {
            if (provinceId == -1) {
                showShort("请选择组织关系所在省")
                return@setOnClickListener
            }
            if (cityId == -1) {
                showShort("请选择组织关系所在市")
                return@setOnClickListener
            }
            type = Constants.AREA
            mPresenter.getProvinceListData(uid,token,provinceId.toString(),cityId.toString())
        }
        tvSubmitBasic.setOnClickListener {
            CircularAnim.hide(tvSubmitBasic)
                .endRadius(progressBar.height / 2f)
                .go { progressBar.visibility = View.VISIBLE }
            mPresenter.getEditPersonalInfoData(uid,token,mId!!,politicsStatus,tvTime.text.toString(),
                orgRelationAddressProvince,orgRelationAddressCity,orgRelationAddressArea,
                etOrgRelationAddress.text.toString(),etCommendStatus.text.toString(),
                null,null,null,null)
        }
    }
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
            tvTime.text = DateUtils.dateToStr(date)
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
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getPersonalInfoData(uid,token,mId!!,typ!!)
    }
    override fun setPersonalInfoData(personalInfoData: PersonalInfoData) {
        tvPoliticsStatus.text = personalInfoData.politics_status
        tvTime.text = personalInfoData.entry_party_time
        tvProvince.text = personalInfoData.org_relation_address_province
        tvCity.text = personalInfoData.org_relation_address_city
        tvArea.text = personalInfoData.org_relation_address_area
        etOrgRelationAddress.text = getEditable(personalInfoData.org_relation_address)
        etCommendStatus.text = getEditable(personalInfoData.commend_status)
    }
    override fun setEditPersonalInfoData(isSuccess: Boolean, msg: String) {
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
        mPvOptions = OptionsPickerBuilder(this@PartyMemberInfoActivity, OnOptionsSelectListener { options1, _, _, _ ->
            when (keyType) {
                Constants.POLITICS_STATUS -> {
                    tvPoliticsStatus.text = data[options1]
                    politicsStatus = nationalityListData[options1].value
                }
            }
        }).build()
        mPvOptions!!.setPicker(data)
        if (mPvOptions != null && !mPvOptions!!.isShowing) {
            mPvOptions?.show()
        }
    }
    override fun setProvinceListData(provinceListData: ArrayList<ProvinceListData>) {
        val data = ArrayList<String>()
        provinceListData.forEach {
            data.add(it.name)
        }
        mPvOptionsProvince =
                OptionsPickerBuilder(this@PartyMemberInfoActivity, OnOptionsSelectListener { options1, _, _, _ ->
                    when (type) {
                        Constants.PROVINCE -> {
                            orgRelationAddressProvince = provinceListData[options1].value
                            tvProvince.text = data[options1]
                            tvCity.text = ""
                            tvArea.text = ""
                            provinceId = provinceListData[options1].value
                            cityId = -1
                        }
                        Constants.CITY -> {
                            orgRelationAddressCity = provinceListData[options1].value
                            tvCity.text = data[options1]
                            tvArea.text = ""
                            cityId = provinceListData[options1].value
                        }
                        Constants.AREA -> {
                            orgRelationAddressArea = provinceListData[options1].value
                            tvArea.text = data[options1]
                        }
                    }
                }).build()
        mPvOptionsProvince!!.setPicker(data)
        if (mPvOptionsProvince != null && !mPvOptionsProvince!!.isShowing) {
            mPvOptionsProvince?.show()
        }
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}