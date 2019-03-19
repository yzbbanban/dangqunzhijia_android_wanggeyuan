package com.haidie.gridmember.mvp.contract.mine

import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.ModifyAvatarData
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Create by   Administrator
 *      on     2019/01/04 10:29
 * description
 */
class ModifyAvatarContract {
    interface View : IBaseView{
        fun setModifyAvatarData(modifyAvatarData: ModifyAvatarData)
        fun showError(msg :String,errorCode : Int)
    }
    interface Presenter : IPresenter<View>{
        fun getModifyAvatarData(admin_id: RequestBody,token: RequestBody,parts: MultipartBody.Part?)
    }
}