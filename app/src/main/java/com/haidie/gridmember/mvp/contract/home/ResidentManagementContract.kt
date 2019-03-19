package com.haidie.gridmember.mvp.contract.home

import android.util.SparseArray
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.haidie.gridmember.base.IBaseView
import com.haidie.gridmember.base.IPresenter
import com.haidie.gridmember.mvp.bean.BlockInfoData
import com.haidie.gridmember.mvp.bean.UnitListData

/**
 * Create by   Administrator
 *      on     2018/12/11 11:51
 * description
 */
class ResidentManagementContract {
    interface View : IBaseView {
        fun setBlockListData(blockInfoData: ArrayList<BlockInfoData>)
        fun setUnitListData(unitListData: ArrayList<UnitListData>)
        fun showError(msg : String,errorCode : Int)
        fun setBlockUnitData(map : SparseArray<ArrayList<UnitListData>>, list : ArrayList<MultiItemEntity>)
    }
    interface Presenter : IPresenter<View> {
        fun getBlockUnitData(uid: Int,token: String)
        fun getBlockListData(uid: Int,token: String)
        fun getUnitListData(uid: Int, token: String, title: String)
    }
}