package com.haidie.gridmember.ui.mine.activity

import android.view.View
import android.widget.TextView
import com.allenliu.versionchecklib.callback.OnCancelListener
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder
import com.allenliu.versionchecklib.v2.builder.NotificationBuilder
import com.allenliu.versionchecklib.v2.builder.UIData
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.api.UrlConstant
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.mvp.bean.CheckVersionData
import com.haidie.gridmember.mvp.contract.mine.AboutUsContract
import com.haidie.gridmember.mvp.presenter.mine.AboutUsPresenter
import com.haidie.gridmember.utils.AppUtils
import com.haidie.gridmember.utils.CompareVersion
import com.haidie.gridmember.view.BaseDialog
import kotlinx.android.synthetic.main.activity_about_us.*
import kotlinx.android.synthetic.main.common_toolbar.*

/**
 * Created by admin2
 *  on 2018/08/18  11:29
 * description  关于我们
 */
class AboutUsActivity : BaseActivity(), AboutUsContract.View  {
    //    1(默认)群众app,2网格员APP,3工作人员app
    private val appType: Int = 2
    private val mPresenter by lazy { AboutUsPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_about_us
    override fun initData() {}
    override fun initView() {
        mPresenter.attachView(this)
        ivBack.visibility = View.VISIBLE
        ivBack.setOnClickListener { onBackPressed() }
        tvTitle.text = "关于网格员"
        val versionName = AppUtils.getVersionName(this)
        tv_version_name.text = String.format(resources.getString(R.string.version_content_text), versionName)
        tvHost.text = "${UrlConstant.BASE_URL_HOST} 版权所有"
        tv_check_for_updates.setOnClickListener {
            //            调用版本检查接口
            mPresenter.getCheckVersionData(appType)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
    override fun start() {}
    private var builder: DownloadBuilder? = null
    override fun setCheckVersionData(checkVersionData: CheckVersionData) {
        //        当前版本
        val versionCode = AppUtils.getVersionName(this)
//        线上版本
        val version = checkVersionData.version
        val compareVersion = CompareVersion.compareVersion(versionCode, version)
//        1.0.5 1.1.0
        if (compareVersion < 0){
            builder = AllenVersionChecker
                .getInstance()
                .downloadOnly(
                    UIData.create()
                        .setDownloadUrl(UrlConstant.BASE_URL_HOST + checkVersionData.download)
                        .setContent(checkVersionData.desc))
            builder?.apply {
                notificationBuilder = createCustomNotification()
                customVersionDialogListener = customVersionDialog()
                downloadAPKPath = Constants.PATH_APK
                onCancelListener = OnCancelListener {}
                executeMission(this@AboutUsActivity)
            }
        }else{
            showShort("当前已是最新版本")
        }
    }
    private fun createCustomNotification(): NotificationBuilder {
        return NotificationBuilder.create()
            .setRingtone(true)
            .setIcon(R.mipmap.ic_app)
            .setContentTitle(AppUtils.getAppName(this))
            .setContentText(getString(R.string.custom_content_text))
    }
    /**
     * 务必用库传回来的context 实例化你的dialog
     * 自定义的dialog UI参数展示，使用versionBundle
     */
    private fun customVersionDialog(): CustomVersionDialogListener {
        return CustomVersionDialogListener { context, versionBundle ->
            val baseDialog = BaseDialog(context, R.style.BaseDialog, R.layout.dialog_check_version)
            val textView = baseDialog.findViewById<TextView>(R.id.tv_msg)
            textView.text = versionBundle!!.content
            baseDialog
        }
    }
    override fun showError(msg: String, errorCode: Int) {
        showShort(msg)
    }
    override fun showLoading() {}
    override fun dismissLoading() {}
}