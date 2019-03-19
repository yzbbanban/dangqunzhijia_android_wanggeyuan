package com.haidie.gridmember.mvp.bean

/**
 * Create by   Administrator
 *      on     2018/12/21 13:10
 * description
 */
data class MessageListData (
    val id: Int,
    val uid_send: Int,
    val uid_read: String?,
    val is_read: Int,
    val is_system: Int,
    val title: String,
    val content: String,
    val jump_id: String?,
    val params: String?,
    val is_broadcast: Int,
    val create_time: String?,
    val update_time: String?,
    val delete_tag: Int,
    val app_id: Int,
    val uid_send_name: String,
    val uid_read_name: String?
)