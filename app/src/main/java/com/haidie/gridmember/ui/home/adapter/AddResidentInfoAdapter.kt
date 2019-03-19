package com.haidie.gridmember.ui.home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R

/**
 * Created by admin2
 *  on 2018/08/17  14:29
 * description
 */
class AddResidentInfoAdapter(layoutResId: Int, data: MutableList<Map<String, Any>>?)
    : BaseQuickAdapter<Map<String, Any>, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: Map<String, Any>?) {

        (helper!!.getView<TextView>(R.id.tvTitle)).text = item!![Constants.TEXT] as String
    }
}