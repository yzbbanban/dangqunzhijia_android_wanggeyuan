package com.haidie.gridmember.mvp.bean

/**
 * Create by   Administrator
 *      on     2018/12/19 14:52
 * description
 */
data class FlowPeopleData(
        val total: Int,
        val pages: Int,
        val current: Int,
        val size: Int,
        val list: ArrayList<FlowPeopleListData>
)

