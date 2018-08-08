package com.mycarx.utils

import java.math.BigDecimal

class DistanceUtils {
    companion object {
        @JvmStatic
        private val EarthRadius: Double = 6378137.0

        /**
         * 计算2个经纬度之间的距离
         */
        @JvmStatic
        fun calculateDistance(sourceLatitudes: Double, sourceLongitude: Double, targetLatitudes: Double, targetLongitude: Double): Double {
            val sourceLatRadius: Double = calculateRadius(sourceLatitudes)
            val sourceLngRadius: Double = calculateRadius(sourceLongitude)
            val targetLatRadius: Double = calculateRadius(targetLatitudes)
            val targetLngRadius: Double = calculateRadius(targetLongitude)
            val latDiff: Double = sourceLatRadius - targetLatRadius
            val lngDiff: Double = sourceLngRadius - targetLngRadius
            val part: Double = Math.pow(Math.sin(latDiff / 2.0), 2.0) + Math.cos(sourceLatRadius) * Math.cos(targetLatRadius) * Math.pow(Math.sin(lngDiff / 2.0), 2.0)
            return Math.round(((2.0 * Math.asin(Math.sqrt(part)) * EarthRadius) * 10000.0)) / 10000.0
        }

        /**
         * 格式化距离字符串
         */
        @JvmStatic
        fun distanceFormat(distance: Double): String {
            if (distance < 1000.0)
                return "约${distance.toInt()}米"

            val fmt: String = formatDouble(distance)
            return "约${fmt}公里"
        }

        /**
         * 获取计算半径
         */
        @JvmStatic
        private fun calculateRadius(latLong: Double): Double {
            return latLong * Math.PI / 180.0
        }

        /**
         * 格式化Double类型数据
         */
        @JvmStatic
        private fun formatDouble(double: Double): String {
            var km: Double = double / 1000.0
            if (km < 10.0) {
                return BigDecimal(km).divide(2).clearLastZero().toString()
            }

            if (km < 100.0) {
                return BigDecimal(km).divide(1).clearLastZero().toString()
            }

            return km.toInt().toString()
        }
    }
}