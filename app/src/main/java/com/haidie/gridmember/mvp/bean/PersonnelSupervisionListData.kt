package com.haidie.gridmember.mvp.bean

/**
 * Create by   Administrator
 *      on     2018/12/18 18:59
 * description
 */
data class PersonnelSupervisionListData(
    val list: MutableList<PersonnelSupervisionListItemData>,
    val total: Int,
    val pages: Int,
    val current: Int,
    val size: Int
) {
    data class PersonnelSupervisionListItemData(
        val id: Int,
        val house_id: Int,
        val name: String,
        val help_info: Int,
        val space: String,
        val unit: String,
        val roomNo: String,
        val is_visist: String?
    )
}