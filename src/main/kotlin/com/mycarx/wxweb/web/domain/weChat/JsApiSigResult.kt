package com.mycarx.wxweb.web.domain.weChat

import com.mycarx.wxweb.web.domain.JsonSerializable

class JsApiSigResult(appId: String, timestamp: Long, nonceStr: String, signature: String) : JsonSerializable() {
    var appId: String = appId
    var timestamp: Long = timestamp
    var nonceStr: String = nonceStr
    var signature: String = signature
}