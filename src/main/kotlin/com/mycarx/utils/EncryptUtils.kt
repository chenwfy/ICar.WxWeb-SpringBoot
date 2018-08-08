package com.mycarx.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * MD5/Sha1加密辅助类
 */
class EncryptUtils {
    companion object {
        @JvmStatic
        fun encrypt(source: String, algorithm: String): String {
            try {
                val instance: MessageDigest = MessageDigest.getInstance(algorithm)
                val digest: ByteArray = instance.digest(source.toByteArray())
                return digest.toHexString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }

            return ""
        }
    }
}