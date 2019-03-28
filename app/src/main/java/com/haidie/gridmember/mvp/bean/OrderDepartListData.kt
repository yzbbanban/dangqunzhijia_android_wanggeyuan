package com.haidie.gridmember.mvp.bean

/**
 * Create by   Administrator
 *      on     2018/12/18 16:20
 * description
 */
class OrderDepartListData(
    val uid: Int,
    val group_id: Int,
    val nickname: String?
) {
    override fun toString(): String {
        return "OrderDepartListData(uid=$uid, group_id=$group_id, nickname=$nickname)"
    }
}
