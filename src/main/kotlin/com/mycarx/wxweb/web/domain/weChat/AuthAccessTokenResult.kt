package com.mycarx.wxweb.web.domain.weChat

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.mycarx.wxweb.web.domain.JsonSerializable

@JsonIgnoreProperties(ignoreUnknown = true)
class AuthAccessTokenResult @JsonCreator constructor(
        @JsonProperty("access_token") access_token: String,
        @JsonProperty("expires_in") expires_in: Int,
        @JsonProperty("refresh_token") refresh_token: String,
        @JsonProperty("openid") openid: String,
        @JsonProperty("scope") scope: String) : JsonSerializable() {

    var accessToken: String = access_token
    var expiresIn: Int = expires_in
    var refreshToken: String = refresh_token
    var openId: String = openid
    var scope: String = scope
}