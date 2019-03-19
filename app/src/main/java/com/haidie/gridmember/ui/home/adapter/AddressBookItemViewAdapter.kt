package com.haidie.gridmember.ui.home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.AddressBookData
import com.haidie.gridmember.utils.ImageLoader

/**
 * Created by admin2
 *  on 2018/08/18  16:07
 * description
 */
class AddressBookItemViewAdapter(layoutResId: Int, data: ArrayList<AddressBookData.AddressBookItemData>?) :
    BaseQuickAdapter<AddressBookData.AddressBookItemData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: AddressBookData.AddressBookItemData?) {
        ImageLoader.loadCircle(mContext, item!!.avatar, helper!!.getView(R.id.ivAvatar))
        helper.getView<TextView>(R.id.tvNickname).text = item.nickname
        helper.getView<TextView>(R.id.tvGroupName).text = item.group_name
        helper.getView<TextView>(R.id.tvMobile).text = item.mobile
    }
}