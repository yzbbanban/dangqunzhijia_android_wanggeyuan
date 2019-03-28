package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.view.View
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.api.UrlConstant
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.WorkDetailData
import com.haidie.gridmember.mvp.contract.home.GetWorkDetailContract
import com.haidie.gridmember.mvp.presenter.home.WorkDetailPresenter
import com.haidie.gridmember.utils.*
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.activity_work_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/17 08:43
 * description  回访登记
 */
class WorkReportDetailActivity : BaseActivity(), GetWorkDetailContract.View {

    private var mOption: LocationClientOption? = null
    private var mLocationClient: LocationClient? = null
    private var addressStr: String? = null
    private var mId: String? = null
    private var dType: String? = null
    private var mData = mutableListOf<String>()
    private var mDoData = mutableListOf<String>()
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var nickname by Preference(Constants.NICKNAME, Constants.EMPTY_STRING)
    private val mPresenter by lazy { WorkDetailPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_work_detail

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

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
                tvWorkAddress.text = addressStr
            } else {
                mLocationClient?.restart()
            }
        }
    }

    override fun initView() {
        mPresenter.attachView(this)

        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener { onBackPressed() }
        tvTitle.text = "详情"


        tvSubmitReport.setOnClickListener { _ ->
            val intent = Intent(this@WorkReportDetailActivity, WorkDetailReportActivity::class.java)
            intent.putExtra(Constants.WORK_ID, "" + mId)
            intent.putExtra(Constants.WORK_TYPE, "" + dType)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)

        }
        try {
            mPresenter.getWorkDetailData(uid, token, mId!!, dType!!)
        } catch (e: Exception) {

        }
        initBanner()

    }

    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)

    }

    override fun setWorkDetailData(workDetailData: WorkDetailData) {
        mData.add(0, workDetailData.img1)
        if (!"".equals(workDetailData.img2)) {
            mData.add(1, workDetailData.img2)
        }
        if (!"".equals(workDetailData.img3)) {
            mData.add(2, workDetailData.img3)
        }

        if (!"".equals(workDetailData.img4)) {
            mData.add(3, workDetailData.img4)
        }
        if (!"".equals(workDetailData.img5)) {
            mData.add(4, workDetailData.img5)
        }
        if (!"".equals(workDetailData.img6)) {
            mData.add(5, workDetailData.img6)
        }
        if (!"".equals(workDetailData.img7)) {
            mData.add(6, workDetailData.img7)
        }
        if (!"".equals(workDetailData.img8)) {
            mData.add(7, workDetailData.img8)
        }
        if (!"".equals(workDetailData.img9)) {
            mData.add(8, workDetailData.img9)
        }


        if (mData.isEmpty()) {
            showShort("暂无数据内容")
            mLayoutStatusView?.showEmpty()
            return
        }

        mDoData.add(0, workDetailData.handle_img1)
        if (!"".equals(workDetailData.handle_img2)) {
            mDoData.add(1, workDetailData.handle_img2)
        }
        if (!"".equals(workDetailData.handle_img3)) {
            mDoData.add(2, workDetailData.handle_img3)

        }
        if (!"".equals(workDetailData.handle_img4)) {
            mDoData.add(3, workDetailData.handle_img4)
        }
        if (!"".equals(workDetailData.handle_img5)) {
            mDoData.add(4, workDetailData.handle_img5)
        }
        if (!"".equals(workDetailData.handle_img6)) {
            mDoData.add(5, workDetailData.handle_img6)
        }
        if (!"".equals(workDetailData.handle_img7)) {
            mDoData.add(6, workDetailData.handle_img7)
        }
        if (!"".equals(workDetailData.handle_img8)) {
            mDoData.add(7, workDetailData.handle_img8)
        }
        if (!"".equals(workDetailData.handle_img9)) {
            mDoData.add(8, workDetailData.handle_img9)
        }

        if (mDoData.isEmpty()) {
            showShort("暂无数据内容")
            mLayoutStatusView?.showEmpty()
            return
        }


        tvContent.setText(workDetailData.desc)
        tvWorkReporter.setText(workDetailData.reporter_name)
        tvWorkReporterTel.setText(workDetailData.reporter_phone)
        var status = "未处理"
        if (workDetailData.handle_status == 3) {
            status = "已处理"
            flSubmit.setVisibility(View.GONE)
            llDoPeo.setVisibility(View.INVISIBLE)
//            tvDuePeople.setText(workDetailData.reporter_name)
            tvDoContent.setText(workDetailData.handle_detail)
            setDoBannerData()
        } else {
            llBanner.setVisibility(View.INVISIBLE)
        }
        setBannerData()
        tvReportStatus.setText(status)
        tvReportTime.setText(workDetailData.create_time)
        tvCreateTime.setText(workDetailData.create_time)
        tvManage.setText(nickname)


    }

    private fun initBanner() {
        mBanner?.apply {
            //设置banner样式
            setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
            //设置图片加载器
            setImageLoader(GlideImageLoader())
            //设置banner动画效果
            setBannerAnimation(Transformer.DepthPage)
            //设置指示器位置（当banner模式中有指示器时）
            setIndicatorGravity(BannerConfig.CENTER)
            isAutoPlay(false)
            //banner设置方法全部调用完毕时最后调用
            start()
        }
        mDoBanner?.apply {
            //设置banner样式
            setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
            //设置图片加载器
            setImageLoader(GlideImageLoader())
            //设置banner动画效果
            setBannerAnimation(Transformer.DepthPage)
            isAutoPlay(false)
            //设置指示器位置（当banner模式中有指示器时）
            setIndicatorGravity(BannerConfig.CENTER)
            //banner设置方法全部调用完毕时最后调用
            start()
        }
    }

    private fun setBannerData() {

        val arrayList = arrayListOf<String>()
        mData.forEach {
            arrayList.add(UrlConstant.BASE_URL_HOST + it)
        }
        mBanner?.apply {
            //设置图片集合
            setImages(arrayList)
            initBanner()
            isAutoPlay(false)
            setOnBannerListener { position ->
            }
        }
    }

    private fun setDoBannerData() {

        val arrayList = arrayListOf<String>()
        mDoData.forEach {
            arrayList.add(UrlConstant.BASE_URL_HOST + it)
        }
        mDoBanner?.apply {
            //设置图片集合
            setImages(arrayList)
            initBanner()
            isAutoPlay(false)
            setOnBannerListener { position ->
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun start() {}

    override fun showLoading() {}
    override fun dismissLoading() {}
}