package com.haidie.gridmember

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlin.properties.Delegates

/**
 * Create by   Administrator
 *      on     2018/11/30 15:46
 * description
 */
class MyApplication : Application() {
    companion object {
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.background_color)
                MaterialHeader(context).setShowBezierWave(true)
            }
        }
        private const val TAG = "MyApplication"
        var context: Context by Delegates.notNull()
            private set
    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        initConfig()
    }
    private fun initConfig() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .tag(Constants.OK_HTTP)
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy){
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG  //如果是Debug  模式 ；打印日志
            }
        })
    }
}