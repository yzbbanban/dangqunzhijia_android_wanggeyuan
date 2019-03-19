package com.haidie.gridmember.ui.home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.HouseList

/**
 * Create by   Administrator
 *      on     2018/12/12 18:50
 * description
 */
class HouseSearchListAdapter(layoutResId: Int, data: List<HouseList.HouseListItem>)
    : BaseQuickAdapter<HouseList.HouseListItem, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: HouseList.HouseListItem?) {
        helper!!.getView<TextView>(R.id.tvRoomNoUsername).text = "${item!!.roomNo} " + (item.username ?: "")
    }
}