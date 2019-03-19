package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/12/13 16:21
 * description
 */
class CheckInPunchContract {
    interface View : IBaseView {
        fun setHouseDetailCheckInPunchData(isSuccess: Boolean, msg: String)
    }
    interface Presenter : IPresenter<View> {
        fun getHouseDetailCheckInPunchData(
            uid: RequestBody, token: RequestBody, id: RequestBody,childen_id: RequestBody?, type: RequestBody,
            content: RequestBody, address: RequestBody, help_time: RequestBody,house_type: RequestBody, parts: MultipartBody.Part
        )
    }
}