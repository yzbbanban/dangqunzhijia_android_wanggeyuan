package com.haidie.gridmember.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * Create by   Administrator
 *      on     2018/11/30 15:51
 * description
 */
object NetworkUtil {
    fun isNetworkAvailable(context: Context): Boolean {
        val manager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val info = manager.activeNetworkInfo
        return !(null == info || !info.isAvailable)
    }
}