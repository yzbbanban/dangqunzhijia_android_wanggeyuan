package com.haidie.gridmember.mvp.contract

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/12/21 09:09
 * description
 */
class ReturnVisitContract {
    interface View : IBaseView {
        fun setReturnVisitData(isSuccess: Boolean, msg: String)
    }

    interface Presenter : IPresenter<View> {
        fun getReturnVisitData(
            admin_id: RequestBody, token: RequestBody, id: RequestBody, type: RequestBody,
            content: RequestBody, address: RequestBody, pic: MultipartBody.Part
        )
    }
}