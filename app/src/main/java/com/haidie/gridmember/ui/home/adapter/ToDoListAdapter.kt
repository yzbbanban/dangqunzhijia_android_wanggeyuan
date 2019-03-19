package com.haidie.gridmember.ui.home.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.ToDoItemData

/**
 * Created by admin2
 *  on 2018/08/17  14:29
 * description
 */
class ToDoListAdapter(layoutResId: Int, data: MutableList<ToDoItemData>)
    : BaseQuickAdapter<ToDoItemData, BaseViewHolder>(layoutResId, data){
    override fun convert(helper: BaseViewHolder?, item: ToDoItemData?) {
//        val imageView = helper!!.getView<ImageView>(R.id.ivBg)
//        imageView.background = ContextCompat.getDrawable(mContext,item!!.color)
        try {
            helper!!.getView<TextView>(R.id.tvTitle).text = item!!.title
            helper.getView<TextView>(R.id.tvNum).text = "${item.num}"
        }catch (e: Exception){

        }

    }
}
