package com.haidie.gridmember.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.haidie.gridmember.R
import com.haidie.gridmember.api.UrlConstant
import com.haidie.gridmember.glide.GlideApp
import java.io.File

/**
 * Create by   Administrator
 *      on     2018/09/08 13:32
 * description
 */
object ImageLoader {
    /**
     * 使用Glide加载圆形ImageView(如头像)时，不要使用占位图
     * @param context context
     * @param pic image url
     * @param iv imageView
     */
    fun load(context: Context, pic: String, iv: ImageView) {
        val url = if (!pic.startsWith("http")) {
            when {
                pic.contains("www.crystal-cloud.top") -> "http://$pic"
                pic.startsWith("192") -> {
                    val s = pic.split("/".toRegex())
                    UrlConstant.BASE_URL_HOST + s[1] + "/" + s[2] + "/" + s[3]
                }
                else -> UrlConstant.BASE_URL_HOST + pic
            }
        } else {
            pic
        }
        GlideApp.with(context).load(url)
            .placeholder(R.drawable.icon_default)
            .diskCacheStrategy(DiskCacheStrategy.DATA).into(iv)
    }
    fun load(context: Context, url: Any?, iv: ImageView) {
        GlideApp.with(context).load(url)
            .placeholder(R.drawable.icon_default)
            .diskCacheStrategy(DiskCacheStrategy.DATA).into(iv)
    }
    fun load(context: Context, url: Int?, iv: ImageView) {
        GlideApp.with(context).load(url)
            .diskCacheStrategy(DiskCacheStrategy.DATA).into(iv)
    }
    fun load(context: Context, file: File, iv: ImageView) {
        GlideApp.with(context).load(file).into(iv)
    }
    /**
     * 加载圆形图
     */
    fun loadCircle(context: Context, pic: String, iv: ImageView) {
//        避免返回图片地址不全时显示出错
        val url = if (!pic.startsWith("http")) {
            when {
                pic.contains("www.crystal-cloud.top") -> "http://$pic"
                pic.startsWith("192") -> {
                    val s = pic.split("/".toRegex())
                    UrlConstant.BASE_URL_HOST + s[1] + "/" + s[2] + "/" + s[3]
                }
                else -> UrlConstant.BASE_URL_HOST + pic
            }
        } else {
            pic
        }
        GlideApp.with(context)
            .load(url)
            .dontAnimate()
            .circleCrop()
            .placeholder(R.drawable.icon_default)
            .into(iv)
    }
}