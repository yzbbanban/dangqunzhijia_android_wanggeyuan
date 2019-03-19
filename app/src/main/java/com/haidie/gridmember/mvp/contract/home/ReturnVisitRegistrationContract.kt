package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/12/21 09:09
 * description
 */
class ReturnVisitRegistrationContract {
    interface View :IBaseView{
        fun setReturnVisitRegistrationData(isSuccess: Boolean, msg: String)
    }
    interface Presenter : IPresenter<View>{
        fun getReturnVisitRegistrationData(
            uid: RequestBody, token: RequestBody, id: RequestBody, childen_id: RequestBody?, type: RequestBody,
            content: RequestBody, address: RequestBody, help_time: RequestBody,  house_type: RequestBody?, parts: MultipartBody.Part
        )
    }
}