package com.haidie.gridmember.ui.home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.ReportManagementItemData

/**
 * Create by   Administrator
 *      on     2018/12/29 16:46
 * description
 */
class ReportManagementAdapter(layoutResId: Int, data: MutableList<ReportManagementItemData>?) :
    BaseQuickAdapter<ReportManagementItemData, BaseViewHolder>(layoutResId, data)  {
    override fun convert(helper: BaseViewHolder?, item: ReportManagementItemData?) {
        helper!!.getView<TextView>(R.id.tv_title).text = item!!.title
        helper.getView<TextView>(R.id.tvNum).text = "${item.num}"
    }
}