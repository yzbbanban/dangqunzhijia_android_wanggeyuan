package com.haidie.gridmember.mvp.bean

/**
 * Created by admin2
 *  on 2018/08/22  13:16
 * description
 */
data class GridMemberData(val userinfo: UserInfo) {
    data class UserInfo(
        val id: Int,
        val username: String,
        val nickname: String,
        val avatar: String,
        val token: String,
        val admin_id: Int,
        val group_id: Int,
        val mobile: String)
}