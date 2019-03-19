package com.haidie.gridmember.ui.home.activity

import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.contract.home.NewVehicleInformationContract
import com.haidie.gridmember.mvp.event.ReloadVehicleInformationListEvent
import com.haidie.gridmember.mvp.presenter.home.NewVehicleInformationPresenter
import com.haidie.gridmember.rx.RxBus
import com.haidie.gridmember.utils.Preference
import kotlinx.android.synthetic.main.activity_new_vehicle_information.*
import kotlinx.android.synthetic.main.common_toolbar.*
import top.wefor.circularanim.CircularAnim

/**
 * Create by   Administrator
 *      on     2018/12/19 13:21
 * description  车辆信息新增
 */
class NewVehicleInformationActivity : BaseActivity(),NewVehicleInformationContract.View {

    private var keyType = ""
    private var mId: Int? = null
    private var vehicleType: Int = -1
    private var mPvOptions: OptionsPickerView<String>? = null
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { NewVehicleInformationPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_new_vehicle_information

    override fun initData() {
        mId = intent.getIntExtra(Constants.ID,-1)
    }

    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = "车辆信息新增"

        llVehicleType.setOnClickListener {
            //通过接口获取√vehical_type
            keyType = Constants.VEHICLE_TYPE
            mPresenter.getNationalityListData(uid, token,keyType)
        }
        tvSubmitBasic.setOnClickListener {
            CircularAnim.hide(tvSubmitBasic)
                .endRadius(progressBar.height / 2f)
                .go { progressBar.visibility = View.VISIBLE }
            mPresenter.getNewVehicleInformationData(uid,token,mId!!,vehicleType,etLecenseId.text.toString(),
                etEngineId.text.toString(),etMotorId.text.toString(),etColor.text.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {}
    override fun setNationalityListData(nationalityListData: ArrayList<NationalityListData>) {
        val data = ArrayList<String>()
        nationalityListData.forEach {
            data.add(it.keyname)
        }
        mPvOptions = OptionsPickerBuilder(this@NewVehicleInformationActivity, OnOptionsSelectListener { options1, _, _, _ ->
            when (keyType) {
                Constants.VEHICLE_TYPE -> {
                    tvVehicleType.text = data[options1]
                    vehicleType = nationalityListData[options1].value
                }
            }
        }).build()
        mPvOptions!!.setPicker(data)
        if (mPvOptions != null && !mPvOptions!!.isShowing) {
            mPvOptions?.show()
        }
    }
    override fun setNewVehicleInformationData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            showShort("新增成功")
//            返回上级页面刷新数据
            RxBus.getDefault().post(ReloadVehicleInformationListEvent())
            finish()
        }else{
            showShort(if (msg.isEmpty()) "新增失败" else msg)
            showLoginSubmit()
        }
    }
    private fun showLoginSubmit() {
        CircularAnim.show(tvSubmitBasic).go()
        progressBar.visibility = View.GONE
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}