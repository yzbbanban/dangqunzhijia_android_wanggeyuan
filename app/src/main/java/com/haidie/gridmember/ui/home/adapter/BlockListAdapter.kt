package com.haidie.gridmember.ui.home.adapter

import android.support.v4.content.ContextCompat
import android.widget.*
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.BlockInfoData

/**
 * Create by   Administrator
 *      on     2018/12/12 18:50
 * description
 */
class BlockListAdapter(layoutResId: Int, data: List<BlockInfoData>) :
    BaseQuickAdapter<BlockInfoData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: BlockInfoData?) {
        helper!!.getView<TextView>(R.id.tvRoomNoUsername).text = "${item!!.title}栋"
        val rlBackGround = helper!!.getView<RelativeLayout>(R.id.rlBackGround)
        //已访问
        if (item!!.is_visist == 1) {
            rlBackGround.background = ContextCompat.getDrawable(mContext, R.color.color_69)
        }

    }
}