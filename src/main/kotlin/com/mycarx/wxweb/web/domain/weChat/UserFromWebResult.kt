package com.mycarx.wxweb.web.domain.weChat

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.mycarx.wxweb.web.domain.JsonSerializable

@JsonIgnoreProperties(ignoreUnknown = true)
class UserFromWebResult @JsonCreator constructor(
        @JsonProperty("openid") openid: String,
        @JsonProperty("nickname") nickname: String,
        @JsonProperty("headimgurl") headimgurl: String,
        @JsonProperty("unionid") unionid: String?) : JsonSerializable() {

    var openId: String = openid
    var nickName: String = nickname
    var sex: Int = 0
    var city: String = ""
    var province: String = ""
    var country: String = ""
    var headImageUrl: String = headimgurl
    var unionId: String? = unionid
    var privilege: List<String>? = emptyList()
}