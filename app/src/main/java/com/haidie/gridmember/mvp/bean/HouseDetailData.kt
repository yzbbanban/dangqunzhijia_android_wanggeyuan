package com.haidie.gridmember.mvp.bean

/**
 * Create by   Administrator
 *      on     2018/12/03 09:37
 * description
 */
data class HouseDetailData(val list: ListData) {
    data class ListData(
        val id: Int,
        val num: String,
        val unit: String,
        val roomNo: String,
        val type: Int,
        val citizen_list: ArrayList<Citizen>?) {
        data class Citizen(
            val id: Int,
            val name: String,
            val gender: String,
            val nationality: String,
            val house_relation: String?,
            val birthday: String?,
            val phone: String,
            val identity: String,
            val avatar: String,
            val person_type: String?
        )
    }
}