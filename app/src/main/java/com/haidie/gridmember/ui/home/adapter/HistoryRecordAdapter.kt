package com.haidie.gridmember.ui.home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.HistoryRecordData

/**
 * Create by   Administrator
 *      on     2018/12/13 09:55
 * description
 */
class HistoryRecordAdapter(layoutResId: Int, data: MutableList<HistoryRecordData>?) :
    BaseQuickAdapter<HistoryRecordData, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: HistoryRecordData?) {
        helper!!.getView<TextView>(R.id.tvTitle).text = item!!.title
        helper.getView<TextView>(R.id.tvNum).text = item.contents
    }
}