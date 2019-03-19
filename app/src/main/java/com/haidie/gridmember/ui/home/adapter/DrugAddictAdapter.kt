package com.haidie.gridmember.ui.home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.PersonnelSupervisionListData

/**
 * Created by admin2
 *  on 2018/08/17  14:29
 * description
 */
class DrugAddictAdapter(layoutResId: Int, data: MutableList<PersonnelSupervisionListData.PersonnelSupervisionListItemData>)
    : BaseQuickAdapter<PersonnelSupervisionListData.PersonnelSupervisionListItemData, BaseViewHolder>(layoutResId, data){
    override fun convert(helper: BaseViewHolder?, item: PersonnelSupervisionListData.PersonnelSupervisionListItemData?) {
        helper!!.getView<TextView>(R.id.tvName).text = item!!.name
        val textView = (helper.getView<TextView>(R.id.tvContent))
//        16栋1单元503
        textView.text = "${item.space}栋${item.unit}单元${item.roomNo}"
    }
}
