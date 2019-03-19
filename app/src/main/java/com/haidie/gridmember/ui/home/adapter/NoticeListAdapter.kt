package com.haidie.gridmember.ui.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.NoticeListData

/**
 * Create by   Administrator
 *      on     2018/12/21 11:25
 * description
 */
class NoticeListAdapter (layoutResId: Int, data: MutableList<NoticeListData>?)
    : BaseQuickAdapter<NoticeListData, BaseViewHolder>(layoutResId, data){
    override fun convert(helper: BaseViewHolder?, item: NoticeListData?) {
        helper!!.setText(R.id.tvTitle, item!!.title)
        helper.setText(R.id.tvContent, item.content ?: "")
        helper.setText(R.id.tvTime, item.create_time ?: "")
    }
}