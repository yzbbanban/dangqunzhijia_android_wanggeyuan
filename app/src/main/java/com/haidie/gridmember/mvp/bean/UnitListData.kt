package com.haidie.gridmember.mvp.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Create by   Administrator
 *      on     2018/12/07 10:19
 * description
 */
data class UnitListData(val id: Int,val unit: String) : MultiItemEntity {
    override fun getItemType(): Int = 2
}