package com.haidie.gridmember.mvp.bean

/**
 * Create by   Administrator
 *      on     2018/12/19 14:52
 * description
 */
data class OrderListData(
    val id: Int,
    val desc: String?,
    val address: String?,
    val handle_status: Int,
    val problem_type: String?,
    val apartment_no: Int,
    val assign_id: Int,
    val img1: String?,
    val reporter_id: String?,
    val reporter_name: String?,
    val handle_time: String?,
    val create_time: String?,
    val complete_time: String?,
    val datatype: String?,
    val reporter_phone: String?

)

