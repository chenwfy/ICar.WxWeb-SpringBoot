package com.mycarx.utils

import java.util.*

class OrderIdUtils {
    companion object {
        /**
         * 创建加油支付订单号
         */
        @JvmStatic
        fun createOilPaymentOrderId(): String {
            return createOrderId("WOG")
        }

        /**
         * 创建人工洗车支付订单号
         */
        @JvmStatic
        fun createWashPaymentOrderId(): String {
            return createOrderId("WOW")
        }

        /**
         * 创建无人值守洗车支付订单号
         */
        @JvmStatic
        fun createWashMachinePaymentOrderId(): String {
            return createOrderId("WOM")
        }

        /**
         * 创建订单号
         */
        @JvmStatic
        private fun createOrderId(prefix: String): String {
            val dateNow = Date()
            val sourceId = "${dateNow.format("yyyyMMddHHmmss")}-${dateNow.time}-${StringUtils.getRandomLetters()}"
            return "$prefix${sourceId.encryptMd5(16)}".toUpperCase()
        }
    }
}
