package com.haidie.gridmember.ui.mine.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R


/**
 * Created by admin2
 *  on 2018/08/11  13:49
 * description
 */
class MineAdapter(layoutResId: Int, data: MutableList<String>?) :
        BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: String?) {
        val textView = helper!!.getView<TextView>(R.id.tvTitle)

        textView.text = item

    }
}