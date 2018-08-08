package com.mycarx.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogUtils {
    companion object {
        @JvmStatic
        val logger: Logger = LoggerFactory.getLogger(LogUtils::class.java)
    }
}