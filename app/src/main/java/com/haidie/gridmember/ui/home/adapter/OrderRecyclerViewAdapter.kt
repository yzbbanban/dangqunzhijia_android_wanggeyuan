package com.haidie.gridmember.ui.home.adapter

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.api.UrlConstant
import com.haidie.gridmember.mvp.bean.AddressBookData
import com.haidie.gridmember.mvp.bean.OrderData
import com.haidie.gridmember.mvp.bean.OrderListData
import com.haidie.gridmember.utils.ImageLoader
import com.haidie.gridmember.view.RecyclerViewDividerItemDecoration

/**
 * Created by admin2
 *  on 2018/08/18  15:59
 * description
 */
class OrderRecyclerViewAdapter(layoutResId: Int, data: ArrayList<OrderListData>?) :
    BaseQuickAdapter<OrderListData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: OrderListData?) {
        val tvReportName = helper!!.getView<TextView>(R.id.tvReportName)
        val tvReportType = helper!!.getView<TextView>(R.id.tvReportType)
        val tvReporter = helper!!.getView<TextView>(R.id.tvReporter)
        val ivReport = helper!!.getView<ImageView>(R.id.ivReport)
        tvReportName.text = item!!.desc
        tvReportType.text = item!!.problem_type
        tvReporter.text = item!!.reporter_name
//        helper.setNestView(R.id.llReportRecyclerItemGroup)
//        helper.addOnClickListener(R.id.recyclerViewItem)

        if (item!!.img1 != null) {
            ImageLoader.load(mContext, UrlConstant.BASE_URL_HOST + item!!.img1 as String, ivReport)
        } else {
            ImageLoader.load(mContext, R.drawable.icon_default, ivReport)
        }

        tvReporter.setOnClickListener { position ->
            val mobile = item!!.reporter_id
            if (mobile!!.isNotEmpty()) {
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