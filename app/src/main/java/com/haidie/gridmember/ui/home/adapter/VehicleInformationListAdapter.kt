package com.haidie.gridmember.ui.home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.VehicleInformationListData

/**
 * Create by   Administrator
 *      on     2018/12/12 18:50
 * description
 */
class VehicleInformationListAdapter(layoutResId: Int, data: List<VehicleInformationListData>)
    : BaseQuickAdapter<VehicleInformationListData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: VehicleInformationListData?) {
        helper!!.getView<TextView>(R.id.tvVehicleType).text = item!!.vehicle_type
        helper.getView<TextView>(R.id.tvLecenseId).text = item.lecense_id
        helper.getView<TextView>(R.id.tvEngineId).text = item.engine_id
        helper.getView<TextView>(R.id.tvMotorId).text = item.motor_id
        helper.getView<TextView>(R.id.tvColor).text = item.color
        helper.addOnClickListener(R.id.tvEdit)
        helper.addOnClickListener(R.id.tvDelete)
    }
}