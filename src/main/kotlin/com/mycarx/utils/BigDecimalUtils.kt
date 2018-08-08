package com.mycarx.utils

import java.math.BigDecimal

class BigDecimalUtils {
    companion object {
        @JvmStatic
        fun setScale(decimal: BigDecimal?): BigDecimal {
            return setScale(decimal, 10)
        }

        @JvmStatic
        fun setScale(decimal: BigDecimal?, scale: Int): BigDecimal {
            return setScale(decimal, scale, false)
        }

        @JvmStatic
        fun setScale(decimal: BigDecimal?, scale: Int, roundDown: Boolean = false): BigDecimal {
            return if (null == decimal) BigDecimal(0) else decimal.setScale(scale, if (roundDown) BigDecimal.ROUND_DOWN else BigDecimal.ROUND_HALF_UP)
        }

        @JvmStatic
        fun parseToBigDecimal(decimal: String): BigDecimal {
            try {
                return BigDecimal(decimal)
            } catch (e: Exception) {

            }
            return BigDecimal.ZERO
        }

        /**
         * 清除BigDecimal类型数据后连续的0
         */
        @JvmStatic
        fun clearBigDecimalLastZeros(source: BigDecimal): BigDecimal {
            var result = "0"

            if (source != BigDecimal.ZERO) {
                val valString: String = source.toString()
                result = valString

                if (valString.contains(".")) {
                    while (result.endsWith("0")) {
                        result = result.substring(0, result.length - 1)
                    }

                    if (result.endsWith(".")) {
                        result = result.substring(0, result.length - 1)
                    }
                }
            }

            return parseToBigDecimal(result)
        }
    }
}