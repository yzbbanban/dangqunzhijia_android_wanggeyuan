package com.haidie.gridmember.api

import com.haidie.gridmember.BuildConfig

/**
 * Create by   Administrator
 *      on     2018/11/30 15:52
 * description
 */
object UrlConstant {
    var BASE_URL_HOST = BuildConfig.API_HOST

    var BASE_URL_LIFE = "${BASE_URL_HOST}index/grid/lifeindex"

//    问题上报
    var BASE_URL_PROBLEM_REPORTING = "${BASE_URL_HOST}index/grid/index?type="

    var BASE_URL_PROBLEM_REPORTING_LIST = "${BASE_URL_HOST}index/grid/lists"
}