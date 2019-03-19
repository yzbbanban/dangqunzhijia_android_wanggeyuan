package com.haidie.gridmember.mvp.bean

/**
 * Create by   Administrator
 *      on     2018/12/03 09:26
 * description
 */
data class CareCountListData(
    val delivery_id: Int,
    val name: String,
    val total: Int,
    val visisted: Int,
    val un_visisted: Int
)