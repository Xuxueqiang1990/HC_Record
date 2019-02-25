package com.myrecord.hc_record.util

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import com.myrecord.hc_record.bean.RecordingItem
import java.io.*

/**
 * 文件操作工具包
 *
 */
class FileUtil {

    companion object {

        var mBaseFileBath = ""


        /**
         * 获取文件夹下所有的mp4文件
         */
        fun getMp4FileList(dirPath :String): List<RecordingItem>? {
            var dirs = File(dirPath)
            if (dirs.exists()){
                var files = dirs.listFiles()
                var datas = ArrayList<RecordingItem>()
                var filesDesc = files.reversed()
                for (file in filesDesc){
                    datas.add(RecordingItem(file.name , file.absolutePath))
                }
                return datas
            } else {
                return null
            }
        }

        /**
         * 根据 path 删除文件
         *
         * @param path  需要删除的文件path
         */
        fun delete(path: String) {
            if (!TextUtils.isEmpty(path)) {
                val file = File(path)
                if (file.exists()) {
                    file.delete()
                }
            }
        }

        /**
         * 创建录音文件夹
         * @param context
         * @return File文件夹
         */
        fun createDir(context: Context) {
            var dir: File?
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val savePath = Environment.getExternalStorageDirectory().absoluteFile.toString() + "/Android/data/" + context.packageName + "/files/sound"
                dir = File(savePath)
                if (!dir.exists()) {
                    dir.mkdirs()
                }
            } else {
                dir = context.filesDir
            }
            mBaseFileBath = dir!!.absolutePath
        }

        /**
         * 创建文件
         * @param filePath
         * @param fileName
         */
        fun createFile(filePath: String, fileName: String) {
            var file = File(filePath, fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
        }
    }

}