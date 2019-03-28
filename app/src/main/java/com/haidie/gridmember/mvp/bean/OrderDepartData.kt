package com.haidie.gridmember.mvp.bean

/**
 * Create by   Administrator
 *      on     2018/12/18 16:20
 * description
 */
class OrderDepartData(

    val id: Int,
    val dptName: String,
    val groupaccess: ArrayList<OrderDepartListData>

) {
    override fun toString(): String {
        return "OrderDepartData(id=$id, dptName='$dptName', groupaccess=$groupaccess)"
    }
}
