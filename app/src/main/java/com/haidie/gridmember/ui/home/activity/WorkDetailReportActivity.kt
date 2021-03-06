package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.view.View
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.bigkoo.pickerview.view.OptionsPickerView
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.contract.home.WorkDetailReportContract
import com.haidie.gridmember.mvp.presenter.home.WorkDetailReportPresenter
import com.haidie.gridmember.utils.*
import com.haidie.gridmember.view.RuntimeRationale
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_work_detail_report.*
import kotlinx.android.synthetic.main.common_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import top.wefor.circularanim.CircularAnim
import java.io.File

/**
 * Create by   Administrator
 *      on     2018/12/13 10:39
 * description
 */
class WorkDetailReportActivity : BaseActivity(), WorkDetailReportContract.View {

    private var houseId: Int? = null
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var mOption: LocationClientOption? = null
    private var mLocationClient: LocationClient? = null
    private var addressStr: String? = null
    private var mId: String? = null
    private var dType: String? = null
    private val type: String = "1"   //拜访类型区分,填1
    private var mPvOptions: OptionsPickerView<String>? = null
    private var selectList = mutableListOf<LocalMedia>()
    private val mPresenter by lazy { WorkDetailReportPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_work_detail_report

    override fun initData() {
        mId = intent!!.getStringExtra(Constants.WORK_ID)
        dType = intent!!.getStringExtra(Constants.WORK_TYPE)
        mOption = LocationClientOption()
        mOption?.apply {
            locationMode = LocationClientOption.LocationMode.Hight_Accuracy
            setCoorType("bd09ll")
            setScanSpan(0)
            setIsNeedAddress(true)
            setNeedDeviceDirect(false)
            setIsNeedLocationDescribe(true)
            setIsNeedLocationPoiList(true)
            isOpenGps = true
        }
        mLocationClient = LocationClient(applicationContext)
        mLocationClient?.apply {
            registerLocationListener(mListener)
            locOption = mOption
            start()
        }
    }

    private val mListener = object : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            if (null != location && location.locType != BDLocation.TypeServerError) {
                if (location.locType != BDLocation.TypeGpsLocation && location.locType != BDLocation.TypeOffLineLocation &&
                    location.locType != BDLocation.TypeNetWorkLocation
                ) {
                    mLocationClient?.restart()
                    return
                }
                addressStr = location.addrStr.toString()
                val sb = StringBuffer(256)
                sb.append("time : ")
                sb.append(location.time)
                sb.append("\nlocType : ")// 定位类型
                sb.append(location.locType)
                sb.append("\nlocType description : ")// *****对应的定位类型说明*****
                sb.append(location.locTypeDescription)
                sb.append("\nlatitude : ")// 纬度
                sb.append(location.latitude)
                sb.append("\nlongitude : ")// 经度
                sb.append(location.longitude)

                sb.append("\nprovince : ")// 获取省份
                sb.append(location.province)

                sb.append("\ncity : ")// 城市
                sb.append(location.city)
                sb.append("\naddressStr : ")
                sb.append(location.addrStr)

                when {
                    location.locType == BDLocation.TypeGpsLocation -> {// GPS定位结果
                        sb.append("\nspeed : ")
                        sb.append(location.speed)// 速度 单位：km/h
                        sb.append("\nsatellite : ")
                        sb.append(location.satelliteNumber)// 卫星数目
                        sb.append("\nheight : ")
                        sb.append(location.altitude)// 海拔高度 单位：米
                        sb.append("\ngps status : ")
                        sb.append(location.gpsAccuracyStatus)// *****gps质量判断*****
                        sb.append("\ndescribe : ")
                        sb.append("gps定位成功")
                    }
                    location.locType == BDLocation.TypeNetWorkLocation -> {// 网络定位结果
                        // 运营商信息
                        if (location.hasAltitude()) {// *****如果有海拔高度*****
                            sb.append("\nheight : ")
                            sb.append(location.altitude)// 单位：米
                        }
                        sb.append("\noperators : ")// 运营商信息
                        sb.append(location.operators)
                        sb.append("\ndescribe : ")
                        sb.append("网络定位成功")
                    }

                    location.locType == BDLocation.TypeCriteriaException -> {
                        sb.append("\ndescribe : ")
                        sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机")
                    }
                }
                LogHelper.d("=======$sb")
                mLocationClient?.stop()
                tvAddress.text = addressStr
            } else {
                mLocationClient?.restart()
            }
        }
    }

    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener { onBackPressed() }
        tvTitle.text = "走访完成"
        ivAddPic.setOnClickListener { _ ->
            AndPermission.with(this)
                .runtime()
                .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.CAMERA)
                .rationale(RuntimeRationale())
                .onGranted {
                    //开启相机拍照
                    PictureSelector.create(this@WorkDetailReportActivity)
                        .openCamera(PictureMimeType.ofImage())
                        .compress(true)
                        .forResult(PictureConfig.CHOOSE_REQUEST)
                    //禁用动画
                    overridePendingTransition(0, 0)
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
        tvSubmitCheck.setOnClickListener {
            CircularAnim.hide(tvSubmitCheck)
                .endRadius((progressBar.height / 2).toFloat())
                .go { progressBar.visibility = View.VISIBLE }
            val content = etContent.text.toString()
            if (content.isEmpty()) {
                showShort("请输入完成说明")
                showSubmit()
                return@setOnClickListener
            }
            if (selectList.isEmpty()) {
                showShort("请上传照片")
                showSubmit()
                return@setOnClickListener
            }

            val filePath = Constants.PATH_PIC + Constants.PIC_PNG
            val file = File(filePath)
            CommonUtils.copyFile(selectList[0].compressPath, filePath)
            val requestFile = RequestBody.create(MediaType.parse(Constants.MULTIPART_FORM_DATA), file)
            val part = MultipartBody.Part.createFormData(Constants.PIC, file.name, requestFile)
            var mData = ArrayList<MultipartBody.Part>()
            mData.add(part)
            mPresenter.getWorkDetailReportData(
                toRequestBody(uid.toString()), toRequestBody(token),
                toRequestBody(mId!!), toRequestBody(dType!!), toRequestBody(content),
                mData
            )
        }
    }


    private fun showSubmit() {
        CircularAnim.show(tvSubmitCheck).go()
        progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    selectList = PictureSelector.obtainMultipleResult(data)
                    val localMedia = selectList[0]
                    ivPic.visibility = View.VISIBLE
                    ivDelete.visibility = View.VISIBLE
                    ivAddPic.visibility = View.GONE
                    ImageLoader.load(this, File(localMedia.compressPath), ivPic)
                }
            }
        }
    }

    override fun start() {}


    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
    }

    override fun setWorkDetailReportData(obj: Object) {
        showShort("上传完成")
        finish()

    }

    override fun showLoading() {}
    override fun dismissLoading() {}
}