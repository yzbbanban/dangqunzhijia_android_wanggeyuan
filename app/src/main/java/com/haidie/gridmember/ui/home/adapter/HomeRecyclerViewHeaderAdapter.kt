package com.haidie.gridmember.ui.home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.utils.ImageLoader

/**
 * Created by admin2
 *  on 2018/08/17  14:29
 * description
 */
class HomeRecyclerViewHeaderAdapter(layoutResId: Int, data: MutableList<Map<String, Any>>?) :
    BaseQuickAdapter<Map<String, Any>, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: Map<String, Any>?) {
        (helper!!.getView<TextView>(R.id.tvName)).text = item!![Constants.TEXT] as String
        ImageLoader.load(mContext, item[Constants.ICON] as Int, helper.getView(R.id.ivImage))
    }
}