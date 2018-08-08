package com.mycarx.utils

import java.util.zip.Adler32

class Adler32Utils {
    companion object {
        @JvmStatic
        fun getCheckSum(buffer: ByteArray): Long {
            val adler32 = Adler32()
            adler32.update(buffer)
            return adler32.value
        }
    }
}