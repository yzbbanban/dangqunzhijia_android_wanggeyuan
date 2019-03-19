package com.haidie.gridmember.mvp.bean

import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Create by   Administrator
 *      on     2018/12/07 09:45
 * description
 */
data class BlockInfoData constructor(var id: Int, var title: String, var number: String? = "", var is_visist: Int) :
    AbstractExpandableItem<UnitListData>(), MultiItemEntity {

    override fun getItemType(): Int = 1
    override fun getLevel(): Int = 1
}