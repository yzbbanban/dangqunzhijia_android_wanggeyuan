package com.haidie.gridmember.ui.home.activity

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.widget.LinearLayout
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.api.UrlConstant
import com.haidie.gridmember.base.BaseActivity
import com.haidie.gridmember.ui.home.androidinterface.AndroidBackDetailInterface
import com.haidie.gridmember.utils.LogHelper
import com.haidie.gridmember.utils.Preference
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebConfig
import kotlinx.android.synthetic.main.activity_web_view_detail.*

/**
 * Created by admin2
 *  on 2018/08/21  9:09
 * description  生活详情
 */
class WebViewDetailActivity : BaseActivity() {

    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var mAgentWeb: AgentWeb? = null
    private var url: String? = null

    companion object {
        fun startActivity(context: FragmentActivity, url: String) {
            val intent = Intent(context, WebViewDetailActivity::class.java)
            intent.putExtra(Constants.URL_KEY, url)
            context.startActivity(intent)
            context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        }
    }

    override fun onPause() {
        mAgentWeb!!.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        try {
            mAgentWeb!!.webLifeCycle.onResume()
        } catch (e: Exception) {

        }
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb!!.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    override fun getLayoutId(): Int = R.layout.activity_web_view_detail
    override fun initData() {
        url = intent.getStringExtra(Constants.URL_KEY)
    }

    override fun initView() {
        try {
            AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url, "${Constants.UID}=$uid")
            AgentWebConfig.syncCookie(UrlConstant.BASE_URL_HOST + url, "${Constants.TOKEN}=$token")
            mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(
                    frame_layout_web_view_detail,
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
                )
                .useDefaultIndicator(ContextCompat.getColor(this, R.color.colorPrimary))
                .setMainFrameErrorView(R.layout.error_view, -1)
                .createAgentWeb()
                .ready()
                .go(UrlConstant.BASE_URL_HOST + url)
            mAgentWeb!!.jsInterfaceHolder.addJavaObject(Constants.ANDROID, AndroidBackDetailInterface(this))

        } catch (e: Exception) {
            LogHelper.d("webView: " + e.message)
        }

    }

    override fun start() {}

}