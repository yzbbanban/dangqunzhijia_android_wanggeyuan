package com.haidie.gridmember.mvp.contract.home

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2018/12/18 16:12
 * description
 */
class WorkDetailReportContract {
    interface View : IBaseView {
        fun setWorkDetailReportData(obj: Object)
        fun showError(msg: String, errorCode: Int)
    }

    interface Presenter : IPresenter<View> {
        fun getWorkDetailReportData(

            admin_id: RequestBody,
            token: RequestBody,
            id: RequestBody,
            datatype: RequestBody,
            handle_detail: RequestBody,
            image: ArrayList<MultipartBody.Part>
        )
    }
}