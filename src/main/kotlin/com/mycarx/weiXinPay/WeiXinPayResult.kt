package com.mycarx.weiXinPay

import com.mycarx.wxweb.web.domain.JsonSerializable

/**
 * 微信支付统一下单请求结果信息
 */
class WeiXinPayResult : JsonSerializable() {
    var returnCode: String = "FAIL"
    var returnMessage: String = ""
    var appId: String = ""
    var mchId: String = ""
    var deviceInfo: String = ""
    var nonceStr: String = ""
    var sign: String = ""
    var resultCode: String = ""
    var errorCode: String = ""
    var errorMessage: String = ""
    var tradeType: String = ""
    var prepayId: String = ""
}