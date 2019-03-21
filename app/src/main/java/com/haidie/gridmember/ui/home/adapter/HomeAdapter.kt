package com.haidie.gridmember.ui.home.adapter

import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.MainData

/**
 * Create by   Administrator
 *      on     2018/11/30 09:33
 * description
 */
class HomeAdapter(layoutResId: Int, data: List<MainData>) :
    BaseQuickAdapter<MainData, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: MainData?) {
//        val imageView = helper!!.getView<ImageView>(R.id.ivBg)
//        imageView.background = ContextCompat.getDrawable(mContext, item!!.color)
        val linearLayout = helper!!.getView<LinearLayout>(R.id.llContent)
        val contents = item!!.contents
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (linearLayout != null) {
            linearLayout.removeAllViews()
        }
        val textTitle = TextView(mContext)
        textTitle.layoutParams = params
        textTitle.setTextColor(ContextCompat.getColor(mContext, R.color.white))
        textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        textTitle.text = item!!.title
        linearLayout.addView(textTitle, params)
        if (contents != null) {
            for (index in 0 until contents!!.size) {
                val text = TextView(mContext)
                text.layoutParams = params
                text.setLineSpacing(1.2f, 1.2f)
                text.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                text.text = contents!![index]
                linearLayout.addView(text, index + 1, params)
            }
        }
    }
}