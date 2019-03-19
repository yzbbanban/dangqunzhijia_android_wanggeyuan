package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Base64
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.api.UrlConstant
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.event.ToProblemReportingEvent
import com.haidie.gridmember.rx.RxBus
import com.haidie.gridmember.ui.home.androidinterface.AndroidProblemReportingInterface
import com.haidie.gridmember.ui.home.androidinterface.AndroidProblemReportingInterface.Companion.mCurrentPhotoPath
import com.haidie.gridmember.utils.LogHelper
import com.haidie.gridmember.utils.Preference
import com.haidie.gridmember.utils.RealPath
import com.yalantis.ucrop.util.BitmapLoadUtils.calculateInSampleSize
import kotlinx.android.synthetic.main.activity_problem_reporting.*
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * Create by   Administrator
 *      on     2018/12/13 15:29
 * description
 */
class ProblemReportingActivity : BaseActivity() {
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var addressStr: String? = null
    private var mOption: LocationClientOption? = null
    private var mLocationClient: LocationClient? = null
    val takePhotoResultCode = 1
    val fileChooserResultCode = 2
    private var mType: Int =
        -1  ///index/grid/index?type=1 问题上报  /index/grid/index?type=2矛盾对纠纷 /index/grid/index?type=3公共安全
    override fun getLayoutId(): Int = R.layout.activity_problem_reporting

    override fun initData() {
        mType = intent.getIntExtra(Constants.TYPE, -1)
        mOption = LocationClientOption()
        mOption?.apply {
            locationMode = LocationClientOption.LocationMode.Battery_Saving  //网络定位
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
                /**
                 * 时间也可以使用systemClock.elapsedRealTime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
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
                LogHelper.d("=======\n$sb")
                mLocationClient?.stop()
                mLayoutStatusView?.showContent()
                var mSettings: WebSettings? = null
                webView?.let {
                    it.webChromeClient = mWebChromeClient
                    mSettings = it.settings
                }
                syncCookie("${UrlConstant.BASE_URL_PROBLEM_REPORTING}$mType", "${Constants.UID}=$uid")
                syncCookie("${UrlConstant.BASE_URL_PROBLEM_REPORTING}$mType", "${Constants.TOKEN}=$token")
                syncCookie(
                    "${UrlConstant.BASE_URL_PROBLEM_REPORTING}$mType",
                    "${Constants.ADDRESS_STR}=${encode(addressStr!!)}"
                )

                webView.loadUrl("${UrlConstant.BASE_URL_PROBLEM_REPORTING}$mType")
                webView.addJavascriptInterface(
                    AndroidProblemReportingInterface(this@ProblemReportingActivity),
                    Constants.ANDROID
                )
                initWebViewSettings(mSettings!!)
            } else {
                mLocationClient?.restart()
            }
        }
    }

    override fun initView() {}
    private var mWebChromeClient: WebChromeClient = object : WebChromeClient() {
        //For Android 5.0
        override fun onShowFileChooser(
            webView: WebView,
            filePathCallback: ValueCallback<Array<Uri>>,
            fileChooserParams: WebChromeClient.FileChooserParams
        ): Boolean {
            choosePicture()
            return true
        }
        //       android 4.0 - android 4.3  安卓4.4.4也用的这个方法
        fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
            choosePicture()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fileChooserResultCode) {
            val result = if (data == null || resultCode != RESULT_OK) null else data.data
            val pathFromUri = RealPath.getRealPathFromUri(this, result!!)
            val string = bitmapToString(pathFromUri)
            setPlatformType(string)
        }
        //因为拍照指定了路径，所以data值为null
        else if (requestCode == takePhotoResultCode) {
            val pictureFile = File(AndroidProblemReportingInterface.mCurrentPhotoPath)
            val uri = Uri.fromFile(pictureFile)
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.data = uri
            sendBroadcast(intent)  // 这里我们发送广播让MediaScanner 扫描我们制定的文件
            // 这样在系统的相册中我们就可以找到我们拍摄的照片了【但是这样一来，就会执行MediaScanner服务中onLoadFinished方法，所以需要注意】
            try {
                val string = bitmapToString(AndroidProblemReportingInterface.mCurrentPhotoPath)
                setPlatformType(string)
            } catch (e: Exception) {
//                捕捉部分手机返回null的情况
                LogHelper.d("=====\n$e")
                //android调用H5代码
                webView.loadUrl("javascript:cancel()")
            }

        }
    }
    // 根据路径获得图片并压缩，返回bitmap用于显示
    private fun getSmallBitmap(filePath: String?): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, options)
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 600, 800)
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }
    //把bitmap转换成String
    private fun bitmapToString(filePath: String?): String {
        val bm = getSmallBitmap(filePath)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
    private fun setPlatformType(result: String?) {
        //android调用H5代码
        webView.loadUrl("javascript:cameraResult('data:image/jpg;base64,$result')")
        webView.loadUrl("javascript:cancel()")
    }
    fun takePicture() {
        //调用系统拍照
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            //解决buildSdk>=24,调用Uri.fromFile时报错的问题
            // https://blog.csdn.net/qq_34709056/article/details/77968456
            //https://blog.csdn.net/qq_34709056/article/details/78528507
            mCurrentPhotoPath = "${Environment.getExternalStorageDirectory()}${File.separator}Pictures" +
                    File.separator + "JPEG_" + System.currentTimeMillis() + ".jpg"
            val file = File(mCurrentPhotoPath)
            val photoFile: Uri?
            photoFile = if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val authority = applicationInfo.packageName + ".provider"
                FileProvider.getUriForFile(applicationContext, authority, file)
            } else {
                Uri.fromFile(file)
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
            //启动拍照的窗体。并注册 回调处理
            startActivityForResult(takePictureIntent, takePhotoResultCode)
        }
    }
    fun choosePicture(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*" // 查看类型
        startActivityForResult(intent, fileChooserResultCode)
    }
    override fun start() {}
    fun callAndroidType(type :String) {
        showShort(type)
        if (type.isNotEmpty()) {
            showShort("发布成功")
            finish()
            // 刷新数据
            RxBus.getDefault().post(ToProblemReportingEvent(type))
        } else {
            showShort("发布失败")
        }
    }
}