package com.haidie.gridmember.ui.home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.BlockInfoData

/**
 * Create by   Administrator
 *      on     2018/12/12 18:50
 * description
 */
class BlockListAdapter(layoutResId: Int, data: List<BlockInfoData>)
    : BaseQuickAdapter<BlockInfoData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: BlockInfoData?) {
        helper!!.getView<TextView>(R.id.tvRoomNoUsername).text = "${item!!.title}栋"
    }
}