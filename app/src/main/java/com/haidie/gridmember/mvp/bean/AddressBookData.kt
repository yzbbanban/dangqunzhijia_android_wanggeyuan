package com.haidie.gridmember.mvp.bean

/**
 * Create by   Administrator
 *      on     2018/12/18 16:20
 * description
 */
class AddressBookData(
    val name: String,
    val list: ArrayList<AddressBookItemData>) {
    data class AddressBookItemData(
        val id: Int,
        val mobile: String,
        val nickname: String,
        val avatar: String,
        val email: String,
        val group_name: String,
        val group_id: Int
    )
}