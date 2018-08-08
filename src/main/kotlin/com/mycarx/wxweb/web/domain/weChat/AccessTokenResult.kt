package com.mycarx.wxweb.web.domain.weChat

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.mycarx.wxweb.web.domain.JsonSerializable

@JsonIgnoreProperties(ignoreUnknown = true)
class AccessTokenResult @JsonCreator constructor(
        @JsonProperty("access_token") access_token: String,
        @JsonProperty("expires_in") expires_in: Int) : JsonSerializable() {
    var accessToken: String = access_token
    var expiresIn: Int = expires_in
}