package com.haidie.gridmember.ui.home.androidinterface

import android.webkit.JavascriptInterface
import com.haidie.gridmember.ui.home.activity.ContradictoryDisputeOrBasicAppealActivity

/**
 * Create by   Administrator
 *      on     2018/08/31 15:11
 * description
 */
class AndroidContradictoryDisputeOrBasicAppealInterface(contradictoryDisputeOrBasicAppealActivity: ContradictoryDisputeOrBasicAppealActivity) {
    private val activity = contradictoryDisputeOrBasicAppealActivity
    @JavascriptInterface
    fun callAndroidIsBack(isBack: Boolean) {
        activity.isBack(isBack)
    }
    @JavascriptInterface
    fun callAndroidType(type: String) {
        activity.callAndroidType(type)
    }

    companion object {
        var mCurrentPhotoPath: String? = null//拍照存储的路径,例如：/storage/emulated/0/Pictures/20170608104809.jpg
    }
    /**
     * 打开相机拍照
     */
    @JavascriptInterface
    fun takePicture() {
        activity.takePicture()
    }
    /**
     * 打开本地相册选择图片
     */
    @JavascriptInterface
    fun choosePic() {
        activity.choosePicture()
    }
}