package com.haidie.gridmember.mvp.bean

/**
 * Create by   Administrator
 *      on     2018/12/12 19:29
 * description
 */
data class HouseList(
    val list: ArrayList<HouseListItem>,
    val total: Int,
    val pages: Int,
    val current: Int,
    val size: Int) {
    data class HouseListItem(
        val id: Int,
        val num: String,
        val unit: String,
        val roomNo: String,
        val username: String?,
        val is_visist: Int
    )
}