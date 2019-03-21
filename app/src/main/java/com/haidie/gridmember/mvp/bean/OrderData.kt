package com.haidie.gridmember.mvp.bean

/**
 * Create by   Administrator
 *      on     2018/12/18 16:20
 * description
 */
class OrderData(

    val total: Int,
    val pages: Int,
    val current: Int,
    val list: ArrayList<OrderListData>

)
