package com.haidie.gridmember.ui.home.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.api.UrlConstant
import com.haidie.gridmember.mvp.bean.FlowPeopleListData
import com.haidie.gridmember.utils.ImageLoader

/**
 * Created by admin2
 *  on 2018/08/18  15:59
 * description
 */
class FlowPeopleRecyclerViewAdapter(layoutResId: Int, data: ArrayList<FlowPeopleListData>?) :
    BaseQuickAdapter<FlowPeopleListData, BaseViewHolder>(layoutResId, data) {

    var uid = 0
    override fun convert(helper: BaseViewHolder?, item: FlowPeopleListData?) {
        val ivHeaderImage = helper!!.getView<ImageView>(R.id.ivHeaderImage)
        val tvFlowName = helper!!.getView<TextView>(R.id.tvFlowName)
        val tvHouseNumber = helper!!.getView<TextView>(R.id.tvHouseNumber)
        if (item!!.avatar != null) {
            ImageLoader.load(mContext, UrlConstant.BASE_URL_HOST + item!!.avatar as String, ivHeaderImage)
        } else {
            ImageLoader.load(mContext, R.drawable.ic_header, ivHeaderImage)
        }

        tvFlowName.text = item!!.name
        //16 栋1单元503
        val sb = StringBuffer(256)
        tvHouseNumber.text =
            sb.append(item!!.block_no).append("栋" + item!!.unit).append("单元" + item!!.apartment_no).toString()

    }

}