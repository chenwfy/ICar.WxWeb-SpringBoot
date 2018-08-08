package com.mycarx.utils

import com.mycarx.wxweb.config.AppConfig
import java.io.File
import java.io.IOException
import java.io.FileOutputStream
import java.util.*


class FileUtils {
    companion object {
        private val FileSaveRoot: String = AppConfig.staticFileSaveRoot

        /**
         * 保存订单语音文件，并返回文件访问URL
         */
        @JvmStatic
        fun saveOrderAudioFileAndCreateUrl(data: ByteArray): String {
            val extName = "mp3"
            val saveFileName = "${StringUtils.getRandomLetters()}.$extName"
            val timeNow = Date()
            val saveDir = "/audio/${timeNow.format("yyyy/MM/dd")}"
            if (saveBytesToFile(data, saveFileName, saveDir)) {
                return "$saveDir/$saveFileName"
            }
            return ""
        }

        /**
         * 保存文件
         */
        @JvmStatic
        fun saveBytesToFile(data: ByteArray, fileName: String, dirUrl: String): Boolean {
            val saveDir: String = getUploadFileSaveDirectory(dirUrl)
            if (saveDir.isNotEmpty()) {
                val saveFileName = "$saveDir${File.separator}$fileName"
                return saveBytesToFile(data, saveFileName)
            }
            return false
        }

        /**
         * 保存文件
         */
        @JvmStatic
        fun saveBytesToFile(data: ByteArray, fileName: String): Boolean {
            try {
                val stream = FileOutputStream(fileName)
                stream.write(data)
                stream.close()
                return true
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }
        }

        /**
         * 创建并获取文件保存目录
         */
        @JvmStatic
        private fun getUploadFileSaveDirectory(dirUrl: String): String {
            val saveDir = "$FileSaveRoot${dirUrl.replace("/", File.separator)}"
            if (createDirectory(saveDir))
                return saveDir
            return ""
        }

        /**
         * 创建目录
         */
        @JvmStatic
        private fun createDirectory(dir: String): Boolean {
            val saveDir = File(dir)
            if (!saveDir.exists())
                return saveDir.mkdirs()
            return true
        }
    }
}