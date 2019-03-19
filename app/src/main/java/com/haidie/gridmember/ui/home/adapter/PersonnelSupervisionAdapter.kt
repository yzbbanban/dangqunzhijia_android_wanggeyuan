package com.haidie.gridmember.ui.home.adapter

import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.SuperviseCountData

/**
 * Created by admin2
 *  on 2018/08/17  14:29
 * description
 */
class PersonnelSupervisionAdapter(layoutResId: Int, data: MutableList<SuperviseCountData>)
    : BaseQuickAdapter<SuperviseCountData, BaseViewHolder>(layoutResId, data){
    override fun convert(helper: BaseViewHolder?, item: SuperviseCountData?) {
        helper!!.getView<TextView>(R.id.tvTitle).text = item!!.keyname
        val textView = (helper.getView<TextView>(R.id.tvNum))
        textView.text = "${item.un_visisted}"
        val iv = (helper.getView<ImageView>(R.id.ivBg))
        val drawable = ContextCompat.getDrawable(mContext, R.drawable.circle_bg)
        val wrap = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrap,ContextCompat.getColor(mContext,R.color.circle_bg_red))
        iv.setImageDrawable(wrap)
    }
}
