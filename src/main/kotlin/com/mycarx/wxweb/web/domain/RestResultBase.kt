package com.mycarx.wxweb.web.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
open class RestResultBase @JsonCreator constructor(code: Int = 1, message: String = "success") : RestResult, JsonSerializable() {
    constructor() : this(1, "success")
    constructor(message: String) : this(0, message)

    var code: Int = code
    var message: String = message
}