package com.haidie.gridmember.ui.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.MessageListData

/**
 * Create by   Administrator
 *      on     2018/12/21 13:35
 * description
 */
class MessageListAdapter(layoutResId: Int, data: MutableList<MessageListData>?)
    : BaseQuickAdapter<MessageListData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: MessageListData?) {
        helper!!.setText(R.id.tvTitle, item!!.title)
        helper.setText(R.id.tvContent, item.content)
        helper.setText(R.id.tvTime, item.create_time ?: "")
    }
}