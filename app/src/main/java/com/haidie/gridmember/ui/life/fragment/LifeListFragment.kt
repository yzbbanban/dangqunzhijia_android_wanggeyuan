package com.haidie.gridmember.ui.life.fragment

import android.os.Bundle
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.api.UrlConstant
import com.haidie.gridmember.base.BaseFragment
import com.haidie.gridmember.ui.life.androidinterface.AndroidLifeListInterface
import com.haidie.gridmember.utils.Preference
import com.just.agentweb.AgentWebConfig.syncCookie
import kotlinx.android.synthetic.main.fragment_life_list.*

/**
 * Created by admin2
 *  on 2018/08/13  20:09
 * description 生活列表
 */
class LifeListFragment : BaseFragment() {
    private var uid by Preference(Constants.UID, -1)
    private var token by Preference(Constants.TOKEN, Constants.EMPTY_STRING)
    private var index: Int? = null
    companion object {
        fun getInstance(position: Int): LifeListFragment {
            val fragment = LifeListFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.index = position
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_life_list
    override fun initView() {

        val mSettings  = webView.settings
        syncCookie(UrlConstant.BASE_URL_LIFE,"${Constants.UID}=$uid")
        syncCookie(UrlConstant.BASE_URL_LIFE,"${Constants.TOKEN}=$token")

        webView.addJavascriptInterface(AndroidLifeListInterface(this@LifeListFragment),Constants.ANDROID)
        initWebViewSettings(mSettings)
    }
    override fun lazyLoad() {
        syncCookie(UrlConstant.BASE_URL_LIFE,"${Constants.TAB}=$index")
        webView.loadUrl(UrlConstant.BASE_URL_LIFE)
    }
}