package com.haidie.gridmember.mvp.bean

/**
 * Create by   Administrator
 *      on     2018/12/20 19:13
 * description
 */
data class NoticeListData (
    val id: Int,
    val uid_send: Int,
    val uid_read: Int,
    val is_read: Int,
    val is_system: Int,
    val title: String,
    val content: String?,
    val jump_id: Int,
    val params: String,
    val is_broadcast: Int,
    val create_time: String?,
    val update_time: String?,
    val delete_tag: Int,
    val app_id: Int,
    val uid_send_name: String,
    val uid_read_name: String
)