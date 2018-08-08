package com.mycarx.utils

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * AES加密/解密辅助类
 */
class AESUtils {
    companion object {
        @JvmStatic
        private val Secret_Key: ByteArray = byteArrayOf(0x08, 0x24, 0x18, 0x19, 0x45, 0x0E, 0x0A, 0x44, 0x16, 0x05, 0x03, 0x21, 0x32, 0x46, 0x12, 0x30)
        @JvmStatic
        private val Secret_IV: ByteArray = byteArrayOf(0x10, 0x04, 0x78, 0x19, 0x33, 0x65, 0x22, 0x45, 0x17, 0x23, 0x56, 0x0F, 0x0E, 0x64, 0x0A, 0x79)
        @JvmStatic
        private val SecretKey_Instance: String = "AES"
        @JvmStatic
        private val Cipher_Instance = "AES/CBC/PKCS5Padding"

        @JvmStatic
        private fun createSecretKey(): SecretKey {
            return SecretKeySpec(Secret_Key, SecretKey_Instance)
        }

        @JvmStatic
        fun encrypt(source: ByteArray): ByteArray {
            val cipher: Cipher = Cipher.getInstance(Cipher_Instance)
            cipher.init(Cipher.ENCRYPT_MODE, createSecretKey(), IvParameterSpec(Secret_IV))
            return cipher.doFinal(source)
        }

        @JvmStatic
        fun decrypt(source: ByteArray): ByteArray {
            val cipher: Cipher = Cipher.getInstance(Cipher_Instance)
            cipher.init(Cipher.DECRYPT_MODE, createSecretKey(), IvParameterSpec(Secret_IV))
            return cipher.doFinal(source)
        }
    }
}