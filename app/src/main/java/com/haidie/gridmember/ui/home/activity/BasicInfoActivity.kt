package com.haidie.gridmember.ui.home.activity

import android.content.Intent
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
import com.haidie.gridmember.glide.GlideApp
import com.haidie.gridmember.mvp.bean.NationalityListData
import com.haidie.gridmember.mvp.bean.ProvinceListData
import com.haidie.gridmember.mvp.contract.home.BasicInfoContract
import com.haidie.gridmember.mvp.event.RefreshHouseDetail
import com.haidie.gridmember.mvp.presenter.home.BasicInfoPresenter
import com.haidie.gridmember.rx.RxBus
import com.haidie.gridmember.utils.CommonUtils
import com.haidie.gridmember.utils.DateUtils
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.utils.RegexUtils
import com.haidie.gridmember.view.RuntimeRationale
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_basic_info.*
import kotlinx.android.synthetic.main.common_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import top.wefor.circularanim.CircularAnim
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Create by   Administrator
 *      on     2018/12/14 08:58
 * description  基本信息
 */
class BasicInfoActivity : BaseActivity(),BasicInfoContract.View {

    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var themeId: Int = 0
    private var houseId: Int? = null
    private  var spaceId:Int? = null
    private var selectList = mutableListOf<LocalMedia>()
    private var pvTime: TimePickerView? = null
    private val mPresenter by lazy { BasicInfoPresenter() }
    private var mPvOptions: OptionsPickerView<String>? = null
    private var mPvOptionsRegisterAddressProvince: OptionsPickerView<String>? = null
    private var mPvOptionsLiveAddressProvince: OptionsPickerView<String>? = null
    private var provinceId: Int = 820  //默认 江苏省
    private var provinceIdLive: Int = 820  //默认 江苏省
    private var cityId: Int = 927  //默认 宿迁市 宿城区
    private var cityIdLive: Int = 927  //默认 宿迁市 宿城区
    private var registerType: Int = Constants.PROVINCE
    private var liveType: Int = Constants.PROVINCE
    private var isClick = false
    private var isClickLive = false
    private var keyType = ""
    private var nationality = "1"  //默认值汉族
    private var gender: String = ""  //性别,0女,1男
    private var houseRelation: String = ""  //与户主关系,身份是否0户主,1家属,2租客
    private var registerAddressProvince: String = "820"
    private var registerAddressCity: String = "927"
    private var registerAddressArea: String = "928"
    private var liveAddressProvince: String = "820"
    private var liveAddressCity: String = "927"
    private var liveAddressArea: String = "928"
    private var liveAddressType: String = ""
    private var marriageStatus: String = ""  //婚姻状况,0未婚,1已婚,2离婚,3丧偶
    private var religion: String = ""
    private var helpInfo: String = ""
    override fun getLayoutId(): Int = R.layout.activity_basic_info

    override fun initData() {
        spaceId = intent.getIntExtra(Constants.SPACE_ID,-1)
        houseId = intent.getIntExtra(Constants.HOUSE_ID,-1)
    }

    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = "基本信息"
        themeId = R.style.picture_default_style
        ivAddPic.setOnClickListener { _ ->
            AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .rationale(RuntimeRationale())
                .onGranted {
                    //弹出相册选择
                    showPictureChoose()
                }
                .onDenied { permissions -> showSettingDialog(this, permissions) }
                .start()
        }
        ivDelete.setOnClickListener {
            ivAddPic.visibility = View.VISIBLE
            ivPic.visibility = View.GONE
            ivDelete.visibility = View.GONE
            selectList.clear()
        }
        initTimePicker()
        tvBirthday.setOnClickListener {
            closeKeyboard(etIdentity,this)
            //弹出生日选项
            if (pvTime != null && !pvTime!!.isShowing) {
                pvTime?.show()
            }
        }
        rgGender.setOnCheckedChangeListener{ _, checkedId ->
            closeKeyboard(etIdentity,this)
            when (checkedId) {
                R.id.rbMale -> gender = "1"
                R.id.rbFemale -> gender = "0"
            }
        }
        tvNationality.setOnClickListener {
            closeKeyboard(etIdentity,this)
            keyType = Constants.NATIONALITY
            mPresenter.getNationalityListData(uid,token,Constants.NATIONALITY)
        }
        tvHouseRelation.setOnClickListener {
            closeKeyboard(etIdentity,this)
            keyType = Constants.HOST_RELATIONSHIP
            mPresenter.getNationalityListData(uid,token,keyType)
        }
        tvRegisterAddressProvince.setOnClickListener {
            closeKeyboard(etIdentity,this)
            registerType = Constants.PROVINCE
            isClick = true
            mPresenter.getProvinceListData(uid,token,null,null)
        }
        tvRegisterAddressCity.setOnClickListener {
            closeKeyboard(etIdentity,this)
            if (provinceId == -1) {
                showShort("请选择户籍所在地省")
                return@setOnClickListener
            }
            registerType = Constants.CITY
            isClick = true
            mPresenter.getProvinceListData(uid,token,provinceId.toString(),null)
        }
        tvRegisterAddressArea.setOnClickListener {
            closeKeyboard(etIdentity,this)
            if (provinceId == -1) {
                showShort("请选择户籍所在地省")
                return@setOnClickListener
            }
            if (cityId == -1) {
                showShort("请选择户籍所在地市")
                return@setOnClickListener
            }
            registerType = Constants.AREA
            isClick = true
            mPresenter.getProvinceListData(uid,token,provinceId.toString(),cityId.toString())
        }
        tvLiveAddressProvince.setOnClickListener {
            closeKeyboard(etIdentity,this)
            liveType = Constants.PROVINCE
            isClickLive = true
            mPresenter.getProvinceListData(uid,token,null,null)
        }
        tvLiveAddressCity.setOnClickListener {
            closeKeyboard(etIdentity,this)
            if (provinceIdLive == -1) {
                showShort("请选择现居住地地址省")
                return@setOnClickListener
            }
            liveType = Constants.CITY
            isClickLive = true
            mPresenter.getProvinceListData(uid,token,provinceIdLive.toString(),null)
        }
        tvLiveAddressArea.setOnClickListener {
            closeKeyboard(etIdentity,this)
            if (provinceIdLive == -1) {
                showShort("请选择现居住地地址省")
                return@setOnClickListener
            }
            if (cityIdLive == -1) {
                showShort("请选择现居住地地址市")
                return@setOnClickListener
            }
            liveType = Constants.AREA
            isClickLive = true
            mPresenter.getProvinceListData(uid,token,provinceIdLive.toString(),cityIdLive.toString())
        }
        tvLiveAddressType.setOnClickListener {
            closeKeyboard(etIdentity,this)
            keyType = Constants.LIVE_ADDRESS_TYPE
            mPresenter.getNationalityListData(uid,token,Constants.LIVE_ADDRESS_TYPE)
        }
        tvMarriageStatus.setOnClickListener {
            closeKeyboard(etIdentity,this)
            keyType = Constants.MARRIAGE_STATUS
            mPresenter.getNationalityListData(uid,token,keyType)
        }
        tvReligion.setOnClickListener {
            closeKeyboard(etIdentity,this)
            // 通过接口获取√religion
            keyType = Constants.RELIGION
            mPresenter.getNationalityListData(uid, token,keyType)
        }
        llHelpInfo.setOnClickListener {
            closeKeyboard(etIdentity,this)
            // 通过接口获取√help_info
            keyType = Constants.HELP_INFO
            mPresenter.getNationalityListData(uid, token,keyType)
        }
        tvSubmitBasic.setOnClickListener {
            closeKeyboard(etIdentity,this)
            CircularAnim.hide(tvSubmitBasic)
                .endRadius(progressBar.height / 2f)
                .go { progressBar.visibility = View.VISIBLE }
            val name = etName.text.toString()
            val identity = etIdentity.text.toString()
            val phone = etPhone.text.toString()
            when {
                name.isEmpty() -> {
                    showShort("请输入姓名")
                    showSubmit()
                }
                identity.isNotEmpty() && !RegexUtils.isIDCard18(identity) -> {
                    showShort("身份证格式错误")
                    showSubmit()
                }
                phone.isNotEmpty() && !RegexUtils.isMobileSimple(phone) ->{
                    showShort("手机格式错误")
                    showSubmit()
                }
                else -> {
                    var part : MultipartBody.Part? = null
                    if (!selectList.isEmpty()) {
                        val filePath = Constants.PATH_PIC + Constants.PIC_PNG
                        val file = File(filePath)
                        CommonUtils.copyFile(selectList[0].compressPath,filePath)
                        val requestFile = RequestBody.create(MediaType.parse(Constants.MULTIPART_FORM_DATA), file)
                        part = MultipartBody.Part.createFormData(Constants.AVATAR, file.name, requestFile)
                    }
                    mPresenter.getBasicInfoSubmitData(toRequestBody(uid.toString()),toRequestBody(token),toRequestBody(name),
                        toRequestBody(gender),toRequestBody(identity),toRequestBody(tvBirthday.text.toString()),
                        toRequestBody(phone),toRequestBody(nationality),toRequestBody(houseRelation),
                        toRequestBody(registerAddressProvince),toRequestBody(registerAddressCity),toRequestBody(registerAddressArea),
                        null,toRequestBody(liveAddressProvince),toRequestBody(liveAddressCity),toRequestBody(liveAddressArea),
                        null,toRequestBody(liveAddressType),toRequestBody(marriageStatus),
                        toRequestBody(religion),toRequestBody(helpInfo),
                        toRequestBody(houseId.toString()),toRequestBody(spaceId.toString()),part)
                }
            }
        }
    }
    private fun showSubmit() {
        CircularAnim.show(tvSubmitBasic).go()
        progressBar.visibility = View.GONE
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    private fun showPictureChoose() {
        PictureSelector.create(this@BasicInfoActivity)
            .openGallery(PictureMimeType.ofImage())
            .theme(themeId)
            .maxSelectNum(1)
            .minSelectNum(1)
            .imageSpanCount(4)
            .selectionMode(PictureConfig.MULTIPLE)
            .previewImage(true)
            .isCamera(true)
            .isZoomAnim(true)
            .imageFormat(PictureMimeType.PNG)
            .compress(true)
            .synOrAsy(true)
            .glideOverride(160, 160)
            .selectionMedia(selectList)
            .minimumCompressSize(100)
            .forResult(PictureConfig.CHOOSE_REQUEST)
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
            tvBirthday.text = DateUtils.dateToStr(date)
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

    override fun start() {}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    selectList = PictureSelector.obtainMultipleResult(data)
                    val media = selectList[0]
                    ivPic.visibility = View.VISIBLE
                    ivDelete.visibility = View.VISIBLE
                    ivAddPic.visibility = View.GONE
                    GlideApp.with(this)
                        .load(media.compressPath)
                        .dontAnimate()
                        .into(ivPic)
                }
            }
        }
    }
    override fun setNationalityListData(nationalityListData: ArrayList<NationalityListData>) {
        val data = ArrayList<String>()
        nationalityListData.forEach {
            data.add(it.keyname)
        }
        mPvOptions = OptionsPickerBuilder(this@BasicInfoActivity, OnOptionsSelectListener { options1, _, _, _ ->
            when (keyType) {
                Constants.NATIONALITY -> {
                    nationality = "${nationalityListData[options1].value}"
                    tvNationality.text = data[options1]
                }
                Constants.HOST_RELATIONSHIP -> {
                    houseRelation = "${nationalityListData[options1].value}"
                    tvHouseRelation.text = data[options1]
                }
                Constants.LIVE_ADDRESS_TYPE -> {
                    liveAddressType = "${nationalityListData[options1].value}"
                    tvLiveAddressType.text = data[options1]
                }
                Constants.MARRIAGE_STATUS -> {
                    marriageStatus = "${nationalityListData[options1].value}"
                    tvMarriageStatus.text = data[options1]
                }
                Constants.RELIGION -> {
                    religion = "${nationalityListData[options1].value}"
                    tvReligion.text = data[options1]
                }
                Constants.HELP_INFO -> {
                    helpInfo = "${nationalityListData[options1].value}"
                    tvHelpInfo.text = data[options1]
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
        mPvOptionsRegisterAddressProvince =
                OptionsPickerBuilder(this@BasicInfoActivity, OnOptionsSelectListener { options1, _, _, _ ->
                    when (registerType) {
                        Constants.PROVINCE -> {
                            registerAddressProvince = "${provinceListData[options1].value}"
                            tvRegisterAddressProvince.text = data[options1]
                            tvRegisterAddressCity.text = ""
                            tvRegisterAddressArea.text = ""
                            provinceId = provinceListData[options1].value
                            cityId = -1
                        }
                        Constants.CITY -> {
                            registerAddressCity = "${provinceListData[options1].value}"
                            tvRegisterAddressCity.text = data[options1]
                            tvRegisterAddressArea.text = ""
                            cityId = provinceListData[options1].value
                        }
                        Constants.AREA -> {
                            registerAddressArea = "${provinceListData[options1].value}"
                            tvRegisterAddressArea.text = data[options1]
                        }
                    }
                }).build()
        mPvOptionsRegisterAddressProvince!!.setPicker(data)
        if (mPvOptionsRegisterAddressProvince != null && !mPvOptionsRegisterAddressProvince!!.isShowing && isClick) {
            mPvOptionsRegisterAddressProvince?.show()
        }
        mPvOptionsLiveAddressProvince =
                OptionsPickerBuilder(this@BasicInfoActivity, OnOptionsSelectListener { options1, _, _, _ ->
                    when (liveType) {
                        Constants.PROVINCE -> {
                            liveAddressProvince = "${provinceListData[options1].value}"
                            tvLiveAddressProvince.text = data[options1]
                            tvLiveAddressCity.text = ""
                            tvLiveAddressArea.text = ""
                            provinceIdLive = provinceListData[options1].value
                            cityIdLive = -1
                        }
                        Constants.CITY -> {
                            liveAddressCity = "${provinceListData[options1].value}"
                            tvLiveAddressCity.text = data[options1]
                            tvLiveAddressArea.text = ""
                            cityIdLive = provinceListData[options1].value
                        }
                        Constants.AREA -> {
                            liveAddressArea = "${provinceListData[options1].value}"
                            tvLiveAddressArea.text = data[options1]
                        }
                    }
                }).build()
        mPvOptionsLiveAddressProvince!!.setPicker(data)
        if (mPvOptionsLiveAddressProvince != null && !mPvOptionsLiveAddressProvince!!.isShowing && isClickLive) {
            mPvOptionsLiveAddressProvince?.show()
        }
        isClick = false
        isClickLive = false
    }
    override fun setBasicInfoSubmitData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            showShort("提交成功")
//            跳转到房间详情页面并刷新数据
            RxBus.getDefault().post(RefreshHouseDetail())
            toActivity(HouseDetailActivity::class.java)
        }else{
            showShort(if (msg.isEmpty()) "提交失败" else msg)
            showSubmit()
        }
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}