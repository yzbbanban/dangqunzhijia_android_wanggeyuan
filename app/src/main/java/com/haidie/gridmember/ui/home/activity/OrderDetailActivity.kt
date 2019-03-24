package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.view.View
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.api.UrlConstant
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.OrderDepartData
import com.haidie.gridmember.mvp.bean.OrderDetailData
import com.haidie.gridmember.mvp.contract.home.GetOrderDepartContract
import com.haidie.gridmember.mvp.contract.home.GetOrderDetailContract
import com.haidie.gridmember.mvp.contract.home.SendOrderContract
import com.haidie.gridmember.mvp.presenter.home.OrderDepartPresenter
import com.haidie.gridmember.mvp.presenter.home.OrderDetailPresenter
import com.haidie.gridmember.mvp.presenter.home.SendOrderPresenter
import com.haidie.gridmember.utils.*
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Create by   Administrator
 *      on     2018/12/17 08:43
 * description  工单详情
 */
class OrderDetailActivity : BaseActivity(), GetOrderDetailContract.View, GetOrderDepartContract.View,
    SendOrderContract.View {

    private var mOption: LocationClientOption? = null
    private var mLocationClient: LocationClient? = null
    private var addressStr: String? = null
    private var mId: String? = null
    private var dType: String? = null
    private var mData = mutableListOf<String>()
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var nickname by Preference(Constants.NICKNAME, Constants.EMPTY_STRING)
    private val mPresenter by lazy { OrderDetailPresenter() }
    private val dPresenter by lazy { OrderDepartPresenter() }
    private val sPresenter by lazy { SendOrderPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_order_detail
    private var mPvOptions: OptionsPickerView<String>? = null
    private var assign_id: String? = null

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
        dPresenter.attachView(this)
        sPresenter.attachView(this)

        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener { onBackPressed() }
        tvTitle.text = "详情"


        tvSubmitReport.setOnClickListener { _ ->
            val intent = Intent(this@OrderDetailActivity, WorkDetailReportActivity::class.java)
            intent.putExtra(Constants.WORK_ID, "" + mId)
            intent.putExtra(Constants.WORK_TYPE, "" + dType)
            startActivity(intent)
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)

        }
        try {
            mPresenter.getOrderDetailData(uid, token, mId!!, dType!!)
        } catch (e: Exception) {

        }
        initBanner()

        tvSubmitReport.setOnClickListener { _ ->
            sPresenter.sendOrderData(uid, token, mId!!, dType!!, assign_id!!)
        }

        tvDoPeople.setOnClickListener { _ ->
            dPresenter.getOrderDepartData(uid, token)
        }

        tvDoDepart.setOnClickListener { _ ->
            dPresenter.getOrderDepartData(uid, token)
        }

        tvDoPeople.setText("请选择")
        tvDoDepart.setText("请选择")
    }

    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)

    }

    override fun setOrderDetailData(orderDetailData: OrderDetailData) {
        mData.add(0, orderDetailData.img1)
        mData.add(1, orderDetailData.img2)
        mData.add(2, orderDetailData.img3)
        mData.add(3, orderDetailData.img4)
        mData.add(4, orderDetailData.img5)
        mData.add(5, orderDetailData.img6)
        mData.add(6, orderDetailData.img7)
        mData.add(7, orderDetailData.img8)
        mData.add(8, orderDetailData.img9)
        if (mData.isEmpty()) {
            showShort("暂无数据内容")
            mLayoutStatusView?.showEmpty()
            return
        }

        tvContent.setText(orderDetailData.desc)
        tvWorkReporter.setText(orderDetailData.reporter_name)
        tvWorkReporterTel.setText(orderDetailData.reporter_phone)
        var status = "未派单"
        if (orderDetailData.handle_status == 3) {
            status = "已派单"
            flSubmit.setVisibility(View.GONE)
        } else {
        }
        setBannerData()
        tvReportStatus.setText(status)
        tvReportTime.setText(orderDetailData.create_time)
        tvCreateTime.setText(orderDetailData.create_time)
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
            //banner设置方法全部调用完毕时最后调用
            start()
        }

    }

    val data1 = ArrayList<String>()
    val data2 = ArrayList<List<String>>()

    override fun setOrderDepartData(orderDepartData: ArrayList<OrderDepartData>) {
        LogHelper.d("--000--> " + orderDepartData)


        for (d in orderDepartData.withIndex()) {
            data1.add(d.value.dptName)
            var depart = d.value.groupaccess
            if (depart.size > 0) {
                val dataTemp = ArrayList<String>()
                for (d2 in depart.withIndex()) {
                    dataTemp.add(d2.value.nickname!! + ":" + d2.value.uid)
                }
                data2.add(dataTemp)
            } else {
                val dataTemp = ArrayList<String>()
                data2.add(dataTemp)
            }

        }

        LogHelper.d("--111--> " + data1)
        LogHelper.d("--222--> " + data2)

        mPvOptions =
            OptionsPickerBuilder(this@OrderDetailActivity, OnOptionsSelectListener { options1, options2, _, _ ->
                var ai = data2.get(options1).get(options2)
                tvDoPeople.setText(data1.get(options1))
                tvDoDepart.setText(ai)
                assign_id = ai.split(":")[0]

            }).build()

        mPvOptions?.setPicker(data1, data2 as List<List<String>>?)

        mPvOptions?.setTitleText("选择部门-指派人员")
        if (mPvOptions != null && !mPvOptions!!.isShowing) {
            mPvOptions?.show()
        }
    }

    override fun setOrderResultData(obj: Object) {
        showShort("提交成功")
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