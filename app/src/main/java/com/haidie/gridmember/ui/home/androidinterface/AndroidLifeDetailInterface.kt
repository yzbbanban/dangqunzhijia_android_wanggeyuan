package com.haidie.gridmember.ui.home.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.gridmember.ui.home.activity.WebViewDetailActivity

/**
 * Created by admin2
 *  on 2018/08/21  9:23
 * description
 */
class AndroidLifeDetailInterface(activity: WebViewDetailActivity) {
    private val webViewDetailActivity = activity
    @JavascriptInterface
    fun callAndroidIsBack(isBack: Boolean) {
        webViewDetailActivity.isBack(isBack)
    }
}