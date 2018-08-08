package com.mycarx.wxweb.web.domain.weChat

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.mycarx.wxweb.web.domain.JsonSerializable

@JsonIgnoreProperties(ignoreUnknown = true)
class UserFromServerResult @JsonCreator constructor(
        @JsonProperty("openid") openid: String,
        @JsonProperty("nickname") nickname: String,
        @JsonProperty("headimgurl") headimgurl: String,
        @JsonProperty("unionid") unionid: String?,
        @JsonProperty("subscribe_time") subscribe_time: Long,
        @JsonProperty("groupid") groupid: Long,
        @JsonProperty("tagid_list") tagid_list: List<Long>?) : JsonSerializable() {

    var subscribe: Int = 0
    var subscribeTime: Long = subscribe_time
    var openId: String = openid
    var nickName: String = nickname
    var sex: Int = 0
    var city: String = ""
    var province: String = ""
    var country: String = ""
    var headImageUrl: String = headimgurl
    var unionId: String? = unionid
    var language: String = "zh_CN"
    var remark: String = ""
    var groupId: Long = groupid
    var tagIdList: List<Long>? = tagid_list
}