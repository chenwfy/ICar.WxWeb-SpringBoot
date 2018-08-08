package com.mycarx.weiXinPay

import com.mycarx.utils.encryptMd5
import com.mycarx.wxweb.config.AppConfig
import com.mycarx.wxweb.web.domain.JsonSerializable
import java.util.*

/**
 * 微信支付统一下单成功后，返回给微信H5调用支付所需参数内容
 */
class WeiXinPayConfirmResult(prepayId: String) : JsonSerializable() {
    constructor() : this("")

    val appId: String = AppConfig.weChatAppId
    val timeStamp: String = (Date().time / 1000).toString()
    val nonceStr: String = com.mycarx.utils.StringUtils.getRandomLetters()
    val signType: String = "MD5"
    var prepayId: String = prepayId
    var paySign: String = ""

    fun createSign(): WeiXinPayConfirmResult {
        val fieldList = listOf("appId=$appId", "timeStamp=$timeStamp", "nonceStr=$nonceStr", "signType=$signType", "package=prepay_id=$prepayId")
        this.paySign = ("${fieldList.sorted().joinToString("&")}&key=${AppConfig.weChatPayKey}".encryptMd5()).toUpperCase()
        return this
    }
}