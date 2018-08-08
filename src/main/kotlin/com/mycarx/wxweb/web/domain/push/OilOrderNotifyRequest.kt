package com.mycarx.wxweb.web.domain.push

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mycarx.wxweb.web.domain.RequestBase

@JsonIgnoreProperties(ignoreUnknown = true)
class OilOrderNotifyRequest(orderNo: String, stationId: Long) : RequestBase() {
    constructor() : this("", 0L)

    var orderNo: String = orderNo
    var stationId: Long = stationId
}