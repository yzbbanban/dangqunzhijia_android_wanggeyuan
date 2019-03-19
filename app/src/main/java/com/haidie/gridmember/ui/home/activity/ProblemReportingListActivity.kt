package com.haidie.gridmember.ui.home.activity

import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.api.UrlConstant
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.ui.home.androidinterface.AndroidBackDetailInterface
import com.haidie.gridmember.utils.DateUtils
import com.haidie.gridmember.utils.Preference
import kotlinx.android.synthetic.main.activity_problem_reporting.*

/**
 * Create by   Administrator
 *      on     2018/12/17 16:28
 * description
 */
class ProblemReportingListActivity : BaseActivity() {
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private lateinit var type: String
    override fun getLayoutId(): Int = R.layout.activity_problem_reporting

    override fun initData() {
        type = intent.getStringExtra(Constants.TYPE)
    }

    override fun initView() {
        syncCookie(UrlConstant.BASE_URL_PROBLEM_REPORTING_LIST ,"${Constants.UID}=$uid")
        syncCookie(UrlConstant.BASE_URL_PROBLEM_REPORTING_LIST,"${Constants.TOKEN}=$token")
        syncCookie(UrlConstant.BASE_URL_PROBLEM_REPORTING_LIST,"${Constants.TYPE}=$type")
        syncCookie(UrlConstant.BASE_URL_PROBLEM_REPORTING_LIST,"${Constants.IS_TO_DO_LIST}=0")
        syncCookie(UrlConstant.BASE_URL_PROBLEM_REPORTING_LIST,"${Constants.YEAR_MONTH}=${DateUtils.getNowTimeYearMonth()}")
        webView.loadUrl(UrlConstant.BASE_URL_PROBLEM_REPORTING_LIST)
        webView.addJavascriptInterface(AndroidBackDetailInterface(this@ProblemReportingListActivity), Constants.ANDROID)
        initWebViewSettings(webView.settings)
    }

    override fun start() {
    }
}