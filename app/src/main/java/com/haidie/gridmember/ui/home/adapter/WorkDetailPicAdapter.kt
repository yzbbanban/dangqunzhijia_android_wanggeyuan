package com.haidie.gridmember.ui.home.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.haidie.gridmember.R
import com.haidie.gridmember.api.UrlConstant
import com.haidie.gridmember.utils.ImageLoader

/**
 * Created by admin2
 *  on 2018/08/17  14:29
 * description
 */
class WorkDetailPicAdapter(layoutResId: Int, data: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        var ivPicReport = helper!!.getView<ImageView>(R.id.ivPicReport)

        if (item != null || "".equals(item)) {
            ImageLoader.load(mContext, UrlConstant.BASE_URL_HOST + item as String, ivPicReport)
        } else {
            ImageLoader.load(mContext, R.drawable.icon_default, ivPicReport)
        }


    }
}
