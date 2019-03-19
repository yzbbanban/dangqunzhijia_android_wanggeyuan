package com.haidie.gridmember.ui.home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.ResidentInfoSearchListData

/**
 * Create by   Administrator
 *      on     2018/12/13 09:55
 * description
 */
class ResidentInfoSearchAdapter(layoutResId: Int, data: MutableList<ResidentInfoSearchListData>?) :
    BaseQuickAdapter<ResidentInfoSearchListData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: ResidentInfoSearchListData?) {
        helper!!.getView<TextView>(R.id.tvName).text = item!!.name
        helper.getView<TextView>(R.id.tvBirthday).text = item.birthday
        helper.getView<TextView>(R.id.tvPhone).text = item.phone
        helper.getView<TextView>(R.id.tvIdentity).text = item.identity
    }
}