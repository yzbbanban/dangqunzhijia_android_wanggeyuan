package com.haidie.gridmember

import android.os.Environment
import java.io.File

/**
 * Created by admin2
 *  on 2018/08/10  15:06
 * description  常量
 */
object Constants {

    const val ICON = "icon"
    const val TEXT = "text"

    //    账号相关
    const val UID = "uid"
    const val TOKEN = "token"
    const val ACCOUNT = "account"
    const val PASSWORD = "password"
    const val GROUP_ID = "group_id"
    const val DEVICE_ID = "device_id"
    const val AUTH_TYPE = "auth_type"
    const val LOGIN_STATUS = "login_status"
    const val USERNAME = "username"
    const val IS_TO_DO_LIST = "istodolist"

    //    个人信息
    const val AVATAR = "avatar"
    const val NICKNAME = "nickname"
    const val MOBILE = "mobile"

    const val OK_HTTP = "OkHttp"
    //    空字符串
    const val EMPTY_STRING = ""
    //    类型
    const val YEAR_MONTH = "year_month"
    const val NAME = "name"
    const val TYPE = "type"
    const val IS_ADD = "is_add"
    const val F_5_F_5_F_5 = "#f5f5f5"
    const val TAB = "tab"
    const val PROVINCE = 0
    const val CITY = 1
    const val AREA = 2
    const val HEIGHT_EDUCATION = "hightest_education"
    const val SPECIALTY = "good_at"
    const val STUDENT_SCORE = "student_score"
    const val STUDENT_SUBJECT = "student_subject"
    const val PROBLEM_YOUTH_TYPE = "problem_youth_type"
    const val HUGE_SICK_STATUS = "huge_sick_status"
    const val OLD_TEACHER_OLD_PARTY = "old_teacher_old_party"
    const val HEALTH = "health"
    const val IS_POOR = "is_poor"
    const val POOR_REASON = "poor_reason"
    const val DISABILITY_STATUS = "disability_status"
    const val DISABLE_LEVEL = "disable_level"
    const val MIND = "mind"
    const val NATIONALITY = "nationality"
    const val POLITICS_STATUS = "politics_status"
    const val VEHICLE_TYPE = "vehical_type"
    const val HELP_INFO = "help_info"
    const val RELIGION = "religion"
    const val INCOME_SOURCE = "income_source"
    const val LIVE_ADDRESS_TYPE = "live_address_type"
    const val MARRIAGE_STATUS = "marriage_status"
    const val HOST_RELATIONSHIP = "host_relationship"
    const val SPACE = "space"
    const val SPACE_ID = "space_id"
    const val UNIT = "unit"
    const val SIZE = 10
    const val ID = "id"
    const val WORK_ID = "work_id"
    const val WORK_TYPE = "work_type"
    const val HOUSE_ID = "house_id"
    const val URL_KEY = "url"
    const val ANDROID = "android"
    const val ADDRESS_STR = "address_Str"
    const val IMAGE = "image/*"
    const val PIC = "pic"
    const val BLOCK_ID = "blockId"
    const val HOURSE_ID = "hourseId"
    const val PIC_PNG = "pic.png"
    const val MULTIPART_FORM_DATA = "multipart/form-data"
    //    图片路径
    var PATH_PIC =
        MyApplication.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).absolutePath + File.separatorChar
    //    下载路径
    var PATH_APK =
        MyApplication.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).absolutePath + File.separatorChar
}