package com.haidie.gridmember.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.haidie.gridmember.Constants
import com.haidie.gridmember.R
import com.haidie.gridmember.mvp.bean.BlockInfoData
import com.haidie.gridmember.mvp.bean.UnitListData
import com.haidie.gridmember.ui.home.activity.HouseSearchListActivity

/**
 * Create by   Administrator
 *      on     2018/12/12 09:52
 * description
 */
 class ResidentManagementAdapter (data: List<MultiItemEntity>)
    : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(data) {
    private var list: List<MultiItemEntity>
    init {
        addItemType(1, R.layout.resident_management_item)
        addItemType(2, R.layout.resident_management_unit_item)
        list = data
    }
    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
        when (helper!!.itemViewType) {
            1 -> {
                val block = item as BlockInfoData
                helper.setText(R.id.tvBlock, "${block.title}栋")
                helper.itemView.setOnClickListener {
                    val pos = helper.adapterPosition
                    if (null == block.getSubItem(0)){
                        showShort("暂无单元数据")
                        return@setOnClickListener
                    }
                    if (block.isExpanded ) {
                        collapse(pos, false)
                    } else {
                        expand(pos, false)
                    }
                }
            }
            2 -> {
                val unit = item as UnitListData
                val block = list[getParentPosition(unit)] as BlockInfoData
                helper.setText(R.id.tvUnit,"${unit.unit}单元")
                helper.itemView.setOnClickListener {
                    val intent = Intent(mContext, HouseSearchListActivity::class.java)
                    intent.putExtra(Constants.SPACE_ID,block.id)
                    intent.putExtra(Constants.SPACE,block.title)
                    intent.putExtra(Constants.UNIT,unit.unit)
                    mContext.startActivity(intent)
                }
            }
        }
    }
    private var myToast: Toast? = null
    @SuppressLint("ShowToast")
    fun showShort( message: String){
        if (myToast == null) {
            myToast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
        } else {
            myToast?.let {
                it.setText(message)
                it.duration = Toast.LENGTH_SHORT
            }
        }
        myToast?.show()
    }
}