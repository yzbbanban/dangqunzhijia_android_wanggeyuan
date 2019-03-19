package com.haidie.gridmember.ui.home.adapter

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.AddressBookData
import com.haidie.gridmember.mvp.bean.OrderData
import com.haidie.gridmember.view.RecyclerViewDividerItemDecoration

/**
 * Created by admin2
 *  on 2018/08/18  15:59
 * description
 */
class OrderRecyclerViewAdapter(layoutResId: Int, data: ArrayList<OrderData>?) :
    BaseQuickAdapter<OrderData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: OrderData?) {
        val tvReportName = helper!!.getView<TextView>(R.id.tvReportName)
        val tvReportType = helper!!.getView<TextView>(R.id.tvReportType)
        val tvReporter = helper!!.getView<TextView>(R.id.tvReporter)
        tvReportName.text = item!!.name
        tvReportType.text = item!!.type
        tvReporter.text = item!!.mobile
//        helper.setNestView(R.id.llReportRecyclerItemGroup)
//        helper.addOnClickListener(R.id.recyclerViewItem)
        tvReporter.setOnClickListener { position ->
            val mobile = item!!.mobile
            if (mobile.isNotEmpty()) {
                call(mobile)
            }
        }
    }

    private fun call(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        mContext.startActivity(intent)
    }
}