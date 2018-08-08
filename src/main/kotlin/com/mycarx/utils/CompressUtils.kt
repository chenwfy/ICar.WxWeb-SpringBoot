package com.mycarx.utils

import java.io.IOException
import java.util.zip.Inflater
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater

/**
 * 数据压缩、解压辅助类
 */
class CompressUtils {
    companion object {
        /**
         * 压缩流
         */
        @JvmStatic
        fun compress(sourceData: ByteArray): ByteArray {
            val worker = Deflater()
            worker.reset()
            worker.setInput(sourceData)
            worker.finish()

            var output: ByteArray
            val outSteam = ByteArrayOutputStream(sourceData.size)
            try {
                val buffer = ByteArray(1024)
                while (!worker.finished()) {
                    val i: Int = worker.deflate(buffer)
                    outSteam.write(buffer, 0, i)
                }
                output = outSteam.toByteArray()
            } catch (e: Exception) {
                output = sourceData
                e.printStackTrace()
            } finally {
                try {
                    outSteam.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            worker.end()
            return output
        }

        /**
         * 解压流
         */
        @JvmStatic
        fun decompress(sourceData: ByteArray): ByteArray {
            val worker = Inflater()
            worker.reset()
            worker.setInput(sourceData)

            var output: ByteArray
            val outSteam = ByteArrayOutputStream(sourceData.size)
            try {
                val buffer = ByteArray(1024)
                while (!worker.finished()) {
                    val i: Int = worker.inflate(buffer)
                    outSteam.write(buffer, 0, i)
                }
                output = outSteam.toByteArray()
            } catch (e: Exception) {
                output = sourceData
                e.printStackTrace()
            } finally {
                try {
                    outSteam.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            worker.end()
            return output
        }
    }
}