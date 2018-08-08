package com.mycarx.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import org.apache.commons.lang3.time.DateFormatUtils
import org.apache.commons.lang3.time.DateUtils
import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.io.SAXReader
import org.springframework.util.Base64Utils
import java.math.BigDecimal
import java.text.ParseException
import java.util.*
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

/**
 * =================================================
 * kotlin方式自定义扩展部分
 * =================================================
 */

/**
 * 写入SESSION
 */
fun HttpSession.setSession(sessionKey: String, sessionValue: Any): HttpSession {
    this.setAttribute(sessionKey, sessionValue)
    return this
}

/**
 * 读取SESSION
 */
fun HttpSession.getSession(sessionKey: String): Any? {
    var sessionValue: Any? = this.getAttribute(sessionKey)
    return if (null != sessionValue) sessionValue else null
}

/**
 * 判断是否为AJAX请求
 */
fun HttpServletRequest.isAjaxRequest(): Boolean {
    var header: String? = this.getHeader("X-Requested-With")
    if (null == header) {
        header = this.getHeader("x-requested-with")
    }

    return header != null && header.toLowerCase().equals("XMLHttpRequest".toLowerCase())
}

/**
 * 获取远程请求IP地址
 */
fun HttpServletRequest.getRemoteIp(): String {
    var remoteAddress: String = this.getHeader("x-forwarded-for") ?: ""
    if (remoteAddress.isEmpty()) {
        remoteAddress = this.remoteAddr ?: "127.0.0.1"
    }

    if (remoteAddress.contains(",")) {
        return remoteAddress.split(",")[0]
    }

    return remoteAddress
}

/**
 * 读取XML格式请求的数据，并将其转换成MAP对象
 */
fun HttpServletRequest.readXmlAsMap(): Map<String, String> {
    try {
        val xmlDoc: Document? = SAXReader().read(this.inputStream)
        if (null != xmlDoc) {
            val elementList: List<Element> = xmlDoc.rootElement?.elements() ?: listOf()
            if (elementList.isNotEmpty()) {
                val map = mutableMapOf<String, String>()
                elementList.forEach { it -> map[it.name] = it.textTrim }
                return map
            }
        }
    } catch (e: Exception) {
    }

    return mapOf("return_code" to "FAIL")
}

/**
 * 写入JSON格式数据响应HEADER
 */
fun HttpServletResponse.setJsonHeader(): HttpServletResponse {
    this.setHeader("content-type", "application/json; charset=utf-8")
    return this
}

/**
 * 写入HTML格式数据响应HEADER
 */
fun HttpServletResponse.setHtmlHeader(): HttpServletResponse {
    this.setHeader("content-type", "text/html")
    return this
}

/**
 * 写入路径重定向响应HEADER
 */
fun HttpServletResponse.redirect(url: String): HttpServletResponse {
    this.setHeader("location", url)
    this.status = 302
    return this
}

/**
 * 响应HTTP请求
 */
fun HttpServletResponse.end() {
    this.setJsonHeader().end(byteArrayOf())
}

/**
 * 响应HTTP请求
 */
fun HttpServletResponse.end(data: String) {
    this.end(data.toByteArray())
}

/**
 * 响应HTTP请求
 */
fun HttpServletResponse.end(data: ByteArray) {
    val outputStream = this.outputStream
    outputStream.write(data)
    outputStream.flush()
    outputStream.close()
}

/**
 * adler32校验码
 */
fun ByteArray.adler32CheckSum(): Long {
    return if (this.isNotEmpty()) Adler32Utils.getCheckSum(this) else 0L
}

/**
 * ByteArray AES加密
 */
fun ByteArray.encryptAES(): ByteArray {
    return if (this.isNotEmpty()) AESUtils.encrypt(this) else this
}

/**
 * ByteArray AES解密
 */
fun ByteArray.decryptAES(): ByteArray {
    return if (this.isNotEmpty()) AESUtils.decrypt(this) else this
}

/**
 * 字符串AES加密，然后再转换为Base64
 */
fun String.encodeBase64ByEncryptAES(): String {
    return if (this.isNotBlank() && this.isNotEmpty()) Base64Utils.encodeToString(this.toByteArray(charset("UTF-8")).encryptAES()) else this
}

/**
 * Base64字符串解密，然后再AES解密
 */
fun String.decodeFromEncryptAESBase64(): String {
    return if (this.isNotBlank() && this.isNotEmpty()) Base64Utils.decodeFromString(this).decryptAES().toString(charset("UTF-8")) else this
}

/**
 * 将指定字符串进行MD5加密
 */
fun String.encryptMd5(len: Int = 32): String {
    val result = if (this.isNotBlank() && this.isNotEmpty()) EncryptUtils.encrypt(this, "MD5") else this
    return if (len == 16) result.substring(8, 24) else result
}

/**
 * 将指定字符串进行Sha1加密
 */
fun String.encryptSha1(): String {
    return if (this.isNotBlank() && this.isNotEmpty()) EncryptUtils.encrypt(this, "Sha1") else this
}

/**
 * 将指定的明文内容加密码盐组合加密为密码格式
 */
fun String.encryptToPassword(salt: String): String {
    return "${this}-MYCARX-$salt".encryptSha1()
}

/**
 * 设置BigDecimal默认精度
 */
fun BigDecimal.divide(): BigDecimal {
    return BigDecimalUtils.setScale(this)
}

/**
 * 设置BigDecimal指定精度
 */
fun BigDecimal.divide(scale: Int): BigDecimal {
    return BigDecimalUtils.setScale(this, scale)
}

/**
 * 设置BigDecimal指定精度
 */
fun BigDecimal.divide(scale: Int, roundDown: Boolean = false): BigDecimal {
    return BigDecimalUtils.setScale(this, scale)
}

/**
 * 去除BigDecimal类型数据末尾连续的“0”
 */
fun BigDecimal.clearLastZero(): BigDecimal {
    return BigDecimalUtils.clearBigDecimalLastZeros(this)
}

/**
 * 将BigDecimal格式的字符串转换为BigDecimal类型
 */
fun String.convertToBigDecimal(): BigDecimal {
    return BigDecimalUtils.parseToBigDecimal(this).divide()
}

/**
 * ByteArray类型压缩
 */
fun ByteArray.bytesCompress(): ByteArray {
    return if (this.isNotEmpty()) CompressUtils.compress(this) else this
}

/**
 * ByteArray解压缩
 */
fun ByteArray.bytesDecompress(): ByteArray {
    return if (this.isNotEmpty()) CompressUtils.decompress(this) else this
}

/**
 * ByteArray转换成HexString
 */
inline fun ByteArray.toHexString(): String {
    return if (this.isNotEmpty()) StringUtils.bytesToHexString(this) else ""
}

/**
 * HexString转换成ByteArray
 */
fun String.hexToBytes(): ByteArray {
    var bytes: ByteArray = byteArrayOf()
    if (this.isNotBlank() && this.isNotEmpty()) {
        try {
            bytes = StringUtils.hexStringToBytes(this)
        } catch (e: Exception) {
            bytes = byteArrayOf()
        }
    }
    return bytes
}

/**
 * 截取指定长度字符串
 */
fun String.SubStr(count: Int): String {
    if (this.isNotBlank() && this.isNotEmpty() && count > 0 && this.length > count) {
        return this.substring(0, count)
    }
    return this
}

/**
 * 隐藏手机号码中间4位数字
 */
fun String.mobileNumberMasked(): String {
    if (this.length == 11) {
        return this.substring(0, 3) + "****" + this.substring(7, 11)
    }
    return this
}

/**
 * 判断字符串是否为有效的手机号码
 */
fun String.isMobileNumber(): Boolean {
    if (this.isNotBlank() && this.isNotEmpty() && this.length == 11) {
        val regExp: String = "^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9]|19[0-9])[0-9]{8}\$"
        return Pattern.compile(regExp).matcher(this).matches()
    }
    return false
}

/**
 * 判断字符串是否为纯数字
 */
fun String.isNumber(): Boolean {
    if (this.isNotBlank() && this.isNotEmpty()) {
        val regExp: String = "^([0-9]){1,10}\$"
        return Pattern.compile(regExp).matcher(this).matches()
    }
    return false
}

/**
 * 判断字符串是否为有效的短信验证码
 */
fun String.isSMSVerifyCode(): Boolean {
    if (this.isNotBlank() && this.isNotEmpty() && this.length == 6) {
        val regExp = "^([0-9]){6}\$"
        return Pattern.compile(regExp).matcher(this).matches()
    }
    return false
}

/**
 * 判断字符串是否为有效的电子邮件格式
 */
fun String.isEMail(): Boolean {
    if (this.isNotBlank() && this.isNotEmpty() && this.length >= 6) {
        val regExp = "\\w+@\\w+(\\.\\w+)+"
        return Pattern.compile(regExp).matcher(this).matches()
    }
    return false
}

/**
 * 在指定的时间上增加指定的秒数，得到新的时间
 */
fun Date.addSeconds(seconds: Int): Date {
    return DateUtils.addSeconds(this, seconds)
}

/**
 * 在指定的时间上增加指定的分钟数，得到新的时间
 */
fun Date.addMinutes(minutes: Int): Date {
    return DateUtils.addMinutes(this, minutes)
}

/**
 * 在指定的时间上增加指定的小时数，得到新的时间
 */
fun Date.addHours(hours: Int): Date {
    return DateUtils.addHours(this, hours)
}

/**
 * 在指定的时间上增加指定的天数，得到新的时间
 */
fun Date.addDays(days: Int): Date {
    return DateUtils.addDays(this, days)
}

/**
 * 在指定的时间上增加指定的年数，得到新的时间
 */
fun Date.addMonths(months: Int): Date {
    return DateUtils.addMonths(this, months)
}

/**
 * 在指定的时间上增加指定的年数，得到新的时间
 */
inline fun Date.addYears(years: Int): Date {
    return DateUtils.addYears(this, years)
}

/**
 * 将日期/时间格式化为指定格式的字符串
 */
fun Date.format(formatter: String = "yyyy-MM-dd HH:mm:ss"): String {
    try {
        return DateFormatUtils.format(this, formatter)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

/**
 * 将日期/时间字符串格式为日期对象
 */
fun String.convertToDate(vararg parsePatterns: String): Date? {
    if (this.isNotBlank() && this.isNotEmpty()) {
        try {
            return DateUtils.parseDate(this, *parsePatterns)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
    return null
}

/**
 * 将任意对象序列化为JSON格式字符串
 */
fun Any.jsonSerialize(): String {
    return JsonUtils.serialize(this)
}

/**
 * 反序列化JSON格式字符串至指定对象
 */
inline fun <reified T> String.jsonDeserialize(): T? {
    return JsonUtils.deserialize(this, T::class.java)
}

/**
 * 反序列化JSON格式字符串至指定对象
 */
fun <T> String.jsonDeserialize(clazz: Class<T>): T? {
    return JsonUtils.deserialize(this, clazz)
}

/**
 * 反序列化JSON格式字符串至指定对象
 */
fun <T> String.jsonDeserialize(typeRef: TypeReference<T>): T? {
    return JsonUtils.deserialize(this, typeRef)
}

/**
 * 反序列化JSON格式字符串至JsonNode对象
 */
fun String.parseToJsonNode(): JsonNode? {
    return JsonUtils.parseToJsonNode(this)
}

/**
 * 格式化距离显示字符串
 */
fun Double.distanceFormat() : String {
    return DistanceUtils.distanceFormat(this)
}
