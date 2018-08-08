package com.mycarx.wxweb.web.domain.weChat

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.mycarx.wxweb.web.domain.JsonSerializable

@JsonIgnoreProperties(ignoreUnknown = true)
class JsApiTicketResult @JsonCreator constructor(
        @JsonProperty("errcode") errcode: Int,
        @JsonProperty("errmsg") errmsg: String,
        @JsonProperty("expires_in") expires_in: Int) : JsonSerializable() {

    var code: Int = errcode
    var message: String = errmsg
    var ticket: String = ""
    var Expired: Int = expires_in
}