package com.mycarx.utils

import org.apache.commons.lang3.RandomStringUtils

/**
 * 常用字符串处理辅助类
 */
class StringUtils {
    companion object {
        @JvmStatic
        fun getRandomLetters(len: Int = 16): String {
            return RandomStringUtils.random(len, true, true)
        }

        @JvmStatic
        fun getRandomNumbers(len: Int = 6): String {
            return RandomStringUtils.random(len, false, true)
        }

        @JvmStatic
        fun bytesToHexString(bytes: ByteArray): String {
            val buffer = StringBuffer(bytes.size)
            var ts: String
            bytes.forEach(fun(value: Byte) {
                ts = Integer.toHexString(0xFF and value.toInt())
                if (ts.length < 2) {
                    buffer.append(0)
                }
                buffer.append(ts.toUpperCase())
            })

            return buffer.toString()
        }

        @JvmStatic
        fun hexStringToBytes(hexString: String): ByteArray {
            val len: Int = hexString.length / 2
            val chars: CharArray = hexString.toCharArray()
            val bytes = ByteArray(len)
            var idx: Int
            for (i in 0 until len) {
                idx = i * 2
                bytes[i] = (((charToByte(chars[idx])) shl 4) or charToByte(chars[idx + 1])).toByte()
            }
            return bytes
        }

        @JvmStatic
        private fun charToByte(c: Char): Int {
            return "0123456789ABCDEF".indexOf(c)
        }
    }
}