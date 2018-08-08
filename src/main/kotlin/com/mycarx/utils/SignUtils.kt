package com.mycarx.utils

/**
 * 请求签名辅助类
 */
class SignUtils {
    companion object {
        @JvmStatic
        fun createSign(kvList: List<String>, salt: String): String {
            val source = "${kvList.sorted().joinToString("&")}&$salt"
            return source.encryptMd5()
        }
    }
}