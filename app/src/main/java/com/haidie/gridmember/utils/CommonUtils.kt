package com.haidie.gridmember.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel


/**
 * Create by Administrator
 * on 2018/08/27 16:25
 */
object CommonUtils {

    /**
     * 根据文件路径拷贝文件
     */
    fun copyFile(pathFrom: String, pathTo: String): Boolean {
        var result = false
        if (pathFrom == pathTo) {
            return result
        }

        var srcChannel: FileChannel? = null
        var dstChannel: FileChannel? = null
        try {
            srcChannel = FileInputStream(File(pathFrom)).channel
            dstChannel = FileOutputStream(File(pathTo)).channel
            srcChannel.transferTo(0, srcChannel.size(), dstChannel)
            srcChannel.close()
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }finally {
            srcChannel?.close()
            dstChannel?.close()
        }
        return result
    }

}