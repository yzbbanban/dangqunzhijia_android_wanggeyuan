package com.haidie.gridmember.ui.home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.HouseDetailData
import com.haidie.gridmember.utils.ImageLoader

/**
 * Create by   Administrator
 *      on     2018/12/13 09:55
 * description
 */
class HouseDetailAdapter(layoutResId: Int, data: MutableList<HouseDetailData.ListData.Citizen>?) :
    BaseQuickAdapter<HouseDetailData.ListData.Citizen, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: HouseDetailData.ListData.Citizen?) {
        helper!!.getView<TextView>(R.id.tvName).text = item!!.name
        helper.getView<TextView>(R.id.tvHouseRelation).text = item.house_relation
        helper.getView<TextView>(R.id.tvGender).text = item.gender
        helper.getView<TextView>(R.id.tvNationality).text = item.nationality
        helper.getView<TextView>(R.id.tvBirthday).text = item.birthday
        helper.getView<TextView>(R.id.tvPhone).text = item.phone
        ImageLoader.loadCircle(mContext, item.avatar, helper.getView(R.id.ivAvatar))
        helper.getView<TextView>(R.id.tvIdentity).text = item.identity
        helper.getView<TextView>(R.id.tvPersonType).text = item.person_type
    }
}