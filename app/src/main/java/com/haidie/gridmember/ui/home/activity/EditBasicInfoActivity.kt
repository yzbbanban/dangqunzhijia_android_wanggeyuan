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
import com.haidie.gridmember.mvp.bean.PersonalInfoData
import com.haidie.gridmember.mvp.bean.ProvinceListData
import com.haidie.gridmember.mvp.contract.home.EditBasicInfoContract
import com.haidie.gridmember.mvp.event.RefreshHouseDetail
import com.haidie.gridmember.mvp.presenter.home.EditBasicInfoPresenter
import com.haidie.gridmember.rx.RxBus
import com.haidie.gridmember.utils.*
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
 *      on     2018/12/19 17:16
 * description  修改个人基本信息
 */
class EditBasicInfoActivity : BaseActivity(),EditBasicInfoContract.View {

    private var mId: Int? = null
    private var houseId: Int? = null
    private  var spaceId:Int? = null
    private var type: Int? = null
    private var isAdd: Boolean = false
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private val mPresenter by lazy { EditBasicInfoPresenter() }
    private var selectList = mutableListOf<LocalMedia>()
    private var themeId: Int = 0
    private var pvTime: TimePickerView? = null
    private var gender: String = ""  //性别,0女,1男
    private var keyType = ""
    private var nationality :String?= null
    private var houseRelation :String?= null  //与户主关系,身份是否0户主,1家属,2租客
    private var mPvOptions: OptionsPickerView<String>? = null
    private var registerType: Int = Constants.PROVINCE
    private var liveType: Int = Constants.PROVINCE
    private var isClick = false
    private var isClickLive = false
    private var provinceId: Int = -1
    private var provinceIdLive: Int = -1
    private var cityId: Int = -1
    private var cityIdLive: Int = -1
    private var mPvOptionsRegisterAddressProvince: OptionsPickerView<String>? = null
    private var mPvOptionsLiveAddressProvince: OptionsPickerView<String>? = null
    private var registerAddressProvince :String?= null
    private var registerAddressCity :String?= null
    private var registerAddressArea :String?= null
    private var liveAddressProvince :String?= null
    private var liveAddressCity :String?= null
    private var liveAddressArea :String?= null
    private var liveAddressType :String?= null
    private var marriageStatus :String?= null  //婚姻状况,0未婚,1已婚,2离婚,3丧偶
    private var religion :String?= null
    private var helpInfo :String?= null
    override fun getLayoutId(): Int = R.layout.activity_basic_info

    override fun initData() {
        spaceId = intent.getIntExtra(Constants.SPACE_ID,-1)
        houseId = intent.getIntExtra(Constants.HOUSE_ID,-1)
        mId = intent.getIntExtra(Constants.ID,-1)
        type = intent.getIntExtra(Constants.TYPE,-1)
        isAdd = intent.getBooleanExtra(Constants.IS_ADD,false)
        if (type == -1) {
            type = null
        }
    }
    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener{ onBackPressed() }
        tvTitle.text = "基本信息"
        ivDelete.setOnClickListener {
            ivAddPic.visibility = View.VISIBLE
            ivPic.visibility = View.GONE
            ivDelete.visibility = View.GONE
            selectList.clear()
        }
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
            mPresenter.getNationalityListData(uid,token,keyType)
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
            mPresenter.getNationalityListData(uid,token,keyType)
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
                    mPresenter.getEditBasicInfoSubmitData(toRequestBody(uid.toString()),toRequestBody(token),toRequestBody(mId.toString()),
                        toRequestBody(name),toRequestBody(gender),toRequestBody(identity),toRequestBody(tvBirthday.text.toString()),
                        toRequestBody(phone),if(nationality == null) null else toRequestBody(nationality!!),
                        if(houseRelation == null) null else  toRequestBody(houseRelation!!),
                        if(liveAddressType == null) null else  toRequestBody(liveAddressType!!),
                        if(registerAddressProvince == null) null else  toRequestBody(registerAddressProvince!!),
                        if(registerAddressCity == null) null else  toRequestBody(registerAddressCity!!),
                        if(registerAddressArea == null) null else  toRequestBody(registerAddressArea!!),
                        if(liveAddressProvince == null) null else  toRequestBody(liveAddressProvince!!),
                        if(liveAddressCity == null) null else  toRequestBody(liveAddressCity!!),
                        if(liveAddressArea == null) null else  toRequestBody(liveAddressArea!!),
                        toRequestBody(houseId.toString()),toRequestBody(spaceId.toString()),
                        if(marriageStatus == null) null else  toRequestBody(marriageStatus!!),
                        if(religion == null) null else  toRequestBody(religion!!),
                        if(helpInfo == null) null else  toRequestBody(helpInfo!!),part)
                }
            }
        }
    }

    private fun showSubmit() {
        CircularAnim.show(tvSubmitBasic).go()
        progressBar.visibility = View.GONE
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
    private fun showPictureChoose() {
        PictureSelector.create(this@EditBasicInfoActivity)
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
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {
        mPresenter.getPersonalInfoData(uid,token,mId!!,type)
    }
    override fun setPersonalInfoData(personalInfoData: PersonalInfoData) {
        etName.text = getEditable(personalInfoData.name)
        ivPic.visibility = View.VISIBLE
        ivDelete.visibility = View.VISIBLE
        ivAddPic.visibility = View.GONE
        val avatar = personalInfoData.avatar
        if (avatar != null) {
            ImageLoader.load(this, avatar,ivPic)
        }else{
            ivPic.visibility = View.GONE
            ivDelete.visibility = View.GONE
            ivAddPic.visibility = View.VISIBLE
        }

        etIdentity.text = getEditable(personalInfoData.identity)
        tvBirthday.text = personalInfoData.birthday
        val gender = personalInfoData.gender
        if ("男" == gender) {
            rbMale.isChecked = true
        }else{
            rbFemale.isChecked = true
        }
        etPhone.text = getEditable(personalInfoData.phone)
        tvNationality.text = personalInfoData.nationality
        tvHouseRelation.text = personalInfoData.house_relation
        tvRegisterAddressProvince.text = personalInfoData.register_address_province
        tvRegisterAddressCity.text = personalInfoData.register_address_city
        tvRegisterAddressArea.text = personalInfoData.register_address_area
        tvLiveAddressProvince.text = personalInfoData.live_address_province
        tvLiveAddressCity.text = personalInfoData.live_address_city
        tvLiveAddressArea.text = personalInfoData.live_address_area
        tvLiveAddressType.text = personalInfoData.live_address_type
        tvMarriageStatus.text = personalInfoData.marriage_status
        tvReligion.text = personalInfoData.religion
        tvHelpInfo.text = personalInfoData.help_info
    }
    override fun setNationalityListData(nationalityListData: ArrayList<NationalityListData>) {
        val data = ArrayList<String>()
        nationalityListData.forEach {
            data.add(it.keyname)
        }
        mPvOptions = OptionsPickerBuilder(this@EditBasicInfoActivity, OnOptionsSelectListener { options1, _, _, _ ->
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
                OptionsPickerBuilder(this@EditBasicInfoActivity, OnOptionsSelectListener { options1, _, _, _ ->
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
                OptionsPickerBuilder(this@EditBasicInfoActivity, OnOptionsSelectListener { options1, _, _, _ ->
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
    override fun setEditBasicInfoSubmitData(isSuccess: Boolean, msg: String) {
        if (isSuccess) {
            showShort("提交成功")
            if (isAdd) {
                //跳转到房间详情页面并刷新数据
                RxBus.getDefault().post(RefreshHouseDetail())
                toActivity(HouseDetailActivity::class.java)
                return
            }
            finish()
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