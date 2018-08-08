package com.mycarx.wxweb.web.domain.baiduMap

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.mycarx.wxweb.web.domain.JsonSerializable

@JsonIgnoreProperties(ignoreUnknown = true)
class LocalPoint @JsonCreator constructor(@JsonProperty("x") x: Double, @JsonProperty("y") y: Double) : JsonSerializable() {
    var longitude: Double = x
    var latitudes: Double = y
}